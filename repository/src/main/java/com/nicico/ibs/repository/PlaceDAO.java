package com.nicico.ibs.repository;

import com.nicico.ibs.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceDAO extends JpaRepository<Place, Long>, JpaSpecificationExecutor<Place> {
    Place findAllById(Long id);
}
