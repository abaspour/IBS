package com.nicico.ibs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProformDTO {

	@NotEmpty
	@ApiModelProperty(required = true)
	private Long id;
	private Long supplierId;
	private Long requestId;
	private Long userId;
	private String inquiryNumber;
	private String proformdscen;
	private String proformnumber;
	private String prformdate;
	private String proformexpiredate;
	private Double totalsumitemscost;
	private Double discountproform;
	private Double rappingcost;
	private Double usancecost;
	private Double othercost;
	private String pureweight;
	private String impureweight;
	private String mass;
	private String deliverttime;
	private String constructorname;
	private String assigndate;
	private String fca;
	private String totalprice;
	private String deliveryCount;
	private String convertRateRial;
	private String convertRateDollar;
	private String convertRateEuro;
	private String replyDate;

	private Long freightId;
	private Long currencyId;
	private Long paymentMethodId;
	private Long purchaseTypeId;
	private Long transmitInstrumentId;
	private Long rappingTypeId;
	private Long countryId;
	private Long countryLoadingId;
	private Long beneficiaryCountryId;
	private Long beneficiaryDeliveryCountryId;
	private Long bankId;
	private Long customId;


	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("ProformInfo")
	public static class Info extends ProformDTO {
		private Long id;
		private CurrencyDTO.Info currency;
		private PaymentMethodDTO.Info paymentMethod;
		private PurchaseTypeDTO.Info purchaseType;
		private TransmitInstrumentDTO.Info transmitInstrument;
		private RappingTypeDTO.Info rappingType;
		private CountryDTO.Info country;
		private CountryDTO.Info countryLoading;
		private CountryDTO.Info beneficiaryCountry;
		private CountryDTO.Info beneficiaryDeliveryCountry;
		private FreightDTO.Info freight;
		private BankDTO.Info bank;
		private CustomDTO.Info custom;
		private Date createdDate;
		private String createdBy;
		private Date lastModifiedDate;
		private String lastModifiedBy;
		private Integer version;
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("ProformCreateRq")
	public static class Create extends ProformDTO {
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("ProformUpdateRq")
	public static class Update extends ProformDTO {
		@NotNull
		@ApiModelProperty(required = true)
		private Integer version;
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("ProformDeleteRq")
	public static class Delete {
		@NotNull
		@ApiModelProperty(required = true)
		private List<Long> ids;
	}
}
