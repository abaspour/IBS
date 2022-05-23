package com.nicico.ibs.controller;

import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.nicico.copper.common.Loggable;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.model.Account;
import com.nicico.ibs.model.Inquiry;
import com.nicico.ibs.model.RegisterPrice;
import com.nicico.ibs.model.User;
import com.nicico.ibs.repository.AccountDAO;
import com.nicico.ibs.repository.InquiryDAO;
import com.nicico.ibs.repository.RegisterPriceDAO;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/print-sakht-cover")
public class PrintSakhtCoverController {

    @Value("${ibs.upload.dir}")
    private String uploadDir;

    private final InquiryDAO inquiryDAO;
    private final RegisterPriceDAO registerPriceDAO;
    private final AccountDAO accountDAO;
    private final ModelMapper modelMapper;

    // ------------------------------
    private String path = "";
    private String reqNumber = "";
    private Long reqItemId = null;
    private Long count = new Long(1);
    private Inquiry inquiryMap;
    private List<RegisterPrice> registerPrice;
    private Account account;
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
                Font f26 = new Font(bf, 12, Font.BOLD, Color.BLACK);
                Font f6 = new Font(bf, 14, Font.BOLD, Color.BLACK);
                Font f3 = new Font(bf, 18, Font.BOLD, Color.BLACK);

                PdfPCell cell = new PdfPCell();

                float[] relativeWidthsHeader = {20, 20, 20, 20, 20, 20};
                headerTable.setWidths(relativeWidthsHeader);
                headerTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                headerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerTable.setSplitLate(true);
                headerTable.setSpacingAfter(0.2f);
                headerTable.setWidthPercentage(90);

                titleTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                float[] relativeWidthsHeader1 = {20, 20, 20, 20, 20, 20, 20, 20, 20};
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
                String file2 = path + "/images/En_logo_nfmc_new_3d.jpg"; //tblCompanyInfo.getLogo()
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
                cell.setPhrase(new Phrase(10, " ", f1));
                titleTable.addCell(cell);

                table.setWidths(new int[]{24, 24, 2});
                table.setTotalWidth(150);
                table.setLockedWidth(true);
                table.getDefaultCell().setFixedHeight(20);
                table.getDefaultCell().setBorder(0);
                table.addCell("");
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
                titleTable.writeSelectedRows(0, 4, 24, 580, writer.getDirectContent());

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

			/*cell.setPhrase(new Phrase(10, (placeMap.get("ADDRESS")!= null ? placeMap.get("ADDRESS").toString():"")
					+ "\t \t \t \t \t" + " تلفن: " +  ( placeMap.get("TEL") != null ? placeMap.get("TEL").toString():"") + " \n"
					+ (placeMap.get("RESPONSIBLE_POSITION")!= null ? placeMap.get("RESPONSIBLE_POSITION").toString():""), f1));*/
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
    //**********************************************

    private ByteArrayOutputStream pdfInternalFx(String path) {
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 200, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			PdfWriter writer = PdfWriter.getInstance(document, baos);
            TableHeader event = new TableHeader();
            writer.setPageEvent(event);
            document.open();

            BaseFont bf = BaseFont.createFont(path + "/fonts/" + "karnik.ttf",
                    BaseFont.IDENTITY_H, true);

            Font f2 = new Font(bf, 16, Font.BOLD, Color.BLACK);
            Font f1 = new Font(bf, 18, Font.BOLD, Color.BLACK);

            Phrase pr = new Phrase();
            PdfPCell cell = new PdfPCell(pr);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPTable tblDSC = new PdfPTable(1);
            tblDSC.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            tblDSC.setWidthPercentage(99);
            tblDSC.setSpacingAfter(0.5f);
            tblDSC.setSplitLate(true);
            cell.setBorderWidthBottom(0);
            cell.setBorderWidthTop(0);
            cell.setBorderWidthRight(0);
            cell.setBorderWidthLeft(0);

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_LEFT);

