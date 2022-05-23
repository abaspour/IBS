package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.date.DateTimeDTO;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.util.file.FileInfo;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.ProformDTO;
import com.nicico.ibs.iservice.IProformService;
import com.nicico.ibs.model.Inquiry;
import com.nicico.ibs.model.Proform;
import com.nicico.ibs.repository.InquiryDAO;
import com.nicico.ibs.repository.ProformDAO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProformService implements IProformService {

    private final ProformDAO proformDAO;
    private final InquiryDAO inquiryDAo;
    private final ModelMapper modelMapper;
    @Value("${ibs.upload.dir}")
    private String uploadDir;

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public ProformDTO.Info get(Long id) {
        final Proform proform = proformDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.ProformNotFound));

        return modelMapper.map(proform, ProformDTO.Info.class);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public List<ProformDTO.Info> list() {
        final List<Proform> countries = proformDAO.findAll();

        return modelMapper.map(countries, new TypeToken<List<ProformDTO.Info>>() {
        }.getType());
    }

    //	@PreAuthorize("hasAuthority('C_COUNTRY')")
    @Transactional
    @Override
    public ProformDTO.Info create(ProformDTO.Create request) {
        final Proform proform = modelMapper.map(request, Proform.class);

        return save(proform);
    }

    //	@PreAuthorize("hasAuthority('U_COUNTRY')")
    @Transactional
    @Override
    public ProformDTO.Info update(Long id, ProformDTO.Update request, MultipartFile file,Long supplierId) throws IOException {
        final Proform proform = proformDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.ProformNotFound));

        final Inquiry inquiry = inquiryDAo.findById(new Long(proform.getInquiryNumber()))
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.InquiryNotFound));
        DateTimeDTO.DateTimeStrRs dateTimeStrRs = modelMapper.map(new Date(), DateTimeDTO.DateTimeStrRs.class);

        if (inquiry.getSupplierId().compareTo(supplierId) != 0 || inquiry.getEndReplyDate().compareTo(dateTimeStrRs.getDate()) < 0 ||
                !(inquiry.getVerifyStatus().equals("n") || inquiry.getVerifyStatus().equals("v")))
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

                new File(uploadDir + "fani" + File.separator).mkdirs();
                destinationFile = new File(uploadDir + "fani/" + changedFileName);
                file.transferTo(destinationFile);
                fileInfo.setFileName(destinationFile.getPath());
                fileInfo.setFileSize(file.getSize());
//                request.setAttachmentFileName(file.getOriginalFilename());
//                request.setAttachmentFileName(changedFileName);
//                request.setAttachmentFileSize((file.getSize()));
//                request.setAttachmentDateCreated(currentDate);
//			register.setoAttachExtension(FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase());
            }

        }
        final Proform updating = new Proform();
        modelMapper.map(proform, updating);
        modelMapper.map(request, updating);
//        if (file != null && proform.getAttachmentNewName() != null)
//            new File(uploadDir + "fani/" + proform.getAttachmentNewName()).delete();
        return save(updating);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(Long id) {
        proformDAO.deleteById(id);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(ProformDTO.Delete request) {
        final List<Proform> countries = proformDAO.findAllById(request.getIds());

        proformDAO.deleteAll(countries);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public SearchDTO.SearchRs<ProformDTO.Info> search(SearchDTO.SearchRq request) {
        return SearchUtil.search(proformDAO, request, proform -> modelMapper.map(proform, ProformDTO.Info.class));
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public TotalResponse<ProformDTO.Info> search(NICICOCriteria criteria) {
        Boolean attach = criteria.getCriteria().toString().contains("id");
        int i = 0;
        List<String> ids = new ArrayList<>();
        List<String> fns = new ArrayList<>();
        List<String> dsc = new ArrayList<>();
        return SearchUtil.search(proformDAO, criteria, proform -> {
                    ProformDTO.Info r = modelMapper.map(proform, ProformDTO.Info.class);
//                    if (attach) {
//                        String[] u = proformDAO.findInquiryTypeAndSpareById(proform.getInquiryNumber()).split("@@@@##@@");
//                        if (u != null && u[0] != null)
//                            r.setInquiryType(u[0]);
//                        if (u != null && u[1] != null)
//                            r.setSpareProductTime(u[1]);
//                    }
                    return r;
                }
        );
    }

    private ProformDTO.Info save(Proform proform) {
        final Proform saved = proformDAO.saveAndFlush(proform);
        return modelMapper.map(saved, ProformDTO.Info.class);
    }

    public String getDownloadFileName(String data,Long userId){
        if (data.startsWith("att"))
            return proformDAO.findItemAttachName(userId,new Long(data.substring(3)));
        else if (data.startsWith("map"))
                return proformDAO.findMapName(userId,new Long(data.substring(3)));
        return null;
    }
}
