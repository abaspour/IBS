package com.nicico.ibs.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "tbl_register",
		uniqueConstraints = {
				@UniqueConstraint(
						columnNames = {"register_no", "register_country"},
						name = "tbl_register_uk_no_country"
				)
		})
public class Register extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "register_seq")
	@SequenceGenerator(name = "register_seq", sequenceName = "seq_register_id", allocationSize = 1)
	private Long id;

	@Column(name = "full_name", nullable = false, length = 200)
	private String fullName;

	@Column(name = "register_no", nullable = false, length = 100)
	private String registerNo;

	@Column(name = "register_date", nullable = false)
	private Date registerDate;

	@Column(name = "register_country", nullable = false)
	private String registerCountry;

	@Column(name = "register_place", nullable = false, length = 100)
	private String registerPlace;

	@Column(name = "full_address", nullable = false, length = 1000)
	private String fullAddress;

	@Column(name = "tel_no", nullable = false, length = 100)
	private String telNo;

	@Column(name = "fax_no", nullable = false, length = 100)
	private String faxNo;

	@Column(name = "website", nullable = false, length = 100)
	private String website;

	@Column(name = "main_email", nullable = false, length = 100)
	private String mainEmail;

	@Column(name = "office_full_address", length = 1000)
	private String officeFullAddress;

	@Column(name = "office_tel_no", length = 100)
	private String officeTelNo;

	@Column(name = "office_fax_no", length = 100)
	private String officeFaxNo;

	@Column(name = "office_website", length = 100)
	private String officeWebsite;

	@Column(name = "office_main_email", length = 100)
	private String officeMainEmail;

	@Column(name = "company_type", nullable = false, length = 20)
	private String companyType;

	@Column(name = "company_activity", nullable = false, length = 250)
	private String companyActivity;

	@Column(name = "iran_agency", length = 10)
	private String iranAgency;

	@Column(name = "agency_type", length = 20)
	private String agencyType;

	@Column(name = "agency_full_name", length = 200)
	private String agencyFullName;

	@Column(name = "agency_register_no", length = 100)
	private String agencyRegisterNo;

	@Column(name = "agency_register_date")
	private Date agencyRegisterDate;

	@Column(name = "agency_register_country")
	private Long agencyRegisterCountry;

	@Column(name = "agency_register_place", length = 100)
	private String agencyRegisterPlace;

	@Column(name = "agency_full_address", length = 1000)
	private String agencyFullAddress;

	@Column(name = "agency_tel_no", length = 100)
	private String agencyTelNo;

	@Column(name = "agency_cel_no", length = 100)
	private String agencyCelNo;

	@Column(name = "agency_fax_no", length = 100)
	private String agencyFaxNo;

	@Column(name = "agency_website", length = 100)
	private String agencyWebsite;

	@Column(name = "agency_main_email", length = 100)
	private String agencyMainEmail;

	@Column(name = "agency_represent_valid_date", length = 100)
	private String agencyRepresentValidDate;

	@Column(name = "contact_person_name", length = 200)
	private String contactPersonName;

	@Column(name = "contact_person_position", length = 200)
	private String contactPersonPosition;

	@Column(name = "contact_person_tel", length = 100)
	private String contactPersonTel;

	@Column(name = "contact_person_email", length = 100)
	private String contactPersonEmail;

	@Column(name = "products_detail", length = 4000)
	private String productsDetail;

	@Column(name = "old_file_name", length = 300)
	private String oldFileName;

	@Column(name = "new_file_name", length = 300)
	private String newFileName;

}
