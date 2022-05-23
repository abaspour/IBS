package com.nicico.ibs.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.ibs.dto.UnitDTO;
import com.nicico.ibs.iservice.IUnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/unit")
public class UnitRestController {

	private final IUnitService unitService;

	// ------------------------------
/*
	@Loggable
	@GetMapping(value = "/{id}")
	public ResponseEntity<UnitDTO.Info> get(@PathVariable Long id) {
		return new ResponseEntity<>(unitService.get(id), HttpStatus.OK);
	}

	@Loggable
	@GetMapping(value = "/list")
	public ResponseEntity<List<UnitDTO.Info>> list() {
		return new ResponseEntity<>(unitService.list(), HttpStatus.OK);
	}

	@Loggable
	@PostMapping
	public ResponseEntity<UnitDTO.Info> create(@Validated @RequestBody UnitDTO.Create request) {
		return new ResponseEntity<>(unitService.create(request), HttpStatus.CREATED);
	}

	@Loggable
	@PutMapping(value = "/{id}")
	public ResponseEntity<UnitDTO.Info> update(@PathVariable Long id, @RequestBody UnitDTO.Update request) {
		return new ResponseEntity<>(unitService.update(id, request), HttpStatus.OK);
	}

	@Loggable
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		unitService.delete(id);
		return new ResponseEntity(HttpStatus.OK);
	}

	@Loggable
	@DeleteMapping(value = "/list")
	public ResponseEntity<Void> delete(@Validated @RequestBody UnitDTO.Delete request) {
		unitService.delete(request);
		return new ResponseEntity(HttpStatus.OK);
	}
 */
	@Loggable
	@GetMapping(value = "/iso-search")
	public ResponseEntity<TotalResponse<UnitDTO.Info>> list(@RequestParam MultiValueMap<String, String> criteria) throws IOException {
		final NICICOCriteria nicicoCriteria = NICICOCriteria.of(criteria);
		if (nicicoCriteria.get_sortBy() == null)
			nicicoCriteria.set_sortBy(Collections.singletonList("id"));
		return new ResponseEntity<>(unitService.search(nicicoCriteria), HttpStatus.OK);
	}
 /*
	@Loggable
	@GetMapping(value = "/search")
	public ResponseEntity<SearchDTO.SearchRs<UnitDTO.Info>> search(@RequestBody SearchDTO.SearchRq request) {
		return new ResponseEntity<>(unitService.search(request), HttpStatus.OK);
	}

 */
}
