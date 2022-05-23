package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.TransmitInstrumentDTO;
import com.nicico.ibs.iservice.ITransmitInstrumentService;
import com.nicico.ibs.model.TransmitInstrument;
import com.nicico.ibs.repository.TransmitInstrumentDAO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransmitInstrumentService implements ITransmitInstrumentService {

	private final TransmitInstrumentDAO transmitInstrumentDAO;
	private final ModelMapper modelMapper;

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public TransmitInstrumentDTO.Info get(Long id) {
		final TransmitInstrument transmitInstrument = transmitInstrumentDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.TransmitInstrumentNotFound));

		return modelMapper.map(transmitInstrument, TransmitInstrumentDTO.Info.class);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public List<TransmitInstrumentDTO.Info> list() {
		final List<TransmitInstrument> countries = transmitInstrumentDAO.findAll();

		return modelMapper.map(countries, new TypeToken<List<TransmitInstrumentDTO.Info>>() {
		}.getType());
	}

	//	@PreAuthorize("hasAuthority('C_COUNTRY')")
	@Transactional
	@Override
	public TransmitInstrumentDTO.Info create(TransmitInstrumentDTO.Create request) {
		final TransmitInstrument transmitInstrument = modelMapper.map(request, TransmitInstrument.class);

		return save(transmitInstrument);
	}

	//	@PreAuthorize("hasAuthority('U_COUNTRY')")
	@Transactional
	@Override
	public TransmitInstrumentDTO.Info update(Long id, TransmitInstrumentDTO.Update request) {
		final TransmitInstrument transmitInstrument = transmitInstrumentDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.TransmitInstrumentNotFound));

		final TransmitInstrument updating = new TransmitInstrument();
		modelMapper.map(transmitInstrument, updating);
		modelMapper.map(request, updating);

		return save(updating);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(Long id) {
		transmitInstrumentDAO.deleteById(id);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(TransmitInstrumentDTO.Delete request) {
		final List<TransmitInstrument> countries = transmitInstrumentDAO.findAllById(request.getIds());

		transmitInstrumentDAO.deleteAll(countries);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public SearchDTO.SearchRs<TransmitInstrumentDTO.Info> search(SearchDTO.SearchRq request) {
		return SearchUtil.search(transmitInstrumentDAO, request, transmitInstrument -> modelMapper.map(transmitInstrument, TransmitInstrumentDTO.Info.class));
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public TotalResponse<TransmitInstrumentDTO.Info> search(NICICOCriteria criteria) {
		return SearchUtil.search(transmitInstrumentDAO, criteria, transmitInstrument -> modelMapper.map(transmitInstrument, TransmitInstrumentDTO.Info.class));
	}

	private TransmitInstrumentDTO.Info save(TransmitInstrument transmitInstrument) {
		final TransmitInstrument saved = transmitInstrumentDAO.saveAndFlush(transmitInstrument);
		return modelMapper.map(saved, TransmitInstrumentDTO.Info.class);
	}
}
