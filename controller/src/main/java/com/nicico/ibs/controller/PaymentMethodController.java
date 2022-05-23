package com.nicico.ibs.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.ibs.dto.PaymentMethodDTO;
import com.nicico.ibs.iservice.IPaymentMethodService;
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
@RequestMapping(value = "/api/paymentMethod")
public class PaymentMethodController {

	private final IPaymentMethodService paymentMethodService;

	// ------------------------------
/*
	@Loggable
	@GetMapping(value = "/{id}")
	public ResponseEntity<PaymentMethodDTO.Info> get(@PathVariable Long id) {
		return new ResponseEntity<>(paymentMethodService.get(id), HttpStatus.OK);
	}

	@Loggable
	@GetMapping(value = "/list")
	public ResponseEntity<List<PaymentMethodDTO.Info>> list() {
		return new ResponseEntity<>(paymentMethodService.list(), HttpStatus.OK);
	}

	@Loggable
	@PostMapping
	public ResponseEntity<PaymentMethodDTO.Info> create(@Validated @RequestBody PaymentMethodDTO.Create request) {
		return new ResponseEntity<>(paymentMethodService.create(request), HttpStatus.CREATED);
	}

	@Loggable
	@PutMapping(value = "/{id}")
	public ResponseEntity<PaymentMethodDTO.Info> update(@PathVariable Long id, @RequestBody PaymentMethodDTO.Update request) {
		return new ResponseEntity<>(paymentMethodService.update(id, request), HttpStatus.OK);
	}

	@Loggable
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		paymentMethodService.delete(id);
		return new ResponseEntity(HttpStatus.OK);
	}

	@Loggable
	@DeleteMapping(value = "/list")
	public ResponseEntity<Void> delete(@Validated @RequestBody PaymentMethodDTO.Delete request) {
		paymentMethodService.delete(request);
		return new ResponseEntity(HttpStatus.OK);
	}
 */
	@Loggable
	@GetMapping(value = "/iso-search")
	public ResponseEntity<TotalResponse<PaymentMethodDTO.Info>> list(@RequestParam MultiValueMap<String, String> criteria) throws IOException {
		final NICICOCriteria nicicoCriteria = NICICOCriteria.of(criteria);
		if (nicicoCriteria.get_sortBy() == null)
			nicicoCriteria.set_sortBy(Collections.singletonList("id"));
		return new ResponseEntity<>(paymentMethodService.search(nicicoCriteria), HttpStatus.OK);
	}
 /*
	@Loggable
	@GetMapping(value = "/search")
	public ResponseEntity<SearchDTO.SearchRs<PaymentMethodDTO.Info>> search(@RequestBody SearchDTO.SearchRq request) {
		return new ResponseEntity<>(paymentMethodService.search(request), HttpStatus.OK);
	}

 */
}
