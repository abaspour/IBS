package com.nicico.ibs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.nicico.copper.common.Loggable;
import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.date.DateTimeDTO;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.core.util.file.FileUtil;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.InquiryDTO;
import com.nicico.ibs.iservice.IInquiryService;
import com.nicico.ibs.model.Inquiry;
import com.nicico.ibs.model.RegisterPrice;
import com.nicico.ibs.model.User;
import com.nicico.ibs.repository.InquiryDAO;
import com.nicico.ibs.repository.RegisterPriceDAO;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/inquiry")
public class InquiryRestController {
	@Value("${ibs.upload.dir}")
	private String uploadDir;

	private final IInquiryService inquiryService;
	private final ModelMapper modelMapper;
	private final ObjectMapper objectMapper;
	private final InquiryDAO inquiryDAO;
	private final RegisterPriceDAO registerPriceDAO;
	private final FileUtil fileUtil;

	// ------------------------------

//	@Loggable
//	@GetMapping(value = "/{id}")
//	public ResponseEntity<InquiryDTO.Info> get(@PathVariable Long id) {
//		return new ResponseEntity<>(inquiryService.get(id), HttpStatus.OK);
//	}
//
//	@Loggable
//	@GetMapping(value = "/list")
//	public ResponseEntity<List<InquiryDTO.Info>> list() {
//		return new ResponseEntity<>(inquiryService.list(), HttpStatus.OK);
//	}
//
//	@Loggable
//	@PostMapping
//	public ResponseEntity<InquiryDTO.Info> create(@Validated @RequestBody InquiryDTO.Create request) {
//		return new ResponseEntity<>(inquiryService.create(request), HttpStatus.CREATED);
//	}

	@Loggable
	@PutMapping(value = "/{id}")
	public ResponseEntity<InquiryDTO.Info> update(@PathVariable Long id, @Validated @RequestParam("data") String requestJSON
			                                    , @RequestParam(value = "file", required = false) MultipartFile file
												,Principal principal) throws IOException {
			UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
			User u= modelMapper.map( a.getPrincipal(), User.class);
		InquiryDTO.Update request = objectMapper.readValue(requestJSON, InquiryDTO.Update.class);
		return new ResponseEntity<>(inquiryService.update(id, request , file,u.getSupplierId()), HttpStatus.OK);
	}

	@Loggable
	@PutMapping(value = "/status/{id}")
	public ResponseEntity<String> inquiryStatusCount(@PathVariable Long id, Principal principal) {
		UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
		User u= modelMapper.map( a.getPrincipal(), User.class);
		int status=inquiryService.inquiryStatusCount(id,u.getSupplierId());
		return new ResponseEntity<>(String.valueOf(status), HttpStatus.OK);
	}

	@Loggable
	@PutMapping(value = "/copy/{id}")
	public ResponseEntity<String> inquiryCopy(@PathVariable Long id, Principal principal) {
		UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
		User u= modelMapper.map( a.getPrincipal(), User.class);
		String copy=inquiryService.inquiryCopy(id,u.getId());
		String out = " { \"data\" : \"" + copy + "\" }";
		return new ResponseEntity<>(out, HttpStatus.OK);
	}

//	@Loggable
//	@DeleteMapping(value = "/{id}")
//	public ResponseEntity<Void> delete(@PathVariable Long id) {
//		inquiryService.delete(id);
//		return new ResponseEntity(HttpStatus.OK);
//	}
//
//	@Loggable
//	@DeleteMapping(value = "/list")
//	public ResponseEntity<Void> delete(@Validated @RequestBody InquiryDTO.Delete request) {
//		inquiryService.delete(request);
//		return new ResponseEntity(HttpStatus.OK);
//	}
//
	@Loggable
	@GetMapping(value = "/iso-search") // greaterOrEqual  lessOrEqual
	public ResponseEntity<TotalResponse<InquiryDTO.Info>> list(@RequestParam MultiValueMap<String, String> criteria,Principal principal) throws IOException {
		UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
		User u= modelMapper.map( a.getPrincipal(), User.class);

		DateTimeDTO.DateTimeStrRs dateTimeStrRs = modelMapper.map(new Date(), DateTimeDTO.DateTimeStrRs.class);

		String value="{ \"operator\":\"and\", \"criteria\" : [  { \"fieldName\":\"supplierId\", \"operator\":\"equals\", \"value\": \""+u.getSupplierId()+"\"  } , " +
				                                             "  { \"fieldName\":\"endReplyDate\", \"operator\":\"greaterOrEqual\", \"value\": \""+dateTimeStrRs.getDate()+"\"  } , " +
															 "  {  \"operator\": \"or\", \"criteria\": [{ \"fieldName\" : \"verifyStatus\", \"operator\": \"equals\", \"value\" : \"n\" }, " +
																	                        "           { \"fieldName\" : \"verifyStatus\", \"operator\": \"equals\", \"value\" : \"v\" } ]  }, "
				+criteria.get("criteria").get(0)+" ] } ";
		criteria.remove("criteria");
		criteria.add("criteria",value);
		final NICICOCriteria nicicoCriteria = NICICOCriteria.of(criteria);
		if (nicicoCriteria.get_sortBy() == null)
			nicicoCriteria.set_sortBy(Collections.singletonList("id"));
		return new ResponseEntity<>(inquiryService.search(nicicoCriteria), HttpStatus.OK);
	}

