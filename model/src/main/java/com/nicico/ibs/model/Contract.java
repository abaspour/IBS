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
@Table(schema = "FB", name = "TBL_CONTRACT")
public class Contract  {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_seq")
	@SequenceGenerator(name = "contract_seq", sequenceName = "FB.SEQ_INQUIRY", allocationSize = 1)
	private Long id;

	@Basic
	@Column(name = "SUPPILER_ID")
	private Long supplierId;

	@Basic
	@Column(name = "REQUEST_ID")
	private Long requestId;

	@Basic
	@Column(name = "PURCHASE_ORDER")
	private String purchaseOrder;

	@Basic
	@Column(name = "CODE")
	private String code;

	@Basic
	@Column(name = "DSC")
	private String dsc;

	@Basic
	@Column(name = "STATUS")
	private String status;

	@Basic
	@Column(name = "FILE_NEW_NAME")
	private String fileNewName;
	@Basic
	@Column(name = "FILE_OLD_NAME")
	private String fileOldName;

}
