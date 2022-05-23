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
public class ProformitemDTO {

	@NotEmpty
	@ApiModelProperty(required = true)
	private Long id;
	private String price;
	private Float amount;
	private String attachmentFileName;
	private String attachmentDateCreated;
	private String attachmentNewName;
	private Long attachmentFileSize;
	private String itemDescl;
	private String requestItemSpec;
	private String totalPrice;
	private String constructorName;
	private String verifyPrice;
	private String itemDescription;
	private String ignorePricing;
	private String technicalDesc;
	private String warrantyDesc;
	private String packingDesc;
	private Long requestItemId;
	private Long unitId;
	private Long proformId;
	private Long countryByConstructureId;

	// from attachments and maps
	private List<String> listAttachIds;
	private List<String> listAttachFileName;
	private List<String> listAttachDesc;
	private Long currencyId;
	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("ProformitemInfo")
	public static class Info extends ProformitemDTO {
		private Long id;
		private UnitDTO.Info unit;
		private ProformDTO.Info proform;
		private CountryDTO.Info countryByConstructure;
		private RequestItemDTO.Info requestItem;
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
	@ApiModel("ProformitemCreateRq")
	public static class Create extends ProformitemDTO {
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("ProformitemUpdateRq")
	public static class Update extends ProformitemDTO {
		@NotNull
		@ApiModelProperty(required = true)
		private Integer version;
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("ProformitemDeleteRq")
	public static class Delete {
		@NotNull
		@ApiModelProperty(required = true)
		private List<Long> ids;
	}
}