	@Loggable
	@GetMapping(value = "/iso-search-oher") // greaterOrEqual  lessOrEqual
	public ResponseEntity<TotalResponse<InquiryDTO.Info>> listOther(@RequestParam MultiValueMap<String, String> criteria,Principal principal) throws IOException {
		UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
		User u= modelMapper.map( a.getPrincipal(), User.class);

		DateTimeDTO.DateTimeStrRs dateTimeStrRs = modelMapper.map(new Date(), DateTimeDTO.DateTimeStrRs.class);
		if (criteria.get("criteria").get(0).contains(" dateTimeStrRs.getDate() "))
			criteria.get("criteria").set(0, criteria.get("criteria").get(0).replace(" dateTimeStrRs.getDate() ", dateTimeStrRs.getDate()));
		String value="{ \"operator\":\"and\", \"criteria\" : [  { \"fieldName\":\"supplierId\", \"operator\":\"equals\", \"value\": \""+u.getSupplierId()+"\"  } , " +
 				 criteria.get("criteria").get(0)+" ] } ";
		criteria.remove("criteria");
		criteria.add("criteria",value);
		final NICICOCriteria nicicoCriteria = NICICOCriteria.of(criteria);
		if (nicicoCriteria.get_sortBy() == null)
			nicicoCriteria.set_sortBy(Collections.singletonList("id"));
		return new ResponseEntity<>(inquiryService.search(nicicoCriteria), HttpStatus.OK);
	}

//	@Loggable
//	@GetMapping(value = "/search")
//	public ResponseEntity<SearchDTO.SearchRs<InquiryDTO.Info>> search(@RequestBody SearchDTO.SearchRq request) {
//		return new ResponseEntity<>(inquiryService.search(request), HttpStatus.OK);
//	}

	@Loggable
	@GetMapping(value = "/inquiry-status-count")
	public ResponseEntity<String> inquiryStatusCount( Principal principal) {
			UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
			User u= modelMapper.map( a.getPrincipal(), User.class);
		    List<Object[]> status=inquiryDAO.inquiryStatusCount(u.getSupplierId()); //0 count    1 startus
		    String out=" { \"data\" : [  ";
			for (Object[] line:status)	{
				out+=" { \"count\": \""+line[0].toString()+"\" , \"status\": \""+line[1].toString()+"\" }, ";
			}
			out=out.substring(0,out.length()-2)+" ] } ";
		return new ResponseEntity<>(out, HttpStatus.OK);
	}

