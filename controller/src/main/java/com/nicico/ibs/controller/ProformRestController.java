package com.nicico.ibs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.util.date.DateUtil;
import com.nicico.copper.core.util.file.FileUtil;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.ProformDTO;
import com.nicico.ibs.iservice.IProformService;
import com.nicico.ibs.model.*;
import com.nicico.ibs.repository.*;
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
import java.rmi.RemoteException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/proform")
public class ProformRestController {

	@Value("${ibs.upload.dir}")
	private String uploadDir;

	private final IProformService proformService;
	private final ObjectMapper objectMapper;
	private final FileUtil fileUtil;
	private final ModelMapper modelMapper;
	private final InquiryDAO inquiryDAO;
	private final ProformDAO proformDAO;
	private final ProformitemDAO proformitemDAO;
	private final PlaceDAO placeDAO;
	private final UnitDAO unitDAO;

	// ------------------------------

//	@Loggable
//	@GetMapping(value = "/{id}")
//	public ResponseEntity<ProformDTO.Info> get(@PathVariable Long id) {
//		return new ResponseEntity<>(proformService.get(id), HttpStatus.OK);
//	}

//	@Loggable
//	@GetMapping(value = "/list")
//	public ResponseEntity<List<ProformDTO.Info>> list() {
//		return new ResponseEntity<>(proformService.list(), HttpStatus.OK);
//	}
//
//	@Loggable
//	@PostMapping
//	public ResponseEntity<ProformDTO.Info> create(@Validated @RequestBody ProformDTO.Create request) {
//		return new ResponseEntity<>(proformService.create(request), HttpStatus.CREATED);
//	}
//
	@Loggable
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProformDTO.Info> update(@PathVariable Long id, @Validated @RequestParam("data") String requestJSON,
															  @RequestParam(value = "file", required = false) MultipartFile file, Principal principal) throws IOException {

		UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
		User u= modelMapper.map( a.getPrincipal(), User.class);

		ProformDTO.Update request = objectMapper.readValue(requestJSON, ProformDTO.Update.class);
		return new ResponseEntity<>(proformService.update(id,request , file,u.getSupplierId()), HttpStatus.OK);
	}
//
//	@Loggable
//	@DeleteMapping(value = "/{id}")
//	public ResponseEntity<Void> delete(@PathVariable Long id) {
//		proformService.delete(id);
//		return new ResponseEntity(HttpStatus.OK);
//	}
//
//	@Loggable
//	@DeleteMapping(value = "/list")
//	public ResponseEntity<Void> delete(@Validated @RequestBody ProformDTO.Delete request) {
//		proformService.delete(request);
//		return new ResponseEntity(HttpStatus.OK);
//	}

	@Loggable
	@GetMapping(value = "/iso-search")
	public ResponseEntity<TotalResponse<ProformDTO.Info>> list(@RequestParam MultiValueMap<String, String> criteria,Principal principal) throws IOException {
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
		return new ResponseEntity<>(proformService.search(nicicoCriteria), HttpStatus.OK);
	}

//	@Loggable
//	@GetMapping(value = "/search")
//	public ResponseEntity<SearchDTO.SearchRs<ProformDTO.Info>> search(@RequestBody SearchDTO.SearchRq request) {
//		return new ResponseEntity<>(proformService.search(request), HttpStatus.OK);
//	}

