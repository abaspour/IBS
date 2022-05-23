package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.date.DateTimeDTO;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.common.util.date.DateUtil;
import com.nicico.copper.core.util.file.FileInfo;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.InquiryDTO;
import com.nicico.ibs.iservice.IInquiryService;
import com.nicico.ibs.model.Inquiry;
import com.nicico.ibs.repository.InquiryDAO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InquiryService implements IInquiryService {
    @Value("${ibs.upload.dir}")
    private String uploadDir;

    private final InquiryDAO inquiryDAO;
    private final ModelMapper modelMapper;

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public InquiryDTO.Info get(Long id) {
        final Inquiry inquiry = inquiryDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.InquiryNotFound));

        return modelMapper.map(inquiry, InquiryDTO.Info.class);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public List<InquiryDTO.Info> list() {
        final List<Inquiry> countries = inquiryDAO.findAll();

        return modelMapper.map(countries, new TypeToken<List<InquiryDTO.Info>>() {
        }.getType());
    }

    //	@PreAuthorize("hasAuthority('C_COUNTRY')")
    @Transactional
    @Override
    public InquiryDTO.Info create(InquiryDTO.Create request) {
        final Inquiry inquiry = modelMapper.map(request, Inquiry.class);

        return save(inquiry);
    }

    @Transactional
    @Override
    public 	int inquiryStatusCount(Long id,Long supplierId){
        return inquiryDAO.inquiryStatusCount( id, supplierId);
    }

    @Transactional
    @Override
    public 	String inquiryCopy(Long id,Long userId){
        return inquiryDAO.inquiryCopy( id, userId);
    }

    //	@PreAuthorize("hasAuthority('U_COUNTRY')")
    @Transactional
    @Override
    public InquiryDTO.Info update(Long id, InquiryDTO.Update request, MultipartFile file, Long supplierId) throws IOException {
        final Inquiry inquiry = inquiryDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.InquiryNotFound));
        DateTimeDTO.DateTimeStrRs dateTimeStrRs = modelMapper.map(new Date(), DateTimeDTO.DateTimeStrRs.class);

        if (inquiry.getSupplierId().compareTo(supplierId) != 0 || inquiry.getEndReplyDate().compareTo(dateTimeStrRs.getDate()) < 0 ||
                !(inquiry.getVerifyStatus().equals("n") || inquiry.getVerifyStatus().equals("v")))
            throw new IBSException(IBSException.ErrorType.InquiryNotFound);
        if (request.getVerifyStatus().equals("c"))
            request.setCancelDate(dateTimeStrRs.getDate());
        else if (request.getVerifyStatus().equals("r"))
            request.setReplyDate(dateTimeStrRs.getDate());
        else if (!(request.getVerifyStatus().equals("n") || request.getVerifyStatus().equals("v")))
            throw new IBSException(IBSException.ErrorType.InquiryNotFound);
        if (file != null) {
            FileInfo fileInfo = new FileInfo();
            File destinationFile = null;
            System.out.println(FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase());
            if (FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase().equals("EXE")) {
                System.out.println("not this extention");
            }
            if (!file.isEmpty()) {

                String currentDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

                String changedFileName = file.getOriginalFilename().replace(file.getOriginalFilename(), UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename())).toUpperCase();

                new File(uploadDir + "price" + File.separator).mkdirs();
                destinationFile = new File(uploadDir + "price/" + changedFileName);
                file.transferTo(destinationFile);
                fileInfo.setFileName(destinationFile.getPath());
                fileInfo.setFileSize(file.getSize());
                request.setAttachmentFileName(file.getOriginalFilename());
                request.setAttachmentNewName(changedFileName);
                request.setAttachmentFileSize((file.getSize()));
                request.setAttachmentDateCreated(currentDate);
//			register.setoAttachExtension(FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase());
            }
        }
        final Inquiry updating = new Inquiry();
        modelMapper.map(inquiry, updating);
        modelMapper.map(request, updating);
        if (file != null && inquiry.getAttachmentNewName() != null)
            new File(uploadDir + "price/" + inquiry.getAttachmentNewName()).delete();

        return save(updating);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(Long id) {
        inquiryDAO.deleteById(id);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(InquiryDTO.Delete request) {
        final List<Inquiry> countries = inquiryDAO.findAllById(request.getIds());

        inquiryDAO.deleteAll(countries);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public SearchDTO.SearchRs<InquiryDTO.Info> search(SearchDTO.SearchRq request) {
        return SearchUtil.search(inquiryDAO, request, inquiry -> modelMapper.map(inquiry, InquiryDTO.Info.class));
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public TotalResponse<InquiryDTO.Info> search(NICICOCriteria criteria) {
   //     return SearchUtil.search(inquiryDAO, criteria, inquiry -> modelMapper.map(inquiry, InquiryDTO.Info.class));
        DateTimeDTO.DateTimeStrRs dateTimeStrRs = modelMapper.map(new Date(), DateTimeDTO.DateTimeStrRs.class);
        return SearchUtil.search(inquiryDAO, criteria, inquiry ->  {
            InquiryDTO.Info r = modelMapper.map(inquiry, InquiryDTO.Info.class);
            if ((r.getVerifyStatus().equals("n") || r.getVerifyStatus().equals("v")) && r.getEndReplyDate().compareTo(dateTimeStrRs.getDate()) < 0) {
                r.setCancelDate(r.getEndReplyDate());
                if (r.getInquiryType().equals("e"))
                    r.setSupDesc("Expired without any response.");
                else
                    r.setSupDesc("انقضای زمان پاسخ");
            }
            if (r.getInquiryType().equals("e")) {
                r.setEnInquiryDate(DateUtil.convertKhToMi1(r.getInquiryDate()));
                if (r.getCancelDate()!=null) r.setEnCancelDate(DateUtil.convertKhToMi1(r.getCancelDate()));
                r.setEnSendDate(DateUtil.convertKhToMi1(r.getSendDate()));
                r.setEnEndReplyDate(DateUtil.convertKhToMi1(r.getEndReplyDate()));
            }
            return r;
        });

    }

    private InquiryDTO.Info save(Inquiry inquiry) {
        final Inquiry saved = inquiryDAO.saveAndFlush(inquiry);
        return modelMapper.map(saved, InquiryDTO.Info.class);
    }

    public String getDownloadFileName(String id, Long supplierId) {
        return inquiryDAO.findAttachName(new Long(id), supplierId);
    }

}
