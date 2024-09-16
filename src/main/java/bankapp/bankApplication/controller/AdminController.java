package bankapp.bankApplication.controller;

import bankapp.bankApplication.model.Admin;
import bankapp.bankApplication.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public List<Admin> getAll(){ return adminService.getAll();}

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getById(@PathVariable Long id){
        Optional<Admin> admin = adminService.getById(id);
        return admin.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }
}