	@Loggable
	@GetMapping(value = "/downloadFile")
	public void downloadFile(@RequestParam String data, HttpServletRequest request, HttpServletResponse response,Principal principal) {
		try {
			UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
			User u= modelMapper.map( a.getPrincipal(), User.class);

			String downloadFileName=proformService.getDownloadFileName(data,u.getSupplierId());
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
	public void execute(HttpServletRequest request, HttpServletResponse response,Principal principal) {
		try
		{
			UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
			User u= modelMapper.map( a.getPrincipal(), User.class);

			Inquiry inquiry=inquiryDAO.findById(new Long(request.getParameter("INQUIRYNUMBER").toString()))
					.orElseThrow(() -> new IBSException(IBSException.ErrorType.InquiryNotFound));

			if (inquiry.getSupplierId().compareTo(u.getSupplierId()) != 0 )
				throw new IBSException(IBSException.ErrorType.InquiryNotFound);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos = pdfFx(inquiry);
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());
			baos.writeTo(out);
			out.flush();

		} catch (RemoteException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return ;
	}

	private ByteArrayOutputStream pdfFx(Inquiry inquiry) {
		Proform proform=proformDAO.findById(inquiry.getId())
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.ProformitemNotFound));

		List<Proformitem> proformitems=proformitemDAO.findAllByProformId(proform.getId());
		String[] account=proformitemDAO.getAccount(proform.getSupplierId()).split("@##@");

		Document document = new Document(PageSize.A4, 10, 10, 10, 10 );
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(document, baos);
			document.open();
			String path = "C:\\IdeaProjects\\ibs\\web\\src\\assets";// this.getServlet().getServletContext().getRealPath("/");

			BaseFont bf = BaseFont.createFont(path + "/fonts/" + "verdana.ttf", BaseFont.IDENTITY_H, true);
			BaseFont bf1 = BaseFont.createFont(path + "/fonts/" + "tahoma.ttf", BaseFont.IDENTITY_H, true);

			Font f0 = new Font(bf, 10, Font.NORMAL, Color.BLACK);
			Font f1 = new Font(bf, 11, Font.NORMAL, Color.BLACK);
			Font f2 = new Font(bf, 13, Font.NORMAL, Color.BLACK);
			Font f12 = new Font(bf1, 12, Font.NORMAL, Color.BLACK);
			Font f3 = new Font(bf, 12, Font.NORMAL, Color.BLACK);
			Font f4 = new Font(bf, 14, Font.NORMAL, Color.BLACK);

			// Craete table titleTable
			String sendDate = "";
//            TblInquiry[] tblInquiries = null;
//            TblInquiry inquiry = new TblInquiry();
//            InquiryDelegate inquiryDelegate = new InquiryDelegate();
//			InquiryDAOImpl inquiryDAOImpl =new  InquiryDAOImpl();
//            TblInquiryHeader tblInquiryHeaders = null;
//            InquiryHeaderDAOImpl inquiryHeaderDAOImpl= new InquiryHeaderDAOImpl();


			 {

// 				 inquiry = inquiryDAOImpl.findInquiryByInquiryHeader(inquiryIds[h].toString());
// 				 tblInquiries = inquiryDelegate.findInquiryByInquiryNumber(inquiry.getInquirynumber());
// 				 tblInquiryHeaders= inquiryHeaderDAOImpl.findInquiryHeaderByPrimaryKey(new Long(inquiryIds[h]));

				document.setPageSize(PageSize.A4);
				document.newPage();


//            	 HeaderFooter footer = new HeaderFooter(new Phrase("DATE "+sendDate+"\nPAGE "+document.getPageNumber()),true);
//                 document.setFooter(footer);

				PdfPTable titleTable = new PdfPTable(17);
				titleTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				Phrase pr = new Phrase();
				PdfPCell cell = new PdfPCell(pr);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(0);
				cell.setBorderWidth(0);
				cell.setPaddingBottom(0);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				titleTable.addCell(cell);
//                 RegisterPriceDelegate registerPriceDelegate = new RegisterPriceDelegate();

//                 TblCompanyInfo tblCompanyInfo = new TblCompanyInfo();

//                 if(inquiry.getDatetype() == null || inquiry.getDatetype().equals("k") ){
//
//                	 tblCompanyInfo = registerPriceDelegate.companyInfoByDate(tblInquiryHeaders.getInquirydate());
//
//                 }else
//                      tblCompanyInfo = registerPriceDelegate.companyInfoByMiladiDate(tblInquiryHeaders.getInquirydate());
//
				//String file = path + companyInfoMap.get("LOGO").toString();
				String file = path + "/images/En_logo_nfmc_new_En.jpg";
				Image image = Image.getInstance(file);

				image.scalePercent(20);
				Chunk chk = new Chunk(image, 0, 0);

				cell.setPhrase(new Phrase(chk));
				cell.setColspan(1);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				titleTable.addCell(cell);

				cell.setPhrase(new Phrase(10,"", f12));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				titleTable.addCell(cell);

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// if(tblCompanyInfo != null)
				cell.setPhrase(new Phrase(10,"NON FERROUS MINES AND METALS COMMERCIAL SERVICES COMPANY \n (NICICO PRO CUREMENT AGENT)"+"\n\n\n"+"«LETTER OF ENQUIRY»", f0));  // tblCompanyInfo.getNameEn()
				//tblCompanyInfo.getNameEn()+
				cell.setColspan(10);
				titleTable.addCell(cell);

				file = path + "/images/arm_en.gif";
				image = Image.getInstance(file);
				image.scalePercent(20);
				chk = new Chunk(image, 0, 0);

				cell.setPhrase(new Phrase(chk));
				cell.setColspan(1);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				titleTable.addCell(cell);

				cell.setPhrase(new Phrase(10,"", f12));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				titleTable.addCell(cell);

				titleTable.setSpacingAfter(5.0f);
				titleTable.setWidthPercentage(90);

				cell.setBorderWidthBottom(0.15f);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthLeft(0.15f);
				cell.setBorderWidthRight(0.15f);

				PdfPTable definitions = new PdfPTable(4);
				definitions.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				definitions.setWidthPercentage(90);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(4);
				cell.setBorderWidth(0.15f);
				cell.setPaddingBottom(6);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
 
				cell.setPhrase(new Phrase(10,"To:"+ (account[1]!= null?account[1]:""), f12));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthBottom(0);
				definitions.addCell(cell);

				cell.setPhrase(new Phrase(10,"Please quote these reference in full:", f2));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthBottom(0);
				definitions.addCell(cell);

				cell.setPhrase(new Phrase(10,"Fax No.:"+RTL2LTR(account[1]!= null?account[1]:"-" ), f2));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthBottom(0);
				definitions.addCell(cell);

				cell.setPhrase(new Phrase(10,"Inquiry No.:"+inquiry.getInquiryNumber(), f2));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthBottom(0);
				definitions.addCell(cell);

				cell.setPhrase(new Phrase(10,"Attn: Sales Manager", f2));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthBottom(0);
				definitions.addCell(cell);

				cell.setPhrase(new Phrase(10,"Our Ref.:"+inquiry.getInquiryNumber()+" - 01", f2));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthBottom(0);
				definitions.addCell(cell);

				cell.setPhrase(new Phrase(10,"Cc:", f2));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthBottom(0);
				definitions.addCell(cell);
				DateUtil dateConvertor  = new DateUtil();

				cell.setPhrase(new Phrase(10,"Date:"+(inquiry.getSendDate()!= null? dateConvertor.convertKhToMi(inquiry.getSendDate()):"" ), f2));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthBottom(0);
				definitions.addCell(cell);

				cell.setPhrase(new Phrase(10,"Subject: Request For Quotation", f2));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthBottom(0.15f);
				definitions.addCell(cell);

				cell.setPhrase(new Phrase(10,"Pages: 01", f2));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthBottom(0.15f);
				definitions.addCell(cell);

				cell.setBorderWidthBottom(0f);
				cell.setBorderWidthTop(new Float(0).floatValue());
				cell.setBorderWidthLeft(new Float(0).floatValue());
				cell.setBorderWidthRight(new Float(0).floatValue());

				PdfPTable body = new PdfPTable(6);
				body.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				body.setWidthPercentage(90);

				String bodyText1P1 = "Please submit your firm quotation along with related data " +
						",catalogue or brochure for supplying materials as per attached specifications" +
						" and quantities.Your quotation for the requested item/items should be submitted " +
						"up to&including ";
				String bodyText1P2 = " and containted" +
						" the following conditions:\n1-Unit & total price of each item and sum of prices including" +
						" packing and handling charges on ";
				String bodyText1P3 = " basis simultaneous indicating freight charges separately." +
						"\n2-country of origin(exact name of countries) ,manufacturer's&beneficiary's name and customs" +
						" tariff No. ,embarkation and destination port should be clarified.\n3-Standard code that offered" +
						" products comply with.\n4-Shipping details including type of packing suitable for shipment by " +
						"land/sea/air,estimated weights(net & gross) ,volume and method of shipment.\n6-Validity of" +
						" the offer for a minimum period of 90 days from the date of issuance.\n" +
						"7-Earliest possible date of delivery.\n"+
						"8-NICICO commercial ID No. is '10100582059' and should be included in your proforma invoice.\n";
				Phrase phrase = new Phrase(2f);
				phrase.add(new Chunk("Dear Sir / Madam\n", f4));
				phrase.add(new Chunk(bodyText1P1,f3));
				if(inquiry.getEndReplyDate() != null)
					phrase.add(new Chunk(dateConvertor.convertKhToMi(inquiry.getEndReplyDate()),f4));
				else
					phrase.add(new Chunk("-",f4));
				phrase.add(new Chunk(bodyText1P2,f3));
				String purchaseType = "";
//                 if(inquiry.get("DELIVARYPLACE")!= null)
//                 	purchaseType = tblInquiryHeaders.getDeliveryplace();
				phrase.add(new Chunk(((inquiry.getDeliveryLocation()!=null && inquiry.getDeliveryLocation().getDeliveryNameFa()!= null && inquiry.getDeliveryLocation().getDeliveryNameFa()!= null)?inquiry.getDeliveryLocation().getDeliveryNameFa():"-"),f4));
				phrase.add(new Chunk(bodyText1P3,f3));
				cell.setPhrase(phrase);
				cell.setColspan(6);
				cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0);
				cell.setBorderWidthLeft(0.15f);
				cell.setBorderWidthRight(0.15f);
				body.addCell(cell);

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPhrase(new Phrase(10,"OTHER CONDITIONS\n", f4));
				cell.setColspan(6);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0);
				cell.setBorderWidthLeft(0.15f);
				cell.setBorderWidthRight(0.15f);
				body.addCell(cell);

