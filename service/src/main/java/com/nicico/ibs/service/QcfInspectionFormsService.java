package com.nicico.ibs.service;


import com.nicico.ibs.iservice.IQcfInspectionFormsService;
import com.nicico.ibs.model.ReportTransmiters;
import com.nicico.ibs.repository.RegisterPriceDAO;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class QcfInspectionFormsService implements IQcfInspectionFormsService {

    private final RegisterPriceDAO registerPriceDAO;

    public ReportTransmiters[] findAllQcf(Long reqItemId){
        ReportTransmiters[] reportTransmiters = null;
        List<Object> list = registerPriceDAO.findAllQcf(reqItemId);
        ArrayList<ReportTransmiters> arrayList = new ArrayList<ReportTransmiters>();
        try {
            Object[] row;
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    row = (Object[]) list.get(i);
                    ReportTransmiters reportTransmiters2 = new ReportTransmiters();
                    ;
                    if (row != null) {
                        if (row[0] != null)
                            reportTransmiters2.setQcfId(new Long (row[0].toString()));

                        if (row[1] != null)
                            reportTransmiters2.setLongDescP(row[1].toString());

                        if (row[2] != null)
                            reportTransmiters2.setLongDescL(row[2].toString());

                        if (row[3] != null)
                            reportTransmiters2.setDescP(row[3].toString());

                        if (row[4] != null)
                            reportTransmiters2.setDescL(row[4].toString());

                        if (row[5] != null)
                            reportTransmiters2.setMaterialCode(row[5].toString());

                        if (row[6] != null)
                            reportTransmiters2.setRequestitemrow(row[6].toString());

                        if (row[7] != null)
                            reportTransmiters2.setItemamount(row[7].toString());

                        if (row[8] != null)
                            reportTransmiters2.setReqNumber(row[8].toString());

                        if (row[9] != null )
                            reportTransmiters2.setMapNumber(row[9].toString()+(row[10]!=null ? "-Rev "+row[10].toString() : "") );

                        if (row[11] != null )
                            reportTransmiters2.setExpertId(new Long(row[11].toString()));

                        if (row[12] != null )
                            reportTransmiters2.setApproverId(new Long(row[12].toString()));

                        if (row[13] != null )
                            reportTransmiters2.setResponsibleId(new Long(row[13].toString()));

                        if (row[14] != null )
                            reportTransmiters2.setName_fa((row[14].toString()));

                        if (row[15] != null )
                            reportTransmiters2.setEngDeliveryBase((row[15].toString()));

                        arrayList.add(reportTransmiters2);
                    }
                }
            }
            reportTransmiters = (ReportTransmiters[]) arrayList.toArray(new ReportTransmiters[arrayList.size()]);

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return reportTransmiters;
    }
    public ReportTransmiters[] findAllQcfInspection(Long qcfId) {
        ReportTransmiters[] reportTransmiters = null;
        List<Object> list = registerPriceDAO.findAllQcfInspection(qcfId);
        ArrayList<ReportTransmiters> arrayList = new ArrayList<ReportTransmiters>();
        try {
            Object[] row;
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    row = (Object[]) list.get(i);
                    ReportTransmiters reportTransmiters2 = new ReportTransmiters();
                    ;
                    if (row != null) {
                        if (row[0] != null)
                            reportTransmiters2.setInspectionId(new Long (row[0].toString()));

                        if (row[1] != null)
                            reportTransmiters2.setInspectionName(row[1].toString());

                        if (row[2] != null)
                            reportTransmiters2.setInspectionXcomment(row[2].toString());

                        arrayList.add(reportTransmiters2);
                    }
                }
            }
            reportTransmiters = (ReportTransmiters[]) arrayList.toArray(new ReportTransmiters[arrayList.size()]);

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return reportTransmiters;
    }
    public ReportTransmiters[] findAllQcCertificate( Long qcfId){
        List<Object> list = registerPriceDAO.findAllQcCertificate( qcfId);
        ReportTransmiters[] reportTransmiters = null;
        ArrayList<ReportTransmiters> arrayList = new ArrayList<ReportTransmiters>();
        try {
            Object[] row;
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    row = (Object[]) list.get(i);
                    ReportTransmiters reportTransmiters2 = new ReportTransmiters();
                    ;
                    if (row != null) {
                        if (row[0] != null)
                            reportTransmiters2.setCertificateId(new Long (row[0].toString()));

                        if (row[1] != null)
                            reportTransmiters2.setCertificateName(row[1].toString());

                        if (row[2] != null)
                            reportTransmiters2.setCertificateXcomment(row[2].toString());

                        arrayList.add(reportTransmiters2);
                    }
                }
            }
            reportTransmiters = (ReportTransmiters[]) arrayList.toArray(new ReportTransmiters[arrayList.size()]);

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return reportTransmiters;
    }
    public ReportTransmiters[] findAllQcfQuality(Long qcfId){
        ReportTransmiters[] reportTransmiters = null;
        List<Object> list = registerPriceDAO.findAllQcfQuality(qcfId);
        ArrayList<ReportTransmiters> arrayList = new ArrayList<ReportTransmiters>();
        try {
            Object[] row;
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    row = (Object[]) list.get(i);
                    ReportTransmiters reportTransmiters2 = new ReportTransmiters();
                    ;
                    if (row != null) {
                        if (row[0] != null)
                            reportTransmiters2.setQualityId(new Long (row[0].toString()));

                        if (row[1] != null)
                            reportTransmiters2.setQualityName(row[1].toString());

                        if (row[2] != null)
                            reportTransmiters2.setQualityXcomment(row[2].toString());

                        arrayList.add(reportTransmiters2);
                    }
                }
            }
            reportTransmiters = (ReportTransmiters[]) arrayList.toArray(new ReportTransmiters[arrayList.size()]);

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return reportTransmiters;
    }
    public ReportTransmiters[] findAllQcfQuantity(Long qcfId) {
        ReportTransmiters[] reportTransmiters = null;
        List<Object> list = registerPriceDAO.findAllQcfQuantity(qcfId);
        ArrayList<ReportTransmiters> arrayList = new ArrayList<ReportTransmiters>();
        try {
            Object[] row;
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    row = (Object[]) list.get(i);
                    ReportTransmiters reportTransmiters2 = new ReportTransmiters();
                    ;
                    if (row != null) {
                        if (row[0] != null)
                            reportTransmiters2.setQuantityId(new Long(row[0].toString()));
                        if (row[1] != null)
                            reportTransmiters2.setQuantityName(row[1].toString());

                        if (row[2] != null)
                            reportTransmiters2.setQuantityXcomment(row[2].toString());

                        arrayList.add(reportTransmiters2);
                    }
                }
            }
            reportTransmiters = (ReportTransmiters[]) arrayList.toArray(new ReportTransmiters[arrayList.size()]);

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return reportTransmiters;
    }
    public ReportTransmiters[] findAllQcfQuantityItem(Long qtId){
        ReportTransmiters[] reportTransmiters = null;
        List<Object> list = registerPriceDAO.findAllQcfQuantityItem(qtId);
        ArrayList<ReportTransmiters> arrayList = new ArrayList<ReportTransmiters>();
        try {
            Object[] row;
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    row = (Object[]) list.get(i);
                    ReportTransmiters reportTransmiters2 = new ReportTransmiters();
                    ;
                    if (row != null) {
                        if (row[0] != null)
                            reportTransmiters2.setQuantityId(new Long(row[0].toString()));
                        if (row[1] != null)
                            reportTransmiters2.setQuantityName(row[1].toString());

                        if (row[2] != null)
                            reportTransmiters2.setQuantityXcomment(row[2].toString());

                        if (row[3] != null)
                            reportTransmiters2.setQuantityParameter(row[3].toString());

                        if (row[4] != null)
                            reportTransmiters2.setMinValue(row[4].toString());

                        if (row[5] != null)
                            reportTransmiters2.setMaxValue(row[5].toString());

                        if (row[6] != null)
                            reportTransmiters2.setQuantitySandard(row[6].toString());

                        if (row[7] != null)
                            reportTransmiters2.setQuantityTools(row[7].toString());

//                        if (row[8] != null)
//                            reportTransmiters2.setMinValue_v(row[8].toString());
//
//                        if (row[9] != null)
//                            reportTransmiters2.setMaxValue_v(row[9].toString());

                        arrayList.add(reportTransmiters2);
                    }
                }
            }
            reportTransmiters = (ReportTransmiters[]) arrayList.toArray(new ReportTransmiters[arrayList.size()]);

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return reportTransmiters;
    }
    public ReportTransmiters[] findAllQcfQualityItem( Long qtId) {
        ReportTransmiters[] reportTransmiters = null;
        List<Object> list = registerPriceDAO.findAllQcfQualityItem(qtId);
        ArrayList<ReportTransmiters> arrayList = new ArrayList<ReportTransmiters>();
        try {
            Object[] row;
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    row = (Object[]) list.get(i);
                    ReportTransmiters reportTransmiters2 = new ReportTransmiters();
                    ;
                    if (row != null) {
                        if (row[0] != null)
                            reportTransmiters2.setQualityId(new Long(row[0].toString()));
                        if (row[1] != null)
                            reportTransmiters2.setQualityName(row[1].toString());

                        if (row[2] != null)
                            reportTransmiters2.setQualityXcomment(row[2].toString());

                        if (row[3] != null)
                            reportTransmiters2.setQualityItemName(row[3].toString());

                        if (row[4] != null)
                            reportTransmiters2.setQualitySandard(row[4].toString());

                        if (row[5] != null)
                            reportTransmiters2.setQualityTools(row[5].toString());

                        arrayList.add(reportTransmiters2);
                    }
                }
            }
            reportTransmiters = (ReportTransmiters[]) arrayList.toArray(new ReportTransmiters[arrayList.size()]);

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return reportTransmiters;
    }
    public ReportTransmiters[] findAllQcfCertificateItem(Long qtId) {
        ReportTransmiters[] reportTransmiters = null;
        List<Object> list = registerPriceDAO.findAllQcfCertificateItem(qtId);
        ArrayList<ReportTransmiters> arrayList = new ArrayList<ReportTransmiters>();
        try {
            Object[] row;
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    row = (Object[]) list.get(i);
                    ReportTransmiters reportTransmiters2 = new ReportTransmiters();
                    ;
                    if (row != null) {
                        if (row[0] != null)
                            reportTransmiters2.setCertificateId(new Long (row[0].toString()));

                        if (row[1] != null)
                            reportTransmiters2.setCertificateName(row[1].toString());

                        if (row[2] != null)
                            reportTransmiters2.setCertificateXcomment(row[2].toString());

                        if (row[3] != null)
                            reportTransmiters2.setCertificateItemName(row[3].toString());

                        if (row[4] != null)
                            reportTransmiters2.setCertificateStandard(row[4].toString());

                        arrayList.add(reportTransmiters2);
                    }
                }
            }
            reportTransmiters = (ReportTransmiters[]) arrayList.toArray(new ReportTransmiters[arrayList.size()]);

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return reportTransmiters;
    }
}
