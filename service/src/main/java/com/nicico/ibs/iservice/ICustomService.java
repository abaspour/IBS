package com.nicico.ibs.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.dto.CustomDTO;

import java.util.List;

public interface ICustomService {

	CustomDTO.Info get(Long id);

	List<CustomDTO.Info> list();

	CustomDTO.Info create(CustomDTO.Create request);

	CustomDTO.Info update(Long id, CustomDTO.Update request);

	void delete(Long id);

	void delete(CustomDTO.Delete request);

	SearchDTO.SearchRs<CustomDTO.Info> search(SearchDTO.SearchRq request);

	TotalResponse<CustomDTO.Info> search(NICICOCriteria criteria);
}
