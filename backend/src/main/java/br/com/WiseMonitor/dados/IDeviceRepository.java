package br.com.WiseMonitor.dados;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.WiseMonitor.modelos.Device;
import br.com.WiseMonitor.modelos.User;

public interface IDeviceRepository extends JpaRepository<Device,Long>{

	Long countById(Long id);
	Long countByNome(String nome);
	boolean existsByNome(String nome);
	Optional<Device> findByNome(String nome);


}
