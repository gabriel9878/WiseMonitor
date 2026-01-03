package br.com.WiseMonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.WiseMonitor.dados.IUserRepository;
import br.com.WiseMonitor.modelos.User;
import br.com.WiseMonitor.modelos.UserRole;

@SpringBootApplication
public class WiseMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(WiseMonitorApplication.class, args);
	}

	@Bean
	CommandLineRunner initDefaultUser(IUserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		return args -> {
			// cria usuário admin padrão se ainda não existir
			String defaultLogin = "admin";
			if (userRepository.findByLogin(defaultLogin).isEmpty()) {
				User user = new User();
				user.setLogin(defaultLogin);
				user.setSenha(passwordEncoder.encode("admin"));
				user.setCpf("00000000000");
				user.setEmail("admin@wisemonitor.local");
				user.setRole(UserRole.ADMIN);
				userRepository.save(user);
			}
		};
	}

}
