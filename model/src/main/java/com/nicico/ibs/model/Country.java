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
@Table(schema = "MAINPARTS", name = "tbl_country")
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_seq")
	@SequenceGenerator(name = "country_seq", sequenceName = "seq_country_id", allocationSize = 1)
	private Long id;

	@Column(name = "NAME_FA")
	private String countryNameLocal;

	@Column(name = "NAME_EN", nullable = false)
	private String countryNameEn;

	@Column(name = "CODE")
	private String countryCallingCode;

	@Column(name = "ISACTIVE", nullable = false)
	private String isActive;

	@Column(name = "INV_ID", nullable = false)
	private String invId;

}
