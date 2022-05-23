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
@Table(schema = "CRM", name = "TBL_ACCOUNT")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
	@SequenceGenerator(name = "account_seq", sequenceName = "CRM.SEQ_ACCOUNT_ID", allocationSize = 1)
	private Long id;

	@Basic
	@Column(name = "ACCOUNT_NAME")
	private String accountName;

	@Column(name = "PRE_PHONE")
	private String          prePhone;

	@Column(name = "ACTIVE_DATE")
	private String          activeDate;

	@Column(name = "ACCOUNT_LOGO")
	private String     accountLogo     ;

	@Column(name = "ADDRESS")
	private String			address;

	@Column(name = "ECONOMICAL_CODE")
	private String			economicalCode;

	@Column(name = "ORG_NATIONAL_CODE")
	private String			orgNationalCode;

	@Column(name = "COUNTRY_ID", nullable = true)
	private Long countryId;

//		@ManyToOne
//		@JoinColumn(name = "RELATION_TYPE_ID", nullable = true)
//		private TblCategory tblCategory;
//
//		@ManyToOne
//		@JoinColumn(name = "INDUSTRY_ID")
//		private TblIndustry tblIndustry;
//
//		@ManyToOne
//		@JoinColumn(name = "COMPANY_ID")
//		private TblCompany		tblCompany;
//
//		@ManyToOne
//		@JoinColumn(name = "STATE_ID")
//		private TblState	tblState;
//
//		@ManyToOne
//		@JoinColumn(name = "CURRENCY_ID", nullable = true)
//		private TblParameter	tblParameterByCurrencyId;
//
//		@ManyToOne
//		@JoinColumn(name = "ADDRESS_TYPE", nullable = true)
//		private TblParameter	tblParameterByAddressType;
//
//		@ManyToOne
//		@JoinColumn(name = "USER_CREATOR_ID")
//		private TblUser	tblPersonByUserCreatorId;
//
//		@ManyToOne
//		@JoinColumn(name = "PRIMARY_CONTACT_ID")
//		private TblContact	tblContactByPrimaryContactId;
//
//		@ManyToOne
//		@JoinColumn(name = "USER_LAST_EDITOR_ID")
//		private TblUser	tblPersonByUserLastEditorId;
//
//		@ManyToOne
//		@JoinColumn(name = "CITY_ID", nullable = true)
//		private TblCity tblCity;
//
//		@ManyToOne
//		@JoinColumn(name = "LANGUAGE_ID")
//		private TblLanguage tblLanguage;
//
//		@ManyToOne
//		@JoinColumn(name = "USER_FOLLOWER_ID", nullable = true)
//		private TblContact tblContactByUserFollowerId;

	@Column(name = "IBAN")
	private String			iban;

	@Column(name = "BRAND")
	private String			brand;

	@Column(name = "ACCESS_TYPE")
	private String			accessType;

//		@ManyToOne
//		@JoinColumn(name = "PARENT_ACCOUNT_ID")
//		private TblAccount tblAccount;

	@Basic
	@Column(name = "ACCOUNT_NUMBER", length = 60)
	private String accountNumber;

	@Basic
	@Column(name = "AVL_STATUS", length = 60)
	private String avlStatus;

	@Basic
	@Column(name = "OWNERSHIP", length = 10)
	private String ownership;

	@Basic
	@Column(name = "ACCOUNT_TYPE", length = 10)
	private String accountType;

	@Basic
	@Column(name = "MAIN_PHONE", length = 10)
	private String mainPhone;

	@Basic
	@Column(name = "OTHER_PHONE", length = 20)
	private String otherPhone;

	@Basic
	@Column(name = "FAX_NUMBER", precision = 10)
	private String faxNumber;

	@Basic
	@Column(name = "WEB_SITE", length = 100)
	private String webSite;

	@Basic
	@Column(name = "EMAIL", length = 100)
	private String email;

	@Basic
	@Column(name = "EMAIL1", length = 100)
	private String email1;

	@Basic
	@Column(name = "TICKER_SYMBOL", length = 10)
	private String tickerSymbol;

	@Basic
	@Column(name = "EMPLOYEES", length = 10)
	private String employees;

	@Basic
	@Column(name = "COMMERCE_BLACKDATESTART", length = 10)
	private String commerceBlackDateStart;

	@Basic
	@Column(name = "COMMERCE_BLACKDATEEND", length = 10)
	private String commerceBlackDateEndt;

	@Basic
	@Column(name = "BRANCH_ID")
	private String receipted;

	@Basic
	@Column(name = "ACCOUNTING_NAME" ,length =60)
	private String accountingName;

	@Column(name = "TAFZILI_CODE" )
	private String 	tafziliCode;

	@Basic
	@Column(name = "COMMERCE_BLACKLIST" ,length =60)
	private String commerceBlackList;

	@Column(name = "POST_CODE")
	private String  postCode;

	@Column(name = "REGISTER_NUMBER")
	private String 	registerNumber;

	@Column(name = "TSRID")
	private Long tsrId;

	@Column(name = "TSAID")
	private Long tsaId;

	@Column(name = "STOCK_TYPE")
	private String stockType;

	@Column(name = "CREATE_DATE")
	private String	createDate;

	@Column(name = "INQ_AGENT_RECIVED")
	private String inqAgentRecived;

	@Column(name = "INQ_AGENT_MOBILE")
	private String inqAgentMobile;

	@Column(name = "MANAGER_MOBILE_PHONE")
	private String managerMobilePhone;

	@Column(name = "CREDIT_DATE")
	private String creditDate;

	@Column(name = "BANK_NAME")
	private String bankName;

}
