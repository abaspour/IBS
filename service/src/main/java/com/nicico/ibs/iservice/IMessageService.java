package com.nicico.ibs.iservice;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.dto.MessageDTO;
import com.nicico.ibs.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IMessageService {

	MessageDTO.Info get(Long id);

	List<MessageDTO.Info> list();

	MessageDTO.Info create(MessageDTO.Create request, MultipartFile file, User supplier) throws IOException;

	MessageDTO.Info update(Long id, Map request , Long supplierId) ;

	void delete(Long id);

	void delete(MessageDTO.Delete request);

	SearchDTO.SearchRs<MessageDTO.Info> search(SearchDTO.SearchRq request);

	TotalResponse<MessageDTO.Info> search(NICICOCriteria criteria);

	String getDownloadFileName(String data, Long userId);
}
