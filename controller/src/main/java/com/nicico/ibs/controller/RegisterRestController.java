package com.nicico.ibs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.dto.RegisterDTO;
import com.nicico.ibs.iservice.IRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/register")
public class RegisterRestController {

	private final ObjectMapper objectMapper;
	private final IRegisterService registerService;

	// ------------------------------

	@Loggable
	@GetMapping(value = "/{id}")
	public ResponseEntity<RegisterDTO.Info> get(@PathVariable Long id) {
		return new ResponseEntity<>(registerService.get(id), HttpStatus.OK);
	}

	@Loggable
	@GetMapping(value = "/list")
	public ResponseEntity<List<RegisterDTO.Info>> list() {
		return new ResponseEntity<>(registerService.list(), HttpStatus.OK);
	}

	@Loggable
	@PostMapping
	public ResponseEntity<RegisterDTO.Info> create(@Validated @RequestParam("data") String requestJSON, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
		RegisterDTO.Create request = objectMapper.readValue(requestJSON, RegisterDTO.Create.class);
		if (file != null)
			return new ResponseEntity<>(registerService.attach(request, file), HttpStatus.CREATED);
		else
			return new ResponseEntity<>(registerService.create(request), HttpStatus.CREATED);
	}

	@Loggable
	@PutMapping(value = "/{id}")
	public ResponseEntity<RegisterDTO.Info> update(@PathVariable Long id, @RequestBody RegisterDTO.Update request) {
		return new ResponseEntity<>(registerService.update(id, request), HttpStatus.OK);
	}

	@Loggable
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		registerService.delete(id);
		return new ResponseEntity(HttpStatus.OK);
	}

	@Loggable
	@DeleteMapping(value = "/list")
	public ResponseEntity<Void> delete(@Validated @RequestBody RegisterDTO.Delete request) {
		registerService.delete(request);
		return new ResponseEntity(HttpStatus.OK);
	}

	@Loggable
	@GetMapping(value = "/iso-search")
	public ResponseEntity<TotalResponse<RegisterDTO.Info>> list(@RequestParam MultiValueMap<String, String> criteria) throws IOException {
		final NICICOCriteria nicicoCriteria = NICICOCriteria.of(criteria);
		return new ResponseEntity<>(registerService.search(nicicoCriteria), HttpStatus.OK);
	}

	@Loggable
	@GetMapping(value = "/search")
	public ResponseEntity<SearchDTO.SearchRs<RegisterDTO.Info>> search(@RequestBody SearchDTO.SearchRq request) {
		return new ResponseEntity<>(registerService.search(request), HttpStatus.OK);
	}
}
