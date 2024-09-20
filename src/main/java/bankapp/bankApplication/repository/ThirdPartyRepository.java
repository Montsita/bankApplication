package bankapp.bankApplication.repository;


//import bankapp.bankApplication.model.ThirdParty;
import bankapp.bankApplication.model.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long>  {
}
