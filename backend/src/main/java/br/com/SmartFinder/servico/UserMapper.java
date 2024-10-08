package br.com.SmartFinder.servico;

import org.springframework.stereotype.Service;

import br.com.SmartFinder.modelos.User;
import br.com.SmartFinder.modelos.UserRequestDto;
import br.com.SmartFinder.modelos.LoginRequest;

@Service
public class UserMapper {

    public LoginRequest userToDtoResponse(User u){

        if(u == null){

            throw new NullPointerException("User não pode ser nulo");

        }

        return new LoginRequest(u.getLogin(),u.getEmail());

    }

    public User dtoToUser(UserRequestDto uDto){

        if(uDto == null){

            throw new NullPointerException("UserDto não pode ser nulo");

        }

        return new User(0L,uDto.login(),uDto.senha(),uDto.cpf(),uDto.email());

        

    }


}
