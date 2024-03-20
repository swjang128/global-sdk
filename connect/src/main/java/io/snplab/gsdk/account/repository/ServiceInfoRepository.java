package io.snplab.gsdk.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ServiceInfoRepository extends JpaRepository<ServiceInfo, Long> {
    Optional<ServiceInfo> findByCompanyIdAndName(Long companyId, String name);
}
