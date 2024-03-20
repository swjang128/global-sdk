package io.snplab.gsdk.keyword.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
	List<Keyword> findByIsActivatedAndServiceId(boolean isActivated, Long serviceId);

	List<Keyword> findByServiceIdAndIsActivated(Long serviceId, boolean isActivated);

	List<Keyword> findByServiceIdAndUpdatedAtGreaterThan(Long serviceId, LocalDateTime updatedAt);
}
