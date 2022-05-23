package com.nicico.ibs.repository;

import com.nicico.ibs.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDAO extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {

    Message findAllById(Long id);

    @Query(value = " select crm.SEQ_MESSAGE_ID.nextval  from dual  ", nativeQuery = true)
    public Long findNewId();

    @Modifying
    @Query(value = " insert into crm.tbl_message (ID , SUBJECT,USER_CREATOR_ID,SENDER_NAME,SEND_SMS,SEND_EMAIL,SEND_FAX,SEND_IM,TO_IM, MESSAGE_TEXT,STATUS,MESSAGE_KIND,INQUIRYNUMBER,SEND_DATE,                         SEND_TIME) " +
            "                            values  (?1   , ?2        , ?3          , ?4        ,'n'     ,'n',       'n',    'y',    ?5 ,       ?6    ,'SENT',    ?7      ,   ?8        ,TO_CHAR(SYSDATE,'YYYY/MM/DD','NLS_CALENDAR=''Persian'''),TO_CHAR(SYSDATE,'HH24:MM:SS','NLS_CALENDAR=''Persian'''))   ", nativeQuery = true)
    public int insertMsg(long mId,String subject,Long creatorId,String creatorName,String receiverName,String message ,String kind,String inquiryNo);

    @Modifying
    @Query(value = " insert into CRM.tbl_message_reciever (ID,MESSAGE_ID,USER_ID,USER_FOLDER_ID,IS_VIEWED,MESSAGE_TYPE,IS_FORWARDED,IS_REPLIED) " +
                "  values(crm.SEQ_MESSAGE_RECIEVER_ID.nextval,?1        ,311281 ,0,             'n'        ,'i'        ,'n'         ,'n')  ", nativeQuery = true)
    public int insertReceiveMsg(Long mId);

    @Modifying
    @Query(value = "INSERT INTO crm.tbl_message_attachment (id,message_id,file_new_name,file_old_name) " +
            " VALUES (crm.SEQ_MESSAGE_ATTACHMENT_ID.nextval , ?1 , ?2 , ?3 ) ", nativeQuery = true)
    public int insertAttMsg(Long mId,String fileNewName,String FileOldName);

    @Modifying
    @Query(value = " update  CRM.tbl_message_reciever set IS_VIEWED='y' where MESSAGE_ID= ?1 and USER_ID=?2 and nvl(IS_VIEWED,' ')!='y'  ", nativeQuery = true)
    public int setViewedMsg(Long mId,Long userId);

    @Modifying
    @Query(value = "   update  CRM.tbl_message_reciever set USER_FOLDER_ID=1 where MESSAGE_ID= ?1  and nvl(USER_FOLDER_ID,5)!= 1 " +
                        "  and (USER_ID= ?2  or (select USER_CREATOR_ID from crm.tbl_message where id= ?1 ) = ?2 )  ", nativeQuery = true)
    public int setTrashMsg(Long mId,Long userId);

    @Modifying
    @Query(value = "  update  CRM.tbl_message_reciever set USER_FOLDER_ID= case when USER_ID= ?2 then 0 else 2 end " +
            "          where MESSAGE_ID= ?1  and nvl(USER_FOLDER_ID,5)=1 " +
            "  and (USER_ID= ?2 or (select USER_CREATOR_ID from crm.tbl_message where id= ?1 ) = ?2 )  ", nativeQuery = true)
    public int setRecoverMsg(Long mId,Long userId);



}
