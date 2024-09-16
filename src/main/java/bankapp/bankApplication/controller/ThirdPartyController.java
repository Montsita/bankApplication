package bankapp.bankApplication.controller;

import bankapp.bankApplication.model.Admin;
import bankapp.bankApplication.model.ThirdParty;
import bankapp.bankApplication.service.AdminService;
import bankapp.bankApplication.service.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
