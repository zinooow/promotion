package jinho.han.couponservice.adapter.persistance

import jinho.han.couponservice.domain.Coupon
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository: JpaRepository<Coupon, Long> {
}