package com.nicico.ibs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.core.util.file.FileUtil;
import com.nicico.ibs.dto.RegisterPriceDTO;
import com.nicico.ibs.iservice.IRegisterPriceService;
import com.nicico.ibs.model.ReportTransmiters;
import com.nicico.ibs.model.User;
import com.nicico.ibs.service.QcfInspectionFormsService;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/registerPrice")
public class RegisterPriceRestController {

	@Value("${ibs.upload.dir}")
	private String uploadDir;

	private final IRegisterPriceService registerPriceService;
	private final QcfInspectionFormsService qcfInspectionFormsService;
	private final ObjectMapper objectMapper;
	private final ModelMapper modelMapper;
	private final FileUtil fileUtil;

	// ------------------------------
	private String path = "";
	private String reqNumber = "";
	private Long reqItemId = null;
	private Long count = new Long(1);
	PdfTemplate total;


//	@Loggable
//	@GetMapping(value = "/{id}")
//	public ResponseEntity<RegisterPriceDTO.Info> get(@PathVariable Long id) {
//		return new ResponseEntity<>(registerPriceService.get(id), HttpStatus.OK);
//	}

//	@Loggable
//	@GetMapping(value = "/list")
//	public ResponseEntity<List<RegisterPriceDTO.Info>> list() {
//		return new ResponseEntity<>(registerPriceService.list(), HttpStatus.OK);
//	}
//
//	@Loggable
//	@PostMapping
//	public ResponseEntity<RegisterPriceDTO.Info> create(@Validated @RequestBody RegisterPriceDTO.Create request) {
//		return new ResponseEntity<>(registerPriceService.create(request), HttpStatus.CREATED);
//	}

	@Loggable
	@PutMapping(value = "/{id}")
	public ResponseEntity<RegisterPriceDTO.Info> create(@PathVariable Long id, @Validated @RequestParam("data") String requestJSON,
														@RequestParam(value = "file", required = false) MultipartFile file, Principal principal) throws IOException {
		UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
		User u= modelMapper.map( a.getPrincipal(), User.class);

		RegisterPriceDTO.Update request = objectMapper.readValue(requestJSON, RegisterPriceDTO.Update.class);
		return new ResponseEntity<>(registerPriceService.update(id,request , file,u.getSupplierId()), HttpStatus.OK);
	}

//	@Loggable
//	@DeleteMapping(value = "/{id}")
//	public ResponseEntity<Void> delete(@PathVariable Long id) {
//		registerPriceService.delete(id);
//		return new ResponseEntity(HttpStatus.OK);
//	}
//
//	@Loggable
//	@DeleteMapping(value = "/list")
//	public ResponseEntity<Void> delete(@Validated @RequestBody RegisterPriceDTO.Delete request) {
//		registerPriceService.delete(request);
//		return new ResponseEntity(HttpStatus.OK);
//	}

	@Loggable
	@GetMapping(value = "/iso-search")
	public ResponseEntity<TotalResponse<RegisterPriceDTO.Info>> list(@RequestParam MultiValueMap<String, String> criteria,Principal principal) throws IOException {
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
		return new ResponseEntity<>(registerPriceService.search(nicicoCriteria), HttpStatus.OK);
	}

//	@Loggable
//	@GetMapping(value = "/search")
//	public ResponseEntity<SearchDTO.SearchRs<RegisterPriceDTO.Info>> search(@RequestBody SearchDTO.SearchRq request) {
//		return new ResponseEntity<>(registerPriceService.search(request), HttpStatus.OK);
//	}

