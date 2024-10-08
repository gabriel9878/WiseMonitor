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

  const device = {

    id: 0,
    nome: '',
    user: user

  }

  //UseState
  const [btnEdicao, setBtnEdicao] = useState(true)
  const [users, setUsers] = useState([])
  const [userDtos, setDtos] = useState([])
  const [objUDto, setObjUDto] = useState(userDto)
  const [objUser, setObjUser] = useState(user)
  const [devices, setDevices] = useState([])
  const [objDevice, setObjDevice] = useState(device)

  //UseEffect
  useEffect(() => {



  }, []);

  const UserDtoParaUser = (userDto) => {
    setObjUser({

      ...user, // Mantém outros atributos se existirem
      login: userDto.login,
      senha: userDto.senha,
      cpf: userDto.cpf,
      email: userDto.email

    });
  };

  const respostaTeclado = (e) => {

    setObjUDto({ ...objUDto, [e.target.name]: e.target.value })

  }

  const limpaObjUDto = () => {

    setObjUDto(userDto)

  }

  const selecionaUser = (indice) => {

    setObjUser(users[indice])

  }

  const selecionaDevice = (indice) => {

    setObjDevice(devices[indice])
    setBtnEdicao(false)

  }

  const removeSelecaoDevice = () => {

    setObjDevice(null)
    setBtnEdicao(true)

  }

  const cadastrar = () => {

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
          limpaObjUDto()
        }

        

      })

  }

  const selecionar = () => {

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
          limpaObjUDto()
        }

      })



  }

  const alterar = () => {

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
          limpaObjUDto()


        }



      })

  }

  const remover = () => {

    fetch('http://localhost:8080/exclusaoUsuario-{id}' + objUDto.id, {

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
          limpaObjUDto()


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

  const router = createBrowserRouter([
    {

      path: "/",
      element: <FormLogin eventoTeclado={respostaTeclado} limpaObjUDto={limpaObjUDto} obj={objUDto} />,

    },

    {

      path: "cadastrar",
      element: <FormCadastro eventoTeclado={respostaTeclado} cadastrar={cadastrar}
        obj={objUDto} />

    },

    {

      path: "home",
      element: <Home usersList={users} removeSelecaoDevice={removeSelecaoDevice}
        selecionaDevice={selecionaDevice} btnEdicao={btnEdicao} limpaObjUDto={limpaObjUDto} selecionaUsuarioAtivo={selecionaUsuarioAtivo} objUser={objUser} />

    }


  ])


  return (
    <div>

      <RouterProvider router={router} />

    </div>


  );
}

export default App;
