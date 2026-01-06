package br.com.WiseMonitor.servico;

import org.springframework.stereotype.Service;

import br.com.WiseMonitor.modelos.LoginRequest;
import br.com.WiseMonitor.modelos.User;
import br.com.WiseMonitor.modelos.UserDto;
import br.com.WiseMonitor.modelos.UserResponseDto;
import br.com.WiseMonitor.modelos.UserRole;

@Service
public class UserMapper {

    public UserResponseDto userToUserResponseDto(User u){

        if(u == null){

            throw new NullPointerException("User não pode ser nulo");

        }
        
        

        return new UserResponseDto(u.getLogin(),u.getEmail(),u.getRole(),u.getDispositivos());

    }

    public User dtoToUser(UserDto uDto){

        if(uDto == null){

            throw new NullPointerException("UserDto não pode ser nulo");

        }

        return new User(0L,uDto.login(),uDto.senha(),uDto.cpf(),uDto.email(),uDto.role());

        

    }


}
