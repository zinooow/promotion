package jinho.han.couponservice.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.lang.Exception

@Component
class UserIdInterceptor: HandlerInterceptor {

    companion object {
        private val USER_ID_HEADER = "X-User-Id"
        private val currentUserId = ThreadLocal<Long>();

        fun getCurrentUserId(): Long {
            return currentUserId.get()?: throw IllegalStateException("Current user ID is null")
        }
    }

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val userId = request.getHeader(USER_ID_HEADER)
            ?.let{ it.toLongOrNull()?:throw IllegalStateException("Invalid X-User-Id format") }
            ?: throw IllegalStateException("Required X-User-Id header")

        currentUserId.set(userId)
        return super.preHandle(request, response, handler)
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        currentUserId.remove()
    }
}