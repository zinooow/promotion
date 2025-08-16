package jinho.han.apigateway.filter

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationFilter(
    @LoadBalanced webClientBuilder: WebClient.Builder   // LB 의도 명시
) : AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config>(Config::class.java) {

    private val userServiceClient: WebClient = webClientBuilder
        .baseUrl("http://user-service")
        .build()

    override fun apply(config: Config): GatewayFilter =
        GatewayFilter { exchange, chain ->
            val token = exchange.request.headers.getFirst("Authorization")
                ?.takeIf { it.startsWith("Bearer ", ignoreCase = true) }
                ?.substring(7)
                ?.trim()

            return@GatewayFilter Mono.justOrEmpty(token)
                .flatMap(::validateToken)                                   // Mono<Long> or empty
                .flatMap { userId -> proceedWithUserId(userId, exchange, chain) }
                .switchIfEmpty(chain.filter(exchange))                      // 토큰 없거나 invalid → 통과
                .onErrorResume { e -> handleAuthenticationError(exchange, e) }
        }

    private fun handleAuthenticationError(exchange: ServerWebExchange, e: Throwable): Mono<Void> {
        exchange.response.statusCode = HttpStatus.UNAUTHORIZED
        return exchange.response.setComplete()
    }

    private fun validateToken(token: String): Mono<Long> {
        return userServiceClient.post()
            .uri("api/v1/users/validate-token")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(mapOf("token" to token))
            .retrieve()
            .bodyToMono(Map::class.java)
            .flatMap { response ->
                val map = response as Map<*, *>
                when (val idAny = map["id"]) {
                    null -> Mono.empty()                       // id 없음 → invalid
                    is Number -> Mono.just(idAny.toLong())     // 숫자 → Long
                    else -> idAny.toString().toLongOrNull()
                        ?.let { Mono.just(it) }
                        ?: Mono.empty()                        // 변환 불가 → invalid
                }
            }
            .onErrorResume { e ->
                if (e is WebClientResponseException && e.statusCode.is4xxClientError)
                    Mono.empty()                               // 4xx → invalid로 간주
                else
                    Mono.error(e)                              // 나머지 → 진짜 에러
            }
    }

    private fun proceedWithUserId(
        userId: Long,
        exchange: ServerWebExchange,
        chain: GatewayFilterChain
    ): Mono<Void> {
        // 요청 객체를 실제로 교체해야 헤더가 반영됩니다.
        val mutatedExchange = exchange.mutate()
            .request { req ->
                req.headers { h ->
                    h.add("X-USER-ID", userId.toString())
                    // 정책에 따라 Authorization 제거할지 결정:
                    // h.remove(HttpHeaders.AUTHORIZATION)
                }
            }
            .build()

        return chain.filter(mutatedExchange)
    }

    class Config
}
