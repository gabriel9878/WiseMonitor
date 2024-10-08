package br.com.SmartFinder.servico;




import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.SmartFinder.modelos.User;
import br.com.SmartFinder.modelos.UserRequestDto;
import br.com.SmartFinder.modelos.LoginRequest;

 

public class UserMapperTest {

    
    private UserMapper mapper;
    
    @BeforeEach
    void setUp(){

        mapper = new UserMapper();

    }

    @Test
    public void testUserDtoToUser(){

        UserRequestDto uDto =  new UserRequestDto("teste","senhateste","cpfteste","emailteste@email.teste");

        User u = this.mapper.dtoToUser(uDto);

        assertEquals(u.getLogin(), uDto.login());
        assertEquals(u.getCpf(), uDto.cpf());
        assertEquals(u.getEmail(), uDto.email());

    }

    @Test
    public void testThrowNullPointerExceptionWhenUserDtoToUserNull(){


        var exc = assertThrows(NullPointerException.class,() -> this.mapper.dtoToUser(null));
        assertEquals("UserDto is null",exc.getMessage());

    }

    @Test
    public void testUserToDtoResponse(){

        User u = new User(0L,"teste","senhateste","cpfteste","emailteste@email.teste");
        LoginRequest uR = this.mapper.userToDtoResponse(u);

        assertEquals(u.getLogin(),uR.login());
        assertEquals(u.getEmail(),uR.senha());

    }

    @Test
    public void testThrowNullPointerExceptionWhenUserToDtoResponseNull(){


        var exc = assertThrows(NullPointerException.class,() -> this.mapper.userToDtoResponse(null));
        assertEquals("User is null",exc.getMessage());
        

    }

}