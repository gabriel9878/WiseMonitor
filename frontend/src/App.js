import { useState } from 'react';
import { useEffect } from 'react';

import './App.css';

import FormCadastro from './FormCadastro';
import FormLogin from './FormLogin';
import Home from './Home';

import { createBrowserRouter } from 'react-router-dom';
import { RouterProvider } from 'react-router-dom';


function App() {

  //Objeto User

  const user = {

    id: 0,
    login: '',
    senha: '',
    cpf: '',
    email: '',
    dispositivos: []

  }

  const userDto = {

    login: '',
    senha: '',
    cpf: '',
    email: '',

  }

  const userRequest = {

    login: '',
    senha: ''

  }


  //UseState
  const [users, setUsers] = useState([])
  //const [userDtos, setDtos] = useState([])
  const [objUser, setObjUser] = useState(user)
  const [objUDto, setObjUDto] = useState(userDto)


  //UseEffect
  useEffect(() => {



  }, []);

  /*const UserDtoParaUser = (userDto) => {
    setObjUser({

      ...user, // Mantém outros atributos se existirem
      login: userDto.login,
      senha: userDto.senha,
      cpf: userDto.cpf,
      email: userDto.email

    });
  };*/

  const respostaTeclado = (e) => {

    setObjUDto({ ...objUDto, [e.target.name]: e.target.value })

  }


  const selecionaUser = (indice) => {

    setObjUser(users[indice])

  }


  const cadastrarUsuario = () => {

    fetch('http://localhost:8080/cadastroUsuario', {

      method: 'post',
      body: JSON.stringify(objUDto),
      headers: {

        'Content-type': 'application/json',
        'Accept': 'application/json'

      }

    }).then(retorno => retorno.json())
      .then(retornoJson => {

        if (retornoJson.mensagem !== undefined) {

          alert(retornoJson.mensagem)

        } else {

          setUsers([...users, retornoJson])//Modificar para userResponseDto(Com devices) posteriormente
          alert('Usuario cadastrado com sucesso')
          setObjUDto(userDto)

        }



      })

  }

  const selecionarUsuarios = () => {

    fetch('http://localhost:8080/exibicaoUsuarios', {

      method: 'get',

      headers: {

        'Content-type': 'application/json',
        'Accept': 'application/json'

      }

    }).then(retorno => retorno.json())
      .then(retornoJson => {

        if (retornoJson.mensagem) {

          alert(retornoJson.mensagem)

        }

        else {

          setUsers(retornoJson)//Modificar para userResponseDto(Com devices) posteriormente

        }

      })

  }

  const alterarUsuario = () => {

    fetch('http://localhost:8080/edicaoUsuario', {

      method: 'put',
      body: JSON.stringify(objUDto),
      headers: {

        'Content-type': 'application/json',
        'Accept': 'application/json'

      }

    })

      .then(retorno => retorno.json())
      .then(retornoJson => {

        if (retornoJson.mensagem !== undefined) {

          alert(retornoJson.mensagem)

        }
        else {


          alert('Usuario editado com sucesso')

          let usersTemp = [...users]

          let indice = usersTemp.findIndex((u) => {

            return u.id === objUser.id

          })

          usersTemp[indice] = objUser

          setUsers(usersTemp)
          setObjUDto(userDto)


        }



      })

  }

  const removerUsuario = () => {

    fetch('http://localhost:8080/exclusaoUsuario/' + objUDto.id, {

      method: 'delete',
      headers: {

        'Content-type': 'application/json',
        'Accept': 'application/json'

      }

    })
      .then(retorno => retorno.json())
      .then(retornoJson => {

        if (retornoJson.mensagem !== undefined) {

          alert(retornoJson.mensagem)

        } else {

          let usersTemp = [...users]

          let indice = usersTemp.findIndex((u) => {

            return u.id === objUser.id

          })

          usersTemp.splice(indice, 1)

          setUsers(usersTemp)

        }

      })
  }

  const selecionaUsuarioAtivo = () => {

    fetch('http://localhost:8080/exibicaoUsuarioAtivo',

      {
        method: 'get',
        headers: {

          'Content-type': 'application/json',
          'Accept': 'application/json'

        }

      }).then(retorno => retorno.json())
      .then(retornoJson => {

        if (retornoJson.mensagem !== undefined) {

          alert(retornoJson.mensagem)

        }
        else {

          setObjUser(retornoJson)

        }


      })


  }

const salvaUsuarioAtivo = () => {

    fetch('http://localhost:8080/salvaUsuarioAtivo',

      {

        method: 'put',
        body: JSON.stringify(objUser),
        headers: {

          'Content-type': 'application/json',
          'Accept': 'application/json'

        }

      }).then(retorno => retorno.json())
      .then(retornoJson => {

        if (retornoJson.mensagem !== undefined) {

          alert(retornoJson.mensagem)
          return
          
        }
        
        setObjUser(retornoJson)
        alert("ObjUser após ser salvo" + JSON.stringify(retornoJson))

       })


    }



  const router = createBrowserRouter([
    {

      path: "/",
      element: <FormLogin eventoTeclado={respostaTeclado} setObjUDto={setObjUDto} obj={objUDto} userDto={userDto} />,

    },

    {

      path: "cadastrar",
      element: <FormCadastro eventoTeclado={respostaTeclado} cadastrarUsuario={cadastrarUsuario}
        obj={objUDto} />

    },

    {

      path: "home",
      element: <Home usersList={users} user={user} selecionaUsuarioAtivo={selecionaUsuarioAtivo}
        salvaUsuarioAtivo={salvaUsuarioAtivo} alterarUsuario={alterarUsuario} objUser={objUser}
        setObjUser={setObjUser} userDto={userDto} setObjUDto={setObjUDto} />

    }


  ])


  return (
    <div>

      <RouterProvider router={router} />

    </div>


  );
}

export default App;
