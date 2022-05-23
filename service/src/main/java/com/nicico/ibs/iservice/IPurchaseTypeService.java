package com.nicico.ibs.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.dto.PurchaseTypeDTO;

import java.util.List;

public interface IPurchaseTypeService {

	PurchaseTypeDTO.Info get(Long id);

	List<PurchaseTypeDTO.Info> list();

	PurchaseTypeDTO.Info create(PurchaseTypeDTO.Create request);

	PurchaseTypeDTO.Info update(Long id, PurchaseTypeDTO.Update request);

	void delete(Long id);

	void delete(PurchaseTypeDTO.Delete request);

	SearchDTO.SearchRs<PurchaseTypeDTO.Info> search(SearchDTO.SearchRq request);

	TotalResponse<PurchaseTypeDTO.Info> search(NICICOCriteria criteria);
}
