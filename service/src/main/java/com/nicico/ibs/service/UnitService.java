package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.UnitDTO;
import com.nicico.ibs.iservice.IUnitService;
import com.nicico.ibs.model.Unit;
import com.nicico.ibs.repository.UnitDAO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UnitService implements IUnitService {

	private final UnitDAO unitDAO;
	private final ModelMapper modelMapper;

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public UnitDTO.Info get(Long id) {
		final Unit unit = unitDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.UnitNotFound));

		return modelMapper.map(unit, UnitDTO.Info.class);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public List<UnitDTO.Info> list() {
		final List<Unit> countries = unitDAO.findAll();

		return modelMapper.map(countries, new TypeToken<List<UnitDTO.Info>>() {
		}.getType());
	}

	//	@PreAuthorize("hasAuthority('C_COUNTRY')")
	@Transactional
	@Override
	public UnitDTO.Info create(UnitDTO.Create request) {
		final Unit unit = modelMapper.map(request, Unit.class);

		return save(unit);
	}

	//	@PreAuthorize("hasAuthority('U_COUNTRY')")
	@Transactional
	@Override
	public UnitDTO.Info update(Long id, UnitDTO.Update request) {
		final Unit unit = unitDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.UnitNotFound));

		final Unit updating = new Unit();
		modelMapper.map(unit, updating);
		modelMapper.map(request, updating);

		return save(updating);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(Long id) {
		unitDAO.deleteById(id);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(UnitDTO.Delete request) {
		final List<Unit> countries = unitDAO.findAllById(request.getIds());

		unitDAO.deleteAll(countries);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public SearchDTO.SearchRs<UnitDTO.Info> search(SearchDTO.SearchRq request) {
		return SearchUtil.search(unitDAO, request, unit -> modelMapper.map(unit, UnitDTO.Info.class));
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public TotalResponse<UnitDTO.Info> search(NICICOCriteria criteria) {
		return SearchUtil.search(unitDAO, criteria, unit -> modelMapper.map(unit, UnitDTO.Info.class));
	}

	private UnitDTO.Info save(Unit unit) {
		final Unit saved = unitDAO.saveAndFlush(unit);
		return modelMapper.map(saved, UnitDTO.Info.class);
	}
}
