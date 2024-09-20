package bankapp.bankApplication.service;

import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.Address;
import bankapp.bankApplication.repository.AddressRepository;
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

    public Address create(Address address, String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            return addressRepository.save(address);
        }else{
            throw new UnauthorizedException("Only ADMIN users can create address.");
        }
    }
    public Optional<Address> change(Address address, String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            if (addressRepository.existsById(address.getId())) {
                return Optional.of(addressRepository.save(address));
            }
            return Optional.empty();
        }else{
            throw new UnauthorizedException("Only ADMIN users can change address.");
        }
    }

    public boolean delete(Long id, String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            if(addressRepository.existsById(id)){
                addressRepository.deleteById(id);
                return true;
            }else{
                return false;
            }
        }else{
            throw new UnauthorizedException("Only ADMIN users can delete address.");
        }
    }
}
