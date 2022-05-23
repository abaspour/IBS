package com.nicico.ibs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterPriceHeaderDTO {

	@NotEmpty
	@ApiModelProperty(required = true)
	private Long id;
	private String inquiryNumber;
	private String supplierId;
	private String portageCost;
	private String subSpareProductCost;
	private String otherCost;
	private String discount;
	private String sumPrice;
	private String taxCost;
	private String checkInfo;
	private String subInstallCost;
	private String portage;
	private String installCostRequire;
	private String otherCostRequire;
	private String spareProductFileDateCreated;
	private String spareProductFileSize;
	private String spareProductFileName;
	private Long taxCostValue;
	private String supplierResponseTime;
	private String supplierResponseDate;
	private String spareProductFileNewName;
	private String replyDate;
	private Long sumPrincipal;
	private String changeStatus;
	private String packingCost;

	private String inquiryType;
	private String spareProductTime;
	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("RegisterPriceHeaderInfo")
	public static class Info extends RegisterPriceHeaderDTO {
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
	@ApiModel("RegisterPriceHeaderCreateRq")
	public static class Create extends RegisterPriceHeaderDTO {
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("RegisterPriceHeaderUpdateRq")
	public static class Update extends RegisterPriceHeaderDTO {
		@NotNull
		@ApiModelProperty(required = true)
		private Integer version;
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("RegisterPriceHeaderDeleteRq")
	public static class Delete {
		@NotNull
		@ApiModelProperty(required = true)
		private List<Long> ids;
	}
}
