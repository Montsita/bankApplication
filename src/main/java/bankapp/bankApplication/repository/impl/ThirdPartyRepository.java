package bankapp.bankApplication.repository.impl;


//import bankapp.bankApplication.model.ThirdParty;
import bankapp.bankApplication.model.users.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long>  {
}
