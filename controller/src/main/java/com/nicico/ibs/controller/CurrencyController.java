package com.nicico.ibs.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.ibs.dto.CurrencyDTO;
import com.nicico.ibs.iservice.ICurrencyService;
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
@RequestMapping(value = "/api/currency")
public class CurrencyController {

	private final ICurrencyService currencyService;

	// ------------------------------
/*
	@Loggable
	@GetMapping(value = "/{id}")
	public ResponseEntity<CurrencyDTO.Info> get(@PathVariable Long id) {
		return new ResponseEntity<>(currencyService.get(id), HttpStatus.OK);
	}

	@Loggable
	@GetMapping(value = "/list")
	public ResponseEntity<List<CurrencyDTO.Info>> list() {
		return new ResponseEntity<>(currencyService.list(), HttpStatus.OK);
	}

	@Loggable
	@PostMapping
	public ResponseEntity<CurrencyDTO.Info> create(@Validated @RequestBody CurrencyDTO.Create request) {
		return new ResponseEntity<>(currencyService.create(request), HttpStatus.CREATED);
	}

	@Loggable
	@PutMapping(value = "/{id}")
	public ResponseEntity<CurrencyDTO.Info> update(@PathVariable Long id, @RequestBody CurrencyDTO.Update request) {
		return new ResponseEntity<>(currencyService.update(id, request), HttpStatus.OK);
	}

	@Loggable
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		currencyService.delete(id);
		return new ResponseEntity(HttpStatus.OK);
	}

	@Loggable
	@DeleteMapping(value = "/list")
	public ResponseEntity<Void> delete(@Validated @RequestBody CurrencyDTO.Delete request) {
		currencyService.delete(request);
		return new ResponseEntity(HttpStatus.OK);
	}
 */
	@Loggable
	@GetMapping(value = "/iso-search")
	public ResponseEntity<TotalResponse<CurrencyDTO.Info>> list(@RequestParam MultiValueMap<String, String> criteria) throws IOException {
		final NICICOCriteria nicicoCriteria = NICICOCriteria.of(criteria);
		if (nicicoCriteria.get_sortBy() == null)
			nicicoCriteria.set_sortBy(Collections.singletonList("id"));
		return new ResponseEntity<>(currencyService.search(nicicoCriteria), HttpStatus.OK);
	}
 /*
	@Loggable
	@GetMapping(value = "/search")
	public ResponseEntity<SearchDTO.SearchRs<CurrencyDTO.Info>> search(@RequestBody SearchDTO.SearchRq request) {
		return new ResponseEntity<>(currencyService.search(request), HttpStatus.OK);
	}

 */
}