				String testNeeded ="";
				if(inquiry.getTestDesc() != null )
					testNeeded = "NO";
				else
					testNeeded = "YES";
				String controlBy = "";
/*				if(inquiry.get("CONTROL_BY") != null && inquiry.get("CONTROL_BY").toString().equals("b"))
					controlBy = "Buyer";
				else if(inquiry.get("CONTROL_BY") != null && inquiry.get("CONTROL_BY").toString().equals("s"))
					controlBy = "Seller";*/
				String bodyText2P1 = "1-Manufacturer Test certificate should be issued :";
				String bodyText2P2 = "\n2-Third Party Inspection certificate is needed if the total price will be more " +
						"than 15.000 Euro,relevant charges will be borne by : ";
				String bodyText2P3 = " and the inspector" +
						" will be agreed upon.Scope of inspection will be advised later.\n3-Payment would be " +
						"effected through Letter of Credit (L/C)/CAD/Payable by bank guaranty.\n4-If the transaction is upon a" +
						" contract the relevent payments will be affected after signing the contract." +
						"\n5-Indicate your Bank details " +
						"and Account number(i.e. IBAN ,SWIFT,and …..)\n6-Your e-mail address and contact person" +
						" should be declared along with your postal address.\n7-Kindly submit your Technical and" +
						" Commercial offers separately.\n8-Confirm that the supplied materials are Brand New and " +
						"Genuine.\n9-In case of any misunderstanding or argument Incoterms 2000 will be criteria";
				Phrase phrase2 = new Phrase(35);
				phrase2.add(new Chunk(bodyText2P1,f3));
				phrase2.add(new Chunk(testNeeded,f4));
				phrase2.add(new Chunk(bodyText2P2,f3));
				phrase2.add(new Chunk(controlBy,f4));
				phrase2.add(new Chunk(bodyText2P3,f3));
//     	        phrase.
				cell.setPhrase(phrase2);
				cell.setColspan(6);
				cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0);
				cell.setBorderWidthLeft(0.15f);
				cell.setBorderWidthRight(0.15f);
				body.addCell(cell);

				cell.setPhrase(new Phrase(10,"", f2));
				cell.setColspan(6);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0);
				cell.setBorderWidthLeft(0.15f);
				cell.setBorderWidthRight(0.15f);
				body.addCell(cell);

				String bodyText3 = "You are kindly requested to send your offer offer via "+ "\n"  +
						" email to the below mentioned address . ";
				cell.setPhrase(new Phrase(10,bodyText3, f3));
				cell.setColspan(6);
				cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0);
				cell.setBorderWidthLeft(0.15f);
				cell.setBorderWidthRight(0.15f);
				body.addCell(cell);
				Place place=placeDAO.findAllById(inquiry.getPlaceId());
				String bodyText4P1 =
						"Best Regards\n"
								+ (place!= null && place.getResponsibleInquiryName()!= null && !place.getResponsibleInquiryName().equals("null") ?place.getResponsibleInquiryName():" " )
								+"\n"
								+(place!= null && place.getResponsibleInquiryPosition()!= null  && !place.getResponsibleInquiryPosition().equals("null")?place.getResponsibleInquiryPosition():" " );
				String bodyText4P2 =
						//tblCompanyInfo.getNameEn()
						//(companyInfoMap.get("NAME_EN")!= null?companyInfoMap.get("NAME_EN").toString():"")
						"\n"+"N.F.M.C.Co"
								+(place!= null && place.getAddress()!= null && !place.getAddress().equals("null") ?("\n"+place.getAddress()):" " )
								+"\nTel: "
								+(place!= null && place.getTel()!= null && !place.getTel().equals("null") ?place.getTel():" " )
								+" Fax:"
								+(place!= null && place.getFax()!= null && !place.getFax().equals("null") ?place.getFax():" " )
								+"\ne-mail: procurement_foreign@nicico.com";
				Phrase phrase3 = new Phrase(15);
				phrase3.add(new Chunk(bodyText4P1,f2));
				phrase3.add(new Chunk(bodyText4P2,f1));
				cell.setPhrase(phrase3);
				cell.setColspan(4);
				cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0);
				cell.setBorderWidthLeft(0.15f);
				cell.setBorderWidthRight(0);
				body.addCell(cell);

				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				String file2 = path + "/images/rectangle.gif";
				Image image2 = Image.getInstance(file2);
				Chunk chk2 = new Chunk(image2, 0, 0);
				String optionsTitle = "SENT\n\n";
				String firstOption = "1st ,dd:\n\n";
				String secondOption = "2nd ,dd:\n\n";
				String thirdOption = "3rd ,dd:\n\n";
				String fourthption = "4th ,dd:\n\n";
				Phrase phrase4 = new Phrase(15);
				phrase4.add(new Chunk(optionsTitle,f2));
				phrase4.add(chk2);
				phrase4.add(new Chunk(firstOption,f2));
				phrase4.add(chk2);
				phrase4.add(new Chunk(secondOption,f2));
				phrase4.add(chk2);
				phrase4.add(new Chunk(thirdOption,f2));
				phrase4.add(chk2);
				phrase4.add(new Chunk(fourthption,f2));
				cell.setPhrase(phrase4);
				cell.setColspan(2);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthBottom(0.15f);
				cell.setBorderWidthLeft(0.15f);
				cell.setBorderWidthRight(0.15f);
				body.addCell(cell);

				cell.setPhrase(new Phrase(10,"", f2));
				cell.setColspan(6);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorderWidthTop(0);
				cell.setBorderWidthBottom(0.15f);
				cell.setBorderWidthLeft(0.15f);
				cell.setBorderWidthRight(0.15f);
				body.addCell(cell);

				document.add(titleTable);
				document.add(definitions);
				document.add(body);

				document.setPageSize(PageSize.A4.rotate());
				document.newPage();


				PdfPTable productDSC = new PdfPTable(15);
				productDSC.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthBottom(0.15f);
				cell.setBorderWidthLeft(0.15f);
				cell.setBorderWidthRight(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);

				productDSC.setWidthPercentage(98);
				productDSC.setSpacingAfter(5.0f);
				productDSC.setSplitLate(true);

				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setPhrase(new Phrase(10," "+"INQUIRY\nNO", f1));
				cell.setColspan(1);
				cell.setNoWrap(true);
				productDSC.addCell(cell);

				cell.setNoWrap(false);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthBottom(0.15f);
				cell.setBorderWidthLeft(0);
				cell.setBorderWidthRight(0);

				cell.setPhrase(new Phrase(10,"ITEM", f1));
				cell.setColspan(1);
				productDSC.addCell(cell);

				cell.setPhrase(new Phrase(10,"UNIT", f1));
				cell.setColspan(1);
				productDSC.addCell(cell);

				cell.setPhrase(new Phrase(10,"QTY",f1));
				cell.setColspan(2);
				productDSC.addCell(cell);

				cell.setPhrase(new Phrase(10,"DESCRIPTION", f1));
				cell.setColspan(5);
				productDSC.addCell(cell);

				cell.setPhrase(new Phrase(10,"SUPPLIER", f1));
				cell.setColspan(2);
				productDSC.addCell(cell);


				cell.setPhrase(new Phrase(10,"MFG", f1));
				cell.setColspan(1);
				productDSC.addCell(cell);

				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthBottom(0.15f);
				cell.setBorderWidthLeft(0);
				cell.setBorderWidthRight(0.15f);
				cell.setPhrase(new Phrase(10,"PART NO", f1));
				cell.setColspan(2);
				productDSC.addCell(cell);

				//  MaterialDelegate materialDelegate = new MaterialDelegate();
				// int materialCountrer = tblInquiries.length;
				for (int i = 0; i < proformitems.size() ; i++)
				{

//					DSRequest dsReqReqItem = new DSRequest("TblRequestItemDS", "fetch");;
//					dsReqReqItem.addToCriteria("ID", new Long(proformitems.get(i)..get("REQUESTITEMID").toString()));
//					List rqItemList = dsReqReqItem.execute().getDataList();
//					rqItemMap = (HashMap<String, Object>) rqItemList.get(0);

					cell.setBorderWidthBottom(0.15f);
//					if(rqItemMap != null && rqItemMap.get("PRITEMDESC")!= null && !rqItemMap.get("PRITEMDESC").equals("")) //tblInquiries[i].getTblRequestitem().getPritemdesc()
//					{
//						cell.setBorderWidthBottom(0);
//					}
					cell.setBorderWidthTop(0.15f);
					cell.setBorderWidthLeft(0.15f);
					cell.setBorderWidthRight(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);

					cell.setPhrase(new Phrase(10,inquiry.getInquiryNumber() ,f1)); //tblInquiries[i].getInquirynumber().toString()
					cell.setNoWrap(true);
					cell.setColspan(1);
					productDSC.addCell(cell);

					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setNoWrap(false);
					cell.setBorderWidthTop(0.15f);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					cell.setPhrase(new Phrase(10, (proformitems.get(i).getRequestItem().getItemRow().toString()),f1));
					cell.setColspan(1);
					productDSC.addCell(cell);

					Unit unit=unitDAO.findAllById(proformitems.get(i).getUnitId());
					cell.setPhrase(new Phrase(10,(unit.getNameFa()),f1));
					cell.setColspan(1);
					productDSC.addCell(cell);

					String inquiryAmountStr = "";
					Long inquiryAmounLong = null;

					if(proformitems.get(i).getAmount()!= null) //tblInquiries[i].getInquiryAmount()!=null && tblInquiries[i].getInquiryAmount()!=0
					{
						inquiryAmountStr = proformitems.get(i).getAmount().toString();
						if(inquiryAmountStr.contains("."))
						{
							inquiryAmounLong = proformitems.get(i).getAmount().longValue();
							cell.setPhrase(new Phrase(10, inquiryAmounLong+ "\n" ,f2));
						}else{
							cell.setPhrase(new Phrase(10, inquiryAmountStr + "\n",f2));
						}
					}
					else {
						cell.setPhrase(new Phrase(10," " + "\n" + " ",f2));
					}
					cell.setColspan(2);
					productDSC.addCell(cell);
					String dsc = "";
					if(proformitems.get(i).getItemDescl() != null) //tblInquiries[i].getItemDscl()
					{
						dsc = proformitems.get(i).getItemDescl();
					}
					cell.setPhrase(new Phrase(10, dsc ,f12));
					cell.setColspan(5);
					cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					productDSC.addCell(cell);

					cell.setPhrase(new Phrase(10,(proformitems.get(i).getRequestItemSpec()!= null?proformitems.get(i).getRequestItemSpec():""), f1));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					productDSC.addCell(cell);

					String maker = "";

					cell.setPhrase(new Phrase(10,maker ,f1));
					cell.setColspan(1);
					productDSC.addCell(cell);

					cell.setBorderWidthTop(0.15f);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0.15f);

					productDSC.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
					if(proformitems.get(i).getRequestItemSpec()!=null)
						cell.setPhrase(new Phrase(10,proformitems.get(i).getRequestItemSpec(),f1));
					cell.setColspan(2);
					productDSC.addCell(cell);;
					if(proformitems.get(i).getTechnicalDesc()!=null) {
						cell.setBorderWidthTop(0);
						cell.setBorderWidthBottom(0.15f);
						cell.setBorderWidthLeft(0.15f);
						cell.setBorderWidthRight(0.15f);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setPhrase(new Phrase(10,"Technical Condition : " + proformitems.get(i).getTechnicalDesc(),f1));
						cell.setColspan(15);
						productDSC.addCell(cell);
					}
					if(proformitems.get(i).getWarrantyDesc()!=null) {
						cell.setBorderWidthTop(0);
						cell.setBorderWidthBottom(0.15f);
						cell.setBorderWidthLeft(0.15f);
						cell.setBorderWidthRight(0.15f);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setPhrase(new Phrase(10,"Warranty Condition : " + proformitems.get(i).getWarrantyDesc(),f1));
						cell.setColspan(15);
						productDSC.addCell(cell);
					}
					if(proformitems.get(i).getPackingDesc()!=null) {
						cell.setBorderWidthTop(0);
						cell.setBorderWidthBottom(0.15f);
						cell.setBorderWidthLeft(0.15f);
						cell.setBorderWidthRight(0.15f);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setPhrase(new Phrase(10,"Packing Condition : " + proformitems.get(i).getPackingDesc(),f1));
						cell.setColspan(15);
						productDSC.addCell(cell);
					}

