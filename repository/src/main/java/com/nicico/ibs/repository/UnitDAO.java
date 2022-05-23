package com.nicico.ibs.repository;

import com.nicico.ibs.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitDAO extends JpaRepository<Unit, Long>, JpaSpecificationExecutor<Unit> {
    Unit findAllById(Long id);
}
