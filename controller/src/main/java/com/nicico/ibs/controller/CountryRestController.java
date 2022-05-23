package com.nicico.ibs.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.ibs.dto.CountryDTO;
import com.nicico.ibs.iservice.ICountryService;
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
@RequestMapping(value = "/api/country")
public class CountryRestController {

	private final ICountryService countryService;

	// ------------------------------
/*
	@Loggable
	@GetMapping(value = "/{id}")
	public ResponseEntity<CountryDTO.Info> get(@PathVariable Long id) {
		return new ResponseEntity<>(countryService.get(id), HttpStatus.OK);
	}

	@Loggable
	@GetMapping(value = "/list")
	public ResponseEntity<List<CountryDTO.Info>> list() {
		return new ResponseEntity<>(countryService.list(), HttpStatus.OK);
	}

	@Loggable
	@PostMapping
	public ResponseEntity<CountryDTO.Info> create(@Validated @RequestBody CountryDTO.Create request) {
		return new ResponseEntity<>(countryService.create(request), HttpStatus.CREATED);
	}

	@Loggable
	@PutMapping(value = "/{id}")
	public ResponseEntity<CountryDTO.Info> update(@PathVariable Long id, @RequestBody CountryDTO.Update request) {
		return new ResponseEntity<>(countryService.update(id, request), HttpStatus.OK);
	}

	@Loggable
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		countryService.delete(id);
		return new ResponseEntity(HttpStatus.OK);
	}

	@Loggable
	@DeleteMapping(value = "/list")
	public ResponseEntity<Void> delete(@Validated @RequestBody CountryDTO.Delete request) {
		countryService.delete(request);
		return new ResponseEntity(HttpStatus.OK);
	}
 */
	@Loggable
	@GetMapping(value = "/iso-search")
	public ResponseEntity<TotalResponse<CountryDTO.Info>> list(@RequestParam MultiValueMap<String, String> criteria) throws IOException {
		final NICICOCriteria nicicoCriteria = NICICOCriteria.of(criteria);
		if (nicicoCriteria.get_sortBy() == null)
			nicicoCriteria.set_sortBy(Collections.singletonList("id"));
		return new ResponseEntity<>(countryService.search(nicicoCriteria), HttpStatus.OK);
	}
 /*
	@Loggable
	@GetMapping(value = "/search")
	public ResponseEntity<SearchDTO.SearchRs<CountryDTO.Info>> search(@RequestBody SearchDTO.SearchRq request) {
		return new ResponseEntity<>(countryService.search(request), HttpStatus.OK);
	}

 */
}
