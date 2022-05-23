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
@Table(schema = "FB", name = "TBL_INS_INQUIRY")
public class InquiryPortage extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proform_seq")
	@SequenceGenerator(name = "proform_seq", sequenceName = "FB.SEQ_INQUIRY", allocationSize = 1)
	private Long id;
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false, updatable = false, name = "CURRENCY_ID", nullable = false)
	private Currency currency;
	@Basic
	@Column(name = "CURRENCY_ID")
	private Long currencyId;
	@Basic
	@Column(name = "INQUIRY_NUMBER")
	private String inquiryNumber;
	@Basic
	@Column(name = "MATERIAL")
	private String material;
	@Basic
	@Column(name = "TRANSPORT_ORIGIN")
	private String transportOrigin;
	@Basic
	@Column(name = "PRICE")
	private String price;
	@Basic
	@Column(name = "QUANTITY")
	private Float quantity;
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
	@Column(name = "PACKAGE_TYPE")
	private String packingType;
	@Basic
	@Column(name = "STANDARD_CODE")
	private String standardCode;
	@Basic
	@Column(name = "NUM_OF_SHIPMENT")
	private String numberOfShipment;
	@Basic
	@Column(name = "INSPECTION_RANGE")
	private String inspectionRange;
	@Basic
	@Column(name = "SUPPLIER_DESC")
	private String supplierDescription;
	@Basic
	@Column(name = "PURE_WIEGHT")
	private String pureWeight;
	@Basic
	@Column(name = "GROSS_WIEGHT")
	private String grossWeight;
	@Basic
	@Column(name = "TRANSPORT_CITY_ORIGIN")
	private String transportCityOrigin;
	@Basic
	@Column(name = "TRANSPORT_CITY_DESTINATION")
	private String transportCityDestination;
	@Basic
	@Column(name = "TRANSPORT_DESTINATION")
	private String transportDestination;
	@Basic
	@Column(name = "PORTAGE_TYPE")
	private String portageType;
	@Basic
	@Column(name = "PORTAGE_TERM")
	private String portageTerm;
	@Basic
	@Column(name = "PACKING_DEMENSION")
	private String packagingDimension;

}
