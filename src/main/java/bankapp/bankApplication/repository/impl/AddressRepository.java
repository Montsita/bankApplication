package bankapp.bankApplication.repository.impl;

import bankapp.bankApplication.model.contacts.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}