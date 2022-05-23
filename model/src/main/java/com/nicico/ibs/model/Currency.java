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
@Table(schema = "COMMERCE", name = "TBL_CURRENCY")
public class Currency {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_seq")
	@SequenceGenerator(name = "currency_seq", sequenceName = "FB.SEQ_INQUIRY", allocationSize = 1)
	private Long id;

	@Basic
	@Column(name = "NAME_FA", nullable = false)
	private String nameFa;

	@Basic
	@Column(name = "NAME_EN", nullable = false)
	private String nameEn;

	@Column(name = "INV_ID")
	private String invId;

	@Column(name = "ISACTIVE")
	private String isActive;
}
