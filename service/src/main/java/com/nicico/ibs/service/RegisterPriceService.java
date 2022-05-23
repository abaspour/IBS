package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.date.DateTimeDTO;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.util.file.FileInfo;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.RegisterPriceDTO;
import com.nicico.ibs.iservice.IRegisterPriceService;
import com.nicico.ibs.model.Inquiry;
import com.nicico.ibs.model.RegisterPrice;
import com.nicico.ibs.repository.InquiryDAO;
import com.nicico.ibs.repository.RegisterPriceDAO;
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
public class RegisterPriceService implements IRegisterPriceService {

    private final RegisterPriceDAO registerPriceDAO;
    private final InquiryDAO inquiryDAo;
    private final ModelMapper modelMapper;
    @Value("${ibs.upload.dir}")
    private String uploadDir;

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public RegisterPriceDTO.Info get(Long id) {
        final RegisterPrice registerPrice = registerPriceDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.RegisterPriceNotFound));

        return modelMapper.map(registerPrice, RegisterPriceDTO.Info.class);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public List<RegisterPriceDTO.Info> list() {
        final List<RegisterPrice> countries = registerPriceDAO.findAll();

        return modelMapper.map(countries, new TypeToken<List<RegisterPriceDTO.Info>>() {
        }.getType());
    }

    //	@PreAuthorize("hasAuthority('C_COUNTRY')")
    @Transactional
    @Override
    public RegisterPriceDTO.Info create(RegisterPriceDTO.Create request) {
        final RegisterPrice registerPrice = modelMapper.map(request, RegisterPrice.class);

        return save(registerPrice);
    }

    //	@PreAuthorize("hasAuthority('U_COUNTRY')")
    @Transactional
    @Override
    public RegisterPriceDTO.Info update(Long id, RegisterPriceDTO.Update request, MultipartFile file, Long supplierId) throws IOException {
        final RegisterPrice registerPrice = registerPriceDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.RegisterPriceNotFound));
        final Inquiry inquiry = inquiryDAo.findById(new Long(registerPrice.getInquiryNumber()))
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.InquiryNotFound));
        DateTimeDTO.DateTimeStrRs dateTimeStrRs = modelMapper.map(new Date(), DateTimeDTO.DateTimeStrRs.class);

        if (inquiry.getSupplierId().compareTo(supplierId) != 0 || inquiry.getEndReplyDate().compareTo(dateTimeStrRs.getDate()) < 0 ||
                registerPrice.getChangeStatus().equals("1")||
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
                request.setAttachFileName(file.getOriginalFilename());
                request.setAttachNewFileName(changedFileName);
                request.setAttachFileSize(file.getSize());
                request.setAttachDateCreated(currentDate);
//			register.setoAttachExtension(FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase());
            }

        }
        final RegisterPrice updating = new RegisterPrice();
        modelMapper.map(registerPrice, updating);
        modelMapper.map(request, updating);
        if (file != null && registerPrice.getAttachNewFileName() != null)
            new File(uploadDir + "fani/" + registerPrice.getAttachNewFileName()).delete();
        return save(updating);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(Long id) {
        registerPriceDAO.deleteById(id);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(RegisterPriceDTO.Delete request) {
        final List<RegisterPrice> countries = registerPriceDAO.findAllById(request.getIds());

        registerPriceDAO.deleteAll(countries);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public SearchDTO.SearchRs<RegisterPriceDTO.Info> search(SearchDTO.SearchRq request) {
        return SearchUtil.search(registerPriceDAO, request, registerPrice -> modelMapper.map(registerPrice, RegisterPriceDTO.Info.class));
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public TotalResponse<RegisterPriceDTO.Info> search(NICICOCriteria criteria) {
        Boolean attach = criteria.getCriteria().toString().contains("id");
        int i = 0;
        List<String> ids = new ArrayList<>();
        List<String> fns = new ArrayList<>();
        List<String> dsc = new ArrayList<>();
        return SearchUtil.search(registerPriceDAO, criteria, registerPrice -> {
                    RegisterPriceDTO.Info r = modelMapper.map(registerPrice, RegisterPriceDTO.Info.class);

                    String[] u = registerPriceDAO.findPartAndUnitNameById(registerPrice.getId()).split("@@@@##@@");
                    if (u != null && u[1] != null)
                        r.setUnitName(u[1]);
                    if (u != null && u[0] != null)
                        r.setEngMaterialProperty(u[0]);
                    if (u != null && u[2] != null)
                        r.setInquiryType(u[2]);
                    if (u != null && u[3] != null)
                        r.setPlaceID(u[3]);
                    if (attach) {
                        List<Object[]> att = registerPriceDAO.findItemAttach(registerPrice.getSupplierId(), registerPrice.getRequestItemId());
                        att.forEach(objects -> {
                            ids.add(objects[0].toString());
                            fns.add(objects[1].toString());
                            dsc.add(objects[2].toString());
                        });
                        if (attach && r.getInquiryType().equalsIgnoreCase("s")) {
                            List<Object[]> map = registerPriceDAO.findMap(registerPrice.getSupplierId(), registerPrice.getRequestItemId());
                            map.forEach(objects -> {
                                ids.add(objects[0].toString());
                                fns.add(objects[1].toString());
                                dsc.add("نقشه");
                            });
                        }
                        r.setListAttachIds(ids);
                        r.setListAttachFileName(fns);
                        r.setListAttachDesc(dsc);
                    }
                    return r;
                }
        );
    }

    private RegisterPriceDTO.Info save(RegisterPrice registerPrice) {
        final RegisterPrice saved = registerPriceDAO.saveAndFlush(registerPrice);
        return modelMapper.map(saved, RegisterPriceDTO.Info.class);
    }

    public String getDownloadFileName(String data, Long userId) {
        if (data.startsWith("att"))
            return registerPriceDAO.findItemAttachName(userId, new Long(data.substring(3)));
        else if (data.startsWith("map"))
            return registerPriceDAO.findMapName(userId, new Long(data.substring(3)));
        return null;
    }
}
