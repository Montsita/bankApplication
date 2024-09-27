package bankapp.bankApplication.controller.impl;

import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.users.ThirdParty;
import bankapp.bankApplication.service.impl.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/thirdParty")
public class ThirdPartyController {
    @Autowired
    private ThirdPartyService thirdPartyService;

    @GetMapping
    public List<ThirdParty> getAll(){ return thirdPartyService.getAll();}

    @GetMapping("/{id}")
    public ResponseEntity<ThirdParty> getById(@PathVariable Long id){
        Optional<ThirdParty> thirdParty = thirdPartyService.getById(id);
        return thirdParty.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ThirdParty create(@RequestBody ThirdParty thirdParty , @RequestParam String userRegistrationUserName) {
        try {
            return thirdPartyService.create(thirdParty,userRegistrationUserName);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/change")
    public ResponseEntity<ThirdParty> change (@RequestBody ThirdParty thirdParty) {
        try{
            Optional<ThirdParty> change = thirdPartyService.change(thirdParty);
            return change.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws UnauthorizedException {
        try {
            if (thirdPartyService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
