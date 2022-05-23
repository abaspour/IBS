package com.nicico.ibs.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(schema = "FB", name = "TBL_PROFORM")
public class Proform extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proform_seq")
	@SequenceGenerator(name = "proform_seq", sequenceName = "FB.SEQ_INQUIRY", allocationSize = 1)
	private Long id;

	@Basic
	@Column(name = "SUPPLIERID")
	private Long supplierId;

	@Basic
	@Column(name = "REQUESTID")
	private Long requestId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false,updatable = false,name = "TRANSMITPATHID")
	private Freight freight;
	@Basic
	@Column(name = "TRANSMITPATHID")
	private Long freightId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false,updatable = false,name = "MONETTYPEID", nullable = false)
	private Currency currency;
	@Basic
	@Column(name = "MONETTYPEID")
	private Long currencyId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false,updatable = false,name = "PAYMENTTYPEID")
	private PaymentMethod paymentMethod;
	@Basic
	@Column(name = "PAYMENTTYPEID")
	private Long paymentMethodId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false,updatable = false,name = "PURCHASETYPEID")
	private PurchaseType purchaseType;
	@Basic
	@Column(name = "PURCHASETYPEID")
	private Long purchaseTypeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false,updatable = false,name = "TRANSMITINSTRUMENTID")
	private TransmitInstrument transmitInstrument;
	@Basic
	@Column(name = "TRANSMITINSTRUMENTID")
	private Long transmitInstrumentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false,updatable = false,name = "RAPPINGTYPEID")
	private RappingType rappingType;
	@Basic
	@Column(name = "RAPPINGTYPEID")
	private Long rappingTypeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false,updatable = false,name = "CONSTRUCTORCOUNTRYID")
	private Country country;
	@Basic
	@Column(name = "CONSTRUCTORCOUNTRYID")
	private Long countryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false,updatable = false,name = "COUNTRY_LOADING")
	private Country countryLoading;
	@Basic
	@Column(name = "COUNTRY_LOADING")
	private Long countryLoadingId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false,updatable = false,name = "BENEFICIARY_COUNTRY_ID")
	private Country beneficiaryCountry;
	@Basic
	@Column(name = "BENEFICIARY_COUNTRY_ID")
	private Long beneficiaryCountryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false,updatable = false,name = "BENEFICIARY_DEL_COUNTRY_ID")
	private Country beneficiaryDeliveryCountry;
	@Basic
	@Column(name = "BENEFICIARY_DEL_COUNTRY_ID")
	private Long beneficiaryDeliveryCountryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false,updatable = false,name = "BANKID")
	private Bank bank;
	@Basic
	@Column(name = "BANKID")
	private Long bankId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false,updatable = false,name = "CUSTOMID")
	private Custom custom;
	@Basic
	@Column(name = "CUSTOMID")
	private Long customId;

	@Basic
	@Column(name = "USERID")
	private Long userId;

	@Basic
	@Column(name = "INQUIRY_NUMBER")
	private String inquiryNumber;
	@Basic
	@Column(name = "PROFORMDSCEN")
	private String proformdscen;
	@Basic
	@Column(name = "PROFORMNUMBER")
	private String proformnumber;
	@Basic
	@Column(name = "PRFORMDATE")
	private String prformdate;
	@Basic
	@Column(name = "PROFORMEXPIREDATE")
	private String proformexpiredate;
	@Basic
	@Column(name = "TOTALSUMITEMSCOST")
	private Double totalsumitemscost;
	@Basic
	@Column(name = "DISCOUNTPROFORM")
	private Double discountproform;
	@Basic
	@Column(name = "RAPPINGCOST")
	private Double rappingcost;
	@Basic
	@Column(name = "USANCECOST")
	private Double usancecost;
	@Basic
	@Column(name = "OTHERCOST")
	private Double othercost;
	@Basic
	@Column(name = "PUREWEIGHT")
	private String pureweight;
	@Basic
	@Column(name = "IMPUREWEIGHT")
	private String impureweight;
	@Basic
	@Column(name = "MASS")
	private String mass;
	@Basic
	@Column(name = "DELIVERTTIME")
	private String deliverttime;
	@Basic
	@Column(name = "CONSTRUCTORNAME")
	private String constructorname;
	@Basic
	@Column(name = "ASSIGNDATE")
	private String assigndate;
	@Basic
	@Column(name = "FCA")
	private String fca;
	@Basic
	@Column(name = "TOTALPRICE")
	private String totalprice;
	@Basic
	@Column(name = "DELIVERY_COUNT")
	private String deliveryCount;
	@Basic
	@Column(name = "CONVERT_RATE_RIAL")
	private String convertRateRial;
	@Basic
	@Column(name = "CONVERT_RATE_DOLLAR")
	private String convertRateDollar;
	@Basic
	@Column(name = "CONVERT_RATE_EURO")
	private String convertRateEuro;
	@Basic
	@Column(name = "REPLY_DATE")
	private String replyDate;

}
