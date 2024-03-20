package io.snplab.gsdk.upload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
	List<Keyword> findByIsActivatedAndServiceId(boolean isActivated, Long serviceId);

}
