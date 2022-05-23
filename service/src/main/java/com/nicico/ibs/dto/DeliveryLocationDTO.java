package com.nicico.ibs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryLocationDTO {

	@NotEmpty
	@ApiModelProperty(required = true)
	private String deliveryNameFa;
	// ------------------------------

	@Getter
	@Setter
	@Accessors(chain = true)
	@ApiModel("DeliveryLocationInfo")
	public static class Info extends DeliveryLocationDTO {
		private Long id;
	}

}
