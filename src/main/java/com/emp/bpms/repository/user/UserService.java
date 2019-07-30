package com.emp.bpms.repository.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserDAO userDAO;
	
	boolean enabled = true;
    boolean accountNonExpired = true;
    boolean credentialsNonExpired = true;
    boolean accountNonLocked = true;
    
    public UserDTO loadLoginUserDTO() {
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String email = principal.toString();
    	UserDTO userDTO = userDAO.selectLoginUser(email);
    	
        return userDTO;
    }

    public boolean matchesPassword(String password1, String password2) {
        if(password1.equals(password2))
        	return true;
        else
        	return false;
    }

    public void updateUserLastLoginTime(String email) {
    	UserDTO result = new UserDTO();
        result = userDAO.selectLoginUser(email);
        userDAO.updateUser(result);        
    }

	@Override
	public UserDTO loadUserByUsername(String email) throws UsernameNotFoundException {
		UserDTO userDTO = userDAO.selectLoginUser(email);
        if(userDTO==null) {
            throw new UsernameNotFoundException("?ù¥Î©îÏùº?ù¥ Ï°¥Ïû¨?ïòÏß? ?ïä?äµ?ãà?ã§.");
        }
        return userDTO;
    }
    
}
