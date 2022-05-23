package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.date.DateTimeDTO;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.core.util.file.FileInfo;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.InquiryPortageDTO;
import com.nicico.ibs.iservice.IInquiryPortageService;
import com.nicico.ibs.model.Inquiry;
import com.nicico.ibs.model.InquiryPortage;
import com.nicico.ibs.model.User;
import com.nicico.ibs.repository.InquiryDAO;
import com.nicico.ibs.repository.InquiryPortageDAO;
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
public class InquiryPortageService implements IInquiryPortageService {

	private final InquiryPortageDAO inquiryPortageDAO;
	private final InquiryDAO inquiryDAo;
	private final ModelMapper modelMapper;
	@Value("${ibs.upload.dir}")
	private String uploadDir;

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public InquiryPortageDTO.Info get(Long id) {
		final InquiryPortage inquiryPortage = inquiryPortageDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.InquiryPortageNotFound));

		return modelMapper.map(inquiryPortage, InquiryPortageDTO.Info.class);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public List<InquiryPortageDTO.Info> list() {
		final List<InquiryPortage> countries = inquiryPortageDAO.findAll();

		return modelMapper.map(countries, new TypeToken<List<InquiryPortageDTO.Info>>() {
		}.getType());
	}

	//	@PreAuthorize("hasAuthority('C_COUNTRY')")
	@Transactional
	@Override
	public InquiryPortageDTO.Info create(InquiryPortageDTO.Create request) {
		final InquiryPortage inquiryPortage = modelMapper.map(request, InquiryPortage.class);

		return save(inquiryPortage);
	}

	//	@PreAuthorize("hasAuthority('U_COUNTRY')")
	@Transactional
	@Override
	public InquiryPortageDTO.Info update(Long id, InquiryPortageDTO.Update request, MultipartFile file, Long supplierId) throws IOException {
		final InquiryPortage inquiryPortage = inquiryPortageDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.InquiryPortageNotFound));

		final Inquiry inquiry = inquiryDAo.findById(new Long(inquiryPortage.getInquiryNumber()))
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
				request.setAttachmentFileName(file.getOriginalFilename());
				request.setAttachmentFileName(changedFileName);
				request.setAttachmentFileSize(Float.valueOf(file.getSize()));
				request.setAttachmentDateCreated(currentDate);
//			register.setoAttachExtension(FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase());
			}

		}
		final InquiryPortage updating = new InquiryPortage();
		modelMapper.map(inquiryPortage, updating);
		modelMapper.map(request, updating);
//        if (file != null && inquiryPortage.getAttachmentNewName() != null)
//            new File(uploadDir + "fani/" + inquiryPortage.getAttachmentNewName()).delete();
		return save(updating);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(Long id) {
		inquiryPortageDAO.deleteById(id);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(InquiryPortageDTO.Delete request) {
		final List<InquiryPortage> countries = inquiryPortageDAO.findAllById(request.getIds());

		inquiryPortageDAO.deleteAll(countries);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public SearchDTO.SearchRs<InquiryPortageDTO.Info> search(SearchDTO.SearchRq request) {
		return SearchUtil.search(inquiryPortageDAO, request, inquiryPortage -> modelMapper.map(inquiryPortage, InquiryPortageDTO.Info.class));
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public TotalResponse<InquiryPortageDTO.Info> search(NICICOCriteria criteria, User u) {
//		Boolean attach = criteria.getCriteria().toString().contains("id");
		return SearchUtil.search(inquiryPortageDAO, criteria, inquiryPortage -> {
					InquiryPortageDTO.Info r = modelMapper.map(inquiryPortage, InquiryPortageDTO.Info.class);
					final Inquiry inquiry = inquiryDAo.findById(new Long(inquiryPortage.getInquiryNumber()))
							.orElseThrow(() -> new IBSException(IBSException.ErrorType.InquiryNotFound));
					if (inquiry.getSupplierId().compareTo(u.getSupplierId()) != 0)
						r = null;
					return r;
				}
		);
	}

	private InquiryPortageDTO.Info save(InquiryPortage inquiryPortage) {
		final InquiryPortage saved = inquiryPortageDAO.saveAndFlush(inquiryPortage);
		return modelMapper.map(saved, InquiryPortageDTO.Info.class);
	}

	public String getDownloadFileName(String data, Long userId) {
//        if (data.startsWith("att"))
//            return inquiryPortageDAO.findItemAttachName(userId,new Long(data.substring(3)));
//        else if (data.startsWith("map"))
//                return inquiryPortageDAO.findMapName(userId,new Long(data.substring(3)));
		return null;
	}
}
