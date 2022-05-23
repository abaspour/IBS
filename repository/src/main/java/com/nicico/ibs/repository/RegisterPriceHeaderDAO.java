package com.nicico.ibs.repository;

import com.nicico.ibs.model.RegisterPriceHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterPriceHeaderDAO extends JpaRepository<RegisterPriceHeader, Long>, JpaSpecificationExecutor<RegisterPriceHeader> {

    RegisterPriceHeader findAllById(long id);
    @Query(value = " select nvl(inq.INQUIRY_TYPE,' ')||'@@@@##@@'||nvl(inq.SPARE_PRODUCT_TIME,' ')  " +
            "from fb.TBL_INQIRY inq where inq.id= ?1   ", nativeQuery = true)
    public String findInquiryTypeAndSpareById(String id);

    @Query(value = " select distinct 'att'||a.id,nvl(ATTACHED_FILE_FILENAME,'Na'),nvl(ATTACHED_TYPE,'Na') from  " +
            " COMMERCE.TBL_REQUEST_ITEM_ATTACHED a, FB.TBL_REGISTERPRICE p  " +
            "where a.REQUEST_ITEM_ID = p.REQUESTITEMID and p.ACCOUNT_ID= ?1 and REQUEST_ITEM_ID= ?2 and ATTACHED_FILE_FILENAME is not null  " +
            "order by nvl(ATTACHED_TYPE,'Na')     ", nativeQuery = true)
    public List<Object[]> findItemAttach(Long accountId,Long requestItemId);

    @Query(value = "  select distinct 'map'||mv.id , mv.file_new_name FILE_NEW_NAME  " +
            "from COMMERCE.tbl_map_view mv   " +
            "join COMMERCE.tbl_map m on m.id = mv.map_id   " +
            "join COMMERCE.tbl_map_group mg on mg.id = m.magroup_id   " +
            "join COMMERCE.tbl_requestitem ri on ri.materialid = mg.material_id   " +
            "join  FB.TBL_REGISTERPRICE p on p.REQUESTITEMID=ri.id and p.ACCOUNT_ID= ?1   " +
            "where m.is_active = '1' and ri.id =  ?2   and ( mv.file_new_name like '%.pdf' or mv.file_new_name like '%.jpg' or   " +
            "      mv.file_new_name like '%.jpeg' or  mv.file_new_name like '%.PDF' or  mv.file_new_name like '%.JPG' or    " +
            "      mv.file_new_name like '%.JPEG' or  mv.file_new_name like '%.dwg' or  mv.file_new_name like '%.tif') " +
            "      and mv.file_new_name is not null  " +
            "order by 'map'||mv.id desc  ", nativeQuery = true)
    public List<Object[]> findMap(Long accountId,Long requestItemId);

    @Query(value = " select min(a.ATTACHED_FILE_NEWNAME) from  " +
            " COMMERCE.TBL_REQUEST_ITEM_ATTACHED a, FB.TBL_REGISTERPRICE p  " +
            "where a.REQUEST_ITEM_ID = p.REQUESTITEMID and p.ACCOUNT_ID= ?1 and a.id= ?2 and ATTACHED_FILE_FILENAME is not null  " +
            "order by nvl(ATTACHED_TYPE,'Na')     ", nativeQuery = true)
    public String findItemAttachName(Long accountId,Long id);

    @Query(value = "  select min(mv.FILE_NEW_NAME)  " +
            "from COMMERCE.tbl_map_view mv   " +
            "join COMMERCE.tbl_map m on m.id = mv.map_id   " +
            "join COMMERCE.tbl_map_group mg on mg.id = m.magroup_id   " +
            "join COMMERCE.tbl_requestitem ri on ri.materialid = mg.material_id   " +
            "join  FB.TBL_REGISTERPRICE p on p.REQUESTITEMID=ri.id and p.ACCOUNT_ID= ?1   " +
            "where m.is_active = '1' and  mv.id= ?2  and ( mv.file_new_name like '%.pdf' or mv.file_new_name like '%.jpg' or   " +
            "      mv.file_new_name like '%.jpeg' or  mv.file_new_name like '%.PDF' or  mv.file_new_name like '%.JPG' or    " +
            "      mv.file_new_name like '%.JPEG' or  mv.file_new_name like '%.dwg' or  mv.file_new_name like '%.tif') " +
            "      and mv.file_new_name is not null  " +
            "order by 'map'||mv.id desc  ", nativeQuery = true)
    public String findMapName(Long accountId,Long mapId);


}
