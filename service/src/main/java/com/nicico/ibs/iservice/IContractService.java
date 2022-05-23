package com.nicico.ibs.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.dto.ContractDTO;

import java.util.List;

public interface IContractService {

	ContractDTO.Info get(Long id);

	List<ContractDTO.Info> list();

	ContractDTO.Info create(ContractDTO.Create request);

	ContractDTO.Info update(Long id, Long supplierId) ;

	void delete(Long id);

	void delete(ContractDTO.Delete request);

	SearchDTO.SearchRs<ContractDTO.Info> search(SearchDTO.SearchRq request);

	TotalResponse<ContractDTO.Info> search(NICICOCriteria criteria);

	String getDownloadFileName(String data, Long userId);
}
