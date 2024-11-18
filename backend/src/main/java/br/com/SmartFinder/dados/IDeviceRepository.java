package br.com.SmartFinder.dados;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.SmartFinder.modelos.Device;
import br.com.SmartFinder.modelos.User;

public interface IDeviceRepository extends JpaRepository<Device,Long>{

	Long countById(Long id);
	Long countByNome(String nome);
	boolean existsByNome(String nome);
	Optional<Device> findByNome(String nome);


}
