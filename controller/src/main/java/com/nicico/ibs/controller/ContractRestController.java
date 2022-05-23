package com.nicico.ibs.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.core.util.file.FileUtil;
import com.nicico.ibs.dto.ContractDTO;
import com.nicico.ibs.iservice.IContractService;
import com.nicico.ibs.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping(value = "/api/contract")
public class ContractRestController {

	@Value("${ibs.upload.dir}")
	private String uploadDir;

	private final IContractService contractService;
	private final FileUtil fileUtil;
	private final ModelMapper modelMapper;

	// ------------------------------
	@Loggable
	@PutMapping(value = "/{id}")
	public ResponseEntity<ContractDTO.Info> update(@PathVariable Long id, Principal principal) throws IOException {

		UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
		User u= modelMapper.map( a.getPrincipal(), User.class);

		return new ResponseEntity<>(contractService.update(id,u.getSupplierId()), HttpStatus.OK);
	}

	@Loggable
	@GetMapping(value = "/iso-search")
	public ResponseEntity<TotalResponse<ContractDTO.Info>> list(@RequestParam MultiValueMap<String, String> criteria,Principal principal) throws IOException {
		UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
		User u= modelMapper.map( a.getPrincipal(), User.class);

		String value="{ \"operator\":\"and\", \"criteria\" : [  { \"fieldName\":\"supplierId\", \"operator\":\"equals\", \"value\": \""+u.getSupplierId()+"\"  } , " +
				criteria.get("criteria").get(0)+" ] } ";
		criteria.remove("criteria");
		criteria.add("criteria",value);
		final NICICOCriteria nicicoCriteria = NICICOCriteria.of(criteria);
		if (nicicoCriteria.get_sortBy() == null)
			nicicoCriteria.set_sortBy(Collections.singletonList("id"));
		return new ResponseEntity<>(contractService.search(nicicoCriteria), HttpStatus.OK);
	}

	@Loggable
	@GetMapping(value = "/downloadFile")
	public void downloadFile(@RequestParam String data, HttpServletRequest request, HttpServletResponse response,Principal principal) {
		try {
			UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
			User u= modelMapper.map( a.getPrincipal(), User.class);

			ContractDTO.Info contarct= contractService.update(new Long(data),u.getSupplierId());
			String downloadFileName=contarct.getFileNewName();
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
