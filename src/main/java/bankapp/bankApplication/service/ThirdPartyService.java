package bankapp.bankApplication.service;
import bankapp.bankApplication.model.Admin;
import bankapp.bankApplication.model.ThirdParty;
import bankapp.bankApplication.repository.AdminRepository;
import bankapp.bankApplication.repository.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThirdPartyService {

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    public List<ThirdParty> getAll() { return thirdPartyRepository.findAll(); }

    public Optional<ThirdParty> getById(Long id){ return thirdPartyRepository.findById(id); }
}