package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.CustomDTO;
import com.nicico.ibs.iservice.ICustomService;
import com.nicico.ibs.model.Custom;
import com.nicico.ibs.repository.CustomDAO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomService implements ICustomService {

	private final CustomDAO customDAO;
	private final ModelMapper modelMapper;

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public CustomDTO.Info get(Long id) {
		final Custom custom = customDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.CustomNotFound));

		return modelMapper.map(custom, CustomDTO.Info.class);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public List<CustomDTO.Info> list() {
		final List<Custom> countries = customDAO.findAll();

		return modelMapper.map(countries, new TypeToken<List<CustomDTO.Info>>() {
		}.getType());
	}

	//	@PreAuthorize("hasAuthority('C_COUNTRY')")
	@Transactional
	@Override
	public CustomDTO.Info create(CustomDTO.Create request) {
		final Custom custom = modelMapper.map(request, Custom.class);

		return save(custom);
	}

	//	@PreAuthorize("hasAuthority('U_COUNTRY')")
	@Transactional
	@Override
	public CustomDTO.Info update(Long id, CustomDTO.Update request) {
		final Custom custom = customDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.CustomNotFound));

		final Custom updating = new Custom();
		modelMapper.map(custom, updating);
		modelMapper.map(request, updating);

		return save(updating);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(Long id) {
		customDAO.deleteById(id);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(CustomDTO.Delete request) {
		final List<Custom> countries = customDAO.findAllById(request.getIds());

		customDAO.deleteAll(countries);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public SearchDTO.SearchRs<CustomDTO.Info> search(SearchDTO.SearchRq request) {
		return SearchUtil.search(customDAO, request, custom -> modelMapper.map(custom, CustomDTO.Info.class));
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public TotalResponse<CustomDTO.Info> search(NICICOCriteria criteria) {
		return SearchUtil.search(customDAO, criteria, custom -> modelMapper.map(custom, CustomDTO.Info.class));
	}

	private CustomDTO.Info save(Custom custom) {
		final Custom saved = customDAO.saveAndFlush(custom);
		return modelMapper.map(saved, CustomDTO.Info.class);
	}
}
