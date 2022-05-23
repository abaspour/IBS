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
public class RegisterFirstDTO {

	@NotNull
	@ApiModelProperty(required = true)
	private String registerNo;
	@NotNull
	@ApiModelProperty(required = true)
	private String registerCountry;
	@NotNull
	@ApiModelProperty(required = true)
	private String contactPersonEmail;
	@NotNull
	@ApiModelProperty(required = true)
	private String registerId;
	@NotNull
	@ApiModelProperty(required = true)
	private String status;

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("RegisterFirstInfoTuple")
	public static class InfoTuple {
		private Long id;
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("RegisterFirstInfo")
	public static class Info extends RegisterFirstDTO {
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
	@ApiModel("RegisterFirstCreateRq")
	public static class Create extends RegisterFirstDTO {
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("RegisterFirstUpdateRq")
	public static class Update extends RegisterFirstDTO {
		@NotNull
		@ApiModelProperty(required = true)
		private Integer version;
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("RegisterFirstDeleteRq")
	public static class Delete {
		@NotNull
		@ApiModelProperty(required = true)
		private List<Long> ids;
	}
}
