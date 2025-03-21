package br.com.SmartFinder.dados;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.SmartFinder.modelos.User;


public interface IUserRepository extends JpaRepository<User,Long>{
	
	Long countById(Long id);

	Optional<User> findByLogin(String login);

	boolean existsByLogin(String login);
	
	boolean existsByCpf(String cpf);
	

}
