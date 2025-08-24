package jinho.han.couponservice.adapter.persistance

import jakarta.persistence.LockModeType
import jinho.han.couponservice.domain.CouponPolicy
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface CouponPolicyRepository: JpaRepository<CouponPolicy, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from CouponPolicy p WHERE p.id = :id")
    fun findByIDWithLock(id: Long)
}