package com.nicico.ibs;

import com.nicico.copper.common.IErrorCode;
import com.nicico.copper.common.NICICOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class IBSException extends NICICOException {

	@Getter
	@RequiredArgsConstructor
	public enum ErrorType implements IErrorCode {
		CountryNotFound(404),
		InquiryNotFound(404),
		RegisterNotFound(404),
		RegisterPriceHeaderNotFound(404),
		UnitNotFound(404),
		CurrencyNotFound(404),
		CustomNotFound(404),
		PaymentMethodNotFound(404),
		PurchaseTypeNotFound(404),
		DeliveryTypeNotFound(404),
		ProformNotFound(404),
		ProformitemNotFound(404),
		TransmitInstrumentNotFound(404),
		ContractNotFound(404),
		MessageNotFound(404),
		InquiryPortageNotFound(404),
		RegisterPriceNotFound(404);

		private final Integer httpStatusCode;

		@Override
		public String getName() {
			return name();
		}
	}

	// ------------------------------

	public IBSException(IErrorCode errorCode) {
		super(errorCode);
	}

	public IBSException(ErrorType errorCode) {
		this(errorCode, null);
	}

	public IBSException(ErrorType errorCode, String field) {
		super(errorCode, field);
	}
}
