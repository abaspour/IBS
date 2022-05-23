package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.PurchaseTypeDTO;
import com.nicico.ibs.iservice.IPurchaseTypeService;
import com.nicico.ibs.model.PurchaseType;
import com.nicico.ibs.repository.PurchaseTypeDAO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PurchaseTypeService implements IPurchaseTypeService {

	private final PurchaseTypeDAO purchaseTypeDAO;
	private final ModelMapper modelMapper;

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public PurchaseTypeDTO.Info get(Long id) {
		final PurchaseType purchaseType = purchaseTypeDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.PurchaseTypeNotFound));

		return modelMapper.map(purchaseType, PurchaseTypeDTO.Info.class);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public List<PurchaseTypeDTO.Info> list() {
		final List<PurchaseType> countries = purchaseTypeDAO.findAll();

		return modelMapper.map(countries, new TypeToken<List<PurchaseTypeDTO.Info>>() {
		}.getType());
	}

	//	@PreAuthorize("hasAuthority('C_COUNTRY')")
	@Transactional
	@Override
	public PurchaseTypeDTO.Info create(PurchaseTypeDTO.Create request) {
		final PurchaseType purchaseType = modelMapper.map(request, PurchaseType.class);

		return save(purchaseType);
	}

	//	@PreAuthorize("hasAuthority('U_COUNTRY')")
	@Transactional
	@Override
	public PurchaseTypeDTO.Info update(Long id, PurchaseTypeDTO.Update request) {
		final PurchaseType purchaseType = purchaseTypeDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.PurchaseTypeNotFound));

		final PurchaseType updating = new PurchaseType();
		modelMapper.map(purchaseType, updating);
		modelMapper.map(request, updating);

		return save(updating);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(Long id) {
		purchaseTypeDAO.deleteById(id);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(PurchaseTypeDTO.Delete request) {
		final List<PurchaseType> countries = purchaseTypeDAO.findAllById(request.getIds());

		purchaseTypeDAO.deleteAll(countries);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public SearchDTO.SearchRs<PurchaseTypeDTO.Info> search(SearchDTO.SearchRq request) {
		return SearchUtil.search(purchaseTypeDAO, request, purchaseType -> modelMapper.map(purchaseType, PurchaseTypeDTO.Info.class));
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public TotalResponse<PurchaseTypeDTO.Info> search(NICICOCriteria criteria) {
		return SearchUtil.search(purchaseTypeDAO, criteria, purchaseType -> modelMapper.map(purchaseType, PurchaseTypeDTO.Info.class));
	}

	private PurchaseTypeDTO.Info save(PurchaseType purchaseType) {
		final PurchaseType saved = purchaseTypeDAO.saveAndFlush(purchaseType);
		return modelMapper.map(saved, PurchaseTypeDTO.Info.class);
	}
}
