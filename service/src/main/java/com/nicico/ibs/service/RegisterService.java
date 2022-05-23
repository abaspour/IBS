package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.GridResponse;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.util.file.FileInfo;
import com.nicico.copper.core.util.mail.MailUtil;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.RegisterDTO;
import com.nicico.ibs.iservice.IRegisterService;
import com.nicico.ibs.model.Register;
import com.nicico.ibs.model.RegisterFirst;
import com.nicico.ibs.repository.RegisterDAO;
import com.nicico.ibs.repository.RegisterFirstDAO;
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
import java.util.*;

@RequiredArgsConstructor
@Service
public class RegisterService implements IRegisterService {

    private final RegisterDAO registerDAO;
    private final RegisterFirstDAO registerFirstDAO;
    private final ModelMapper modelMapper;
    private final MailUtil mailUtil;
    private Register register;
    @Value("${ibs.upload.dir}")
    private String uploadDir;

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public RegisterDTO.Info get(Long id) {
        final Register register = registerDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.RegisterNotFound));

        return modelMapper.map(register, RegisterDTO.Info.class);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public List<RegisterDTO.Info> list() {
        final List<Register> countries = registerDAO.findAll();

        return modelMapper.map(countries, new TypeToken<List<RegisterDTO.Info>>() {
        }.getType());
    }

    @Transactional
    @Override
    public RegisterDTO.Info attach(RegisterDTO.Create request, MultipartFile file) throws IOException {
        // update header record to the latest status
        saveHeader(request);

        FileInfo fileInfo = new FileInfo();
        File destinationFile = null;

        System.out.println(FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase());
        if (FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase().equals("EXE")) {
            System.out.println("not this extention");
        }
        if (!file.isEmpty()) {

            String currentDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

            String changedFileName = file.getOriginalFilename().replace(file.getOriginalFilename(), UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename())).toUpperCase();

            new File(uploadDir + File.separator).mkdirs();
            destinationFile = new File(uploadDir + changedFileName);
            file.transferTo(destinationFile);
            fileInfo.setFileName(destinationFile.getPath());
            fileInfo.setFileSize(file.getSize());
            register.setOldFileName(file.getOriginalFilename());
            register.setNewFileName(changedFileName);
//			register.setoAttachExtension(FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase());
        }

        return save(register);
    }

    //	@PreAuthorize("hasAuthority('C_COUNTRY')")
    @Transactional
    @Override
    public RegisterDTO.Info create(RegisterDTO.Create request) {
        RegisterFirst registerFirst = registerFirstDAO.findByRegisterNoAndRegisterCountry(request.getRegisterNo(),request.getRegisterCountry());
        if (registerFirst == null || !registerFirst.getStatus().equalsIgnoreCase("0"))
            throw new IBSException(IBSException.ErrorType.RegisterNotFound);
        registerFirst.setStatus("1");
        registerFirstDAO.save(registerFirst);
        saveHeader(request);
        return save(register);
    }

    private void saveHeader(RegisterDTO.Create request) {
        register = modelMapper.map(request, Register.class);
    }

    private RegisterDTO.Info save(Register register) {
        final Register saved = registerDAO.saveAndFlush(register);
        return modelMapper.map(saved, RegisterDTO.Info.class);
    }


    //	@PreAuthorize("hasAuthority('U_COUNTRY')")
    @Transactional
    @Override
    public RegisterDTO.Info update(Long id, RegisterDTO.Update request) {
        final Register register = registerDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.RegisterNotFound));

        final Register updating = new Register();
        modelMapper.map(register, updating);
        modelMapper.map(request, updating);

        return save(updating);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(Long id) {
        registerDAO.deleteById(id);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(RegisterDTO.Delete request) {
        final List<Register> countries = registerDAO.findAllById(request.getIds());

        registerDAO.deleteAll(countries);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public SearchDTO.SearchRs<RegisterDTO.Info> search(SearchDTO.SearchRq request) {
        return SearchUtil.search(registerDAO, request, register -> modelMapper.map(register, RegisterDTO.Info.class));
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public TotalResponse<RegisterDTO.Info> search(NICICOCriteria criteria) {
        return SearchUtil.search(registerDAO, criteria, register -> modelMapper.map(register, RegisterDTO.Info.class));
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public TotalResponse<RegisterDTO.InfoTuple> searchInfoTuple(NICICOCriteria criteria) {
        return SearchUtil.search(registerDAO, criteria, register -> modelMapper.map(register, RegisterDTO.InfoTuple.class));
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional
    @Override
    public TotalResponse<RegisterDTO.InfoTuple> searchInfoTuple(String criteria) {
        GridResponse g = new GridResponse();
        TotalResponse<RegisterDTO.InfoTuple> rtn = new TotalResponse<>(g);
        String[] fields = criteria.split(",");fields[0]=fields[0].trim();fields[1]=fields[1].trim();fields[2]=fields[2].trim();
        if (fields.length==3) {
            RegisterFirst registerFirst = registerFirstDAO.findByRegisterNoAndRegisterCountry(fields[0], fields[1]);
            if (registerFirst == null) {
                Random r = new Random();
                int id = 0;
                while (true) {
                    id = r.nextInt(100000);
                    RegisterFirst rf = registerFirstDAO.findById(new Long(id)).orElse(null);
                    if (rf == null)
                        break;
                }
                registerFirst = new RegisterFirst();
                registerFirst.setRegisterNo(fields[0]);
                registerFirst.setRegisterCountry(fields[1]);
                registerFirst.setContactPersonEmail(fields[2]);
                registerFirst.setRegisterId(String.valueOf(id));
                registerFirst.setStatus("0");
                registerFirst = registerFirstDAO.save(registerFirst);
                rtn.getResponse().setTotalRows(0);
                try {
                    mailUtil.sendNicicoEmailMessage("abaspour@nicico.com", "abaspour@nicico.com".split(","), null
                            , "shipmentEmail.", "EmailBody " + String.valueOf(id),
                            null, null);
                } catch (Exception e) {
                    rtn.getResponse().setTotalRows(-1);
                    e.printStackTrace();
                }
            } else if (!registerFirst.getStatus().equalsIgnoreCase("0")) {
                rtn.getResponse().setTotalRows(1);
            } else
                try {
                    mailUtil.sendNicicoEmailMessage("abaspour@nicico.com", "abaspour@nicico.com".split(","), null
                            , "shipmentEmail.", "EmailBody " + registerFirst.getRegisterId(),
                            null, null);
                    rtn.getResponse().setTotalRows(0);
                } catch (Exception e) {
                    rtn.getResponse().setTotalRows(-1);
                    e.printStackTrace();
                }
        } else { // find registerID
            RegisterFirst r=registerFirstDAO.findByRegisterId(fields[3].trim());
            if (r==null)
                rtn.getResponse().setTotalRows(0);
            else {
                if (r.getStatus().equalsIgnoreCase("0")) {
                    rtn.getResponse().setTotalRows(1);
                    RegisterDTO.InfoTuple i = modelMapper.map(r, RegisterDTO.InfoTuple.class);
                    List<RegisterDTO.InfoTuple> l = Arrays.asList(i);
                    ;

                    rtn.getResponse().setData(l);
                } else {
                    rtn.getResponse().setTotalRows(2);
                }
            }
        }
        return rtn;
    }
/*
      final String criteria1 = "{ \"operator\":\"and\", \"criteria\" : " +
			  " [  { \"fieldName\":\"registerNo\", \"operator\":\"equals\", \"value\": "+fields[0]+"  } " +
        " , { \"fieldName\":\"registerCountry\", \"operator\":\"equals\", \"value\": "+fields[1]+"  }	 "+
        " , { \"fieldName\":\"contactPersonEmail\", \"operator\":\"equals\", \"value\": "+fields[2]+"   } " +
			  "	] } ";
		final MultiValueMap<String, String> args = new LinkedMultiValueMap<>();
		args.put("criteria", Collections.singletonList(criteria1));

		final NICICOCriteria nicicoCriteria = NICICOCriteria.of(args);
		TotalResponse<RegisterDTO.InfoTuple> s= SearchUtil.search(registerDAO, nicicoCriteria, register -> modelMapper.map(register, RegisterDTO.InfoTuple.class));
 */

}
