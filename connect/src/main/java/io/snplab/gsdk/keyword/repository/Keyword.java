package io.snplab.gsdk.keyword.repository;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {
        @Index(name = "KEYWORD_IDX_01", columnList = "updatedAt"),
        @Index(name = "KEYWORD_IDX_02", columnList = "serviceId, updatedAt")
})
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    private Long historyId;

    @Column(length = 36)
    private String serviceId;

    @Column(length = 70)
    private String keyword;

    @Column(columnDefinition = "boolean default true")
    private boolean isActivated;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Keyword(String keyword, Long historyId, String serviceId, boolean isActivated) {
        this.keyword = keyword;
        this.historyId = historyId;
        this.serviceId = serviceId;
        this.isActivated = isActivated;
    }
}
