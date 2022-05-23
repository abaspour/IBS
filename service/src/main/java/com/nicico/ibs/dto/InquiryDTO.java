package com.nicico.ibs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nicico.ibs.model.DeliveryLocation;
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
public class InquiryDTO {

	@NotEmpty
	@ApiModelProperty(required = true)
	private Long supplierId;
	private String inquiryNumber;
	private String inquiryName;
	private String inquiryDesc;
	private String inquiryDate;
	private String sendDate;
	private String endReplyDate;
	private String inquiryType;
	private String validTime;

	private String enCancelDate;
	private String enInquiryDate;
	private String enSendDate;
	private String enEndReplyDate;

	private String verifyStatus;
	private String testDesc;
	private String briefingPlace;
	private String briefingDate;
	private String technicalDesc;
	private String verify;
	private String attachmentDateCreated;
	private String attachmentFileName;
	private Long attachmentFileSize;
	private String attachmentNewName;
	private String portage;
	private String instalCost;
	private String otherCost;
	private String checkOffer;
	private String spareProductTime;
	private String spareProductTimeRequire;
	private String briefing;
	private String replyDate;
	private String supDesc;
	private String cancelDate;
	private String changeStatus;
	private Long placeId;

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("InquiryInfo")
	public static class Info extends InquiryDTO {
		private DeliveryLocation deliveryLocation;
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
	@ApiModel("InquiryCreateRq")
	public static class Create extends InquiryDTO {
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("InquiryUpdateRq")
	public static class Update extends InquiryDTO {
		@NotNull
		@ApiModelProperty(required = true)
		private Integer version;
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("InquiryDeleteRq")
	public static class Delete {
		@NotNull
		@ApiModelProperty(required = true)
		private List<Long> ids;
	}
}
