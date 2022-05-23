package com.nicico.ibs.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.dto.RegisterDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IRegisterService {

	RegisterDTO.Info get(Long id);

	List<RegisterDTO.Info> list();

	RegisterDTO.Info create(RegisterDTO.Create request);

	RegisterDTO.Info attach(RegisterDTO.Create request, MultipartFile file) throws IOException;

	RegisterDTO.Info update(Long id, RegisterDTO.Update request);

	void delete(Long id);

	void delete(RegisterDTO.Delete request);

	SearchDTO.SearchRs<RegisterDTO.Info> search(SearchDTO.SearchRq request);

	TotalResponse<RegisterDTO.Info> search(NICICOCriteria criteria);

	TotalResponse<RegisterDTO.InfoTuple> searchInfoTuple(NICICOCriteria criteria);

	TotalResponse<RegisterDTO.InfoTuple> searchInfoTuple(String criteria);
}
