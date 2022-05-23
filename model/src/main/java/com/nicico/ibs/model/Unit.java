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
@Table(schema = "COMMERCE", name = "tbl_unit")
public class Unit {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_seq")
	@SequenceGenerator(name = "unit_seq", sequenceName = "FB.SEQ_INQUIRY", allocationSize = 1)
	private Long id;

	@Basic
	@Column(name = "NAME_FA", length = 100)
	private String nameFa;

	@Basic
	@Column(name = "NAME_EN", length = 100)
	private String nameEn;

	@Basic
	@Column(name = "INV_ID", length = 100)
	private String invId;

	@Basic
	@Column(name = "ISACTIVE", length = 100)
	private String isActive;
}

