package com.nicico.ibs.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Immutable
@Subselect(
	"select m.id ,m.message_text,m.subject,m.inquirynumber,m.sender_name,m.to_im receiver_name, m.send_date,m.send_time,m.status,mr.is_viewed,mr.is_replied, " +
		"m.USER_CREATOR_ID serner_id,mr.user_id receiver_id,m.MESSAGE_KIND,a.FILE_NEW_NAME,a.FILE_OLD_NAME,mr.USER_FOLDER_ID " +
	"from crm.tbl_message m " +
	"inner join CRM.tbl_message_reciever mr on mr.message_id=m.id " +
	"left join (select min(id)id,MESSAGE_ID from  CRM.TBL_MESSAGE_ATTACHMENT group by MESSAGE_ID)  aa on aa.MESSAGE_ID=m.id " +
	"left join CRM.TBL_MESSAGE_ATTACHMENT a on aa.id=a.id " +
	"where m.inquirynumber is not null and m.status='SENT' ")
public class Message  {

	@Id
	private Long id;
	@Basic
	@Column(name = "serner_id")
	private Long senderId;
	@Basic
	@Column(name = "receiver_id")
	private Long receiverId;
	@Basic
	@Column(name = "inquirynumber")
	private String inquiryNumber;
	@Basic
	@Column(name = "subject")
	private String subject;
	@Basic
	@Column(name = "message_text")
	private String messageText;
	@Basic
	@Column(name = "sender_name")
	private String senderName;
	@Basic
	@Column(name = "receiver_name")
	private String receiverName;
	@Basic
	@Column(name = "send_date")
	private String sendDate;
	@Basic
	@Column(name = "send_time")
	private String sendTime;
	@Basic
	@Column(name = "status")
	private String status;
	@Basic
	@Column(name = "is_viewed")
	private String isViewed;
	@Basic
	@Column(name = "is_replied")
	private String isReplied;
	@Basic
	@Column(name = "MESSAGE_KIND")
	private String messageKind;
	@Basic
	@Column(name = "FILE_NEW_NAME")
	private String fileNewName;
	@Basic
	@Column(name = "FILE_OLD_NAME")
	private String fileOldName;
	@Basic
	@Column(name = "USER_FOLDER_ID")
	private String userFolderId;
}
