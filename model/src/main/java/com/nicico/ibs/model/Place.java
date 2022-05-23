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
@Table(schema = "COMMERCE", name = "TBL_PLACE")
public class Place {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requestItem_seq")
	@SequenceGenerator(name = "requestItem_seq", sequenceName = "FB.SEQ_INQUIRY", allocationSize = 1)
	private Long id;
	@Basic
	@Column(name = "NAME_FA", nullable = false, length = 100)
	private String nameFa;
	@Basic
	@Column(name = "NAME_EN", nullable = false, length = 100)
	private String nameEn;
	@Column(name = "INV_ID")
	private String invId;
	@Column(name = "ISACTIVE")
	private String isactive;
	@Column(name = "DOMAIN")
	private String domain;
	@Column(name = "LETTER_RECIEVER_NAME")
	private String letterRecieverName;
	@Column(name = "TEL")
	private String tel;
	@Column(name = "FAX")
	private String fax;
	@Column(name = "WEB_SITE")
	private String webSite;
	@Column(name = "EMAIL")
	private String email;
	@Column(name = "HOST")
	private String host;
	@Column(name = "FIRST_TITLE")
	private String firstTitle;
	@Column(name = "SECOND_TITLE")
	private String secondTitle;
	@Column(name = "RESPONSIBLE_NAME")
	private String responsibleName;
	@Column(name = "RESPONSIBLE_NAME_EN")
	private String responsibleNameEn;
	@Column(name = "RESPONSIBLE_POSITION")
	private String responsiblePosition;
	@Column(name = "RESPONSIBLE_POSITION_EN")
	private String responsiblePositionEn;
	@Column(name = "ADDRESS")
	private String address;
	@Column(name = "FORMAL_TITLE")
	private String formalTitle;
	@Column(name = "ADDRESSEENG")
	private String addresseng;
	@Column(name = "PLACE_TYPE")
	private String placeType;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "HEADER_DESC")
	private String headerDesc;
	@Column(name = "FILE_NEW_NAME")
	private String fileNewName;
	@Column(name = "FILE_OLD_NAME")
	private String fileOldName;
	@Column(name = "DIGITAL_DESC")
	private String digitalDesc;
	@Column(name = "NON_DIGITAL_DESC")
	private String nonDigitalDesc;
	@Column(name = "APPROVER_POSITION")
	private String approverPosition;
	@Column(name = "CONFIRM_ORDER_POSITION")
	private String confirmOrderPosition;
	@Column(name = "RESPONSIBLE_INQUIRY_POSITION")
	private String responsibleInquiryPosition;
	@Column(name = "RESPONSIBLE_INQUIRY_NAME")
	private String responsibleInquiryName;
	@Column(name = "PLACE")
	private String place;
	@Column(name = "CITY_ID")
	private Long cityId;
	@Column(name = "APPROVER_ID")
	private Long approverId;
	@Column(name = "RESPONSIBL_ID")
	private Long responsibleId;
	@Column(name = "CONFIRM_ORDER_ID")
	private Long confirmOrderId;
	@Column(name = "RESPONSIBLE_INQUIRY_ID")
	private Long tresponsibleInquiryId;

	}
