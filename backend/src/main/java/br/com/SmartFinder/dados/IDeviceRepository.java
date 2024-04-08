package br.com.SmartFinder.dados;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.SmartFinder.modelos.Device;

public interface IDeviceRepository extends JpaRepository<Device,Long>{

	Long countById(Long id);
	
}
