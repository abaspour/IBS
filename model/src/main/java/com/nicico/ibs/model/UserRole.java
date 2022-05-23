package com.nicico.ibs.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table( schema = "MAINPARTS", name = "TBL_ROLE")
public class UserRole implements Serializable  {
	private static final long serialVersionUID = 6874667425302308430L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	@SequenceGenerator(name = "user_seq", sequenceName = "FB.SEQ_INQUIRY", allocationSize = 1)
	private Long id;

		@Column(name = "rolename", length = 200)
		private String rolename;

		@Column(name = "roledesc", length = 200)
		private String roledesc;

	}
