package jinho.han.couponservice.adapter.persistance

import jinho.han.couponservice.domain.Coupon
import jinho.han.couponservice.domain.CouponPolicy
import jinho.han.couponservice.domain.CouponStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CouponRepository: JpaRepository<Coupon, Long> {

    @Query("SELECT COUNT(c) FROM Coupon c WHERE c.couponPolicy.id = :policyId")
    fun countByCouponPolicyId(@Param("policyId") couponPolicyId: Long): Long

    fun findByUserId(userId: Long): List<Coupon>
    fun findByUserIdAndCouponPolicyAndStatusNot(userId: Long, couponPolicy: CouponPolicy, status: CouponStatus): Coupon?
}