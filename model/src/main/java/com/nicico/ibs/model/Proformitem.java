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
@Table(schema = "FB", name = "TBL_PROFORMITEM")
public class Proformitem extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inquiry_seq")
	@SequenceGenerator(name = "inquiry_seq", sequenceName = "FB.SEQ_INQUIRY", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(insertable = false,updatable = false,name = "REQUESTITEMID")
	private RequestItem requestItem;
	@Basic
	@Column(name = "REQUESTITEMID")
	private Long requestItemId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(insertable = false,updatable = false,name = "UNITID", nullable = false)
	private Unit unit;
	@Basic
	@Column(name = "UNITID")
	private Long unitId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false,updatable = false,name = "PROFORMID", nullable = false)
	private Proform proform;
	@Basic
	@Column(name = "PROFORMID")
	private Long proformId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false,updatable = false,name = "CONSTRUCTOR_COUNTRY")
	private Country countryByConstructure;
	@Basic
	@Column(name = "CONSTRUCTOR_COUNTRY")
	private Long countryByConstructureId;

	@Basic
	@Column(name = "PRICE")
	private String price;

	@Basic
	@Column(name = "AMOUNT")
	private Float amount;

	@Basic
	@Column(name = "ATTACHMENT_FILENAME")
	private String attachmentFileName;

	@Basic
	@Column(name = "ATTACHMENT_DATE_CREATED")
	private String attachmentDateCreated;

	@Basic
	@Column(name = "ATTACHMENT_NEWNAME")
	private String attachmentNewName;

	@Basic
	@Column(name = "ATTACHMENT_FILESIZE")
	private Float attachmentFileSize;

	@Basic
	@Column(name = "ITEM_DSCL")
	private String itemDescl;

	@Basic
	@Column(name = "REQUEST_ITEM_SPEC")
	private String requestItemSpec;

	@Basic
	@Column(name = "TOTAL_PRICE")
	private String totalPrice;

	@Basic
	@Column(name = "CONSTRUCTOR_NAME")
	private String constructorName;

	@Basic
	@Column(name = "VERIFY_PRICE")
	private String verifyPrice;

	@Basic
	@Column(name = "ITEM_DESCRIPTION")
	private String itemDescription;

	@Basic
	@Column(name = "IGNOREPRICING")
	private String ignorePricing;

	@Basic
	@Column(name = "TECHNICAL_DESC")
	private String technicalDesc;

	@Basic
	@Column(name = "WARRANTY_DESC")
	private String warrantyDesc;

	@Basic
	@Column(name = "PACKING_DESC")
	private String packingDesc;

}
