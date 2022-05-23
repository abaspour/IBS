package com.nicico.ibs.service;

import com.nicico.copper.common.domain.criteria.NICICOCriteria;
import com.nicico.copper.common.domain.criteria.SearchUtil;
import com.nicico.copper.common.dto.grid.TotalResponse;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.ibs.IBSException;
import com.nicico.ibs.dto.ContractDTO;
import com.nicico.ibs.iservice.IContractService;
import com.nicico.ibs.model.Contract;
import com.nicico.ibs.repository.ContractDAO;
import com.nicico.ibs.repository.InquiryDAO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ContractService implements IContractService {

    private final ContractDAO contractDAO;
    private final InquiryDAO inquiryDAo;
    private final ModelMapper modelMapper;
    @Value("${ibs.upload.dir}")
    private String uploadDir;

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public ContractDTO.Info get(Long id) {
        final Contract contract = contractDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.ContractNotFound));

        return modelMapper.map(contract, ContractDTO.Info.class);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public List<ContractDTO.Info> list() {
        final List<Contract> countries = contractDAO.findAll();

        return modelMapper.map(countries, new TypeToken<List<ContractDTO.Info>>() {
        }.getType());
    }

    //	@PreAuthorize("hasAuthority('C_COUNTRY')")
    @Transactional
    @Override
    public ContractDTO.Info create(ContractDTO.Create request) {
        final Contract contract = modelMapper.map(request, Contract.class);

        return save(contract);
    }

    //	@PreAuthorize("hasAuthority('U_COUNTRY')")
    @Transactional
    @Override
    public ContractDTO.Info update(Long id,Long supplierId) {
        final Contract contract = contractDAO.findById(id)
                .orElseThrow(() -> new IBSException(IBSException.ErrorType.ContractNotFound));
        if (contract.getSupplierId().compareTo(supplierId)!=0)
            throw new  IBSException(IBSException.ErrorType.ContractNotFound);
        if (contract.getStatus().equalsIgnoreCase("n")) {
            contract.setStatus("v");
            return save(contract);
        }
        return modelMapper.map(contract, ContractDTO.Info.class);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(Long id) {
        contractDAO.deleteById(id);
    }

    //	@PreAuthorize("hasAuthority('D_COUNTRY')")
    @Transactional
    @Override
    public void delete(ContractDTO.Delete request) {
        final List<Contract> countries = contractDAO.findAllById(request.getIds());

        contractDAO.deleteAll(countries);
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public SearchDTO.SearchRs<ContractDTO.Info> search(SearchDTO.SearchRq request) {
        return SearchUtil.search(contractDAO, request, contract -> modelMapper.map(contract, ContractDTO.Info.class));
    }

    //	@PreAuthorize("hasAuthority('R_COUNTRY')")
    @Transactional(readOnly = true)
    @Override
    public TotalResponse<ContractDTO.Info> search(NICICOCriteria criteria) {
//        Boolean attach = criteria.getCriteria().toString().contains("id");
//        int i = 0;
//        List<String> ids = new ArrayList<>();
//        List<String> fns = new ArrayList<>();
//        List<String> dsc = new ArrayList<>();
        return SearchUtil.search(contractDAO, criteria, contract -> {
                    ContractDTO.Info r = modelMapper.map(contract, ContractDTO.Info.class);
//                    if (attach) {
//                        String[] u = contractDAO.findInquiryTypeAndSpareById(contract.getInquiryNumber()).split("@@@@##@@");
//                        if (u != null && u[0] != null)
//                            r.setInquiryType(u[0]);
//                        if (u != null && u[1] != null)
//                            r.setSpareProductTime(u[1]);
//                    }
                    return r;
                }
        );
    }

    private ContractDTO.Info save(Contract contract) {
        final Contract saved = contractDAO.saveAndFlush(contract);
        return modelMapper.map(saved, ContractDTO.Info.class);
    }

    public String getDownloadFileName(String data,Long userId){
          return contractDAO.findAllByIdAndSupplierId(new Long(data),userId).getFileNewName();
    }
}
