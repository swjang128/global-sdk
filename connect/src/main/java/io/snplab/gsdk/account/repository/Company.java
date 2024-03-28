package io.snplab.gsdk.account.repository;

import io.snplab.gsdk.account.dto.AccountCreateDto;
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
        @Index(name = "COMPANY_IDX_01", columnList = "name")
})
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(length = 40)
    private String name;

    @Column(length = 30)
    private String country;

    @Column(columnDefinition = "boolean default true")
    private boolean isActivated;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Company of(AccountCreateDto accountCreateDto) {
        return Company.builder()
                .name(accountCreateDto.getCompanyName())
                .country(accountCreateDto.getCountry())
                .isActivated(true)
                .build();
    }
}
