package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.date.DateTimeDTO;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.util.file.FileInfo;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.ProformitemDTO;
import com.nicico.ibs.iservice.IProformitemService;
import com.nicico.ibs.model.Inquiry;
import com.nicico.ibs.model.Proformitem;
import com.nicico.ibs.repository.InquiryDAO;
import com.nicico.ibs.repository.ProformitemDAO;
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
public class ProformitemService implements IProformitemService {

    private final ProformitemDAO proformitemDAO;
    private final InquiryDAO inquiryDAo;
    private final ModelMapper modelMapper;
    @Value("${ibs.upload.dir}")
    private String uploadDir;

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public ProformitemDTO.Info get(Long id) {
        final Proformitem proformitem = proformitemDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.ProformitemNotFound));

        return modelMapper.map(proformitem, ProformitemDTO.Info.class);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public List<ProformitemDTO.Info> list() {
        final List<Proformitem> countries = proformitemDAO.findAll();

        return modelMapper.map(countries, new TypeToken<List<ProformitemDTO.Info>>() {
        }.getType());
    }

    //	@PreAuthorize("hasAuthority('C_COUNTRY')")
    @Transactional
    @Override
    public ProformitemDTO.Info create(ProformitemDTO.Create request) {
        final Proformitem proformitem = modelMapper.map(request, Proformitem.class);

        return save(proformitem);
    }

    //	@PreAuthorize("hasAuthority('U_COUNTRY')")
    @Transactional
    @Override
    public ProformitemDTO.Info update(Long id, ProformitemDTO.Update request, MultipartFile file,Long supplierId) throws IOException {
        final Proformitem proformitem = proformitemDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.ProformitemNotFound));

        final Inquiry inquiry = inquiryDAo.findById(new Long(proformitem.getProform().getInquiryNumber()))
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.InquiryNotFound));
        DateTimeDTO.DateTimeStrRs dateTimeStrRs = modelMapper.map(new Date(), DateTimeDTO.DateTimeStrRs.class);

        if (inquiry.getSupplierId().compareTo(supplierId)!=0 || inquiry.getEndReplyDate().compareTo(dateTimeStrRs.getDate()) < 0 ||
//                proformitem.getChangeStatus().equals("1") ||
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
                request.setAttachmentFileName(file.getOriginalFilename());
                request.setAttachmentNewName(changedFileName);
                request.setAttachmentFileSize((file.getSize()));
                request.setAttachmentDateCreated(currentDate);
//			register.setoAttachExtension(FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase());
            }

        }
        final Proformitem updating = new Proformitem();
        modelMapper.map(proformitem, updating);
        modelMapper.map(request, updating);
        if (file != null && proformitem.getAttachmentNewName() != null)
            new File(uploadDir + "fani/" + proformitem.getAttachmentNewName()).delete();
        if (proformitem.getProform().getCurrency().getId().compareTo(request.getCurrencyId())!=0){
            proformitemDAO.updateCurrency(proformitem.getProform().getId(),request.getCurrencyId());
        }
        return save(updating);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(Long id) {
        proformitemDAO.deleteById(id);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(ProformitemDTO.Delete request) {
        final List<Proformitem> countries = proformitemDAO.findAllById(request.getIds());

        proformitemDAO.deleteAll(countries);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public SearchDTO.SearchRs<ProformitemDTO.Info> search(SearchDTO.SearchRq request) {
        return SearchUtil.search(proformitemDAO, request, proformitem -> modelMapper.map(proformitem, ProformitemDTO.Info.class));
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public TotalResponse<ProformitemDTO.Info> search(NICICOCriteria criteria) {
        Boolean attach = criteria.getCriteria().toString().contains("id");
        int i = 0;
        List<String> ids = new ArrayList<>();
        List<String> fns = new ArrayList<>();
        List<String> dsc = new ArrayList<>();
        return SearchUtil.search(proformitemDAO, criteria, proformitem -> {
                    ProformitemDTO.Info r = modelMapper.map(proformitem, ProformitemDTO.Info.class);
                    if (attach) {
                        List<Object[]> att = proformitemDAO.findItemAttach(proformitem.getProform().getSupplierId(), proformitem.getRequestItem().getId());
                        att.forEach(objects -> {
                            ids.add(objects[0].toString());
                            fns.add(objects[1].toString());
                            dsc.add(objects[2].toString());
                        });

                        r.setListAttachIds(ids);
                        r.setListAttachFileName(fns);
                        r.setListAttachDesc(dsc);
                    }
                    return r;
                }
        );
    }

    private ProformitemDTO.Info save(Proformitem proformitem) {
        final Proformitem saved = proformitemDAO.saveAndFlush(proformitem);
        return modelMapper.map(saved, ProformitemDTO.Info.class);
    }

    public String getDownloadFileName(String data,Long userId){
        if (data.startsWith("att"))
            return proformitemDAO.findItemAttachName(userId,new Long(data.substring(3)));
        else if (data.startsWith("map"))
                return proformitemDAO.findMapName(userId,new Long(data.substring(3)));
        return null;
    }
}
