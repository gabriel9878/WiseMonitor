package br.com.WiseMonitor.servico;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.WiseMonitor.dados.IUserRepository;

@Service
public class userDetailsService implements UserDetailsService {

	private IUserRepository iUserRepository;
	
	public userDetailsService(IUserRepository iUserRepository) {
		
		
		this.iUserRepository = iUserRepository;
	
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
			return (UserDetails) this.iUserRepository.findByLogin(username).orElse(null);
		
	}

	
}
