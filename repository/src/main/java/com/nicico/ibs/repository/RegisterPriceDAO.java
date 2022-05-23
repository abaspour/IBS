package com.nicico.ibs.repository;

import com.nicico.ibs.model.RegisterPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterPriceDAO extends JpaRepository<RegisterPrice, Long>, JpaSpecificationExecutor<RegisterPrice> {
    List <RegisterPrice> findAllByInquiryNumber(String inquiryNumber);
    @Query(value = " select nvl(ENG_MATERIAL_PROPERTY,' ')||'@@@@##@@'||nvl(vu.unit_name,' ')||'@@@@##@@'||nvl(inq.INQUIRY_TYPE,' ')||'@@@@##@@'||nvl(inq.PLACE_ID,' ')  " +
                            "from FB.TBL_REGISTERPRICE r  " +
                            "join fb.TBL_INQIRY inq on inq.id=r.INQUIRY_NUMBER " +
                            "left join FB.VIW_UNIT vu on r.SALEUNITID=vu.ID  " +
                            "left join COMMERCE.TBL_REQUESTITEM ri on  r.REQUESTITEMID=ri.ID " +
                            "left join COMMERCE.TBL_MATERIAL ma on ri.MATERIALID=ma.ID " +
                            "where r.id= ?1   ", nativeQuery = true)
    public String findPartAndUnitNameById(Long id);

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

    @Query(value =
            " select "
                    +" q.id ," //0
                    +" SUBSTR(TO_CHAR(M.LONGDESCP),1,1990) LONGDESCP, "  //1
                    +" SUBSTR(TO_CHAR(M.LONGDESCL),1,1990) LONGDESCL, " //2
                    +" m.descp ITEM_DESCP ,"  //3
                    +" m.descl ITEM_DESCL ,"  //4
                    +" m.code as materialCode ," //5
                    +" ri.REQUESTITEMROW ," //6
                    +" ri.ITEMAMOUNT ," //7
                    +" r.REQNUMBER ," //8
                    +" listagg (to_char( map.CODE),',') within group (order by map.code) mapCode ," //9
                    +" map.VERSION ," //10
                    +" q.EXPERT_ID," //11
                    +" q.APPROVER_ID," //12
                    +" q.RESPONSIBLE_ID ," //13
                    +" u.NAME_FA unitName ," //14
                    +" m.ENG_DELIVERYBASE " //15
                    +" from COMMERCE.TBL_QCF q "
                    +" inner join COMMERCE.tbl_material m on m.id = q.material_id "
                    +" inner join COMMERCE.tbl_requestitem ri on ri.id = q.REQUESTITEM_ID "
                    +" inner join COMMERCE.TBL_UNIT u on u.id = ri.ITEMUNIT "
                    +" inner join COMMERCE.tbl_request r on r.id = ri.REQUESTID "
                    +" left join COMMERCE.TBL_MAP_GROUP mg on mg.MATERIAL_ID = m.id "
                    +" left join COMMERCE.TBL_MAP map on map.MAGROUP_ID = mg.id "
                    +" where ri.ID= ?1 AND (map.IS_ACTIVE= 1 OR map.IS_ACTIVE is null) "
                    +" group by  q.id  ,SUBSTR(TO_CHAR(M.LONGDESCP),1,1990) ,SUBSTR(TO_CHAR(M.LONGDESCL),1,1990) , "
                    +"        m.descp , m.descl  , m.code   , ri.REQUESTITEMROW ,ri.ITEMAMOUNT ,r.REQNUMBER ,map.VERSION ," +
                           "  q.EXPERT_ID,q.APPROVER_ID,q.RESPONSIBLE_ID,u.NAME_FA , m.ENG_DELIVERYBASE "
            , nativeQuery = true)
    public List<Object> findAllQcf( Long reqItemId);

    @Query(value = // abas
            " select "
                    +" i.id ," //0
                    +" to_char(i.NAME) inspectionName, " //1
                    +" to_char(i.XCOMMENT) inspectionXcomment " //2
                    +" from COMMERCE.TBL_QCF_INSPECTION i "
                    +" inner join COMMERCE.TBL_QCF qcf on i.QCF_ID = qcf.id "
                    +"  where  qcf.ID= ?1  "
            , nativeQuery = true)
    public List<Object> findAllQcfInspection( Long qcfId);

    @Query(value = //abas
            " select "
                    +" c.ID ," //0
                    +" TO_CHAR(c.name) ,  " //1
                    +" TO_CHAR(c.XCOMMENT)   " //2
                    +" from COMMERCE.TBL_QCF_CERTIFICATE c "
                    +" inner join COMMERCE.TBL_QCF qcf on c.QCF_ID = qcf.id "
                    +"  where qcf.ID= ?1 "            , nativeQuery = true)
    public List<Object> findAllQcCertificate( Long qcfId);

    @Query(value = //abas
            " select "
                    +" qq.id ," //0
                    +" TO_CHAR(	qq.NAME) qualityName , " //1
                    +" TO_CHAR(qq.XCOMMENT) qualityXcomment " //2
                    +" from COMMERCE.TBL_QCF_QUALITY qq "
                    +" inner join COMMERCE.TBL_QCF qcf on qq.QCF_ID = qcf.id "
                    +"  where qcf.ID= ?1 "            , nativeQuery = true)
    public List<Object> findAllQcfQuality(Long qcfId);

    @Query(value = // abas
            " select "
                    +" qt.id ," //0
                    +" TO_CHAR(qt.NAME) quantityName, " //1
                    +" TO_CHAR(qt.XCOMMENT) quantityXcomment " //2
                    +" from COMMERCE.TBL_QCF_QUANTITY qt "
                    +" inner join COMMERCE.TBL_QCF qcf on qcf.id = qt.QCF_ID "
                    +"  where qcf.ID= ?1 "            , nativeQuery = true)
    public List<Object> findAllQcfQuantity(Long qcfId);

    @Query(value =   //abas
            " select "
                    +" qt.id ," //0
                    +" to_char(qt.NAME) quantityName, " //1
                    +" to_char(qt.XCOMMENT) quantityXcomment, " //2
                    +" to_char(qti.PARAMETER) quantityParameter, "//3
                    +" to_char(qti.MIN_VALUE), " //4
                    +" to_char(qti.MAX_VALUE), " //5
                    +" to_char(sqt.name) quantitySandard, " //6
                    +" to_char(tqt.XNAME) quantityTools " //7
 //                   +" qv.min_value_v, " //8
 //                   +" qv.max_value_v "	//9
                    +" from COMMERCE.TBL_QCF_QUANTITY_ITEMS qti "
//                    +" left join COMMERCE.tbl_qcf_quantity_value qv on qv.qcf_quantity_item_id = qti.id "
                    +" inner join COMMERCE.TBL_QCF_QUANTITY qt on qti.QCF_QUANTITY_ID = qt.id "
                    +" left join COMMERCE.TBL_STANDARDS sqt on qti.STANDARD_ID = sqt.id "
                    +" left join COMMERCE.TBL_TOOLS tqt on qti.TOOL_ID = tqt.id "
                    +"  where  qt.ID = ?1 "            , nativeQuery = true)
    public List<Object> findAllQcfQuantityItem(Long qtId);

    @Query(value = // abas
            " select "
                    +" qq.id ," //0
                    +" to_char(qq.NAME) qualityName, " //1
                    +" to_char(qq.XCOMMENT) qualityXcomment, " //2
                    +" to_char(qqi.XCOMMENT) qualityItemName, "//3
                    +" to_char(sqq.name) qualitySandard, " //4
                    +" to_char(tqq.XNAME) qualityTools " //5
                    +" from COMMERCE.TBL_QCF_QUALITY_ITEMES qqi "
                    +" inner join COMMERCE.TBL_QCF_QUALITY qq on qqi.QCF_QUALITY_ID = qq.id "
//                    +" left join COMMERCE.TBL_QCF_QUALITY_VALUES qqv on qqv.QCF_QUALITY_ITEM_ID = qqi.id "
                    +" left join COMMERCE.TBL_STANDARDS sqq on qqi.STANDARD_ID = sqq.id "
                    +" left join COMMERCE.TBL_TOOLS tqq on qqi.TOOL_ID = tqq.id "
                    +"  where qq.id= ?1 "            , nativeQuery = true)
    public List<Object> findAllQcfQualityItem(Long qtId);

    @Query(value = // abas
        " select "
            +" c.id ," //0
            +" to_char(c.name) certificateName," //1
            +" to_char(c.xcomment) certificateXcomment, " //12
            +" to_char(ci.name) certificateItemName, " //3
            +" to_char(sc.name) certificateStandard " //4
            +" from COMMERCE.TBL_QCF_CERTIFICATE_ITEMS ci "
            +" inner join COMMERCE.TBL_QCF_CERTIFICATE c on ci.QCF_CERTIFICATE_ID = c.id "
            +" left join COMMERCE.TBL_STANDARDS sc on ci.STANDARD_ID = sc.id "
            +"  where c.ID= ?1  ", nativeQuery = true)
    public List<Object> findAllQcfCertificateItem(Long qtId);


}
