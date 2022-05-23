package com.nicico.ibs.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.dto.InquiryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IInquiryService {

	InquiryDTO.Info get(Long id);

	List<InquiryDTO.Info> list();

	InquiryDTO.Info create(InquiryDTO.Create request);

	int inquiryStatusCount(Long id,Long supplierId);

	String inquiryCopy(Long id,Long supplierId);

	InquiryDTO.Info update(Long id, InquiryDTO.Update request, MultipartFile file,Long supplierId) throws IOException ;

	void delete(Long id);

	void delete(InquiryDTO.Delete request);

	SearchDTO.SearchRs<InquiryDTO.Info> search(SearchDTO.SearchRq request);

	TotalResponse<InquiryDTO.Info> search(NICICOCriteria criteria);

	String getDownloadFileName(String data,Long supplierId);

	}
