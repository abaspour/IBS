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
@Table( schema = "FB", name = "TBL_INQIRY")
public class Inquiry extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inquiry_seq")
	@SequenceGenerator(name = "inquiry_seq", sequenceName = "FB.SEQ_INQUIRY", allocationSize = 1)
	private Long id;

	@Basic
	@Column(name = "ACCOUNT_ID" , nullable = false )
	private Long supplierId;

	@Basic
	@Column(name = "INQUIRYNUMBER" , nullable = false )
	private String inquiryNumber;

	@Basic
	@Column(name = "INQ_NAME"  )
	private String inquiryName;

	@Basic
	@Column(name = "INQUIRYDESC" , nullable = false )
	private String inquiryDesc;

	@Basic
	@Column(name = "INQUIRYDATE" , nullable = false )
	private String inquiryDate;

	@Basic
	@Column(name = "SENDDATE" , nullable = false )
	private String sendDate;

	@Basic
	@Column(name = "ENDREPLYDATE" , nullable = false )
	private String endReplyDate;

	@Basic
	@Column(name = "REPLY_DATE" , nullable = false )
	private String replyDate;

	@Basic
	@Column(name = "INQUIRY_TYPE" , nullable = false )
	private String inquiryType;

	@Basic
	@Column(name = "VERRIFY_STATUS" , nullable = false )
	private String verifyStatus;

	@Basic
	@Column(name = "VERRIFY" , nullable = false )
	private String verify;

	@Basic
	@Column(name = "TEST_DESC" , nullable = false )
	private String testDesc;

	@Basic
	@Column(name = "BRIEFING_PLACE" , nullable = false )
	private String briefingPlace;

	@Basic
	@Column(name = "BRIEFING_DATE" , nullable = false )
	private String briefingDate;

	@Basic
	@Column(name = "TECHNICLA_DESC" , nullable = false )
	private String technicalDesc;

	@Basic
	@Column(name = "ATTACHMENT_DATE_CREATED"  )
	private String attachmentDateCreated;

	@Basic
	@Column(name = "ATTACHMENT_FILENAME"  )
	private String attachmentFileName;

	@Basic
	@Column(name = "ATTACHMENT_FILESIZE" )
	private Long attachmentFileSize;

	@Basic
	@Column(name = "ATTACHMENT_NEWNAME"  )
	private String attachmentNewName;

	@ManyToOne
	@JoinColumn(name = "DELIVARYPLACE"  )
	private DeliveryLocation deliveryLocation;

	@Basic
	@Column(name = "PORTAGE"  )
	private String portage;

	@Basic
	@Column(name = "INSTAL_COST"  )
	private String instalCost;

	@Basic
	@Column(name = "OTHER_COST"  )
	private String otherCost;

	@Basic
	@Column(name = "CHECK_OFFER"  )
	private String checkOffer;

	@Basic
	@Column(name = "SPARE_PRODUCT_TIME_REQUIRE"  )
	private String spareProductTimeRequire;

	@Basic
	@Column(name = "SPARE_PRODUCT_TIME"  )
	private String spareProductTime;

	@Basic
	@Column(name = "BRIEFING" )
	private String briefing;

	@Basic
	@Column(name = "SUP_DESC" )
	private String supDesc;

	@Basic
	@Column(name = "CANCEL_DATE" )
	private String cancelDate;

	@Basic
	@Column(name = "CHANGE_STATUS" )
	private String changeStatus;

	@Basic
	@Column(name = "VALID_TIME" )
	private String validTime;

	@Basic
	@Column(name = "PLACE_ID" )
	private Long placeId;

