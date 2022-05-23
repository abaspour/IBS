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
public class MessageDTO {

	@NotEmpty
	@ApiModelProperty(required = true)
	private Long id;
	private Long senderId;
	private Long receiverId;
	private String inquiryNumber;
	private String subject;
	private String messageText;
	private String senderName;
	private String receiverName;
	private String sendDate;
	private String sendTime;
	private String status;
	private String isViewed;
	private String isReplied;
	private String messageKind;
	private String fileNewName;
	private String fileOldName;
	private String userFolderId;

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("MessageInfo")
	public static class Info extends MessageDTO {
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
	@ApiModel("MessageCreateRq")
	public static class Create extends MessageDTO {
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("MessageUpdateRq")
	public static class Update extends MessageDTO {
		@NotNull
		@ApiModelProperty(required = true)
		private Integer version;
	}

	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("MessageDeleteRq")
	public static class Delete {
		@NotNull
		@ApiModelProperty(required = true)
		private List<Long> ids;
	}
}
