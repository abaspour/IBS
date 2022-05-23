package com.nicico.ibs.repository;

import com.nicico.ibs.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodDAO extends JpaRepository<PaymentMethod, Long>, JpaSpecificationExecutor<PaymentMethod> {
}