//					if(rqItemMap != null &&  rqItemMap.get("PRITEMDESC")!= null  && !rqItemMap.get("PRITEMDESC").equals(""))
//					{
//						cell.setBorderWidthTop(0);
//						cell.setBorderWidthBottom(0.15f);
//						cell.setBorderWidthLeft(0.15f);
//						cell.setBorderWidthRight(0.15f);
//						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//						cell.setPhrase(new Phrase(10,"Details : " + rqItemMap.get("PRITEMDESC").toString(),f1));
//						cell.setColspan(15);
//						productDSC.addCell(cell);
//					}
				}
//				if(inquiry.get("DSC") != null && !inquiry1.get("DSC").equals(""))
//				{
//					cell.setBorderWidthTop(0);
//					cell.setBorderWidthBottom(0.15f);
//					cell.setBorderWidthLeft(0.15f);
//					cell.setBorderWidthRight(0.15f);
//					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//					cell.setPhrase(new Phrase(10,"Commercial Note : " + inquiry1.get("DSC").toString(),f1)); //tblInquiryHeaders.getDsc()
//					cell.setColspan(15);
//					productDSC.addCell(cell);
//				}
//				if(inquiry1.get("TECHNICLA_DESC") != null && !inquiry1.get("TECHNICLA_DESC").equals(""))  //tblInquiryHeaders.getTechnicalDesc()
//				{
//					cell.setBorderWidthTop(0);
//					cell.setBorderWidthBottom(0.15f);
//					cell.setBorderWidthLeft(0.15f);
//					cell.setBorderWidthRight(0.15f);
//					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//					cell.setPhrase(new Phrase(10,"Technical Note : " + inquiry1.get("TECHNICLA_DESC").toString(),f1));
//					cell.setColspan(15);
//					productDSC.addCell(cell);
//				}
				document.add(productDSC);
			}
			document.close();
		}
		catch (Exception de) {
			de.printStackTrace();
		}
		return baos;
	}
	private String RTL2LTR(String in)
	{
		return RTL2LTR(in, "/");
	}

	private String RTL2LTR(String in, String d)
	{
		ArrayList a = new ArrayList();
		String result = "";
		while (in.indexOf(d) > -1)
		{
			a.add(in.substring(0, in.indexOf(d)));
			in = in.substring(in.indexOf(d) + 1);
		}
		a.add(in);
		String[] strs = (String[]) a.toArray(new String[a.size()]);
		for (int i = strs.length - 1; i > 0; i--)
		{
			result += strs[i] + d;
		}

		result += strs[0];
		return result;
	}


}