	@Loggable
	@GetMapping(value = "/downloadFile")
	public void downloadFile(@RequestParam String data, HttpServletRequest request, HttpServletResponse response,Principal principal) {
		try {
			UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
			User u= modelMapper.map( a.getPrincipal(), User.class);

			String downloadFileName=inquiryService.getDownloadFileName(data,u.getSupplierId());
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
		try {

			if (request.getParameter("inquiryId") != null) {
				inquiryIds =request.getParameter("inquiryId").toString().split(",");

			}
			UsernamePasswordAuthenticationToken a= ((UsernamePasswordAuthenticationToken) principal);
			User u= modelMapper.map( a.getPrincipal(), User.class);
			loginAccountId= u.getSupplierId();

			Inquiry inquiryMap=inquiryDAO.findById(new Long(inquiryIds[0]))
					.orElseThrow(() -> new IBSException(IBSException.ErrorType.InquiryNotFound));
			if  (inquiryMap.getSupplierId().compareTo(loginAccountId) != 0)
				throw new IBSException(IBSException.ErrorType.InquiryNotFound);
			List<RegisterPrice> registerPrice=registerPriceDAO.findAllByInquiryNumber(inquiryIds[0]);

			inqiuryNumber = inquiryMap.getInquiryNumber();

			inqiuryDate = inquiryMap.getInquiryDate();
			path = "C:\\IdeaProjects\\ibs\\web\\src\\assets";// this.getServlet().getServletContext().getRealPath("/");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos = pdfInternalFx(inquiryMap,registerPrice);
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());
			baos.writeTo(out);
			out.flush();

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ;
	}
	private ByteArrayOutputStream pdfInternalFx(Inquiry inquiryMap,List<RegisterPrice> registerMap) {
		Document document = new Document(PageSize.A4.rotate(), 20, 20, 100, 20);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, baos);

			TableHeader event = new TableHeader();
			writer.setPageEvent(event);
			document.open();

			BaseFont bf = BaseFont.createFont(path + "/fonts/" + "karnik.ttf",
					BaseFont.IDENTITY_H, true);
			BaseFont bfk = BaseFont.createFont(path + "/fonts/" + "tahoma.ttf",
					BaseFont.IDENTITY_H, true);

			Font f1 = new Font(bf, 9, Font.BOLD, Color.BLACK);
			Font f2 = new Font(bf, 11, Font.BOLD, Color.BLACK);
			Font f26 = new Font(bf, 12, Font.BOLD, Color.BLACK);
			Font f3 = new Font(bf, 18, Font.BOLD, Color.BLACK);
			Font f7 = new Font(bf, 14, Font.BOLD, Color.BLACK);
			Font f13 = new Font(bf, 12, Font.BOLD, Color.BLACK);
			Font f14 = new Font(bfk, 11, Font.BOLD, Color.BLACK);

			DecimalFormat decimalFormat = new DecimalFormat();
			decimalFormat.applyPattern("###,###.###");

			for (int h = 0; h < inquiryIds.length; h++) {

				// Craete table productDSC

				PdfPTable productDSC = new PdfPTable(13);
				productDSC.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				productDSC.setWidthPercentage(99);
				productDSC.setSpacingAfter(0.5f);
				productDSC.setSplitLate(true);

				Phrase pr = new Phrase();
				PdfPCell cell = new PdfPCell(pr);
				cell.setBorderWidthBottom(0.15f);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);

				cell.setPhrase(new Phrase(10, "رديف", f26));
				cell.setColspan(1);
				productDSC.addCell(cell);

				cell.setPhrase(new Phrase(10, "رديف آيتم", f26));
				cell.setColspan(1);
				productDSC.addCell(cell);

				cell.setPhrase(new Phrase(10, "شرح کالا ", f26));
				cell.setColspan(6);
				productDSC.addCell(cell);

				cell.setPhrase(new Phrase(10, "تعداد / واحد", f26));
				cell.setColspan(1);
				productDSC.addCell(cell);


				cell.setPhrase(new Phrase(10, "مشخصات  فني", f26));
				cell.setColspan(1);
				productDSC.addCell(cell);

				cell.setPhrase(new Phrase(10, "اطلاعات اختصاصي هر آيتم", f26));
				cell.setColspan(3);
				productDSC.addCell(cell);
				int i=-1;
				for (RegisterPrice registrMap: registerMap) {
					i++;
					String unitName=registerPriceDAO.findPartAndUnitNameById(registrMap.getId()).split("@@@@##@@")[1];
					List<Object[]> att=registerPriceDAO. findItemAttach(loginAccountId,registrMap.getRequestItemId());

					cell.setPhrase(new Phrase(10, new Long(i + 1).toString(),f13));
					cell.setColspan(1);
					productDSC.addCell(cell);

					cell.setPhrase(new Phrase(10,(registrMap.getItemRow()!= null?registrMap.getItemRow():""), f2));
					cell.setColspan(1);
					productDSC.addCell(cell);

					String dsc = "";
					PdfPTable descTable = new PdfPTable(1);

					if (registrMap.getItemDescp() != null) {
						dsc = registrMap.getItemDescp();
					}
					descTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
					descTable.addCell(new Phrase(10, dsc, f13));

					if (registrMap.getItemDescl() != null) {
						dsc = registrMap.getItemDescl();
					}
					descTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
					descTable.addCell(new Phrase(10, dsc, f13));

					cell.addElement(descTable);
					cell.setColspan(6);
					productDSC.addCell(cell);

					String inquiryAmountStr = "";
					Long inquiryAmounLong = null;

					if (registrMap.getItemQty() != null && !registrMap.getItemQty().equalsIgnoreCase("0")) {
						inquiryAmountStr = registrMap.getItemQty();
					}
					if (inquiryAmountStr.contains(".0")) {
						inquiryAmounLong = new Double(registrMap.getItemQty()).longValue();
						cell.setPhrase(new Phrase(10, inquiryAmounLong
								+ "\n"
								+ (unitName!= null?unitName:"")
								, f2));
					} else {
						cell.setPhrase(new Phrase(10, inquiryAmountStr
								+ "\n"
								+ (unitName!= null?unitName:"")
								, f2));
					}

					cell.setColspan(1);
					productDSC.addCell(cell);

					cell.setPhrase(new Phrase(10,(registrMap.getShomareNaghshe()!= null?registrMap.getShomareNaghshe():""), f2));
					cell.setColspan(1);
					productDSC.addCell(cell);


					String testNeeded = "";
					if(registrMap.getTestNeeded()!=null && registrMap.getTestNeeded().equalsIgnoreCase("y"))
					{
						testNeeded = "نياز به تست عملي نمونه پيشنهاد دهنده دارد. \n";
					}
					String attachment = "";
					if(att != null){
						attachment = "داراي پيوست فني مي باشد \n";
					}

					String costMode = "";
					if(registrMap.getCostMode() != null){
						if(registrMap.getCostMode().equalsIgnoreCase("s"))
							costMode = "نياز به ساخت قالب دارد و هزينه آن به عهده فروشنده مي باشد \n";
						else if(registrMap.getCostMode().toString().equalsIgnoreCase("b"))
							costMode = "نياز به ساخت قالب دارد و هزينه آن به عهده خريدار مي باشد \n";
					}
					String costModel = "";
					if(registrMap.getCostModel() != null){
						if(registrMap.getCostModel() .equalsIgnoreCase("s"))
							costModel = "نياز به ساخت مدل دارد و هزينه آن به عهده فروشنده مي باشد \n";
						else if(registrMap.getCostModel() .equalsIgnoreCase("b"))
							costModel = "نياز به ساخت مدل دارد و هزينه آن به عهده خريدار مي باشد \n";
					}


					String guranteeTime = "";
					if(registrMap.getGuranteeTime() != null){
						guranteeTime = "شرايط گارانتي  و مدت آن : " + registrMap.getGuranteeTime() + " \n" ;
					}

					String partMaterial = "";
					if(registrMap.getPartMaterial() != null){
						partMaterial = "جنس قطعه : " + registrMap.getPartMaterial() + " \n" ;
					}

					String unitWeight = "";
					if(registrMap.getUnitWeight()!= null){
						unitWeight = "وزن تقريبي واحد : " + registrMap.getUnitWeight() + " \n" ;
					}

					String modeMaterial = "";
					if(registrMap.getModeMaterial()!= null){
						modeMaterial = "جنس قالب : " + registrMap.getModeMaterial() + " \n" ;
					}

					String modelMaterial = "";
					if(registrMap.getModelMaterial()!= null){
						modelMaterial = "جنس مدل : " + registrMap.getModelMaterial() + " \n" ;
					}
					String noSample= "";
					String sample= "";

					if(registrMap.getItemSample()!= null && !registrMap.getItemSample().equalsIgnoreCase("n") ){
						sample= " نمونه کالاي اين آيتم رويت شده \n";
						noSample= " نمونه کالاي اين آيتم رويت نشده \n";

					}
					String neededTime = "";
					if(registrMap.getDeliveryDate()!= null ){
						neededTime = "زمان تحويل :" + registrMap.getDeliveryDate().toString()+ " روز " +" \n" ;
					}
					String verifyPrice = "";
					if(registrMap.getVerifyPrice()!= null && registrMap.getVerifyPrice().equalsIgnoreCase("nv")){
						verifyPrice = "قيمت فوق براي شرح کالاي متفاوت با شرح فوق بوده و در توضيحات زير آمده است\n" ;
					}
					String packingCondition = "";
					if(registrMap.getPackageCondition()!= null ){
						packingCondition = " شرايط بسته بندي :" + registrMap.getPackageCondition()+ " \n" ;
					}
					String technicalDesc = "";
					if(registrMap.getTechnicalDesc()!= null ){
						technicalDesc = " شرايط فنی خریدار :" + registrMap.getTechnicalDesc()+ " \n" ;
					}
					String warrantyDesc = "";
					if(registrMap.getWarrantyDesc()!= null ){
						warrantyDesc = " شرايط گارانتی خریدار :" + registrMap.getWarrantyDesc()+ " \n" ;
					}
					String packingDesc = "";
					if(registrMap.getPackingDesc()!= null ){
						packingDesc = " شرايط بسته بندی خریدار :" + registrMap.getPackingDesc()+ " \n" ;
					}
					String techInfo = "";
					if( !attachment.equalsIgnoreCase("") || !costMode.equalsIgnoreCase("") ||
							!costModel.equalsIgnoreCase("") ||  !guranteeTime.equalsIgnoreCase("") ||
							!partMaterial.equalsIgnoreCase("") ||  !unitWeight.equalsIgnoreCase("") ||
							!modeMaterial.equalsIgnoreCase("") || !modelMaterial.equalsIgnoreCase("") ||
							!neededTime.equalsIgnoreCase("") )
					{
						techInfo = "اطلاعات فني : "+"\n" ;
					}

					//***********************************
					PdfPTable checkedTable = new PdfPTable(6);
					PdfPCell cell1 = new PdfPCell(pr);
					checkedTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
					cell1.setBorder(0);

					cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell1.setVerticalAlignment(Element.ALIGN_LEFT);
					cell1.setPhrase(new Phrase(10,	 sample
							+ noSample
							+ testNeeded
							+ techInfo
							+ attachment
							+ costMode
							+ costModel
							+ guranteeTime
							+ partMaterial
							+ unitWeight
							+ modeMaterial
							+ modelMaterial
							+ neededTime
							+ verifyPrice
							+ packingCondition
							+ technicalDesc
							+ packingCondition
							+ warrantyDesc
							+ packingDesc
							+(registrMap.getDescription()!= null? "توضيحات : "+registrMap.getDescription():""), f2));

					if(registrMap.getItemSample()!= null && registrMap.getItemSample().equalsIgnoreCase("y") ){
						cell1.setColspan(5);
						checkedTable.addCell(cell1);
						cell1.setColspan(1);
						String fil2 = path + "/images/rec.png";
						Image img2 = Image.getInstance(fil2);
						Chunk chnk2 = new Chunk(img2, 0, 0);
						cell1.setPhrase(new Phrase(chnk2));
						checkedTable.addCell(cell1);
					}else{
						cell1.setColspan(6);
						checkedTable.addCell(cell1);
					}

					cell.addElement(checkedTable);
					cell.setColspan(5);
					productDSC.addCell(cell);
				}

				String unit="ريال";

				cell.setPhrase(new Phrase(10, "شرايط بازرگاني : ", f3));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setVerticalAlignment(Element.ALIGN_LEFT);
				cell.setColspan(13);
				productDSC.addCell(cell);

				cell.setBorderWidthTop(0);

				if (inquiryMap.getPortage() != null) {
					cell.setPhrase(new Phrase(
							10,
							(inquiryMap.getPortage().equalsIgnoreCase("s")?"انجام بار گيري و حمل به عهده فروشنده بوده و هزينه حمل به عهده  فروشنده مي باشد ":"انجام بار گيري و حمل به عهده فروشنده بوده و هزينه حمل به عهده خريدار و به صورت پس کرايه مي باشد "), f26));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(13);
					productDSC.addCell(cell);
				}

				if (inquiryMap.getInstalCost() != null ) {
					cell.setPhrase(new Phrase(
							10,
							" پيشنهاد دهنده متعهد است كه نصب و راه اندازي را انجام دهد و هزينه نصب و راه اندازي بر عهده "+ (inquiryMap.getInstalCost().equalsIgnoreCase("s")?"فروشنده":"خريدار")+" مي باشد", f26));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(13);
					productDSC.addCell(cell);
				}
				if (inquiryMap.getOtherCost()  != null && inquiryMap.getOtherCost().equalsIgnoreCase("y")) {
					cell.setPhrase(new Phrase(
							10,
							" ساير هزينه هاي متفرقه (اسكان ، اياب و ذهاب و غذا) به عهده "+ (inquiryMap.getOtherCost().equalsIgnoreCase("s")?"فروشنده":"خريدار") + " مي باشد ", f26));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(13);
					productDSC.addCell(cell);
				}
				if (inquiryMap.getCheckOffer() != null
						&& inquiryMap.getCheckOffer().equalsIgnoreCase("y")) {
					cell.setPhrase(new Phrase(
							10,
							"امکان بررسي پيشنهاد از ساير برندهاي مشابه ، با ارائه اطلاعات فني کامل قابل بررسي مي باشد.",
							f26));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(13);
					productDSC.addCell(cell);
				}

				String[] dscBlob=inquiryDAO.findBlobs(inquiryMap.getId());
				String tempDSC = "";
				if( dscBlob.length>0 && dscBlob[0]!= null ) {
					cell.setPhrase(new Phrase(10," توضيحات بازرگاني : "+  dscBlob[0] + "\n \n", f7));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(13);
					cell.setBorderWidthBottom(0.15f);
					productDSC.addCell(cell);
				}


				// Craete table definitions

				PdfPTable definitions = new PdfPTable(6);
				definitions.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				definitions.setSplitLate(true);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(4);
				cell.setBorderWidth(1);
				cell.setPaddingBottom(6);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

				cell.setPhrase(new Phrase(10, "شرايط فني :", f3));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setVerticalAlignment(Element.ALIGN_LEFT);
				cell.setColspan(6);
				cell.setBorderWidthBottom(0.15f);
				cell.setBorderWidthLeft(0.15f);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthRight(0.15f);;
				definitions.addCell(cell);



				if(inquiryMap.getSpareProductTime() != null && inquiryMap.getSpareProductTimeRequire().equalsIgnoreCase("y")){
					cell.setPhrase(new Phrase(
							10,
							" ارائه ريز قيمت ليست قطعات يدکي براي مدت "
									+ inquiryMap.getSpareProductTime() + " ماه ، به پيوست الزامي مي باشد.",
							f7));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(6);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0);
					cell.setBorderWidthLeft(0.15f);
					cell.setBorderWidthRight(0.15f);
					definitions.addCell(cell);
				}
				if (inquiryMap.getBriefing() != null && inquiryMap.getBriefing().equalsIgnoreCase("y")) {
					cell.setPhrase(new Phrase(
							10," جلسه توجيهي در تاريخ "+(RTL2LTR(inquiryMap.getBriefingDate()!=null?inquiryMap.getBriefingDate():"-"))+
							" در محل "+(inquiryMap.getBriefingPlace()!=null?inquiryMap.getBriefingPlace():"-")+
							" برگزار خواهد شد و پيشنهاد دهندگان مي بايست قيمت پيشنهادي خود را پس از تنظيم صورت جلسه توجيهي ارايه نمايند. "	,f7));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(6);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0);
					cell.setBorderWidthLeft(0.15f);
					cell.setBorderWidthRight(0.15f);
					definitions.addCell(cell);
				}

				if(inquiryMap.getTestDesc() !=null){
					cell.setPhrase(new Phrase(10," آزمايشهاي مورد درخواست :"+ inquiryMap.getTestDesc(), f7));
					cell.setPhrase(new Phrase(10,"",f26));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(6);
					cell.setBorderWidthBottom(0);
					cell.setBorderWidthLeft(0.15f);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthRight(0.15f);
					definitions.addCell(cell);
				}
