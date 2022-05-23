package com.nicico.ibs.controller;

import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.nicico.copper.common.Loggable;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.model.*;
import com.nicico.ibs.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.rmi.RemoteException;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/print-internal")
public class PrintInternalController {

    @Value("${ibs.upload.dir}")
    private String uploadDir;

    private final InquiryDAO inquiryDAO;
    private final RegisterPriceDAO registerPriceDAO;
    private final RegisterPriceHeaderDAO registerPriceHeaderDAO;
    private final AccountDAO accountDAO;
    private final ModelMapper modelMapper;
    private final PlaceDAO placeDAO;
    private final UnitDAO unitDAO;

    // ------------------------------
    private String path = "";
    private String reqNumber = "";
    private Long reqItemId = null;
    private Long count = new Long(1);
    private Inquiry inquiryMap;
    private RegisterPriceHeader registerPriceHeader;
    private List<RegisterPrice> registerPrice;
    private Account account;
    private Place place;
    PdfTemplate total;


    @Loggable
    @GetMapping(value = "/pdf")
    public void execute(HttpServletRequest request, HttpServletResponse response, Principal principal) {

        try {

            String[] inquiryIds = request.getParameter("inquiryId").toString().split(",");

            UsernamePasswordAuthenticationToken a = ((UsernamePasswordAuthenticationToken) principal);
            User u = modelMapper.map(a.getPrincipal(), User.class);

            inquiryMap = inquiryDAO.findById(new Long(inquiryIds[0]))
                    .orElseThrow(() -> new IBSException(IBSException.ErrorType.InquiryNotFound));
            if (inquiryMap.getSupplierId().compareTo(u.getSupplierId()) != 0)
                throw new IBSException(IBSException.ErrorType.InquiryNotFound);
            account=accountDAO.findAllById(inquiryMap.getSupplierId());
            registerPrice = registerPriceDAO.findAllByInquiryNumber(inquiryIds[0]);
			path = "C:\\IdeaProjects\\ibs\\web\\src\\assets";
            place=placeDAO.findAllById(inquiryMap.getPlaceId());
            registerPriceHeader=registerPriceHeaderDAO.findAllById(inquiryMap.getId());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos = pdfInternalFx(path);
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

                titleTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                float[] relativeWidthsHeader1 = { 30, 20,20,20,20,20,20,20,20};
                titleTable.setWidths(relativeWidthsHeader1);
                titleTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                titleTable.setSplitLate(true);
                titleTable.setSpacingAfter(0.5f);
                titleTable.setWidthPercentage(99);

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(0);
                cell.setBorderWidth(0);
                cell.setPaddingBottom(0);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                titleTable.addCell(cell);

                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                String file2 = path + "/images/arm.gif"; //tblCompanyInfo.getLogo()
                Image image2 = Image.getInstance(file2);
                image2.scalePercent(15);
                Chunk chk2 = new Chunk(image2, 0, 0);
                cell.setPhrase(new Phrase(chk2));
                titleTable.addCell(cell);

                cell.setColspan(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPhrase(new Phrase(10, "برگه استعلام بها"
                        + "\n" + "(شركت ملي صنايع مس ايران)",
                        f3));
                titleTable.addCell(cell);


                cell.setColspan(1);
                String file3 = path + "/images/En_logo_nfmc_new_3d.jpg"; //tblCompanyInfo.getLogo()
                Image image = Image.getInstance(file3);
                image.scalePercent(15);
                Chunk chk = new Chunk(image, 0, 0);

                cell.setPhrase(new Phrase(chk));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                titleTable.addCell(cell);


                table.setWidths(new int[]{24, 24, 2});
                table.setTotalWidth(150);
                table.setLockedWidth(true);
                table.getDefaultCell().setFixedHeight(20);
                table.getDefaultCell().setBorder(0);
                table.addCell(" ");
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                PdfPCell cell1 = new PdfPCell(Image.getInstance(total));
                cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                table.addCell(String.format("Page %d Of", writer.getPageNumber()));
                cell1.setBorder(0);
                table.addCell(cell1);
                cell1.addElement(table);
                cell1.setColspan(1);
                titleTable.addCell(cell1);

                titleTable.setWidthPercentage(90);
                titleTable.setTotalWidth(795);
                titleTable.writeSelectedRows(0, 4, 24, 580,writer.getDirectContent());

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(4);
                cell.setBorderWidth(1);
                cell.setPaddingBottom(6);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell.setColspan(4);
                cell.setPhrase(new Phrase(10, "فروشنده : " + account.getAccountName(), f6));
                headerTable.addCell(cell);

                cell.setColspan(1);
                cell.setPhrase(new Phrase(10, "شماره استعلام : " + (inquiryMap.getInquiryNumber() ), f26));
                headerTable.addCell(cell);

                cell.setPhrase(new Phrase(10, "تاريخ استعلام : "
                        + RTL2LTR(inquiryMap.getSendDate() != null ? inquiryMap.getSendDate() : "-"), f26));
                headerTable.addCell(cell);
                headerTable.setWidthPercentage(90);

                cell.setColspan(4);
                cell.setPhrase(new Phrase(10, "شناسه ملي / کد ملي : " + (account.getOrgNationalCode() != null ? account.getOrgNationalCode() : "-")
                        + "               کد اقتصادي: " + (account.getEconomicalCode() != null ? account.getEconomicalCode() : "-"), f6));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                headerTable.addCell(cell);

//				cell.setColspan(1);
//				cell.setPhrase(new Phrase(10, "زمان تحويل :", f26));
//				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//				cell.setVerticalAlignment(Element.ALIGN_LEFT);
//				headerTable.addCell(cell);

                cell.setPhrase(new Phrase(10, "محل تحويل:" + (inquiryMap.getDeliveryLocation()!=null && inquiryMap.getDeliveryLocation().getDeliveryNameFa()!=null ? inquiryMap.getDeliveryLocation().getDeliveryNameFa() : "-"), f26));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                headerTable.addCell(cell);
                headerTable.setWidthPercentage(80);
                String postCode = "-";

                cell.setColspan(4);
                cell.setPhrase(new Phrase(10, "آدرس : " + (account.getAddress() != null ? account.getAddress() : "-")
                        + "     کدپستي: " + (account.getPostCode() != null ? account.getPostCode() : "-"), f6));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                headerTable.addCell(cell);

                cell.setColspan(1);
                cell.setPhrase(new Phrase(10, "مهلت پاسخ : " + RTL2LTR(inquiryMap.getEndReplyDate() != null ? inquiryMap.getEndReplyDate() : "-"), f26));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                headerTable.addCell(cell);

                cell.setColspan(1);
                cell.setPhrase(new Phrase(10, "حداقل مدت اعتبار پيشنهاد : " + (inquiryMap.getValidTime() != null ? inquiryMap.getValidTime() + "روز" : "-"), f26));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                headerTable.addCell(cell);
                headerTable.setWidthPercentage(80);

                cell.setPhrase(new Phrase(10, "تلفن : ", f26));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(1);
                cell.setNoWrap(false);
                headerTable.addCell(cell);

                cell.setPhrase(new Phrase(10, RTL2LTR(account.getMainPhone() != null ? account.getMainPhone() : "-"), f26));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(1);
                cell.setNoWrap(false);
                cell.setRunDirection(1);
                headerTable.addCell(cell);

                cell.setRunDirection(0);
                cell.setPhrase(new Phrase(10, "فاکس فروشنده : ", f26));

                cell.setColspan(1);
                headerTable.addCell(cell);


                cell.setPhrase(new Phrase(10, RTL2LTR(account.getFaxNumber() != null ? account.getFaxNumber() : "-"), f26));
                cell.setColspan(1);
                cell.setRunDirection(1);
                headerTable.addCell(cell);

                cell.setRunDirection(0);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(2);
                cell.setPhrase(new Phrase(10, "ايميل  : " + (account.getEmail() != null ? account.getEmail() : "-"), f26));
                headerTable.addCell(cell);
//aaaaa
                headerTable.setWidthPercentage(90);
                headerTable.setTotalWidth(795);
                headerTable.writeSelectedRows(0, 4, 24, 520,
                        writer.getDirectContent());

            } catch (Exception de) {
                throw new ExceptionConverter(de);
            }

        }
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable footerTable = new PdfPTable(9);
            try {
                BaseFont bf = BaseFont.createFont(path + "/fonts/"
                        + "karnik.ttf", BaseFont.IDENTITY_H, true);

                Font f1 = new Font(bf, 14, Font.BOLD, Color.BLACK);

                PdfPCell cell = new PdfPCell();

                footerTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                footerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                footerTable.setSplitLate(true);
                footerTable.setSpacingAfter(0.2f);
                footerTable.setWidthPercentage(90);

                cell.setPhrase(new Phrase(10, (place.getAddress()!= null && !place.getAddress().equalsIgnoreCase("null") ? place.getAddress():"")
                        + "\t \t \t \t \t" +  ( place.getTel() != null && !place.getTel().equalsIgnoreCase("null")?  " تلفن: " +place.getTel():"") + " \n"
                        + (place.getResponsiblePosition()!= null && !place.getResponsiblePosition().toString().equalsIgnoreCase("null") ? place.getResponsiblePosition():""), f1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(8);
                cell.setBorderWidthBottom(0);
                cell.setBorderWidthLeft(0);
                cell.setBorderWidthRight(0);
                cell.setBorderWidthTop(1);
                footerTable.addCell(cell);

                cell.setPhrase(new Phrase(10, (inquiryMap.getInquiryNumber()), f1));//F-49-06/04
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setColspan(1);
                cell.setBorderWidthBottom(0);
                cell.setBorderWidthLeft(0);
                cell.setBorderWidthRight(0);
                cell.setBorderWidthTop(1);
                footerTable.addCell(cell);

                footerTable.setWidthPercentage(90);
                footerTable.setTotalWidth(795);
                footerTable.writeSelectedRows(0, 4, 24, 50,
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

    // *********************************************************************************

    private ByteArrayOutputStream pdfInternalFx(String path) {
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 205, 50);
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

            {

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
                int materialCountrer = registerPrice.size(); //  tblInquiries.length
//				cell.setPhrase(new Phrase(10, "شرح استعلام : "+ (inquiryMap.get("INQUIRYDESC")!= null?RTL2LTR(inquiryMap.get("INQUIRYDESC").toString()):"-"), f7));
//				cell.setColspan(13);
//				productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10, "رديف", f26));
                cell.setColspan(1);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10, "رديف آيتم", f26));
                cell.setColspan(1);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10, "شرح کالا ", f26));
                cell.setColspan(3);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10, "تعداد / واحد", f26));
                cell.setColspan(1);
                productDSC.addCell(cell);
                
