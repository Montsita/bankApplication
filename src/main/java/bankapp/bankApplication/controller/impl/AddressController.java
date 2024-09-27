package bankapp.bankApplication.controller.impl;

import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.contacts.Address;
import bankapp.bankApplication.service.impl.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping
    public List<Address> getAll(){ return addressService.getAll();}

    @GetMapping("/{id}")
    public ResponseEntity<Address> getById(@PathVariable Long id){
        Optional<Address>  address = addressService.getById(id);
        return address.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }
    @PostMapping("/create")
    public Address create(@RequestBody Address address) {
        try {
            return addressService.create(address);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/change")
    public ResponseEntity<Address> change (@RequestBody Address address) {
        try{
            Optional<Address> change = addressService.change(address);
            return change.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws UnauthorizedException {
        try {
            if (addressService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
