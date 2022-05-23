package com.nicico.ibs.repository;

import com.nicico.ibs.model.TransmitInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransmitInstrumentDAO extends JpaRepository<TransmitInstrument, Long>, JpaSpecificationExecutor<TransmitInstrument> {
}
