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
public class InquiryPortageDTO {

	@NotEmpty
	@ApiModelProperty(required = true)
	private Long id;
	private Long currencyId;
	private String inquiryNumber;
	private String material;
	private String transportOrigin;
	private String price;
	private Float quantity;
	private String attachmentFileName;
	private String attachmentDateCreated;
	private String attachmentNewName;
	private Float attachmentFileSize;
	private String packingType;
	private String standardCode;
	private String numberOfShipment;
	private String inspectionRange;
	private String supplierDescription;
	private String pureWeight;
	private String grossWeight;
	private String transportCityOrigin;
	private String transportCityDestination;
	private String transportDestination;
	private String portageType;
	private String portageTerm;
	private String packagingDimension;

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("InquiryPortageInfo")
	public static class Info extends InquiryPortageDTO {
		private Long id;
		private CurrencyDTO.Info currency;
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
	@ApiModel("InquiryPortageCreateRq")
	public static class Create extends InquiryPortageDTO {
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("InquiryPortageUpdateRq")
	public static class Update extends InquiryPortageDTO {
		@NotNull
		@ApiModelProperty(required = true)
		private Integer version;
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("InquiryPortageDeleteRq")
	public static class Delete {
		@NotNull
		@ApiModelProperty(required = true)
		private List<Long> ids;
	}
}
