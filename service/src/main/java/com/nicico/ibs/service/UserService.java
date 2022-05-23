package com.nicico.ibs.service;

import com.nicico.ibs.iservice.IUserService;
import com.nicico.ibs.model.User;
import com.nicico.ibs.repository.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service(value = "systemUserService")
public class UserService implements IUserService {

	private final UserDAO UserDAO;
	private final PasswordEncoder passwordEncoder;


		private static final  String key1 = "xxxxxxx"; // 128 bit key
		private static final String key2 = "xxxxxx";
		@Override
		public User getUser(String username) {
			User tblUser=UserDAO.findByUsername(username);
//			tblUser.setPassword(passwordEncoder.encode(tblUser.getUsername()));
			return tblUser;
		}

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			try {
				User user;
				user=UserDAO.findByUsername(username);
				if (user!=null)
					user.setPassword(passwordEncoder.encode(user.getUsername()));
//				else {
//					tblUser=userDAO.findByUsername(username);
//					IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));
//
//					SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"),	"AES");
//					Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//					cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//					byte[] original = cipher.doFinal(Base64.decodeBase64(tblUser.getPassword()));
//					tblUser.setPassword( new String(original));
//				}
				return user;
			} catch (Exception e) {
				throw new UsernameNotFoundException(e.getMessage());
			}
		}
		@Override
		public List<Object[]>  findRolesByUsername(String userName)
		{
			return UserDAO.findRolesByUsername(userName);
		}

}
