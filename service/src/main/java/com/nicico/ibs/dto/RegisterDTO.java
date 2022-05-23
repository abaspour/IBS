package com.nicico.ibs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterDTO {

	@NotNull
	@ApiModelProperty(required = true)
	private String fullName;
	@NotNull
	@ApiModelProperty(required = true)
	private String registerNo;
	@NotNull
	@ApiModelProperty(required = true)
	private Date registerDate;
	@NotNull
	@ApiModelProperty(required = true)
	private String registerCountry;
	@NotNull
	@ApiModelProperty(required = true)
	private String registerPlace;
	@NotNull
	@ApiModelProperty(required = true)
	private String fullAddress;
	@NotNull
	@ApiModelProperty(required = true)
	private String telNo;
	@NotNull
	@ApiModelProperty(required = true)
	private String faxNo;
	@NotNull
	@ApiModelProperty(required = true)
	private String website;
	@NotNull
	@ApiModelProperty(required = true)
	private String mainEmail;
	private String officeFullAddress;
	private String officeTelNo;
	private String officeFaxNo;
	private String officeWebsite;
	private String officeMainEmail;
	@NotNull
	@ApiModelProperty(required = true)
	private String companyType;
	@NotNull
	@ApiModelProperty(required = true)
	private String companyActivity;
	private String iranAgency;
	private String agencyType;
	private String agencyFullName;
	private String agencyRegisterNo;
	private Date agencyRegisterDate;
	private Long agencyRegisterCountry;
	private String agencyRegisterPlace;
	private String agencyFullAddress;
	private String agencyTelNo;
	private String agencyCelNo;
	private String agencyFaxNo;
	private String agencyWebsite;
	private String agencyMainEmail;
	private String agencyRepresentValidDate;
	private String contactPersonName;
	private String contactPersonPosition;
	private String contactPersonTel;
	private String contactPersonEmail;
	private String productsDetail;
	private String oldFileName;
	private String newFileName;

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("RegisterInfo")
	public static class Info {
		private Long id;
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("RegisterInfoTuple")
	public static class InfoTuple extends RegisterDTO {
		private Long id;
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
	@ApiModel("RegisterCreateRq")
	public static class Create extends RegisterDTO {
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("RegisterUpdateRq")
	public static class Update extends RegisterDTO {
		@NotNull
		@ApiModelProperty(required = true)
		private Integer version;
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("RegisterDeleteRq")
	public static class Delete {
		@NotNull
		@ApiModelProperty(required = true)
		private List<Long> ids;
	}
}
