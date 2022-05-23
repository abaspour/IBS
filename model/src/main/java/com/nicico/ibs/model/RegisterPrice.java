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
@Table( schema = "FB", name = "TBL_REGISTERPRICE")
public class RegisterPrice extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inquiry_seq")
	@SequenceGenerator(name = "inquiry_seq", sequenceName = "FB.SEQ_INQUIRY", allocationSize = 1)
	private Long id;

	@Basic
	@Column(name = "ACCOUNT_ID", nullable = false)
	private Long supplierId;

	@Basic
	@Column(name = "INQUIRY_NUMBER", nullable = false)
	private String inquiryNumber;

	@Basic
	@Column(name = "REQ_ITEM_ROW", nullable = false)
	private String itemRow;

	@Basic
	@Column(name = "REQUEST_ITEM_QTY", nullable = false)
	private String itemQty;

	@Basic
	@Column(name = "QUANTITY")
	private String quantity;

	@Basic
	@Column(name = "UNITPRICE")
	private String unitPrice;

	@Basic
	@Column(name = "GHEYMATE_KOL")
	private String gheymateKol;

	@Basic
	@Column(name = "ITEM_DSCP", nullable = false)
	private String itemDescp;

	@Basic
	@Column(name = "ITEM_DSCL", nullable = false)
	private String itemDescl;

	@Basic
	@Column(name = "DESCRIPTION", nullable = false)
	private String description;

	@Basic
	@Column(name = "SHOMARE_NAGHSHE")
	private String shomareNaghshe;

	@Basic
	@Column(name = "TECHNICAL_DESC")
	private String technicalDesc;

	@Basic
	@Column(name = "WARRANTY_DESC")
	private String warrantyDesc;

	@Basic
	@Column(name = "PACKING_DESC")
	private String packingDesc;

	@Basic
	@Column(name = "BRAND")
	private String brand;

	@Basic
	@Column(name = "MODEL")
	private String model;

	@Basic
	@Column(name = "MANUFACTURERCOUNTRYID")
	private String manufacturerCountryId;

	@Basic
	@Column(name = "COST_MODE")
	private String costMode;

	@Basic
	@Column(name = "MODE_MATERIAL")
	private String modeMaterial;

	@Basic
	@Column(name = "GHEYMATE_GHALEB")
	private String gheymateGhaleb;

	@Basic
	@Column(name = "COST_MODEL")
	private String costModel;

	@Basic
	@Column(name = "MODEL_MATERIAL")
	private String modelMaterial;

	@Basic
	@Column(name = "GHEYMATE_MODEL")
	private String gheymateModel;

	@Basic
	@Column(name = "UNIT_WEIGHT")
	private String unitWeight;

	@Basic
	@Column(name = "PART_MATERIAL")
	private String partMaterial;

	@Basic
	@Column(name = "MODATE_SAKHTE_SAMPLE")
	private String modateSakhtSample;

	@Basic
	@Column(name = "MODATE_SAKHTE_ANBOH")
	private String modatSakhtAnboh;

	@Basic
	@Column(name = "HAS_SAMPLE")
	private String hasSample;

	@Basic
	@Column(name = "TAHVIL_BAR_ASASE")
	private String tahvilBarAsase;

	@Basic
	@Column(name = "GURANTEE_TIME")
	private String guranteeTime;

	@Basic
	@Column(name = "DELIVERYT_DATE")
	private String deliveryDate;

	@Basic
	@Column(name = "PACKAGING_CONDITION")
	private String packageCondition;

	@Basic
	@Column(name = "PRICEBEFOREDISCOUNT")
	private String priceBeforeDiscount;

	@Basic
	@Column(name = "DISCOUNT")
	private String discount;

	@Basic
	@Column(name = "VERIFY_PRICE")
	private String verifyPrice;

	@Basic
	@Column(name = "SUPPLIER_DSC")
	private String supplierDesc;

	@Basic
	@Column(name = "ATTACH_FILENAME")
	private String attachFileName;

	@Basic
	@Column(name = "ATTACH_NEWNAME")
	private String attachNewFileName;

	@Basic
	@Column(name = "ATTACH_DATE_CREATED")
	private String attachDateCreated;

	@Basic
	@Column(name = "ATTACH_FILESIZE")
	private Long attachFileSize;

	@Basic
	@Column(name = "RECIEVE_SAMPLE")
	private String receiveSample;

	@Basic
	@Column(name = "EXIST_FOR_TEST")
	private String existForTest;

	@Basic
	@Column(name = "RECIEVE_ATTACH")
	private String receiveAttach;

	@Basic
	@Column(name = "GURANTEE_TIME_REQUIRE")
	private String guranteeTimeRequire;

	@Basic
	@Column(name = "PART_MATERIAL_REQUIRE")
	private String partMaterialRequired;

	@Basic
	@Column(name = "MODE_MATERIAL_REQUIRE")
	private String modeMaterialRequire;

	@Basic
	@Column(name = "MODEL_MATERIAL_REQUIRE")
	private String modelMaterialRequire;

	@Basic
	@Column(name = "CHANGE_STATUS")
	private String changeStatus;

	@Basic
	@Column(name = "OFFER_DATE_PRICE")
	private String offerDatePrice;

	@Basic
	@Column(name = "PACKAGING_CONDITION_REQUIRE")
	private String packagingConditionRequire;

	@Basic
	@Column(name = "REFUSE_ITEM_PRICE")
	private String refuseItemPrice;

	@Basic
	@Column(name = "SALEUNITID")
	private Long saleUnitId;

	@Basic
	@Column(name = "REQUESTITEMID", nullable = false)
	private Long requestItemId;

	@Basic
	@Column(name = "IGNOREPRICING")
	private String ignorePricing;
	@Basic
	@Column(name = "TEST_NEEDED")
	private String testNeeded;

	@Basic
	@Column(name = "ITEM_SAMPLE")
	private String itemSample;

}