//				@Query(value = "select  dsc,TECHNICLA_DESC,PACKNG_CONDITATION from FB.TBL_INQIRY where id = ?1   ", nativeQuery = true)
//				public String[] findBlobs(Long id);
//				String[] dscBlob=inquiryDAO.findBlobs(inquiryMap.getId());

				if( dscBlob.length>2 && dscBlob[2]!= null){
					cell.setPhrase(new Phrase(10," شرايط بسته بندي : "+ dscBlob[2], f7));
//					cell.setPhrase(new Phrase(10,"",f26));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(6);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0);
					cell.setBorderWidthLeft(0.15f);
					cell.setBorderWidthRight(0.15f);
					definitions.addCell(cell);
				}

				if( dscBlob.length>1 &&  dscBlob[1]!= null){
					cell.setPhrase(new Phrase(10," شرايط بسته بندي : "+ dscBlob[1], f7));
//					cell.setPhrase(new Phrase(10,"",f26));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(6);
					cell.setBorderWidthBottom(0.15f);
					cell.setBorderWidthLeft(0.15f);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthRight(0.15f);
					definitions.addCell(cell);
				}

				cell.setPhrase(new Phrase(10, " به پیشنهاداتی که از طريق فاکس و يا ايميل ارسال شود ترتيب اثر داده نخواهد شد", f3));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setVerticalAlignment(Element.ALIGN_LEFT);
				cell.setColspan(6);
				cell.setBorderWidthBottom(0.15f);
				cell.setBorderWidthLeft(0.15f);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthRight(0.15f);;
				definitions.addCell(cell);

				cell.setBorderWidthBottom(0);

				if (inquiryMap.getEndReplyDate() != null) {

					{
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setPhrase(new Phrase(10, "", f7));
						cell.setColspan(2);
						cell.setBorderWidthTop(0);
						cell.setBorderWidthBottom(0.15f);
						cell.setBorderWidthLeft(0.15f);
						cell.setBorderWidthRight(0);
						definitions.addCell(cell);
					}

				}

				definitions.setWidthPercentage(99);

				cell.setBorderWidthBottom(0.15f);
				cell.setBorderWidthTop(0.15f);
				cell.setBorderWidthLeft(0.15f);
				cell.setBorderWidthRight(0.15f);


				// ********************************************

				document.add(productDSC);
				document.add(definitions);
				if(h<inquiryIds.length-1)
					document.newPage();
			}

			document.close();
		} catch (Exception de) {
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


//	public static final com.itextpdf.text.Font Footer = null;
	private String path = "";
	private String[] inquiryIds = null;
	private Long loginAccountId = null;
	String inqiuryNumber = "";
	String inqiuryDate = "";


	class TableHeader extends PdfPageEventHelper {

		PdfTemplate total;

		public void onOpenDocument(PdfWriter writer, Document document) {
			total = writer.getDirectContent().createTemplate(30, 16);
			try {

			} catch (Exception de) {
				throw new ExceptionConverter(de);
			}
		}
		public void onStartPage(PdfWriter writer, Document document) {

			PdfPTable headerTable = new PdfPTable(6);
			PdfPTable titleTable = new PdfPTable(9);
			PdfPTable table = new PdfPTable(3);

			try {
				BaseFont bf = BaseFont.createFont(path + "/fonts/"
						+ "karnik.ttf", BaseFont.IDENTITY_H, true);

				Font f1 = new Font(bf, 9, Font.BOLD, Color.BLACK);
				Font f2 = new Font(bf, 11, Font.BOLD, Color.BLACK);
				Font f26 = new Font(bf, 12, Font.BOLD, Color.BLACK);
				Font f6 = new Font(bf, 14, Font.BOLD, Color.BLACK);
				Font f3 = new Font(bf, 18, Font.BOLD, Color.BLACK);
				Font f25 = new Font(bf, 14, Font.BOLD, Color.BLACK);

				PdfPCell cell = new PdfPCell();

				float[] relativeWidthsHeader = { 20, 20,20,20,20,20};
				headerTable.setWidths(relativeWidthsHeader);
				headerTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				headerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
				headerTable.setSplitLate(true);
				headerTable.setSpacingAfter(0.2f);
				headerTable.setWidthPercentage(90);


				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(4);
				cell.setBorderWidth(1);
				cell.setPaddingBottom(6);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

				cell.setColspan(6);
				cell.setPhrase(new Phrase(10, "شماره استعلام : " + (inqiuryNumber), f26));
				headerTable.addCell(cell);

				headerTable.setWidthPercentage(90);
				headerTable.setTotalWidth(795);
				headerTable.writeSelectedRows(0, 4, 24, 520,
						writer.getDirectContent());

			} catch (Exception de) {
				throw new ExceptionConverter(de);
			}

		}
		public void onEndPage(PdfWriter writer, Document document) {
		}

		public void onCloseDocument(PdfWriter writer, Document document) {

			ColumnText.showTextAligned(total, Element.ALIGN_LEFT, new Phrase(
					String.valueOf(writer.getPageNumber() - 1)), 2, 2, 0);
		}
	}

}
