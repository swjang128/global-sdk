package io.snplab.gsdk.account.repository;

import io.snplab.gsdk.account.domain.AccountRoles;
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
        @Index(name = "ACCOUNT_ROLE_IDX_01", columnList = "accountId")
})
public class AccountRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long accountId;

    @Column(length = 36, nullable = false)
    private String serviceId;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private AccountRoles roleName;

    @Column(columnDefinition = "boolean default true")
    private boolean isActivated;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static AccountRole of(Long accountId, String serviceId) {
        return AccountRole.builder()
                .accountId(accountId)
                .serviceId(serviceId)
                .roleName(AccountRoles.ADMIN)
                .isActivated(true)
                .build();
    }
}