/*
DELIVERY_METHOD	NUMBER(10,0)
CONTROL_BY	VARCHAR2(100 BYTE)
DELIVARYPLACE	VARCHAR2(200 BYTE)
CHANGE_STATUS	VARCHAR2(1 BYTE)
VALID_TIME	VARCHAR2(200 BYTE)
CHECK_OFFER	VARCHAR2(200 BYTE)
INQUIRY_KIND	VARCHAR2(200 BYTE)
BRIEFING	VARCHAR2(200 BYTE)
UNIT_COST_ID	NUMBER
SPARE_PRODUCT_TIME_REQUIRE	VARCHAR2(200 BYTE)
TEST_DESC	VARCHAR2(300 BYTE)
IS_INSTALL	VARCHAR2(200 BYTE)
SUP_DESC	VARCHAR2(4000 BYTE)
INQUIRY_TYPE	VARCHAR2(100 BYTE)
BRIEFING_DATE	VARCHAR2(100 BYTE)
BRIEFING_PLACE	VARCHAR2(200 BYTE)
SPARE_PRODUCT_TIME	VARCHAR2(200 BYTE)
PORTAGE	VARCHAR2(200 BYTE)
INSTAL_COST_REQUIRE	VARCHAR2(200 BYTE)
INSTAL_COST	VARCHAR2(200 BYTE)
SENDDATE	VARCHAR2(100 BYTE)
OTHER_COST_REQUIRE	VARCHAR2(100 BYTE)
OTHER_COST	VARCHAR2(200 BYTE)
VERRIFY	VARCHAR2(10 BYTE)
ATTACHMENT	BLOB
ATTACHMENT_FILENAME	VARCHAR2(200 BYTE)
ATTACHMENT_DATE_CREATED	VARCHAR2(200 BYTE)
ATTACHMENT_FILESIZE	NUMBER
ITEM_COUNT	VARCHAR2(200 BYTE)
REQNUMBER	VARCHAR2(200 BYTE)
CANCEL_ATTACHMENT	BLOB
CANCEL_ATTACHMENT_DATE_CREATED	VARCHAR2(200 BYTE)
CANCEL_ATTACHMENT_FILESIZE	NUMBER
CANCEL_ATTACHMENT_FILENAME	VARCHAR2(200 BYTE)
PLACE_ID	VARCHAR2(200 BYTE)
COPY_STATUS	VARCHAR2(20 BYTE)
ENDREPLYDATEM	VARCHAR2(20 BYTE)
ATTACHMENT_NEWNAME	VARCHAR2(150 BYTE)
CANCEL_DATE	VARCHAR2(10 CHAR)
VIEW_DATE	VARCHAR2(10 BYTE)
REPLY_DATE	VARCHAR2(20 BYTE)
DSC	CLOB
PACKNG_CONDITATION	CLOB
TECHNICLA_DESC	CLOB
PACKNG_CONDITATION1	VARCHAR2(3000 BYTE)
INQ_NAME	VARCHAR2(20 BYTE)


		@ManyToOne
		@JoinColumn(name = "USERID" , nullable = false )
		@NotFound(action = NotFoundAction.IGNORE)
		private TblUser tblUser;

		@ManyToOne
		@JoinColumn(name = "SUPPLIERID" , nullable = false )
		private TblAccount tblAccount;

		@Basic
		@Column(name = "RECIPIENT" , nullable = false )
		private String recipient;

		@Basic
		@Column(name = "DATETYPE" , nullable = false )
		private String dateType;
		@ManyToOne
		@JoinColumn(name = "DELIVERY_METHOD" , nullable = false )
		@NotFound(action = NotFoundAction.IGNORE)
		private TblPurchaseType tblPurchaseType;

		@Basic
		@Column(name = "TEST_NEEDED" , nullable = false )
		private String testNeeded;

		@Basic
		@Column(name = "CONTROL_BY" , nullable = false )
		private String controlBy;

		@Basic
		@Column(name = "DELIVERYPLACE" , nullable = false )
		private String deliveryPlace;

		@Basic
		@Column(name = "DSC" , nullable = false )
		private String dsc;

		@Basic
		@Column(name = "ITEM_DSCP" , nullable = false )
		private String itemDscp;

		@Basic
		@Column(name = "ITEM_DSCL" , nullable = false )
		private String itemDscl;

		@Basic
		@Column(name = "INQUIRY_AMOUNT" , nullable = false )
		private Double inquiryAmount;


		@Basic
		@Column(name = "VERIFIED" , nullable = false )
		private String verified;

		@Basic
		@Column(name = "EXPIRE_TIME" , nullable = false )
		private String expireTime;

		@Basic
		@Column(name = "UNIT_COST_ID" , nullable = false )
		private Long unitCostId;

		@Basic
		@Column(name = "PORTAGE" , nullable = false )
		private String portage;

		@Basic
		@Column(name = "INSTAL_COST" , nullable = false )
		private String instalCost;

		@Basic
		@Column(name = "OTHER_COST" , nullable = false )
		private String otherCost;

		@Basic
		@Column(name = "CHECK_OFFER" , nullable = false )
		private String checkOffer;

		@Basic
		@Column(name = "GURANTEE_TIME" , nullable = false )
		private String guranteeTime;

		@Basic
		@Column(name = "INQUIRY_KIND" , nullable = false )
		private String inquiryKind;

		@Basic
		@Column(name = "TECHNICLA_DESC" , nullable = false )
		private String techniclaDesc;

		@Basic
		@Column(name = "TEST" , nullable = false )
		private String test;

		@Basic
		@Column(name = "BRIEFING" , nullable = false )
		private String briefing;

		@Basic
		@Column(name = "SPARE_PRODUCT_TIME" , nullable = false )
		private String spareProductTime;


		@ManyToOne
		@JoinColumn(name = "INQUIRY_HEADER_ID" , nullable = false )
		@NotFound(action = NotFoundAction.IGNORE)
		private TblInquiryHeader tblInquiryHeader;

		@Basic
		@Column(name = "ITEM_SAMPLE" , nullable = false )
		private String itemSample;

		@Basic
		@Column(name = "FILE_NEW_NAME" , nullable = false )
		private String fileNewName;

		@Basic
		@Column(name = "FILE_OLD_NAME" , nullable = false )
		private String fileOldName;

		@Basic
		@Column(name = "COST_MODE_REQUIRE" , nullable = false )
		private String costModeRequire;

		@Basic
		@Column(name = "COST_MODE" , nullable = false )
		private String costMode;

		@Basic
		@Column(name = "COST_MODEL_REQUIRE" , nullable = false )
		private String costModelRequire;


		@Basic
		@Column(name = "COST_MODEL" , nullable = false )
		private String costModel;

		@Basic
		@Column(name = "GURANTEE_TIME_REQUIRE" , nullable = false )
		private String guranteeTimeRequire;

		@Basic
		@Column(name = "PART_MATERIAL" , nullable = false )
		private String partMaterial;

		@Basic
		@Column(name = "PART_MATERIAL_REQUIRE" , nullable = false )
		private String partMaterialRequire;

		@Basic
		@Column(name = "UNIT_WEIGHT" , nullable = false )
		private String unitWeight;

		@Basic
		@Column(name = "UNIT_WEIGHT_REQUIRE" , nullable = false )
		private String unitWeightRequire;

		@Basic
		@Column(name = "MODE_MATERIAL" , nullable = false )
		private String modeMaterial;

		@Basic
		@Column(name = "MODE_MATERIAL_REQUIRE" , nullable = false )
		private String modeMaterialRequire;

		@Basic
		@Column(name = "MODEL_MATERIAL" , nullable = false )
		private String modelMaterial;

		@Basic
		@Column(name = "MODEL_MATERIAL_REQUIRE" , nullable = false )
		private String modelMaterialRequire;

		@Basic
		@Column(name = "CHECK_ATTACHMENT" , nullable = false )
		private String checkAttachment;

		@Basic
		@Column(name = "ATTACH_TEXT" , nullable = false )
		private String attachText;

		@Basic
		@Column(name = "HAS_SAMPLE" , nullable = false )
		private String hasSample;

		@Basic
		@Column(name = "NEEDED_TIME" , nullable = false )
		private String neededTime;

		@Basic
		@Column(name = "DELIVERYBASE" , nullable = false )
		private String deliveryBase;

		@Basic
		@Column(name = "DELIVERY_DATE" , nullable = false )
		private String deliveryDate;

		@Basic
		@Column(name = "VERIFY_PRICE" , nullable = false )
		private String verifyPrice;

		@Basic
		@Column(name = "PACKNG_CONDITATION_REQUIRE" , nullable = false )
		private String packingConditationRequire;

		@Basic
		@Column(name = "PACKNG_CONDITATION")
		private String packngConditation;
*/


	}
