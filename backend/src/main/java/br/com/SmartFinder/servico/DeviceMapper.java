package br.com.SmartFinder.servico;

import org.springframework.stereotype.Service;

import br.com.SmartFinder.modelos.Device;
import br.com.SmartFinder.modelos.DeviceRequestDto;


@Service
public class DeviceMapper{

    public Device dtoToDevice(DeviceRequestDto dto){

        return new Device(null,dto.nome());

    }

    public DeviceRequestDto deviceToDeviceRequestDto(Device d){

        return new DeviceRequestDto(d.getNome());

    }

}