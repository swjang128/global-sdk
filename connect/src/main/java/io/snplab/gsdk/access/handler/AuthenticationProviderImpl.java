package io.snplab.gsdk.access.handler;

import io.snplab.gsdk.access.service.UserDetailsServiceImpl;
import io.snplab.gsdk.common.service.AES256;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationProviderImpl implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AES256 aes256;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null) {
            throw new InternalAuthenticationServiceException("Authentication is null.");
        }

        if (authentication.getCredentials() == null) {
            throw new AuthenticationCredentialsNotFoundException("Credentials is null");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(aes256.encrypt(authentication.getName()));

        if (!passwordEncoder.matches(authentication.getCredentials() + "", userDetails.getPassword())) {
            throw new BadCredentialsException("Password does not match stored value");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
