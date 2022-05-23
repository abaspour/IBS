package com.nicico.ibs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.core.util.file.FileUtil;
import com.nicico.ibs.dto.RegisterPriceHeaderDTO;
import com.nicico.ibs.iservice.IRegisterPriceHeaderService;
import com.nicico.ibs.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/registerPriceHeader")
public class RegisterPriceHeaderRestController {

	@Value("${ibs.upload.dir}")
	private String uploadDir;

	private final IRegisterPriceHeaderService registerPriceHeaderService;
	private final ObjectMapper objectMapper;
	private final FileUtil fileUtil;
	private final ModelMapper modelMapper;

	// ------------------------------

//	@Loggable
//	@GetMapping(value = "/{id}")
//	public ResponseEntity<RegisterPriceHeaderDTO.Info> get(@PathVariable Long id) {
//		return new ResponseEntity<>(registerPriceHeaderService.get(id), HttpStatus.OK);
//	}

//	@Loggable
//	@GetMapping(value = "/list")
//	public ResponseEntity<List<RegisterPriceHeaderDTO.Info>> list() {
//		return new ResponseEntity<>(registerPriceHeaderService.list(), HttpStatus.OK);
//	}
//
//	@Loggable
//	@PostMapping
//	public ResponseEntity<RegisterPriceHeaderDTO.Info> create(@Validated @RequestBody RegisterPriceHeaderDTO.Create request) {
//		return new ResponseEntity<>(registerPriceHeaderService.create(request), HttpStatus.CREATED);
//	}
//
	@Loggable
	@PutMapping(value = "/{id}")
	public ResponseEntity<RegisterPriceHeaderDTO.Info> create(@PathVariable Long id, @Validated @RequestParam("data") String requestJSON,
															  @RequestParam(value = "file", required = false) MultipartFile file, Principal principal) throws IOException {

		UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
		User u= modelMapper.map( a.getPrincipal(), User.class);

		RegisterPriceHeaderDTO.Update request = objectMapper.readValue(requestJSON, RegisterPriceHeaderDTO.Update.class);
		return new ResponseEntity<>(registerPriceHeaderService.update(id,request , file,u.getSupplierId()), HttpStatus.OK);
	}
//
//	@Loggable
//	@DeleteMapping(value = "/{id}")
//	public ResponseEntity<Void> delete(@PathVariable Long id) {
//		registerPriceHeaderService.delete(id);
//		return new ResponseEntity(HttpStatus.OK);
//	}
//
//	@Loggable
//	@DeleteMapping(value = "/list")
//	public ResponseEntity<Void> delete(@Validated @RequestBody RegisterPriceHeaderDTO.Delete request) {
//		registerPriceHeaderService.delete(request);
//		return new ResponseEntity(HttpStatus.OK);
//	}

	@Loggable
	@GetMapping(value = "/iso-search")
	public ResponseEntity<TotalResponse<RegisterPriceHeaderDTO.Info>> list(@RequestParam MultiValueMap<String, String> criteria,Principal principal) throws IOException {
		UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
		User u= modelMapper.map( a.getPrincipal(), User.class);

		String value="{ \"operator\":\"and\", \"criteria\" : [  { \"fieldName\":\"supplierId\", \"operator\":\"equals\", \"value\": \""+u.getSupplierId()+"\"  } , " +
//				"  { \"fieldName\":\"endReplyDate\", \"operator\":\"greaterOrEqual\", \"value\": \""+dateTimeStrRs.getDate()+"\"  } , " +
//				"  {  \"operator\": \"or\", \"criteria\": [{ \"fieldName\" : \"verifyStatus\", \"operator\": \"equals\", \"value\" : \"n\" }, " +
//				"           { \"fieldName\" : \"verifyStatus\", \"operator\": \"equals\", \"value\" : \"v\" } ]  }, "+
				criteria.get("criteria").get(0)+" ] } ";
		criteria.remove("criteria");
		criteria.add("criteria",value);
		final NICICOCriteria nicicoCriteria = NICICOCriteria.of(criteria);
		if (nicicoCriteria.get_sortBy() == null)
			nicicoCriteria.set_sortBy(Collections.singletonList("id"));
		return new ResponseEntity<>(registerPriceHeaderService.search(nicicoCriteria), HttpStatus.OK);
	}

//	@Loggable
//	@GetMapping(value = "/search")
//	public ResponseEntity<SearchDTO.SearchRs<RegisterPriceHeaderDTO.Info>> search(@RequestBody SearchDTO.SearchRq request) {
//		return new ResponseEntity<>(registerPriceHeaderService.search(request), HttpStatus.OK);
//	}

	@Loggable
	@GetMapping(value = "/downloadFile")
	public void downloadFile(@RequestParam String data, HttpServletRequest request, HttpServletResponse response,Principal principal) {
		try {
			UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
			User u= modelMapper.map( a.getPrincipal(), User.class);

			String downloadFileName=registerPriceHeaderService.getDownloadFileName(data,u.getSupplierId());
			if (downloadFileName==null || downloadFileName.equalsIgnoreCase(""))
				return;
			String filePath;
			String dir=data.startsWith("map") ? "map" : "company50";
			filePath = uploadDir + "E2D0785E-18CC-4855-B4D3-4B2F040E43F9.JPG";//data;
			File downloadFile = new File(filePath);
			FileInputStream inputStream = new FileInputStream(downloadFile);

			ServletContext context = request.getServletContext();
			String mimeType = context.getMimeType(filePath);
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			response.setContentType(mimeType);
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFileName);
			response.setHeader(headerKey, headerValue);
			response.setContentLength((int) downloadFile.length());
			OutputStream outputStream = response.getOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.flush();
			inputStream.close();
			fileUtil.download(downloadFileName, response);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

}