            cell.setPhrase(new Phrase(10, "\n ضرروري است كليه شركت كنندگان در استعلام موارد ذيل را رعايت نموده و به همراه مدارك پيشنهاد خود برك شرايط عمومي را مهر "
                    + " و امضا نموده و به پيوست پيشنهاد خود به اين شركت عودت نمايند ."
                    + " شرايط عمومي استعلام (تمامي استعلام ها) " + "\n"
                    + " 1) كليه پيشنهادات از تاريخ تعيين شده به عنوان مهلت ارائه آن به مدت 3 ماه مي بايد داراي اعتبار باشد. " + "\n"
                    + " 2) خواهشمند است درصورت انصراف از شركت دراستعلام، مراتب را به صورت كتبي اعلام نماييد. درغير اينصورت عدم پاسخگويي "
                    + " به منزله عدم امكان شركت در مناقصات بعدي تلقي خواهد شد. " + "\n"
                    + " 3) كليه پيشنهادات مي بايد قبل از مهلت تعيين شده ارسال گردد. " + "\n"
                    + " 4) به پيشنهاداتي كه بعد ازمهلت مقرر شده دريافت شوند ترتيب اثر داده نخواهد شد. " + "\n"
                    + " 5) به همراه پيشنهاد قيمت مي بايستي آناليز قيمت ها و روش توليد، ساخت/تعمير و كنترل و تضمين در مراحل انجام كار ذكر گردد. " + "\n"
                    + " 6) ارائه برنامه زمانبندي مراحل توليد الزامي است. " + "\n"
                    + " 7) اعلام شماره ثبت شركتها (درمورد شركتها) در پيشنهاد ارسالي ضروري است. " + "\n"
                    + " 8) مفاد ذيل بايد در پيشنهاد قيمت ملحوظ گردد:" + "\n"
                    + " 1,8 ) در صورت برنده شدن در استعلام مدل، قالب ،سمبه ،ماتريس و فيكسچر ساخته شده براي هر سفارش متعلق به خريدار بوده و"
                    + " درپايان قراراد به خريدار تحويل خواهد شد." + "\n"
                    + " 2,8 ) سازنده موظف است به هزينه خود نسبت به انجام آزمايشات مورد نياز در آزمايشگاه مورد تائيد دستگاه نظارت اقدام و نتايج"
                    + " مربوطه را ارائه نمايد. بررسي، تائيد يا عدم تائيد آنها توسط دستگاه نظارت انجام خواهد شد، در صورت نياز به تكرار آزمايش در"
                    + " آزمايشگاه ثالث هزينه مربوطه به عهده سازنده خواهد بود." + "\n"
                    + " 3,8 ) زمان مورد نياز جهت تأييد اولين قطعات توليدي به عنوان نمونه در واحد مصرف كننده نهايي حداقل 30 روز كاري از زمان"
                    + " تحويل نمونه مورد نظر متغير است و سازنده بايستي اين زمان را در كليه محاسبات خود لحاظ نمايد." + "\n"
                    + " 4,8 ) تاريخ پيشنهاد قيمت مي بايست به حروف در ذيل برگ پيشنهاد درج و مهر و امضاء گردد." + "\n"
                    + " 5,8 ) قطعات سفارش شده بايستي دربسته بندي اعلام شده مطابق طرح كنترل كيفيت و فرم زمانبندي مراحل كار تحويل گردند و"
                    + " هزينه هاي آن در پيشنهاد قيمت به تفكيك منظور گردد." + "\n"
                    + " 6,8 ) كسورات قانوني اعم از ماليات ، بيمه ، صندوق كارآموزي و غيره كه به موضوع اين قرارداد تعلق مي گيرد به عهده سازنده بوده"
                    + " و از هر صورتحساب وي كسر مي گردد . رعايت ماده(( 104 )) قانون ماليتهاي مستقيم و ماده (( 38 )) سازمان تامين اجتماعي الزامي است ." + "\n"
                    + " 7,8 ) سازنده بايستي شرايط لازم جهت حضور و نظارت و كنترل كيفي قطعات را در حين فرآيند توليد براي دستگاه نظارت خريدار  مهيا سازد." + "\n"
                    + " 8,8 ) نام و مشخصات سازنده و سفارش دهنده در محلي از قطعه ساخته شده كه در اثر كاربرد عملي قطعه از بين نرود به صورت مناسب حك شود." + "\n"
                    + " 9,8 ) نحوه پرداخت ها و تضمين ها :" + "\n\n"
                    + " 1,9,8 ) پيش پرداخت معادل (( 20 %)) كل سفارش خريد (( در دو مرحله )) در قبال اخذ ضمانت نامه مورد قبول به همان مبلغ به"
                    + " عنوان تضمين دريافت پيش پرداخت به سازنده پرداخت خواهد شد . مابقي سفارش خريد پس از تحويل قطعات در مقابل صورت حساب"
                    + " سازنده پس از تأييد دستگاه نظارت و پس ازكسر كسورات قانوني و حسن انجام كار پيش پرداخت ، پرداخت خواهد شد ." + "\n"
                    + " 2,9,8 ) مدرك تسويه حساب قطعي، رسيد انبار شركت ملي صنايع مس ايران مربوط به هر يك از مجتمع هايي كه اقدام به صدور درخواست نموده اند مي باشد." + "\n"
                    + " 3,9,8 ) از هر صورتحساب سازنده (( 10 %)) به عنوان تضمين حسن انجام معامله كسر و پس از پايان دوره تضمين (تحويل قطعي )"
                    + " در صورت تأييد دستگاه نظارت به سازنده مسترد خواهد شد." + "\n"
                    + " 4,9,8 ) به ميزان (( 10 %)) ضمانت نامه مورد قبول كارفرما به عنوان وثيقه حسن انجام كار از سازنده اخذ و تا پايان مدت ضمانت نزد خريدار باقي خواهد ماند ." + "\n"
                    , f2));
            cell.setColspan(1);
            tblDSC.addCell(cell);

            cell.setPhrase(new Phrase(10, "\n", f1));
            cell.setColspan(1);
            tblDSC.addCell(cell);

            cell.setPhrase(new Phrase(10, "                                                                                      تاريخ، مهر وامضاء تامين كننده", f1));
            cell.setColspan(1);
            tblDSC.addCell(cell);


            // ********************************************
            document.add(tblDSC);
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
