package com.emp.bpms.repository.user;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@SuppressWarnings("serial")
@Accessors(chain = true)
public class UserDTO implements UserDetails {

	private long id;
	private String emp_num;
	private String pwd;
	private String name;
	private int team_id;
	private String mobile;
	private int authority_id;
	private String email;
	private boolean enable;
	private int type;
    
	public UserDTO() {
		    
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
		switch(this.authority_id) {
			case 1: 
				auth.add(new SimpleGrantedAuthority("SKTUSER"));
				auth.add(new SimpleGrantedAuthority("BPUSER"));
				break;
			case 2: 
				auth.add(new SimpleGrantedAuthority("BPUSER"));
				break;
			case 3: 
				auth.add(new SimpleGrantedAuthority("BPUSER"));
				auth.add(new SimpleGrantedAuthority("SKTUSER"));
				auth.add(new SimpleGrantedAuthority("SKTADMIN"));
				break;
			case 4: 
				auth.add(new SimpleGrantedAuthority("BPUSER"));
				auth.add(new SimpleGrantedAuthority("SKTUSER"));
				auth.add(new SimpleGrantedAuthority("SKTADMIN"));
				auth.add(new SimpleGrantedAuthority("ADMIN"));
				break;
			case 5: 
				auth.add(new SimpleGrantedAuthority("BPUSER"));
				auth.add(new SimpleGrantedAuthority("SKTUSER"));
				auth.add(new SimpleGrantedAuthority("SKTADMIN"));
				auth.add(new SimpleGrantedAuthority("ADMIN"));
				auth.add(new SimpleGrantedAuthority("SPECIAL"));
				break;
			default :
				break;
		}
        return auth;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return pwd;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enable;
	}
  
    


}
