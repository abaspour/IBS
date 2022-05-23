package com.nicico.ibs.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.dto.RegisterPriceDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IRegisterPriceService {

	RegisterPriceDTO.Info get(Long id);

	List<RegisterPriceDTO.Info> list();

	RegisterPriceDTO.Info create(RegisterPriceDTO.Create request);

	RegisterPriceDTO.Info update(Long id, RegisterPriceDTO.Update request, MultipartFile file,Long supplierId) throws IOException;

	void delete(Long id);

	void delete(RegisterPriceDTO.Delete request);

	SearchDTO.SearchRs<RegisterPriceDTO.Info> search(SearchDTO.SearchRq request);

	TotalResponse<RegisterPriceDTO.Info> search(NICICOCriteria criteria);

	String getDownloadFileName(String data,Long userId);
}
