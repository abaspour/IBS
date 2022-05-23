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
@Table( schema = "FB", name = "TBL_REGISTER_PRICE_HEADER")
public class RegisterPriceHeader extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inquiry_seq")
	@SequenceGenerator(name = "inquiry_seq", sequenceName = "FB.SEQ_INQUIRY", allocationSize = 1)
	private Long id;

	@Basic
	@Column(name = "ACCOUNT_ID", nullable = false)
	private String supplierId;
	@Basic
	@Column(name = "INQUIRY_NUMBER", nullable = false)
	private String inquiryNumber;
	@Basic
	@Column(name = "PORTAGE_COST")
	private String portageCost;
	@Basic
	@Column(name = "SUB_SPARE_PRODUCT_COST")
	private String subSpareProductCost;
	@Basic
	@Column(name = "OTHER_COST")
	private String otherCost;
	@Basic
	@Column(name = "DISCOUNT")
	private String discount;
	@Basic
	@Column(name = "SUM_PRICE")
	private String sumPrice;
	@Basic
	@Column(name = "TAX_COST")
	private String taxCost;
	@Basic
	@Column(name = "CHECKINFO")
	private String checkInfo;
	@Basic
	@Column(name = "SUB_INSTAL_COST")
	private String subInstallCost;
	@Basic
	@Column(name = "PORTAGE")
	private String portage;
	@Basic
	@Column(name = "INSTAL_COST_REQUIRE")
	private String installCostRequire;
	@Basic
	@Column(name = "OTHER_COST_REQUIRE")
	private String otherCostRequire;
	@Basic
	@Column(name = "SPAREPRODUCT_DATE_CREATED")
	private String spareProductFileDateCreated;
	@Basic
	@Column(name = "SPAREPRODUCT_FILESIZE")
	private String spareProductFileSize;
	@Basic
	@Column(name = "SPAREPRODUCT_FILENAME")
	private String spareProductFileName;
	@Basic
	@Column(name = "TAX_COST_VALUE")
	private Long taxCostValue;
	@Basic
	@Column(name = "SUPPLIER_RESPONSE_TIME")
	private String supplierResponseTime;
	@Basic
	@Column(name = "SUPPLIER_RESPONSE_DATE")
	private String supplierResponseDate;
	@Basic
	@Column(name = "SPAREPRODUCT_NEWNAME")
	private String spareProductFileNewName;
	@Basic
	@Column(name = "REPLYDATE")
	private String replyDate;
	@Basic
	@Column(name = "SUMPRINCIPAL")
	private Long sumPrincipal;
	@Basic
	@Column(name = "CHANGE_STATUS")
	private String changeStatus;
	@Basic
	@Column(name = "PACKING_COST")
	private String packingCost;
}