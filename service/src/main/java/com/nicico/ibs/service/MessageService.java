package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.util.file.FileInfo;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.MessageDTO;
import com.nicico.ibs.iservice.IMessageService;
import com.nicico.ibs.model.Inquiry;
import com.nicico.ibs.model.Message;
import com.nicico.ibs.model.User;
import com.nicico.ibs.repository.InquiryDAO;
import com.nicico.ibs.repository.MessageDAO;
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
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MessageService implements IMessageService {

    private final MessageDAO messageDAO;
    private final InquiryDAO inquiryDAo;
    private final ModelMapper modelMapper;
    @Value("${ibs.upload.dir}")
    private String uploadDir;

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public MessageDTO.Info get(Long id) {
        final Message message = messageDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.MessageNotFound));

        return modelMapper.map(message, MessageDTO.Info.class);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public List<MessageDTO.Info> list() {
        final List<Message> countries = messageDAO.findAll();

        return modelMapper.map(countries, new TypeToken<List<MessageDTO.Info>>() {
        }.getType());
    }

    //	@PreAuthorize("hasAuthority('C_COUNTRY')")
    @Transactional
    @Override
    public MessageDTO.Info create(MessageDTO.Create request, MultipartFile file, User supplier) throws IOException{
        final Inquiry inquiry = inquiryDAo.findById(new Long(request.getInquiryNumber()))
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.InquiryNotFound));
        if (inquiry.getSupplierId().compareTo(supplier.getSupplierId()) != 0 )
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
                request.setFileOldName(file.getOriginalFilename());
                request.setFileNewName(changedFileName);
//                request.setAttachmentFileSize((file.getSize()));
//                request.setAttachmentDateCreated(currentDate);
//			register.setoAttachExtension(FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase());
            }
        }

        Long mId=messageDAO.findNewId();
        int i=messageDAO.insertMsg(mId,request.getSubject(),supplier.getId(),supplier.getLastName(),
                "مدير ارتباط با تامين كنندگان",request.getMessageText(),request.getMessageKind(),request.getInquiryNumber());
        i=messageDAO.insertReceiveMsg(mId);
        if (request.getFileNewName()!=null && !request.getFileNewName().equals(""))
            i=messageDAO.insertAttMsg(mId,request.getFileNewName(),request.getFileOldName());
        return new MessageDTO.Info();
    }

    //	@PreAuthorize("hasAuthority('U_COUNTRY')")
    @Transactional
    @Override
    public MessageDTO.Info update(Long id, Map request, Long supplierId)  {
        int i; //isViewed:"na",delete:"na",recover:"na"
        if (request.get("isViewed")!=null && request.get("isViewed").toString().equals("isViewed"))
           i=messageDAO.setViewedMsg(id,supplierId);
        if (request.get("delete")!=null && request.get("delete").equals("delete"))
           i=messageDAO.setTrashMsg(id,supplierId);
        if (request.get("recover")!=null && request.get("recover").equals("recover"))
           i=messageDAO.setRecoverMsg(id,supplierId);
        return new MessageDTO.Info();
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(Long id) {
        messageDAO.deleteById(id);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(MessageDTO.Delete request) {
        final List<Message> countries = messageDAO.findAllById(request.getIds());

        messageDAO.deleteAll(countries);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public SearchDTO.SearchRs<MessageDTO.Info> search(SearchDTO.SearchRq request) {
        return SearchUtil.search(messageDAO, request, message -> modelMapper.map(message, MessageDTO.Info.class));
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public TotalResponse<MessageDTO.Info> search(NICICOCriteria criteria) {
//        Boolean attach = criteria.getCriteria().toString().contains("id");
//        int i = 0;
//        List<String> ids = new ArrayList<>();
//        List<String> fns = new ArrayList<>();
//        List<String> dsc = new ArrayList<>();
        return SearchUtil.search(messageDAO, criteria, message -> {
                    MessageDTO.Info r = modelMapper.map(message, MessageDTO.Info.class);
//                    if (attach) {
//                        String[] u = messageDAO.findInquiryTypeAndSpareById(message.getInquiryNumber()).split("@@@@##@@");
//                        if (u != null && u[0] != null)
//                            r.setInquiryType(u[0]);
//                        if (u != null && u[1] != null)
//                            r.setSpareProductTime(u[1]);
//                    }
                    return r;
                }
        );
    }

    private MessageDTO.Info save(Message message) {
        final Message saved = messageDAO.saveAndFlush(message);
        return modelMapper.map(saved, MessageDTO.Info.class);
    }

    public String getDownloadFileName(String data,Long userId){
        final Message message = messageDAO.findById(new Long(data))
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.MessageNotFound));
        if (message.getSenderId().compareTo(userId) != 0 )
            throw new IBSException(IBSException.ErrorType.InquiryNotFound);
        return message.getFileNewName();
    }
}
