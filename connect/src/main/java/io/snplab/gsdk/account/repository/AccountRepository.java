package io.snplab.gsdk.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    @Query(value = "SELECT email, first_name AS firstName , last_name AS lastName, phone_number AS phoneNumber, c.name AS companyName, si.name AS serviceName " +
            "FROM account a " +
            "LEFT JOIN company c ON c.id = a.company_id " +
            "LEFT JOIN account_role ar ON ar.account_id = a.id " +
            "LEFT JOIN service_info si ON si.id = ar.service_id " +
            "WHERE a.id = ?1 ", nativeQuery = true)
    Optional<AccountProjection> findAccountInfoById(Long id);

    @Query(value = "SELECT email, first_name AS firstName, last_name AS lastName, phone_number AS phoneNumber, c.name AS companyName, si.name AS serviceName " +
            "FROM account a " +
            "LEFT JOIN company c ON c.id = a.company_id " +
            "LEFT JOIN account_role ar ON ar.account_id = a.id " +
            "LEFT JOIN service_info si ON si.id = ar.service_id ", nativeQuery = true)
    List<AccountProjection> findAccountInfoAll();
}
