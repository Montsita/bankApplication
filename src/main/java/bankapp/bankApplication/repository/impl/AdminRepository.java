package bankapp.bankApplication.repository.impl;

import bankapp.bankApplication.model.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>  {
}
