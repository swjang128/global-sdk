package io.snplab.gsdk.keyword.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
	List<Keyword> findByIsActivatedAndServiceId(boolean isActivated, String serviceId);

	List<Keyword> findByServiceIdAndIsActivated(String serviceId, boolean isActivated);

	List<Keyword> findByServiceIdAndUpdatedAtGreaterThan(String serviceId, LocalDateTime updatedAt);
}
