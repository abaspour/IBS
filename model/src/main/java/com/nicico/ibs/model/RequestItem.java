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
@Table(schema = "COMMERCE", name = "TBL_REQUESTITEM")
public class RequestItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requestItem_seq")
	@SequenceGenerator(name = "requestItem_seq", sequenceName = "FB.SEQ_INQUIRY", allocationSize = 1)
	private Long id;

	@Basic
	@Column(name = "REQUESTID")
	private Long requestId;

	@Basic
	@Column(name = "REQUESTITEMROW")
	private Long itemRow;

}
