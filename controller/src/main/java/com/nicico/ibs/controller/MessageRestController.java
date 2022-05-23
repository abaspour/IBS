package com.nicico.ibs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.core.util.file.FileUtil;
import com.nicico.ibs.dto.MessageDTO;
import com.nicico.ibs.iservice.IMessageService;
import com.nicico.ibs.model.User;
import com.nicico.ibs.repository.InquiryDAO;
import com.nicico.ibs.repository.MessageDAO;
import com.nicico.ibs.repository.PlaceDAO;
import com.nicico.ibs.repository.UnitDAO;
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
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/message")
public class MessageRestController {

	@Value("${ibs.upload.dir}")
	private String uploadDir;

	private final IMessageService messageService;
	private final ObjectMapper objectMapper;
	private final FileUtil fileUtil;
	private final ModelMapper modelMapper;
	private final InquiryDAO inquiryDAO;
	private final MessageDAO messageDAO;
	private final PlaceDAO placeDAO;
	private final UnitDAO unitDAO;

	// ------------------------------

//	@Loggable
//	@GetMapping(value = "/{id}")
//	public ResponseEntity<MessageDTO.Info> get(@PathVariable Long id) {
//		return new ResponseEntity<>(messageService.get(id), HttpStatus.OK);
//	}

//	@Loggable
//	@GetMapping(value = "/list")
//	public ResponseEntity<List<MessageDTO.Info>> list() {
//		return new ResponseEntity<>(messageService.list(), HttpStatus.OK);
//	}
//
	@Loggable
	@PostMapping
	public ResponseEntity<MessageDTO.Info> create(@Validated @RequestParam("data") String requestJSON, @RequestParam(value = "file", required = false) MultipartFile file,Principal principal) throws IOException {
		MessageDTO.Create request = objectMapper.readValue(requestJSON, MessageDTO.Create.class);

		UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
		User u= modelMapper.map( a.getPrincipal(), User.class);

		return new ResponseEntity<>(messageService.create(request,file,u), HttpStatus.CREATED);
	}

	@Loggable
	@PutMapping(value = "/{id}")
	public ResponseEntity<MessageDTO.Info> update(@PathVariable Long id, @Validated @RequestParam("data") String requestJSON, Principal principal) throws IOException {

		UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
		User u= modelMapper.map( a.getPrincipal(), User.class);

		Map request = objectMapper.readValue(requestJSON, Map.class);
		return new ResponseEntity<>(messageService.update(id,request ,u.getId()), HttpStatus.OK);
	}
//
//	@Loggable
//	@DeleteMapping(value = "/{id}")
//	public ResponseEntity<Void> delete(@PathVariable Long id) {
//		messageService.delete(id);
//		return new ResponseEntity(HttpStatus.OK);
//	}
//
//	@Loggable
//	@DeleteMapping(value = "/list")
//	public ResponseEntity<Void> delete(@Validated @RequestBody MessageDTO.Delete request) {
//		messageService.delete(request);
//		return new ResponseEntity(HttpStatus.OK);
//	}

	@Loggable
	@GetMapping(value = "/iso-search")
	public ResponseEntity<TotalResponse<MessageDTO.Info>> list(@RequestParam MultiValueMap<String, String> criteria,Principal principal) throws IOException {
		UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
		User u= modelMapper.map( a.getPrincipal(), User.class);

		String value="{ \"operator\":\"and\", \"criteria\" : [  " +
				"  {  \"operator\": \"or\", \"criteria\": [{ \"fieldName\" : \"receiverId\", \"operator\": \"equals\", \"value\" : \""+u.getId()+"\" }, " +
				"           { \"fieldName\" : \"senderId\", \"operator\": \"equals\", \"value\" : \""+u.getId()+"\" } ]  }, "+
				criteria.get("criteria").get(0)+" ] } ";
		criteria.remove("criteria");
		criteria.add("criteria",value);
		final NICICOCriteria nicicoCriteria = NICICOCriteria.of(criteria);
		if (nicicoCriteria.get_sortBy() == null)
			nicicoCriteria.set_sortBy(Collections.singletonList("id"));
		return new ResponseEntity<>(messageService.search(nicicoCriteria), HttpStatus.OK);
	}

//	@Loggable
//	@GetMapping(value = "/search")
//	public ResponseEntity<SearchDTO.SearchRs<MessageDTO.Info>> search(@RequestBody SearchDTO.SearchRq request) {
//		return new ResponseEntity<>(messageService.search(request), HttpStatus.OK);
//	}

	@Loggable
	@GetMapping(value = "/downloadFile")
	public void downloadFile(@RequestParam String data, HttpServletRequest request, HttpServletResponse response,Principal principal) {
		try {
			UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
			User u= modelMapper.map( a.getPrincipal(), User.class);

			String downloadFileName=messageService.getDownloadFileName(data,u.getSupplierId());
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
