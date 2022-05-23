package com.nicico.ibs.iservice;


import com.nicico.ibs.model.ReportTransmiters;

public interface IQcfInspectionFormsService {

	ReportTransmiters[] findAllQcf(Long reqItemId);
	ReportTransmiters[] findAllQcfInspection(Long qcfId);
	ReportTransmiters[] findAllQcCertificate( Long qcfId);
	ReportTransmiters[] findAllQcfQuality(Long qcfId);
	ReportTransmiters[] findAllQcfQuantity(Long qcfId);
	ReportTransmiters[] findAllQcfQuantityItem(Long qtId);
	ReportTransmiters[] findAllQcfQualityItem( Long qtId);
	ReportTransmiters[] findAllQcfCertificateItem( Long qtId);

}
