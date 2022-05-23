package com.nicico.ibs.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table( schema = "FB", name = "VIW_DELIVARY_LOCATION")
public class DeliveryLocation {

	@Id
	private Long id;

	@Basic
	@Column(name = "DELIVARY_NAME_FA" , nullable = false )
	private String deliveryNameFa;

	}