                cell.setPhrase(new Phrase(10, "مشخصات  فني", f26));
                cell.setColspan(1);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10, "بهاي واحد (ريال)", f26));
                cell.setColspan(1);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10, "تخفيف", f26));
                cell.setColspan(1);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10, "بهاي کل (ريال)", f26));
                productDSC.addCell(cell);
                cell.setColspan(1);

                cell.setPhrase(new Phrase(10, "اطلاعات اختصاصي هر آيتم", f26));
                cell.setColspan(3);
                productDSC.addCell(cell);

                for (int i = 0; i < materialCountrer; i++) {

                    RegisterPrice registerMap = registerPrice.get(i);
                    String unitName=registerPriceDAO.findPartAndUnitNameById(registerMap.getId()).split("@@@@##@@")[1];
                    List<Object[]> att=registerPriceDAO. findItemAttach(inquiryMap.getSupplierId(),registerMap.getRequestItemId());

                    cell.setPhrase(new Phrase(10, new Long(i + 1).toString(),f13));
                    cell.setColspan(1);
                    productDSC.addCell(cell);

                    cell.setPhrase(new Phrase(10,(registerMap.getItemRow()!= null?registerMap.getItemRow():""), f2));
                    cell.setColspan(1);
                    productDSC.addCell(cell);

                    String dsc = "";
                    PdfPTable descTable = new PdfPTable(1);

                    if (registerMap.getItemDescp() != null) {
                        dsc = registerMap.getItemDescp();
                    }
                    descTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    descTable.addCell(new Phrase(10, dsc, f13));

                    if (registerMap.getItemDescl() != null) {
                        dsc = registerMap.getItemDescl();
                    }

                    descTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                    descTable.addCell(new Phrase(10, dsc, f13));

                    cell.addElement(descTable);
                    cell.setColspan(3);
                    productDSC.addCell(cell);

                    String inquiryAmountStr = "";
                    Long inquiryAmounLong = null;

                    if (registerMap.getItemQty() != null && !registerMap.getItemQty().equalsIgnoreCase("0")) {
                        inquiryAmountStr = registerMap.getItemQty();
                    }
                    if (inquiryAmountStr.contains(".0")) {
                        inquiryAmounLong = new Double(registerMap.getItemQty()).longValue();
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
                    
                    cell.setPhrase(new Phrase(10,(registerMap.getShomareNaghshe()!= null?registerMap.getShomareNaghshe():""), f2));
                    cell.setColspan(1);
                    productDSC.addCell(cell);

                    cell.setPhrase(new Phrase(10,(registerMap.getUnitPrice()!= null?decimalFormat.format(new Double(registerMap.getUnitPrice())):""), f1));
                    cell.setColspan(1);
                    productDSC.addCell(cell);

                    productDSC.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    cell.setPhrase(new Phrase(10,(registerMap.getDiscount()!= null?decimalFormat.format(new Double(registerMap.getDiscount())):""), f13));
                    cell.setColspan(1);
                    productDSC.addCell(cell);

                    productDSC.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    cell.setPhrase(new Phrase(10, (registerMap.getGheymateKol()!= null?decimalFormat.format(new Double(registerMap.getGheymateKol())):""), f1));
                    cell.setColspan(1);
                    productDSC.addCell(cell);


                    String testNeeded = "";
                    if(registerMap.getTestNeeded()!= null && registerMap.getTestNeeded().equalsIgnoreCase("y"))
                    {
                        testNeeded = "نياز به تست عملي نمونه پيشنهاد دهنده دارد. \n";
                    }
                    String attachment = "";
                    if(att != null){
                        attachment = "داراي پيوست فني مي باشد \n";
                    }

                        String costMode = "";
                        if(registerMap.getCostMode() != null){
                            if(registerMap.getCostMode().equalsIgnoreCase("s"))
                                costMode = "نياز به ساخت قالب دارد و هزينه آن به عهده فروشنده مي باشد \n";
                            else if(registerMap.getCostMode().equalsIgnoreCase("b"))
                                costMode = "نياز به ساخت قالب دارد و هزينه آن به عهده خريدار مي باشد \n";
                        }

                        String costModel = "";
                        if(registerMap.getCostModel() != null){
                            if(registerMap.getCostModel().equalsIgnoreCase("s"))
                                costModel = "نياز به ساخت مدل دارد و هزينه آن به عهده فروشنده مي باشد \n";
                            else if(registerMap.getCostModel().equalsIgnoreCase("b"))
                                costModel = "نياز به ساخت مدل دارد و هزينه آن به عهده خريدار مي باشد \n";
                        }


                        String guranteeTime = "";
                        if(registerMap.getGuranteeTime() != null){
                            guranteeTime = "شرايط گارانتي  و مدت آن : " + registerMap.getGuranteeTime()+ " \n" ;
                        }

                        String partMaterial = "";
                        if(registerMap.getPartMaterial() != null){
                            partMaterial = "جنس قطعه : " + registerMap.getPartMaterial() + " \n" ;
                        }

                        String unitWeight = "";
                        if(registerMap.getUnitWeight()!= null){
                            unitWeight = "وزن تقريبي واحد : " + registerMap.getUnitWeight() + " \n" ;
                        }


                        String modeMaterial = "";
                        if(registerMap.getModeMaterial()!= null){
                            modeMaterial = "جنس قالب : " + registerMap.getModeMaterial() + " \n" ;
                        }

                        String modelMaterial = "";
                        if(registerMap.getModelMaterial()!= null){
                            modelMaterial = "جنس مدل : " + registerMap.getModelMaterial() + " \n" ;
                        }
                        String noSample= "";
                        String sample= "";

                        if(registerMap.getItemSample()!= null && !registerMap.getItemSample().equalsIgnoreCase("n") ){
                            sample= " نمونه کالاي اين آيتم رويت شده \n";
                            noSample= " نمونه کالاي اين آيتم رويت نشده \n";

                        }
                        String neededTime = "";
                        if(registerMap.getDeliveryDate()!= null ){
                            neededTime = "زمان نياز از تاريخ نامه اعلام برنده : " + registerMap.getDeliveryDate()+ " روز "+ " \n" ;
                        }

                        String verifyPrice = "";
                        if(registerMap.getVerifyPrice()!= null && registerMap.getDeliveryDate().equalsIgnoreCase("nv")){
                            verifyPrice = "قيمت فوق براي شرح کالاي متفاوت با شرح فوق بوده و در توضيحات زير آمده است\n" ;
                        }
                        String packingCondition = "";
                        if(registerMap.getPackageCondition()!= null ){
                            packingCondition = " شرايط بسته بندي :" + registerMap.getPackageCondition()+ " \n" ;
                        }
                        String technicalDesc = "";
                        if(registerMap.getTechnicalDesc()!= null ){
                            technicalDesc = " شرايط فنی خریدار :" + registerMap.getTechnicalDesc()+ " \n" ;
                        }
                        String warrantyDesc = "";
                        if(registerMap.getWarrantyDesc()!= null ){
                            warrantyDesc = " شرايط گارانتی خریدار :" + registerMap.getWarrantyDesc()+ " \n" ;
                        }
                        String packingDesc = "";
                        if(registerMap.getPackingDesc()!= null ){
                            packingDesc = " شرايط بسته بندی خریدار :" + registerMap.getWarrantyDesc()+ " \n" ;
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
                    String supplierDesc="";
                    if(registerMap.getSupplierDesc()!= null ){
                        supplierDesc = "توضیحات فروشنده: " + registerMap.getSupplierDesc()+ " \n" ;
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
                            + warrantyDesc
                            +supplierDesc
                            +(registerMap.getDescription()!= null? "توضيحات : "+registerMap.getDescription():""), f2));

                        if(registerMap.getItemSample()!= null && !registerMap.getItemSample().equalsIgnoreCase("n") ){
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

                    if ( inquiryMap.getInstalCost() != null && inquiryMap.getInstalCost().toString().equalsIgnoreCase("b")) {
                    cell.setPhrase(new Phrase(10,
                            "هزينه نصب با در نظر گرفتن کليه کسورات قانوني : ",
                            f26));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_LEFT);
                    cell.setColspan(10);
                    productDSC.addCell(cell);

                    cell.setPhrase(new Phrase(10, "", f26));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_LEFT);
                    cell.setColspan(3);
                    productDSC.addCell(cell);

                }
//				if ( registerMap.get("PORTAGE_COST") != null) {
//					cell.setPhrase(new Phrase(10,
//							"هزينه بسته بندي ، بارگيري و حمل : ", f26));
//					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//					cell.setVerticalAlignment(Element.ALIGN_LEFT);
//					cell.setColspan(10);
//					productDSC.addCell(cell);
//
//					cell.setPhrase(new Phrase(10, "", f26));
//					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//					cell.setVerticalAlignment(Element.ALIGN_LEFT);
//					cell.setColspan(3);
//					productDSC.addCell(cell);
//				}
                    if (inquiryMap.getSpareProductTime() != null
                            && registerPriceHeader.getSubSpareProductCost() != null) {
                        cell.setPhrase(new Phrase(10, "هزينه قطعات يدکي  "
                                + inquiryMap.getSpareProductTime()
                                + " ماهه مطابق ليست پيوست :", f26));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_LEFT);
                    cell.setColspan(10);
                    productDSC.addCell(cell);

                    cell.setPhrase(new Phrase(10, "", f26));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_LEFT);
                    cell.setColspan(3);
                    productDSC.addCell(cell);
                }

                    if (inquiryMap.getOtherCost()!= null && inquiryMap.getOtherCost().toString().equalsIgnoreCase("s")) {
                    cell.setPhrase(new Phrase(10,
                            "هزينه هاي متفرقه(اسکان-اياب ذهاب -غذا) : ", f26));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_LEFT);
                    cell.setColspan(10);
                    productDSC.addCell(cell);

                    cell.setPhrase(new Phrase(10, "", f26));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_LEFT);
                    cell.setColspan(3);
                    productDSC.addCell(cell);

                }
                cell.setPhrase(new Phrase(10, "هزينه ماليات بر ارزش افزوده : " +
                        (registerPriceHeader.getTaxCost()!=null?
                                decimalFormat.format(new Double(registerPriceHeader.getTaxCost())):"")));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(10);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10,"", f26));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(3);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10, "هزينه بسته بندی : " +(registerPriceHeader.getPackingCost()!=null?decimalFormat.format(new Double(registerPriceHeader.getPackingCost())):""),f26));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(10);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10,"", f26));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(3);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10, "سایر هزینه ها : " +(registerPriceHeader.getOtherCost()!=null?decimalFormat.format(new Double(registerPriceHeader.getOtherCost())):""),f26));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(10);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10,"", f26));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(3);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10, "تخفیف : " +(registerPriceHeader.getDiscount()!=null?decimalFormat.format(new Double(registerPriceHeader.getDiscount())):""),f26));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(10);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10,"", f26));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(3);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10, ":هزینه  بارگیری و حمل" +(registerPriceHeader.getPortageCost()!=null?decimalFormat.format(new Double(registerPriceHeader.getPortageCost())):""),f26));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(10);
                productDSC.addCell(cell);

                cell.setPhrase(new Phrase(10,"", f26));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(3);
                productDSC.addCell(cell);


                cell.setPhrase(new Phrase(10, "جمع کل ", f7));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(10);
                productDSC.addCell(cell);

                Double sums = new Double(0.0);
                if(registerPriceHeader.getSumPrice()!= null ){
                    sums = new Double(registerPriceHeader.getSumPrice());
                    if(registerPriceHeader.getTaxCost()!= null ){
                        sums =  sums - new Double(registerPriceHeader.getTaxCost());
                    }
                }
                cell.setPhrase(new Phrase(10, ""+decimalFormat.format(sums),f26));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(3);
                productDSC.addCell(cell);

                cell.setBorderWidthTop(0.15f);
                cell.setBorderWidthBottom(0);
                cell.setBorderWidthLeft(0.15f);
                cell.setBorderWidthRight(0.15f);

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
                    cell.setPhrase(new Phrase(         10,
                            " ساير هزينه هاي متفرقه (اسكان ، اياب و ذهاب و غذا) به عهده "+
                                    (inquiryMap.getOtherCost().equalsIgnoreCase("s")?"فروشنده":"خريدار") + " مي باشد ", f26));
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

