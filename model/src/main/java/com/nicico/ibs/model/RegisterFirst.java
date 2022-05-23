package com.nicico.ibs.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@SuppressWarnings("ALL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "tbl_register_first",
		uniqueConstraints = {
				@UniqueConstraint(
						columnNames = {"register_no", "register_country"},
						name = "tbl_register_f_uk_no_country"
				)
		})
public class RegisterFirst extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "register_seq")
	@SequenceGenerator(name = "register_seq", sequenceName = "seq_register_id", allocationSize = 1)
	private Long id;

	@Column(name = "register_no", nullable = false, length = 100)
	private String registerNo;

	@Column(name = "register_country", nullable = false)
	private String registerCountry;

	@Column(name = "register_id", nullable = false, length = 100)
	private String registerId;

	@Column(name = "contact_person_email", nullable = false, length = 100)
	private String contactPersonEmail;

	@Column(name = "status", nullable = false, length = 20)
	private String status;

}
