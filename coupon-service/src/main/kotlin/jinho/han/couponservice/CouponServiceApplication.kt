package jinho.han.couponservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class CouponServiceApplication

fun main(args: Array<String>) {
    runApplication<CouponServiceApplication>(*args)
}