//                if(inquiryMap.get("DSC")!=null){
//                    cell.setPhrase(new Phrase(10, "توضيحات بازرگاني :"+ inquiryMap.get("DSC").toString()+"\n \n", f7));
//                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                    cell.setVerticalAlignment(Element.ALIGN_LEFT);
//                    cell.setColspan(13);
//                    cell.setBorderWidthBottom(0.15f);
//                    productDSC.addCell(cell);
//                }

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
                cell.setBorderWidthBottom(0);
                cell.setBorderWidthLeft(0.15f);
                cell.setBorderWidthTop(0.15f);
                cell.setBorderWidthRight(0.15f);
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
                            10," جلسه توجيهي در تاريخ "+(RTL2LTR(inquiryMap.getBriefingDate()!=null?inquiryMap.getBriefingDate():"-"))+" در محل "+(inquiryMap.getBriefingPlace()!=null?inquiryMap.getBriefingPlace():"-")+" برگزار خواهد شد و پيشنهاد دهندگان مي بايست قيمت پيشنهادي خود را پس از تنظيم صورت جلسه توجيهي ارايه نمايند. "
                            ,f7));
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
//                if( inquiryMap.get("PACKNG_CONDITATION")!= null){
//                    cell.setPhrase(new Phrase(10," شرايط بسته بندي : "+ inquiryMap.get("PACKNG_CONDITATION").toString(), f7));
//                    cell.setPhrase(new Phrase(10,"",f26));
//                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                    cell.setVerticalAlignment(Element.ALIGN_LEFT);
//                    cell.setColspan(6);
//                    cell.setBorderWidthTop(0);
//                    cell.setBorderWidthBottom(0);
//                    cell.setBorderWidthLeft(0.15f);
//                    cell.setBorderWidthRight(0.15f);
//                    definitions.addCell(cell);
//                }
//                if( inquiryMap.getT("TECHNICLA_DESC") != null){
//                    cell.setPhrase(new Phrase(10,"توضيحات فني  : "+  inquiryMap.get("TECHNICLA_DESC").toString(), f7));
//                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                    cell.setVerticalAlignment(Element.ALIGN_LEFT);
//                    cell.setColspan(6);
//                    cell.setBorderWidthBottom(0.15f);
//                    cell.setBorderWidthLeft(0.15f);
//                    cell.setBorderWidthTop(0);
//                    cell.setBorderWidthRight(0.15f);
//                    definitions.addCell(cell);
//                }
                cell.setBorderWidthBottom(0);

