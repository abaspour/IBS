package com.nicico.ibs.iservice;

import com.nicico.ibs.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface IUserService  extends UserDetailsService {
	 List<Object[]>  findRolesByUsername(String userName);
	 UserDetails loadUserByUsername(String username) throws UsernameNotFoundException ;
	 User getUser(String username);

}
