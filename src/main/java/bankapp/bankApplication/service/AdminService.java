package bankapp.bankApplication.service;

import bankapp.bankApplication.model.AccountHolder;
import bankapp.bankApplication.model.Admin;
import bankapp.bankApplication.repository.AccountHolderRepository;
import bankapp.bankApplication.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public List<Admin> getAll() { return adminRepository.findAll(); }

    public Optional<Admin> getById(Long id){ return adminRepository.findById(id); }
}
