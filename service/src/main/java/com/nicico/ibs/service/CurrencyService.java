package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.CurrencyDTO;
import com.nicico.ibs.iservice.ICurrencyService;
import com.nicico.ibs.model.Currency;
import com.nicico.ibs.repository.CurrencyDAO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CurrencyService implements ICurrencyService {

	private final CurrencyDAO currencyDAO;
	private final ModelMapper modelMapper;

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public CurrencyDTO.Info get(Long id) {
		final Currency currency = currencyDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.CurrencyNotFound));

		return modelMapper.map(currency, CurrencyDTO.Info.class);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public List<CurrencyDTO.Info> list() {
		final List<Currency> countries = currencyDAO.findAll();

		return modelMapper.map(countries, new TypeToken<List<CurrencyDTO.Info>>() {
		}.getType());
	}

	//	@PreAuthorize("hasAuthority('C_COUNTRY')")
	@Transactional
	@Override
	public CurrencyDTO.Info create(CurrencyDTO.Create request) {
		final Currency currency = modelMapper.map(request, Currency.class);

		return save(currency);
	}

	//	@PreAuthorize("hasAuthority('U_COUNTRY')")
	@Transactional
	@Override
	public CurrencyDTO.Info update(Long id, CurrencyDTO.Update request) {
		final Currency currency = currencyDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.CurrencyNotFound));

		final Currency updating = new Currency();
		modelMapper.map(currency, updating);
		modelMapper.map(request, updating);

		return save(updating);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(Long id) {
		currencyDAO.deleteById(id);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(CurrencyDTO.Delete request) {
		final List<Currency> countries = currencyDAO.findAllById(request.getIds());

		currencyDAO.deleteAll(countries);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public SearchDTO.SearchRs<CurrencyDTO.Info> search(SearchDTO.SearchRq request) {
		return SearchUtil.search(currencyDAO, request, currency -> modelMapper.map(currency, CurrencyDTO.Info.class));
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public TotalResponse<CurrencyDTO.Info> search(NICICOCriteria criteria) {
		return SearchUtil.search(currencyDAO, criteria, currency -> modelMapper.map(currency, CurrencyDTO.Info.class));
	}

	private CurrencyDTO.Info save(Currency currency) {
		final Currency saved = currencyDAO.saveAndFlush(currency);
		return modelMapper.map(saved, CurrencyDTO.Info.class);
	}
}
