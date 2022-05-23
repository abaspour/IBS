package com.nicico.ibs.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.dto.RegisterPriceHeaderDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IRegisterPriceHeaderService {

	RegisterPriceHeaderDTO.Info get(Long id);

	List<RegisterPriceHeaderDTO.Info> list();

	RegisterPriceHeaderDTO.Info create(RegisterPriceHeaderDTO.Create request);

	RegisterPriceHeaderDTO.Info update(Long id, RegisterPriceHeaderDTO.Update request, MultipartFile file,Long supplierId) throws IOException;

	void delete(Long id);

	void delete(RegisterPriceHeaderDTO.Delete request);

	SearchDTO.SearchRs<RegisterPriceHeaderDTO.Info> search(SearchDTO.SearchRq request);

	TotalResponse<RegisterPriceHeaderDTO.Info> search(NICICOCriteria criteria);

	String getDownloadFileName(String data, Long userId);
}
