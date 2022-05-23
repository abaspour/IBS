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
@Table(schema = "COMMERCE", name = "TBL_CUSTOM")
public class Custom {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_seq")
	@SequenceGenerator(name = "custom_seq", sequenceName = "FB.SEQ_INQUIRY", allocationSize = 1)
	private Long id;

	@Basic
	@Column(name = "NAME_FA", length = 100)
	private String nameFa;

	@Basic
	@Column(name = "NAME_EN", length = 100)
	private String nameEn;

	@Basic
	@Column(name = "CODE", length = 100)
	private String code;

	@Basic
	@Column(name = "ISACTIVE", length = 100)
	private String isActive;
}

