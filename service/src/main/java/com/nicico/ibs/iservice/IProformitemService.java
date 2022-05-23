package com.nicico.ibs.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.dto.ProformitemDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProformitemService {

	ProformitemDTO.Info get(Long id);

	List<ProformitemDTO.Info> list();

	ProformitemDTO.Info create(ProformitemDTO.Create request);

	ProformitemDTO.Info update(Long id, ProformitemDTO.Update request, MultipartFile file, Long supplierId) throws IOException;

	void delete(Long id);

	void delete(ProformitemDTO.Delete request);

	SearchDTO.SearchRs<ProformitemDTO.Info> search(SearchDTO.SearchRq request);

	TotalResponse<ProformitemDTO.Info> search(NICICOCriteria criteria);

	String getDownloadFileName(String data, Long userId);
}
