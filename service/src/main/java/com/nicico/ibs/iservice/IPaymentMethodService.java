package com.nicico.ibs.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.dto.PaymentMethodDTO;

import java.util.List;

public interface IPaymentMethodService {

	PaymentMethodDTO.Info get(Long id);

	List<PaymentMethodDTO.Info> list();

	PaymentMethodDTO.Info create(PaymentMethodDTO.Create request);

	PaymentMethodDTO.Info update(Long id, PaymentMethodDTO.Update request);

	void delete(Long id);

	void delete(PaymentMethodDTO.Delete request);

	SearchDTO.SearchRs<PaymentMethodDTO.Info> search(SearchDTO.SearchRq request);

	TotalResponse<PaymentMethodDTO.Info> search(NICICOCriteria criteria);
}
