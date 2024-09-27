package bankapp.bankApplication.controller.impl;

import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.users.Admin;
import bankapp.bankApplication.service.impl.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @PutMapping("/change")
    public ResponseEntity<Admin> change (@RequestBody Admin admin) {
        try{
            Optional<Admin> change = adminService.change(admin);
            return change.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @PostMapping("/create")
    public Admin create(@RequestBody Admin admin , @RequestParam String userRegistrationUserName) {
        try {
            return adminService.create(admin,userRegistrationUserName);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws UnauthorizedException {
        try {
            if (adminService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
