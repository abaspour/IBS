package com.nicico.ibs.repository;

import com.nicico.ibs.model.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryDAO extends JpaRepository<Inquiry, Long>, JpaSpecificationExecutor<Inquiry> {
    @Query(value = "select  DBMS_LOB.SUBSTR(dsc,2300),DBMS_LOB.SUBSTR(TECHNICLA_DESC,2300),DBMS_LOB.SUBSTR(PACKNG_CONDITATION,2300) from FB.TBL_INQIRY where id = ?1   ", nativeQuery = true)
    public String[] findBlobs(Long id);

    //    @Query(value = "select count(inquirynumber) INQUIRYCOUNT,verrify_status from fb.tbl_inqiry where account_id=  ?1   group by verrify_status   " +
//            "UNION   " +
//            "select count(1)INQUIRYCOUNT ,'contract_'||status verrify_status from fb.tbl_contract where suppiler_id= ?1  group by status   " +
//            "UNION   " +
//            "select count(1)INQUIRYCOUNT ,'message_'||r.is_viewed from crm.tbl_message m   " +
//            "join crm.tbl_message_reciever r on r.message_id=m.id   " +
//            "where m.status='SENT' and r.user_id=(select min(id) from mainparts.tbl_user where supplierid= ?1 )   " +
//            "group by r.is_viewed", nativeQuery = true)
    @Query(value = "select count(inquirynumber) INQUIRYCOUNT,verrify_status from fb.tbl_inqiry    " +
            "    where account_id=  ?1  and    " +
            "     not((VERRIFY_STATUS='n' or VERRIFY_STATUS='v')and ENDREPLYDATE<to_char(sysdate,'yyyy/mm/dd','NLS_Calendar=persian'))    " +
            "    group by verrify_status   " +
            "UNION   " +
            "select count(inquirynumber) INQUIRYCOUNT,'c' verrify_status from fb.tbl_inqiry    " +
            "    where account_id=  ?1  and ENDREPLYDATE<to_char(sysdate,'yyyy/mm/dd','NLS_Calendar=persian')    " +
            "          and (VERRIFY_STATUS='n' or VERRIFY_STATUS='v')   " +
            "UNION   " +
            "select count(1)INQUIRYCOUNT ,'contract_'||status verrify_status from fb.tbl_contract where suppiler_id= ?1  group by status   " +
            "UNION   " +
            "select count(1)INQUIRYCOUNT ,'message_'||r.is_viewed from crm.tbl_message m   " +
            "join crm.tbl_message_reciever r on r.message_id=m.id   " +
            "where m.status='SENT' and r.user_id=(select min(id) from mainparts.tbl_user where supplierid= ?1 )   " +
            "group by r.is_viewed", nativeQuery = true)
    List<Object[]> inquiryStatusCount(Long accountId);

    @Modifying
    @Query(value = " update fb.tbl_inqiry set VERRIFY_STATUS='v' where VERRIFY_STATUS='n' and inquirynumber= ?1 and account_id= ?2 ", nativeQuery = true)
    int inquiryStatusCount(Long inqueryNumber,Long accountId);

    @Query(value = "select id,ATTACHMENT_NEWNAME  from FB.TBL_INQIRY where id= ?1 and account_id= ?2 ", nativeQuery = true)
    String  findAttachName(Long id,Long accountId);

    @Query(value = " select commerce.FUN_COPY_INQ_FB( ?1 , ?2 ) from dual  ", nativeQuery = true)
    String  inquiryCopy(Long id,Long userId);

}
