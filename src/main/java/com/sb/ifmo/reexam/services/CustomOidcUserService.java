package com.sb.ifmo.reexam.services;

import com.sb.ifmo.reexam.data.CustomUser;
import com.sb.ifmo.reexam.data.CustomUserRepository;
import com.sb.ifmo.reexam.data.GoogleUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private CustomUserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        try {
            return processOidcUser(userRequest, oidcUser);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        GoogleUserInfo googleUserInfo = new GoogleUserInfo(oidcUser.getAttributes());

        // see what other data from userRequest or oidcUser you need

        CustomUser user = userRepository.findByEmailIs(googleUserInfo.getEmail());
        if (user == null) {
            // First username = email
            user = new CustomUser(googleUserInfo.getEmail(), googleUserInfo.getEmail());
            // set other needed data

            userRepository.save(user);
        }
        return oidcUser;
    }
}
