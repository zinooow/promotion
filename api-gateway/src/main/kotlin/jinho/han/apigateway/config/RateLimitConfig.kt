package jinho.han.apigateway.config

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Mono

@Configuration
class RateLimitConfig {

    /*
    * replenishRate : 초당 허용되는 요청 수
    * burstCapacity : 최대 누적 가능한 요청 수
    * 여기선 기본 값 셋팅, 실제로는 yml 설정 따름.
    * */
    @Bean
    fun redisRateLimiter() = RedisRateLimiter(10,20)

    @Bean
    fun userKeyResolver() = KeyResolver { exchange ->
        val userId = exchange.request.headers.getFirst("X-User-ID")
        val ip = exchange.request.remoteAddress?.address?.hostAddress
        Mono.just(userId ?: ip ?: "anonymous")
    }
}