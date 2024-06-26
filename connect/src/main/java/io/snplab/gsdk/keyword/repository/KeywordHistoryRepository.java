package io.snplab.gsdk.keyword.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordHistoryRepository extends JpaRepository<KeywordHistory, Long> {

    Optional<KeywordHistory> findTopByServiceIdOrderByCreatedAtDesc(String serviceId);
}
