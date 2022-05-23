package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.date.DateTimeDTO;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.util.file.FileInfo;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.RegisterPriceHeaderDTO;
import com.nicico.ibs.iservice.IRegisterPriceHeaderService;
import com.nicico.ibs.model.Inquiry;
import com.nicico.ibs.model.RegisterPriceHeader;
import com.nicico.ibs.repository.InquiryDAO;
import com.nicico.ibs.repository.RegisterPriceHeaderDAO;
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
public class RegisterPriceHeaderService implements IRegisterPriceHeaderService {

    private final RegisterPriceHeaderDAO registerPriceHeaderDAO;
    private final InquiryDAO inquiryDAo;
    private final ModelMapper modelMapper;
    @Value("${ibs.upload.dir}")
    private String uploadDir;

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public RegisterPriceHeaderDTO.Info get(Long id) {
        final RegisterPriceHeader registerPriceHeader = registerPriceHeaderDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.RegisterPriceHeaderNotFound));

        return modelMapper.map(registerPriceHeader, RegisterPriceHeaderDTO.Info.class);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public List<RegisterPriceHeaderDTO.Info> list() {
        final List<RegisterPriceHeader> countries = registerPriceHeaderDAO.findAll();

        return modelMapper.map(countries, new TypeToken<List<RegisterPriceHeaderDTO.Info>>() {
        }.getType());
    }

    //	@PreAuthorize("hasAuthority('C_COUNTRY')")
    @Transactional
    @Override
    public RegisterPriceHeaderDTO.Info create(RegisterPriceHeaderDTO.Create request) {
        final RegisterPriceHeader registerPriceHeader = modelMapper.map(request, RegisterPriceHeader.class);

        return save(registerPriceHeader);
    }

    //	@PreAuthorize("hasAuthority('U_COUNTRY')")
    @Transactional
    @Override
    public RegisterPriceHeaderDTO.Info update(Long id, RegisterPriceHeaderDTO.Update request, MultipartFile file,Long supplierId) throws IOException {
        final RegisterPriceHeader registerPriceHeader = registerPriceHeaderDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.RegisterPriceHeaderNotFound));

        final Inquiry inquiry = inquiryDAo.findById(new Long(registerPriceHeader.getInquiryNumber()))
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.InquiryNotFound));
        DateTimeDTO.DateTimeStrRs dateTimeStrRs = modelMapper.map(new Date(), DateTimeDTO.DateTimeStrRs.class);

        if (inquiry.getSupplierId().compareTo(supplierId) != 0 || inquiry.getEndReplyDate().compareTo(dateTimeStrRs.getDate()) < 0 ||
                registerPriceHeader.getChangeStatus().equals("1") ||
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
                request.setSpareProductFileName(file.getOriginalFilename());
                request.setSpareProductFileNewName(changedFileName);
                request.setSpareProductFileSize(String.valueOf(file.getSize()));
                request.setSpareProductFileDateCreated(currentDate);
//			register.setoAttachExtension(FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase());
            }

        }
        final RegisterPriceHeader updating = new RegisterPriceHeader();
        modelMapper.map(registerPriceHeader, updating);
        modelMapper.map(request, updating);
        if (file != null && registerPriceHeader.getSpareProductFileNewName() != null)
            new File(uploadDir + "fani/" + registerPriceHeader.getSpareProductFileNewName()).delete();
        return save(updating);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(Long id) {
        registerPriceHeaderDAO.deleteById(id);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(RegisterPriceHeaderDTO.Delete request) {
        final List<RegisterPriceHeader> countries = registerPriceHeaderDAO.findAllById(request.getIds());

        registerPriceHeaderDAO.deleteAll(countries);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public SearchDTO.SearchRs<RegisterPriceHeaderDTO.Info> search(SearchDTO.SearchRq request) {
        return SearchUtil.search(registerPriceHeaderDAO, request, registerPriceHeader -> modelMapper.map(registerPriceHeader, RegisterPriceHeaderDTO.Info.class));
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public TotalResponse<RegisterPriceHeaderDTO.Info> search(NICICOCriteria criteria) {
        Boolean attach = criteria.getCriteria().toString().contains("id");
        int i = 0;
        List<String> ids = new ArrayList<>();
        List<String> fns = new ArrayList<>();
        List<String> dsc = new ArrayList<>();
        return SearchUtil.search(registerPriceHeaderDAO, criteria, registerPriceHeader -> {
                    RegisterPriceHeaderDTO.Info r = modelMapper.map(registerPriceHeader, RegisterPriceHeaderDTO.Info.class);
                    if (attach) {
                        String[] u = registerPriceHeaderDAO.findInquiryTypeAndSpareById(registerPriceHeader.getInquiryNumber()).split("@@@@##@@");
                        if (u != null && u[0] != null)
                            r.setInquiryType(u[0]);
                        if (u != null && u[1] != null)
                            r.setSpareProductTime(u[1]);
                    }
                    return r;
                }
        );
    }

    private RegisterPriceHeaderDTO.Info save(RegisterPriceHeader registerPriceHeader) {
        final RegisterPriceHeader saved = registerPriceHeaderDAO.saveAndFlush(registerPriceHeader);
        return modelMapper.map(saved, RegisterPriceHeaderDTO.Info.class);
    }

    public String getDownloadFileName(String data,Long userId){
        if (data.startsWith("att"))
            return registerPriceHeaderDAO.findItemAttachName(userId,new Long(data.substring(3)));
        else if (data.startsWith("map"))
                return registerPriceHeaderDAO.findMapName(userId,new Long(data.substring(3)));
        return null;
    }
}
