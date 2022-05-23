package com.nicico.ibs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.ibs.dto.CountryDTO;
import com.nicico.ibs.dto.RegisterDTO;
import com.nicico.ibs.iservice.ICountryService;
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
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/anonymous")
public class AnonymousRestController {

	private final ObjectMapper objectMapper;
	private final IRegisterService registerService;
	private final ICountryService countryService;

	// ------------------------------

	@Loggable
	@PostMapping("/register")
	public ResponseEntity<RegisterDTO.Info> create(@Validated @RequestParam("data") String requestJSON, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
		RegisterDTO.Create request = objectMapper.readValue(requestJSON, RegisterDTO.Create.class);
		if (file != null)
			return new ResponseEntity<>(registerService.attach(request, file), HttpStatus.CREATED);
		else
			return new ResponseEntity<>(registerService.create(request), HttpStatus.CREATED);
	}

	@Loggable
	@GetMapping(value = "/register/iso-search")
	public ResponseEntity<TotalResponse<RegisterDTO.InfoTuple>> registerList(@RequestParam String criteria) {
		return new ResponseEntity<>(registerService.searchInfoTuple(criteria), HttpStatus.OK);
	}

	@Loggable
	@GetMapping(value = "/country/iso-search")
	public ResponseEntity<TotalResponse<CountryDTO.Info>> countryList(@RequestParam MultiValueMap<String, String> criteria) throws IOException {
		String value="{ \"operator\":\"and\", \"criteria\" : [  { \"fieldName\":\"isActive\", \"operator\":\"equals\", \"value\": \"y\"  }  " +
//				"  { \"fieldName\":\"endReplyDate\", \"operator\":\"greaterOrEqual\", \"value\": \""+dateTimeStrRs.getDate()+"\"  } , " +
//				"  {  \"operator\": \"or\", \"criteria\": [{ \"fieldName\" : \"verifyStatus\", \"operator\": \"equals\", \"value\" : \"n\" }, " +
//				"           { \"fieldName\" : \"verifyStatus\", \"operator\": \"equals\", \"value\" : \"v\" } ]  } "+
				(criteria!=null && criteria.size()>0 ? ','+criteria.get("criteria").get(0) : ' ' )+" ] } ";
		criteria.remove("criteria");
		criteria.add("criteria",value);
		final NICICOCriteria nicicoCriteria = NICICOCriteria.of(criteria);
		if (nicicoCriteria.get_sortBy() == null)
			nicicoCriteria.set_sortBy(Collections.singletonList("countryNameEn"));
		return new ResponseEntity<>(countryService.search(nicicoCriteria), HttpStatus.OK);
	}
}
