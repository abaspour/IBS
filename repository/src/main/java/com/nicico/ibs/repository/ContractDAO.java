package com.nicico.ibs.repository;

import com.nicico.ibs.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractDAO extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract> {
    Contract findAllByIdAndSupplierId(Long id,Long supplierId);
}
