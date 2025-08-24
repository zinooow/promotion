package jinho.han.couponservice.adapter.persistance

import jakarta.persistence.LockModeType
import jinho.han.couponservice.domain.Coupon
import jinho.han.couponservice.domain.CouponPolicy
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface CouponRepository: JpaRepository<Coupon, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Coupon c WHERE c.id = :id")
    fun findByIDWithLock(id: Long)

    @Query("SELECT COUNT(c) FROM Coupon c WHERE c.couponPolicy.id = :policyId")
    fun countByCouponPolicyId(policyId: Long): Long

    fun findByUserIdAndStatusOrderByIssuedAtDesc(userId: Long, status: Coupon.Status, pageable: Pageable): Page<Coupon>
    fun findByUserIdAndCouponPolicyAndStatusNot(userId: Long, couponPolicy: CouponPolicy, status: Coupon.Status): Coupon?


}