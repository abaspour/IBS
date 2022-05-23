package com.nicico.ibs.repository;

import com.nicico.ibs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
//    " select ID,ENABLED,PASSWORD,USERNAME,FIRST_NAME,LAST_NAME,SUPPLIERID from ( "+
//            " select p.ID,p.ACTIVITY_STATUS ENABLED,P.PASSWORD,p.USERNAME, p.FIRST_NAME,p.LAST_NAME,a.id SUPPLIERID from CRM.TBL_PERSON P " +
//            "  inner join crm.tbl_contact c on P.CONTACT_ID=c.ID " +
//            "  inner join crm.tbl_account a on c.ACCOUNT_ID=a.ID " +
//            "  where  a.CURRENCY_ID=1009 and P.PERSON_TYPE='e' and p.username= :us ) "

//delete mainparts.tbl_user where user_type='e';
//    INSERT INTO mainparts.tbl_user (id,enabled,password,username,first_name,last_name,user_type,supplierid,currencyid)
//    SELECT   p.id,p.activity_status,p.password,p.username,p.first_name,p.last_name,p.person_type,a.id,a.currency_id
//    FROM    crm.tbl_person p
//    inner join crm.tbl_contact c on P.CONTACT_ID=c.ID
//    inner join crm.tbl_account a on c.ACCOUNT_ID=a.ID
//    where p.person_type='e' and p.username is not null and not exists (select id from mainparts.tbl_user where id=p.id);
//    commit;

    @Query(value=
            " select u.ID,u.ENABLED,u.PASSWORD,u.USERNAME,u.FIRST_NAME,u.LAST_NAME,u.USER_TYPE,u.SIGNATURE_NEW_NAME,u.SIGNATURE_OLD_NAME, " +
                    "u.FILTER_REQUESTER,u.FILTER_PLACE,u.FILTER_CITY,u.FILTER_REQUNIT,u.EN_FIRST_NAME,u.EN_LAST_NAME,u.PASSWORD2,u.supplierid,u.currencyid " +
                    " from  mainparts.tbl_user  u " +
                    " where u.username= replace(:us , '@@','') and USER_TYPE='e' and " +
                    " ((currencyid=1009  and instr(:us , '@@')< 1) or (currencyid!=1009 and instr(:us , '@@')>0)) "
            ,nativeQuery=true)
    public User findByUsername(@Param("us") String username);


    @Query(value="select distinct r.* from mainparts.tbl_user_role ur " +
            " inner join mainparts.tbl_role r on r.id =  ur.role_id " +
            " inner join mainparts.tbl_user u on u.id = ur.user_id " +
            " where u.username= ?1 ",nativeQuery=true)
    public List<Object[]> findRolesByUsername(String userName);

}
