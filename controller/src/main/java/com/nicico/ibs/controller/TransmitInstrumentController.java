package com.nicico.ibs.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.ibs.dto.TransmitInstrumentDTO;
import com.nicico.ibs.iservice.ITransmitInstrumentService;
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
@RequestMapping(value = "/api/transmitInstrument")
public class TransmitInstrumentController {

	private final ITransmitInstrumentService transmitInstrumentService;

	// ------------------------------
/*
	@Loggable
	@GetMapping(value = "/{id}")
	public ResponseEntity<TransmitInstrumentDTO.Info> get(@PathVariable Long id) {
		return new ResponseEntity<>(transmitInstrumentService.get(id), HttpStatus.OK);
	}

	@Loggable
	@GetMapping(value = "/list")
	public ResponseEntity<List<TransmitInstrumentDTO.Info>> list() {
		return new ResponseEntity<>(transmitInstrumentService.list(), HttpStatus.OK);
	}

	@Loggable
	@PostMapping
	public ResponseEntity<TransmitInstrumentDTO.Info> create(@Validated @RequestBody TransmitInstrumentDTO.Create request) {
		return new ResponseEntity<>(transmitInstrumentService.create(request), HttpStatus.CREATED);
	}

	@Loggable
	@PutMapping(value = "/{id}")
	public ResponseEntity<TransmitInstrumentDTO.Info> update(@PathVariable Long id, @RequestBody TransmitInstrumentDTO.Update request) {
		return new ResponseEntity<>(transmitInstrumentService.update(id, request), HttpStatus.OK);
	}

	@Loggable
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		transmitInstrumentService.delete(id);
		return new ResponseEntity(HttpStatus.OK);
	}

	@Loggable
	@DeleteMapping(value = "/list")
	public ResponseEntity<Void> delete(@Validated @RequestBody TransmitInstrumentDTO.Delete request) {
		transmitInstrumentService.delete(request);
		return new ResponseEntity(HttpStatus.OK);
	}
 */
	@Loggable
	@GetMapping(value = "/iso-search")
	public ResponseEntity<TotalResponse<TransmitInstrumentDTO.Info>> list(@RequestParam MultiValueMap<String, String> criteria) throws IOException {
		final NICICOCriteria nicicoCriteria = NICICOCriteria.of(criteria);
		if (nicicoCriteria.get_sortBy() == null)
			nicicoCriteria.set_sortBy(Collections.singletonList("id"));
		return new ResponseEntity<>(transmitInstrumentService.search(nicicoCriteria), HttpStatus.OK);
	}
 /*
	@Loggable
	@GetMapping(value = "/search")
	public ResponseEntity<SearchDTO.SearchRs<TransmitInstrumentDTO.Info>> search(@RequestBody SearchDTO.SearchRq request) {
		return new ResponseEntity<>(transmitInstrumentService.search(request), HttpStatus.OK);
	}

 */
}