//					ParameterDAOImpl parameterDAOImpl = new ParameterDAOImpl();
//					TblParameter tblParameter = new TblParameter();
//					ParameterCaptionDAOImpl captionDAOImpl = new ParameterCaptionDAOImpl();
//					TblParameterCaption tblParameterCaption = new TblParameterCaption();
//					tblParameter = parameterDAOImpl.findByParamNameAndValue("inquiryDesc", "digital");
//					tblParameterCaption = captionDAOImpl.findByParameterAndLanguageId(tblParameter.getId(), new Long(1));
//
                    cell.setPhrase(new Phrase(10,(place.getDigitalDesc()!= null ?place.getDigitalDesc():" " ),f7));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_LEFT);
                    cell.setColspan(6);
                    definitions.addCell(cell);

                cell.setPhrase(new Phrase(10,"اطلاعيه:", f3));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(6);
                definitions.addCell(cell);

                cell.setPhrase(new Phrase(10,"به اطلاع کليه تامين کنندگان مي رساند کليه استعلام هاي شرکت ملي صنايع مس ايران از تاريخ 01/01/1395 فقط از طريق سامانه الکترونيکي ارسال و به صورت الکترونيکي دريافت مي شود.", f7));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(6);
                definitions.addCell(cell);

                cell.setPhrase(new Phrase(10,
                        " نام و امضاء و مهر فروشنده: ", f7));
                cell.setColspan(2);
                cell.setBorderWidthTop(0);
                cell.setBorderWidthBottom(0.15f);
                cell.setBorderWidthLeft(0);
                cell.setBorderWidthRight(0.15f);
                definitions.addCell(cell);

                cell.setPhrase(new Phrase(10, " نام و امضاء و مهر خريدار: "
                        + (place!= null && place.getResponsibleName()!= null && !place.getResponsibleName().equals("null") ?place.getResponsibleName():" " )
                        + "\n"
                        + (place!= null && place.getResponsiblePosition()!= null  && !place.getResponsiblePosition().equals("null")?place.getResponsiblePosition():" " )
                        , f7));
                cell.setColspan(6);
                cell.setBorderWidthTop(0);
                cell.setBorderWidthBottom(0.15f);
                cell.setBorderWidthLeft(0);
                cell.setBorderWidthRight(0);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                definitions.addCell(cell);

                if (inquiryMap.getEndReplyDate() != null) {
                    Image image1 = null;
                    Chunk chk1 = null;
                    if (place.getFileNewName() != null) {
                        String printLogo = "";
                        printLogo = place.getFileNewName();
                        String file1 = path  + printLogo;
                        try {
                            image1 = Image.getInstance(file1);
                            image1.scalePercent(100);
                            chk1 = new Chunk(image1, 0, 0);
                        } catch (Exception e) {
                        }

                    }
                    if (chk1 != null) {
                        cell.setPhrase(new Phrase(chk1));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setColspan(1);
                        cell.setBorderWidthTop(0);
                        cell.setBorderWidthBottom(0.15f);
                        cell.setBorderWidthLeft(0);
                        cell.setBorderWidthRight(0);
                        definitions.addCell(cell);
//
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        definitions.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        cell.setPhrase(new Phrase(10, "", f7));
                        cell.setColspan(1);
                        cell.setBorderWidthTop(0);
                        cell.setBorderWidthBottom(0.15f);
                        cell.setBorderWidthLeft(0.15f);;
                        cell.setBorderWidthRight(0);
                        definitions.addCell(cell);

                    } else
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

}