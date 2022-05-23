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
public class RegisterPriceDTO {

	@NotEmpty
	@ApiModelProperty(required = true)
	private Long id;
	private String inquiryNumber;
	private String itemRow;
	private String itemQty;
	private String quantity;
	private String unitPrice;
	private String gheymateKol;
	private String itemDescp;
	private String itemDescl;
	private String description;
	private String shomareNaghshe;
	private String technicalDesc;
	private String warrantyDesc;
	private String packingDesc;
	private String brand;
	private String model;
	private String manufacturerCountryId;
	private String costMode;
	private String modeMaterial;
	private String gheymateGhaleb;
	private String costModel;
	private String modelMaterial;
	private String gheymateModel;
	private String unitWeight;
	private String partMaterial;
	private String modateSakhtSample;
	private String modateSakhtAnboh;
	private String hasSample;
	private String tahvilBarAsase;
	private String guranteeTime;
	private String deliveryDate;
	private String packageCondition;
	private String priceBeforeDiscount;
	private String discount;
	private String verifyPrice;
	private String supplierDesc;
	private String attach;
	private String attachFileName;
	private String attachNewFileName;
	private String attachDateCreated;
	private Long   attachFileSize;
	private String receiveSample;
	private String existForTest;
	private String receiveAttach;
	private String guranteeTimeRequire;
	private String partMaterialRequired;
	private String modeMaterialRequire;
	private String modelMaterialRequire;
	private String changeStatus;
	private String offerDatePrice;
	private String packagingConditionRequire;
	private String refuseItemPrice;
	private String modatSakhtAnboh;
	private Long saleUnitId;
	private Long requestItemId;
	private String ignorePricing;
	private String testNeeded;
	private String itemSample;
// from unit table
	private String unitName;

	private String engMaterialProperty;
// from inquiry
	private String inquiryType;
	private String placeID;
// from attachments and maps
	private List<String> listAttachIds;
	private List<String> listAttachFileName;
	private List<String> listAttachDesc;

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("RegisterPriceInfo")
	public static class Info extends RegisterPriceDTO {
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
	@ApiModel("RegisterPriceCreateRq")
	public static class Create extends RegisterPriceDTO {
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("RegisterPriceUpdateRq")
	public static class Update extends RegisterPriceDTO {
		@NotNull
		@ApiModelProperty(required = true)
		private Integer version;
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("RegisterPriceDeleteRq")
	public static class Delete {
		@NotNull
		@ApiModelProperty(required = true)
		private List<Long> ids;
	}
}
