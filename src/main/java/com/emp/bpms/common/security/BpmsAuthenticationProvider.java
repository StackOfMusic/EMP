package com.emp.bpms.common.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.emp.bpms.repository.user.UserDTO;
import com.emp.bpms.repository.user.UserService;

@Component
public class BpmsAuthenticationProvider implements AuthenticationProvider {
    
	@Autowired
    private UserService userService;

    @SuppressWarnings("unchecked")
	@Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDTO userDTO = userService.loadUserByUsername(loginId);
 
        if (userService.matchesPassword(password, userDTO.getPwd())) {
            userService.updateUserLastLoginTime(loginId);
            List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
            roles = (ArrayList<GrantedAuthority>) userDTO.getAuthorities();
            return new UsernamePasswordAuthenticationToken(loginId, password, roles);
            
        } else {
            throw new BadCredentialsException("ÎπÑÎ?Î≤àÌò∏Í∞? ?ùºÏπòÌïòÏß? ?ïä?äµ?ãà?ã§.");
        }
    }
   

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
