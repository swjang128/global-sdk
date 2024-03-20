package io.snplab.gsdk.keyword.repository;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
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
@Table(uniqueConstraints = @UniqueConstraint(name = "KEYWORD_HISTORY_UDX_01", columnNames = {"fileUniqueId"}),
        indexes = {@Index(name = "KEYWORD_HISTORY_IDX_01", columnList = "serviceId")
})
public class KeywordHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    private Long serviceId;

    private Long accountId;

    @Column(length = 50)
    private String fileName;

    @Column(length = 100)
    private String filePath;

    @Column(length = 5)
    private String fileExtension;

    @Column(length = 15)
    private String fileUniqueId;

    @CreatedDate
    private LocalDateTime createdAt;
}
