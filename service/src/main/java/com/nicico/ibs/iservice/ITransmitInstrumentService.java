package com.nicico.ibs.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.dto.TransmitInstrumentDTO;

import java.util.List;

public interface ITransmitInstrumentService {

	TransmitInstrumentDTO.Info get(Long id);

	List<TransmitInstrumentDTO.Info> list();

	TransmitInstrumentDTO.Info create(TransmitInstrumentDTO.Create request);

	TransmitInstrumentDTO.Info update(Long id, TransmitInstrumentDTO.Update request);

	void delete(Long id);

	void delete(TransmitInstrumentDTO.Delete request);

	SearchDTO.SearchRs<TransmitInstrumentDTO.Info> search(SearchDTO.SearchRq request);

	TotalResponse<TransmitInstrumentDTO.Info> search(NICICOCriteria criteria);
}
