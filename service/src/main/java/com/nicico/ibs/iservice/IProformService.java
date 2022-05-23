package com.nicico.ibs.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.dto.ProformDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProformService {

	ProformDTO.Info get(Long id);

	List<ProformDTO.Info> list();

	ProformDTO.Info create(ProformDTO.Create request);

	ProformDTO.Info update(Long id, ProformDTO.Update request, MultipartFile file, Long supplierId) throws IOException;

	void delete(Long id);

	void delete(ProformDTO.Delete request);

	SearchDTO.SearchRs<ProformDTO.Info> search(SearchDTO.SearchRq request);

	TotalResponse<ProformDTO.Info> search(NICICOCriteria criteria);

	String getDownloadFileName(String data, Long userId);
}
