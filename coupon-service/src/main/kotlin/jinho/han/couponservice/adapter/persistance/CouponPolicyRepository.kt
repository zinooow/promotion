package jinho.han.couponservice.adapter.persistance

import jinho.han.couponservice.domain.CouponPolicy
import org.springframework.data.jpa.repository.JpaRepository

interface CouponPolicyRepository: JpaRepository<CouponPolicy, Long>