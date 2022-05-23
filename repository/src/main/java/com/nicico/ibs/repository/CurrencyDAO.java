package com.nicico.ibs.repository;

import com.nicico.ibs.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyDAO extends JpaRepository<Currency, Long>, JpaSpecificationExecutor<Currency> {
}
