package spittr.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import spittr.model.Spitter;
import spittr.repository.IRepository.SpitterRepository;

/**
 * 自定义用户存储服务类
 *
 * @author wq
 * repository：2016-12-12
 *
 */
public class SpitterUserService implements UserDetailsService{

	private SpitterRepository spitterRepository;

	public SpitterUserService(SpitterRepository spitterRepository) {
		this.spitterRepository = spitterRepository;
	}

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Spitter spitter = spitterRepository.findByUsername("user");
		if(spitter!= null)
		{
			List<GrantedAuthority> authorities = 
				new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_Admin"));
			
			return new User(
					spitter.getUsername(),
					spitter.getPassword(),
					authorities);
		}
		throw new UsernameNotFoundException("用户： '"+username + "' 未找到");
	}

}
