package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.PaymentMethodDTO;
import com.nicico.ibs.iservice.IPaymentMethodService;
import com.nicico.ibs.model.PaymentMethod;
import com.nicico.ibs.repository.PaymentMethodDAO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentMethodService implements IPaymentMethodService {

	private final PaymentMethodDAO paymentMethodDAO;
	private final ModelMapper modelMapper;

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public PaymentMethodDTO.Info get(Long id) {
		final PaymentMethod paymentMethod = paymentMethodDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.PaymentMethodNotFound));

		return modelMapper.map(paymentMethod, PaymentMethodDTO.Info.class);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public List<PaymentMethodDTO.Info> list() {
		final List<PaymentMethod> countries = paymentMethodDAO.findAll();

		return modelMapper.map(countries, new TypeToken<List<PaymentMethodDTO.Info>>() {
		}.getType());
	}

	//	@PreAuthorize("hasAuthority('C_COUNTRY')")
	@Transactional
	@Override
	public PaymentMethodDTO.Info create(PaymentMethodDTO.Create request) {
		final PaymentMethod paymentMethod = modelMapper.map(request, PaymentMethod.class);

		return save(paymentMethod);
	}

	//	@PreAuthorize("hasAuthority('U_COUNTRY')")
	@Transactional
	@Override
	public PaymentMethodDTO.Info update(Long id, PaymentMethodDTO.Update request) {
		final PaymentMethod paymentMethod = paymentMethodDAO.findById(id)
				.orElseThrow(() -> new IBSException(IBSException.ErrorType.PaymentMethodNotFound));

		final PaymentMethod updating = new PaymentMethod();
		modelMapper.map(paymentMethod, updating);
		modelMapper.map(request, updating);

		return save(updating);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(Long id) {
		paymentMethodDAO.deleteById(id);
	}

	//	@PreAuthorize("hasAuthority('D_COUNTRY')")
	@Transactional
	@Override
	public void delete(PaymentMethodDTO.Delete request) {
		final List<PaymentMethod> countries = paymentMethodDAO.findAllById(request.getIds());

		paymentMethodDAO.deleteAll(countries);
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public SearchDTO.SearchRs<PaymentMethodDTO.Info> search(SearchDTO.SearchRq request) {
		return SearchUtil.search(paymentMethodDAO, request, paymentMethod -> modelMapper.map(paymentMethod, PaymentMethodDTO.Info.class));
	}

	//	@PreAuthorize("hasAuthority('R_COUNTRY')")
	@Transactional(readOnly = true)
	@Override
	public TotalResponse<PaymentMethodDTO.Info> search(NICICOCriteria criteria) {
		return SearchUtil.search(paymentMethodDAO, criteria, paymentMethod -> modelMapper.map(paymentMethod, PaymentMethodDTO.Info.class));
	}

	private PaymentMethodDTO.Info save(PaymentMethod paymentMethod) {
		final PaymentMethod saved = paymentMethodDAO.saveAndFlush(paymentMethod);
		return modelMapper.map(saved, PaymentMethodDTO.Info.class);
	}
}
