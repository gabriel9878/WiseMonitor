package br.com.SmartFinder.servico;

import org.springframework.stereotype.Service;

import br.com.SmartFinder.modelos.Device;
import br.com.SmartFinder.modelos.DeviceDto;
import br.com.SmartFinder.modelos.DeviceResponseDto;

@Service
public class DeviceMapper{

    public Device dtoToDevice(DeviceDto dto){

        return new Device(dto.id(),dto.nome());

    }

    public DeviceResponseDto deviceToDtoReponse(Device d){

        return new DeviceResponseDto(d.getNome(),d.getUser().getId());

    }

}