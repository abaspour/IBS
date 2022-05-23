package com.nicico.ibs.repository;

import com.nicico.ibs.model.PurchaseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseTypeDAO extends JpaRepository<PurchaseType, Long>, JpaSpecificationExecutor<PurchaseType> {
}
