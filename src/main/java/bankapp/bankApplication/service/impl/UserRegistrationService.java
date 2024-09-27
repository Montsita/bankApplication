package bankapp.bankApplication.service.impl;


import bankapp.bankApplication.enums.UserType;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.registrations.Role;
import bankapp.bankApplication.model.registrations.UserRegistration;
import bankapp.bankApplication.repository.impl.RoleRepository;
import bankapp.bankApplication.repository.impl.UserRegistrationRepository;
import bankapp.bankApplication.service.interfaces.UserRegistrationServiceInterface;
import jakarta.persistence.MappedSuperclass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationService  implements UserRegistrationServiceInterface, UserDetailsService {
    @Autowired
    UserRegistrationRepository userRegistrationRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserRegistration> getAll() { return userRegistrationRepository.findAll(); }

    public Optional<UserRegistration> getById(Long id){ return userRegistrationRepository.findById(id);}

    public Optional<UserRegistration> getRegistrationByUserName(String userName){
        return userRegistrationRepository.findByUsername(userName);
    }

    public UserRegistration create(UserRegistration userRegistration) throws UnauthorizedException {
        return userRegistrationRepository.save(userRegistration);
    }

    public Long getIdUserByUserName(String userName){
       Optional<UserRegistration>  user = userRegistrationRepository.findByUsername(userName);
       return user.map(userRegistration -> userRegistration.getUser().getId()).orElse(null);
    }
    public String getFullNameByUserName(String userName){
        Optional<UserRegistration> user = userRegistrationRepository.findByUsername(userName);
        return user.map(userRegistration -> userRegistration.getUser().getName()).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve user with the given username
        Optional<UserRegistration> user = userRegistrationRepository.findByUsername(username);
        // Check if user exists

        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
            // Create a collection of SimpleGrantedAuthority objects from the user's roles
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.get().getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            // Return the user details, including the username, password, and authorities
            return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), authorities);
        }
    }

    /**
     * Saves a new user to the database
     *
     * @param user the user to be saved
     * @return the saved user
     */
    @Override
    public UserRegistration saveUser(UserRegistration user) {
        log.info("Saving new user {} to the database", user.getName());
        // Encode the user's password for security before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRegistrationRepository.save(user);
    }

    /**
     * Saves a new role to the database
     *
     * @param role the role to be saved
     * @return the saved role
     */
    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    /**
     * Adds a role to the user with the given username
     *
     * @param username the username of the user to add the role to
     * @param roleName the name of the role to be added
     */
    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);

        // Retrieve the user and role objects from the repository
        Optional<UserRegistration> user = userRegistrationRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);

        // Add the role to the user's role collection
        user.get().getRoles().add(role);

        // Save the user to persist the changes
        userRegistrationRepository.save(user.get());
    }

    @Override
    public Optional<UserRegistration> getUser(String username) {
        log.info("Fetching user {}", username);
        return userRegistrationRepository.findByUsername(username);
    }

    /**
     * Retrieves all users from the database
     *
     * @return a list of all users
     */
    @Override
    public List<UserRegistration> getUsers() {
        log.info("Fetching all users");
        return userRegistrationRepository.findAll();
    }


}
