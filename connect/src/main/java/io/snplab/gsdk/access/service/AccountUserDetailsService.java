package io.snplab.gsdk.access.service;

import io.snplab.gsdk.account.repository.AccountRepository;
import io.snplab.gsdk.account.repository.AccountRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, InternalAuthenticationServiceException {
        return accountRepository.findByEmail(username)
                .map(account -> User.builder()
                        .username(username)
                        .password(account.getPassword())
                        .roles(accountRoleRepository.findByAccountId(account.getId())
                                .map(accountRole -> accountRole.getRoleName().name())
                                .orElseThrow(() -> new InternalAuthenticationServiceException("UserDetailsService roles error")))
                        .build())
                .orElseThrow(() -> new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation"));
    }
}
