package com.nicico.ibs.repository;

import com.nicico.ibs.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDAO extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    Account findAllById(Long id);
}
