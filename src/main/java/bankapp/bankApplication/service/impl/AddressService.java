package bankapp.bankApplication.service.impl;

import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.contacts.Address;
import bankapp.bankApplication.repository.impl.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRegistrationService userRegistrationService;

    public List<Address> getAll() { return addressRepository.findAll(); }

    public Optional<Address> getById(Long id){
        return addressRepository.findById(id);
    }

    public Address create(Address address) throws UnauthorizedException {
        return addressRepository.save(address);
    }
    public Optional<Address> change(Address address) throws UnauthorizedException {
        if (addressRepository.existsById(address.getId())) {
            return Optional.of(addressRepository.save(address));
        }
        return Optional.empty();
    }

    public boolean delete(Long id) throws UnauthorizedException {
        if(addressRepository.existsById(id)){
            addressRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }
}
