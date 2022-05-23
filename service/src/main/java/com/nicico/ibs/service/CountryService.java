package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.CountryDTO;
import com.nicico.ibs.iservice.ICountryService;
import com.nicico.ibs.model.Country;
import com.nicico.ibs.repository.CountryDAO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CountryService implements ICountryService {

	private final CountryDAO countryDAO;
	private final ModelMapper modelMapper;

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public CountryDTO.Info get(Long id) {
		final Country country = countryDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.CountryNotFound));

		return modelMapper.map(country, CountryDTO.Info.class);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public List<CountryDTO.Info> list() {
		final List<Country> countries = countryDAO.findAll();

		return modelMapper.map(countries, new TypeToken<List<CountryDTO.Info>>() {
		}.getType());
	}

	//	@PreAuthorize("hasAuthority('C_COUNTRY')")
	@Transactional
	@Override
	public CountryDTO.Info create(CountryDTO.Create request) {
		final Country country = modelMapper.map(request, Country.class);

		return save(country);
	}

	//	@PreAuthorize("hasAuthority('U_COUNTRY')")
	@Transactional
	@Override
	public CountryDTO.Info update(Long id, CountryDTO.Update request) {
		final Country country = countryDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.CountryNotFound));

		final Country updating = new Country();
		modelMapper.map(country, updating);
		modelMapper.map(request, updating);

		return save(updating);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(Long id) {
		countryDAO.deleteById(id);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(CountryDTO.Delete request) {
		final List<Country> countries = countryDAO.findAllById(request.getIds());

		countryDAO.deleteAll(countries);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public SearchDTO.SearchRs<CountryDTO.Info> search(SearchDTO.SearchRq request) {
		return SearchUtil.search(countryDAO, request, country -> modelMapper.map(country, CountryDTO.Info.class));
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public TotalResponse<CountryDTO.Info> search(NICICOCriteria criteria) {
		return SearchUtil.search(countryDAO, criteria, country -> modelMapper.map(country, CountryDTO.Info.class));
	}

	private CountryDTO.Info save(Country country) {
		final Country saved = countryDAO.saveAndFlush(country);
		return modelMapper.map(saved, CountryDTO.Info.class);
	}
}
