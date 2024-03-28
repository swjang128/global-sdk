package io.snplab.gsdk.account.repository;

import io.snplab.gsdk.account.dto.AccountCreateDto;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
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
        @Index(name = "SERVICE_INFO_IDX_01", columnList = "companyId")
})
public class ServiceInfo {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long companyId;

    @Column(columnDefinition = "boolean default true")
    private boolean isActivated;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static ServiceInfo of(AccountCreateDto accountCreateDto, Long companyId) {
        return ServiceInfo.builder()
                .name(accountCreateDto.getServiceName())
                .companyId(companyId)
                .isActivated(true)
                .build();
    }
}
