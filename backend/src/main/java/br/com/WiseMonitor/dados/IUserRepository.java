package br.com.WiseMonitor.dados;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.WiseMonitor.modelos.User;


public interface IUserRepository extends JpaRepository<User,Long>{
	
	Long countById(Long id);

	Optional<User> findByLogin(String login);
	
	//Caso o cast não funcione substituir por esse método com uma Query correspondente
	//UserDetails findUserDetailsByLogin(String login);

	boolean existsByLogin(String login);
	
	boolean existsByCpf(String cpf);
	
	boolean existsByEmail(String email);
	

}
