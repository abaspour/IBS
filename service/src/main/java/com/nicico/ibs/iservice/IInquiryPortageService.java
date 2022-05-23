package com.nicico.ibs.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.dto.InquiryPortageDTO;
import com.nicico.ibs.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IInquiryPortageService {

	InquiryPortageDTO.Info get(Long id);

	List<InquiryPortageDTO.Info> list();

	InquiryPortageDTO.Info create(InquiryPortageDTO.Create request);

	InquiryPortageDTO.Info update(Long id, InquiryPortageDTO.Update request, MultipartFile file, Long supplierId) throws IOException;

	void delete(Long id);

	void delete(InquiryPortageDTO.Delete request);

	SearchDTO.SearchRs<InquiryPortageDTO.Info> search(SearchDTO.SearchRq request);

	TotalResponse<InquiryPortageDTO.Info> search(NICICOCriteria criteria, User u);

	String getDownloadFileName(String data, Long userId);
}