	@Loggable
	@GetMapping(value = "/downloadFile")
	public void downloadFile(@RequestParam String data, HttpServletRequest request, HttpServletResponse response,Principal principal) {
		try {
			UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
			User u= modelMapper.map( a.getPrincipal(), User.class);

			String downloadFileName=registerPriceService.getDownloadFileName(data,u.getSupplierId());
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
	@Loggable
	@GetMapping(value = "/pdf")
	public void execute(HttpServletRequest request, HttpServletResponse response) {

		try {
			ReportTransmiters[] tblQcf =null;
			ReportTransmiters[] tblCertificate =null;
			ReportTransmiters[] tblQuality =null;
			ReportTransmiters[] tblQuantity = null;
			ReportTransmiters[] tblInspection =null;

			count = new Long(1);

//			Map criteria = new HashMap();
			if(request.getParameter("reqItemId")!= null){
				reqItemId = new Long(request.getParameter("reqItemId").toString());
//				criteria.put("reqItemId", reqItemId);
			}
			tblQcf = qcfInspectionFormsService.findAllQcf(reqItemId);

			if(tblQcf!= null && tblQcf.length>0)
			{
//				criteria.put("qcfId",tblQcf[0].getQcfId());
				reqNumber = tblQcf[0].getReqNumber();

				tblCertificate = qcfInspectionFormsService.findAllQcCertificate(tblQcf[0].getQcfId());
				tblQuality = qcfInspectionFormsService.findAllQcfQuality(tblQcf[0].getQcfId());
				tblQuantity = qcfInspectionFormsService.findAllQcfQuantity(tblQcf[0].getQcfId());
				tblInspection = qcfInspectionFormsService.findAllQcfInspection(tblQcf[0].getQcfId());

				String path = "C:\\IdeaProjects\\ibs\\web\\src\\assets";// this.getServlet().getServletContext().getRealPath("/");
				this.path = path;

				ServletOutputStream out = response.getOutputStream();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				baos = pdfFx(tblQcf,tblCertificate,tblQuality,tblQuantity,tblInspection);
				response.setContentType("application/pdf");
				response.setContentLength(baos.size());
				baos.writeTo(out);
				out.flush();
			}
			else
				return ;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ;
	}

	class TableHeader extends PdfPageEventHelper {

		PdfTemplate total;

		public void onOpenDocument(PdfWriter writer, Document document) {
			total = writer.getDirectContent().createTemplate(30, 16);
		}

		public void onStartPage(PdfWriter writer, Document document) {
			try {
				BaseFont bfk = BaseFont.createFont(path + "/fonts/"+ "karnik.ttf", BaseFont.IDENTITY_H, true);

				Font f2 = new Font(bfk, 18, Font.BOLD, Color.BLACK);

//				RegisterPriceDelegate registerPriceDelegate = new RegisterPriceDelegate();
//				TblCompanyInfo tblCompanyInfo = new TblCompanyInfo();
//				tblCompanyInfo = registerPriceDelegate.companyInfoFindAll();
				PdfPTable headerTable = new PdfPTable(3);
				headerTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				PdfPCell cell = new PdfPCell();
				cell.setPadding(4);
				cell.setBorderWidth(0);
				cell.setPaddingBottom(2);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);

				String file = path + "/images/logo_khadamat.png";
//				String file = path + tblCompanyInfo.getLogo2();
				Image image = Image.getInstance(file);
				image.scalePercent(15);
				Chunk chk = new Chunk(image, 0, 0);
				cell.setPhrase(new Phrase(chk));
				headerTable.addCell(cell);

				cell.setPhrase(new Phrase(10, "طرح کنترل کيفيت", f2));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				headerTable.addCell(cell);


				String file2 = path + "/images/arm.gif";
//				String file2 = path + tblCompanyInfo.getLogo();
				Image image2 = Image.getInstance(file2);
				image2.scalePercent(15);
				Chunk chk2 = new Chunk(image2, 0, 0);
				cell.setPhrase(new Phrase(chk2));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				headerTable.addCell(cell);

				headerTable.setTotalWidth(500);
				headerTable.writeSelectedRows(0, 10, 40, 820 ,
						writer.getDirectContent());


			} catch (Exception de) {
				throw new ExceptionConverter(de);
			}
		}
		public void onEndPage(PdfWriter writer, Document document) {
			try {
				BaseFont bfk = BaseFont.createFont(path + "/fonts/" + "karnik.ttf",BaseFont.IDENTITY_H, true);

				Font f2 = new Font(bfk, 10, Font.NORMAL, Color.BLACK);

				PdfPTable headerTable = new PdfPTable(6);
				headerTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				PdfPCell cell = new PdfPCell();
				cell.setPadding(4);
				cell.setBorderWidth(0);
				cell.setPaddingBottom(2);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setColspan(2);
				cell.setBorderWidthTop(1);

				cell.setPhrase(new Phrase(10, "", f2));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				headerTable.addCell(cell);
				cell.setPhrase(new Phrase(10,  ""+document.getPageNumber(), f2));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				headerTable.addCell(cell);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPhrase(new Phrase(10, "", f2)); //F-47-30/02
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				headerTable.addCell(cell);

				headerTable.setTotalWidth(515);
				headerTable.writeSelectedRows(0, 10, 40, 50 ,
						writer.getDirectContent());

			} catch (Exception de) {
				throw new ExceptionConverter(de);
			}
		}
		public void onCloseDocument(PdfWriter writer, Document document) {
			ColumnText.showTextAligned(total, Element.ALIGN_LEFT, new Phrase(
					String.valueOf(writer.getPageNumber() - 1)), 2, 2, 0);
		}
	}


	private ByteArrayOutputStream pdfFx(ReportTransmiters[] tblQcf,ReportTransmiters[] tblCertificate,ReportTransmiters[] tblQuality,ReportTransmiters[] tblQuantity,ReportTransmiters[] tblInspection) {
		Document document = new Document(PageSize.A4, 10, 10, 80, 40);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			TableHeader event = new TableHeader();
			writer.setPageEvent(event);
			document.open();

			BaseFont bf = BaseFont.createFont(path + "/fonts/" + "karnik.ttf",BaseFont.IDENTITY_H, true);
			Font f7 = new Font(bf, 12, Font.NORMAL, Color.BLACK);

//			RegisterPriceDelegate registerPriceDelegate = new RegisterPriceDelegate();
//			TblCompanyInfo tblCompanyInfo = new TblCompanyInfo();
//			tblCompanyInfo = registerPriceDelegate.companyInfoFindAll();

			// Start Header Images
			PdfPCell cell = new PdfPCell();
			cell.setPadding(4);
			cell.setBorderWidth(0);
			cell.setPaddingBottom(2);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);

			//Start Definition for desc
			PdfPTable definitions = new PdfPTable(8);
			definitions.setWidthPercentage(90);
			definitions.setSpacingAfter(20f);
			definitions.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			definitions.setSplitLate(true);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(4);
			cell.setBorderWidth(0);
			cell.setPaddingBottom(6);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

//            cell.setPhrase(new Phrase(10,"شماره درخواست:" + tblQcf[0].getReqNumber(), f7));
//            cell.setColspan(2);
//            cell.setBorderWidthTop(1);
//            cell.setBorderWidthLeft(1);
//            cell.setBorderWidthRight(1);
//            cell.setBorderWidthBottom(0);
//            definitions.addCell(cell);

			cell.setPhrase(new Phrase(10,"آيتم:"+ tblQcf[0].getRequestitemrow(), f7));
			cell.setColspan(4);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(1);
			cell.setBorderWidthBottom(0);
			definitions.addCell(cell);

			cell.setPhrase(new Phrase(10,"تعداد کل درخواست:"+ tblQcf[0].getItemamount()+" "+tblQcf[0].getName_fa(), f7));
			cell.setColspan(2);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(1);
			cell.setBorderWidthBottom(0);
			definitions.addCell(cell);

			cell.setPhrase(new Phrase(10,"شماره استاک:"+ tblQcf[0].getMaterialCode(), f7));
			cell.setColspan(2);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(1);
			cell.setBorderWidthBottom(0);
			definitions.addCell(cell);

			cell.setPhrase(new Phrase(10,"شرح کالا:"+ tblQcf[0].getDescP(), f7));
			cell.setColspan(4);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(1);
			cell.setBorderWidthBottom(0);
			definitions.addCell(cell);

			cell.setPhrase(new Phrase(10,"شماره و ويرايش نقشه ها:"+"\n"+ (tblQcf[0].getMapNumber()!= null?tblQcf[0].getMapNumber():""), f7));
			cell.setColspan(4);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(1);
			cell.setBorderWidthBottom(0);
			definitions.addCell(cell);

			String deliveryBase =  "";
			if(tblQcf[0].getEngDeliveryBase()!= null)
			{
				if(tblQcf[0].getEngDeliveryBase().equalsIgnoreCase("nt"))
					deliveryBase = "نقشه وطرح کنترل";
				else if(tblQcf[0].getEngDeliveryBase().equalsIgnoreCase("st"))
					deliveryBase = "نمونه و طرح کنترل";
				else if(tblQcf[0].getEngDeliveryBase().equalsIgnoreCase("nst"))
					deliveryBase = "نقشه و نمونه و طرح کنترل";
				else if(tblQcf[0].getEngDeliveryBase().equalsIgnoreCase("t"))
					deliveryBase = "طرح کنترل";
			}
			cell.setPhrase(new Phrase(10,"مبناي تحويل:"+ deliveryBase, f7));
			cell.setColspan(8);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(1);
			cell.setBorderWidthBottom(1);
			definitions.addCell(cell);

			//Start Definition for desc
			PdfPTable certificate = new PdfPTable(8);
			certificate.setWidthPercentage(90);
			certificate.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			certificate.setSplitLate(true);
			certificate.setSpacingAfter(20f);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(4);
			cell.setBorderWidth(0);
			cell.setPaddingBottom(6);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			for (int i = 0; i < tblCertificate.length; i++)
			{
				cell.setPhrase(new Phrase(10,"شماره رديف :"+count++, f7));
				cell.setColspan(2);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				certificate.addCell(cell);

				cell.setPhrase(new Phrase(10,"مدارک و گواهينامه هاي مورد نياز و استانداردهاي کلي بازرسي و توليد : "+ tblCertificate[i].getCertificateName(), f7));
				cell.setColspan(6);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				certificate.addCell(cell);

				cell.setPhrase(new Phrase(10,"شرح", f7));
				cell.setColspan(6);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				certificate.addCell(cell);

				cell.setPhrase(new Phrase(10,"استاندارد", f7));
				cell.setColspan(2);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				certificate.addCell(cell);

//				QcfInspectionFormsService daoImpl = new QcfInspectionFormsService();
//				Map criteria = new HashMap();
				ReportTransmiters[] tblCertificateItem = null;
//				criteria.put("qtId", tblCertificate[i].getCertificateId());
				tblCertificateItem = qcfInspectionFormsService.findAllQcfCertificateItem(tblCertificate[i].getCertificateId());
				for (int j = 0; j < tblCertificateItem.length; j++)
				{

					cell.setPhrase(new Phrase(10,tblCertificateItem[j].getCertificateItemName(), f7));
					cell.setColspan(6);
					cell.setBorderWidthTop(1);
					cell.setBorderWidthLeft(1);
					cell.setBorderWidthRight(1);
					cell.setBorderWidthBottom(0);
					certificate.addCell(cell);

					cell.setPhrase(new Phrase(10,tblCertificateItem[j].getCertificateStandard(), f7));
					cell.setColspan(2);
					cell.setBorderWidthTop(1);
					cell.setBorderWidthLeft(1);
					cell.setBorderWidthRight(1);
					cell.setBorderWidthBottom(0);
					certificate.addCell(cell);
				}
				cell.setPhrase(new Phrase(10,"توضيحات: "+ tblCertificate[i].getCertificateXcomment(), f7));
				cell.setColspan(8);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(1);
				certificate.addCell(cell);
			}
			//Start Definition for desc
			PdfPTable quantity = new PdfPTable(8);
			quantity.setWidthPercentage(90);
			quantity.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			quantity.setSplitLate(true);
			quantity.setSpacingAfter(20f);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(4);
			cell.setBorderWidth(0);
			cell.setPaddingBottom(6);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

			for (int i = 0; i < tblQuantity.length; i++)
			{

				cell.setPhrase(new Phrase(10,"شماره رديف :"+ count++, f7));
				cell.setColspan(2);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				quantity.addCell(cell);

				cell.setPhrase(new Phrase(10,"شرح فعاليت / تست : "+ tblQuantity[i].getQuantityName(), f7));
				cell.setColspan(6);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				quantity.addCell(cell);

				cell.setPhrase(new Phrase(10,"پارامتر" , f7));
				cell.setColspan(2);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				quantity.addCell(cell);

				cell.setPhrase(new Phrase(10,"مقادير مجاز", f7));
				cell.setColspan(2);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				quantity.addCell(cell);

				cell.setPhrase(new Phrase(10,"استاندارد", f7));
				cell.setColspan(2);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				quantity.addCell(cell);

				cell.setPhrase(new Phrase(10,"ابزار", f7));
				cell.setColspan(2);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				quantity.addCell(cell);

//				QcfInspectionFormsService daoImpl = new QcfInspectionFormsService();
//				Map criteria = new HashMap();
				ReportTransmiters[] tblQuantityItem = null;
//				criteria.put("qtId", tblQuantity[i].getQuantityId());

				tblQuantityItem = qcfInspectionFormsService.findAllQcfQuantityItem(tblQuantity[i].getQuantityId());
				for (int j = 0; j < tblQuantityItem.length; j++)
				{
					cell.setPhrase(new Phrase(10,tblQuantityItem[j].getQuantityParameter(), f7));
					cell.setColspan(2);
					cell.setBorderWidthTop(1);
					cell.setBorderWidthLeft(1);
					cell.setBorderWidthRight(1);
					cell.setBorderWidthBottom(0);
					quantity.addCell(cell);

					cell.setPhrase(new Phrase(10,tblQuantityItem[j].getMaxValue()+ " - " + tblQuantityItem[j].getMinValue(), f7));
					cell.setColspan(2);
					cell.setBorderWidthTop(1);
					cell.setBorderWidthLeft(1);
					cell.setBorderWidthRight(1);
					cell.setBorderWidthBottom(0);
					quantity.addCell(cell);

					cell.setPhrase(new Phrase(10,tblQuantityItem[j].getQuantitySandard(), f7));
					cell.setColspan(2);
					cell.setBorderWidthTop(1);
					cell.setBorderWidthLeft(1);
					cell.setBorderWidthRight(1);
					cell.setBorderWidthBottom(0);
					quantity.addCell(cell);

					cell.setPhrase(new Phrase(10,tblQuantityItem[j].getQuantityTools(), f7));
					cell.setColspan(2);
					cell.setBorderWidthTop(1);
					cell.setBorderWidthLeft(1);
					cell.setBorderWidthRight(1);
					cell.setBorderWidthBottom(0);
					quantity.addCell(cell);
				}
				cell.setPhrase(new Phrase(10,"توضيحات: "+ tblQuantity[i].getQuantityXcomment(), f7));
				cell.setColspan(8);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(1);
				quantity.addCell(cell);

//	            cell.setPhrase(new Phrase(10,"", f7));
//	            cell.setColspan(8);
//	            cell.setBorderWidthTop(0);
//	            cell.setBorderWidthLeft(0);
//	            cell.setBorderWidthRight(0);
//	            cell.setBorderWidthBottom(0);
//	            quantity.addCell(cell);
			}
			//Start Definition for desc
			PdfPTable quality = new PdfPTable(8);
			quality.setWidthPercentage(90);
			quality.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			quality.setSplitLate(true);
			quality.setSpacingAfter(20f);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(4);
			cell.setBorderWidth(0);
			cell.setPaddingBottom(6);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

			for (int i = 0; i < tblQuality.length; i++)
			{

				cell.setPhrase(new Phrase(10,"شماره رديف :"+count++, f7));
				cell.setColspan(2);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				quality.addCell(cell);

				cell.setPhrase(new Phrase(10,"شرح فعاليت / تست : "+ tblQuality[i].getQualityName(), f7));
				cell.setColspan(6);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				quality.addCell(cell);

				cell.setPhrase(new Phrase(10,"شرح", f7));
				cell.setColspan(4);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				quality.addCell(cell);

				cell.setPhrase(new Phrase(10,"استاندارد", f7));
				cell.setColspan(2);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				quality.addCell(cell);

				cell.setPhrase(new Phrase(10,"ابزار", f7));
				cell.setColspan(2);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(0);
				quality.addCell(cell);

//				QcfInspectionFormsService daoImpl = new QcfInspectionFormsService();
//				Map criteria = new HashMap();
				ReportTransmiters[] tblQualityItem = null;
//				criteria.put("qtId", tblQuality[i].getQualityId());
				tblQualityItem = qcfInspectionFormsService.findAllQcfQualityItem( tblQuality[i].getQualityId());

				for (int j = 0; j < tblQualityItem.length; j++)
				{
					cell.setPhrase(new Phrase(10,tblQualityItem[j].getQualityItemName(), f7));
					cell.setColspan(4);
					cell.setBorderWidthTop(1);
					cell.setBorderWidthLeft(1);
					cell.setBorderWidthRight(1);
					cell.setBorderWidthBottom(0);
					quality.addCell(cell);

					cell.setPhrase(new Phrase(10,tblQualityItem[j].getQualitySandard(), f7));
					cell.setColspan(2);
					cell.setBorderWidthTop(1);
					cell.setBorderWidthLeft(1);
					cell.setBorderWidthRight(1);
					cell.setBorderWidthBottom(0);
					quality.addCell(cell);

					cell.setPhrase(new Phrase(10,tblQualityItem[j].getQualityTools(), f7));
					cell.setColspan(2);
					cell.setBorderWidthTop(1);
					cell.setBorderWidthLeft(1);
					cell.setBorderWidthRight(1);
					cell.setBorderWidthBottom(0);
					quality.addCell(cell);
				}
				cell.setPhrase(new Phrase(10,"توضيحات: "+ tblQuality[i].getQualityXcomment(), f7));
				cell.setColspan(8);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(1);
				quality.addCell(cell);

			}

			//Start Definition for desc
			PdfPTable inspection = new PdfPTable(8);
			inspection.setWidthPercentage(90);
			inspection.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			inspection.setSplitLate(true);
			inspection.setSpacingAfter(20f);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(4);
			cell.setBorderWidth(0);
			cell.setPaddingBottom(6);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

			cell.setPhrase(new Phrase(10,"نام", f7));
			cell.setColspan(4);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(1);
			cell.setBorderWidthBottom(0);
			inspection.addCell(cell);

			cell.setPhrase(new Phrase(10,"توضيحات ", f7));
			cell.setColspan(4);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(1);
			cell.setBorderWidthBottom(1);
			inspection.addCell(cell);

			for (int i = 0; i < tblInspection.length; i++)
			{
				cell.setPhrase(new Phrase(10,tblInspection[i].getInspectionName(), f7));
				cell.setColspan(4);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(1);
				inspection.addCell(cell);

				cell.setPhrase(new Phrase(10, tblInspection[i].getInspectionXcomment(), f7));
				cell.setColspan(4);
				cell.setBorderWidthTop(1);
				cell.setBorderWidthLeft(1);
				cell.setBorderWidthRight(1);
				cell.setBorderWidthBottom(1);
				inspection.addCell(cell);
			}
			document.add(definitions);
			document.add(certificate);
			document.add(quantity);
			document.add(quality);
			document.add(inspection);
			document.close();

		} catch (Exception de) {
			de.printStackTrace();
		}
		return baos;
	}

	private String RTL2LTR(String in) {
		return RTL2LTR(in, "/");
	}

	private String RTL2LTR(String in, String d) {
		ArrayList a = new ArrayList();
		String result = "";
		while (in.indexOf(d) > -1) {
			a.add(in.substring(0, in.indexOf(d)));
			in = in.substring(in.indexOf(d) + 1);
		}
		a.add(in);
		String[] strs = (String[]) a.toArray(new String[a.size()]);
		for (int i = strs.length - 1; i > 0; i--) {
			result += strs[i] + d;
		}

		result += strs[0];
		return result;
	}


}
