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

  const device = {

    id: 0,
    nome: ''

  }

  //UseState
  const [btnEdicao, setBtnEdicao] = useState(true)
  const [users, setUsers] = useState([])
  const [objUser, setObjUser] = useState(user)
  const [devices, setDevices] = useState([])
  const [objDevice, setObjDevice] = useState(device)

  //UseEffect
  useEffect(() => {



  }, []);

  const respostaTeclado = (e) => {

    setObjUser({ ...objUser, [e.target.name]: e.target.value })

  }

  const cadastrar = () => {

    fetch('http://localhost:8080/cadastroUsuario', {

      method: 'post',
      body: JSON.stringify(objUser),
      headers: {

        'Content-type': 'application/json',
        'Accept': 'application/json'

      }

    }).then(retorno => retorno.json())
      .then(retornoJson => {

        if (retornoJson.mensagem !== undefined) {

          alert(retornoJson.mensagem)

        } else {

          setUsers([...users, retornoJson])
          alert('Usuario cadastrado com sucesso')

        }



      })

  }

  const exibir = () => {

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

          setUsers(retornoJson)

        }

      })



  }

  const alterar = () => {

    fetch('http://localhost:8080/edicaoUsuario', {

      method: 'put',
      body: JSON.stringify(objUser),
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



        }



      })

  }

  const remover = () => {

    fetch('http://localhost:8080/exclusaoUsuario-{id}' + objUser.id, {

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

  const exibirUsuarioAtivo = () => {

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

  const router = createBrowserRouter([
    {
      path: "/",
      element: <FormLogin eventoTeclado={respostaTeclado} obj={objUser} />,

    },

    {
      path: "cadastrar",
      element: <FormCadastro eventoTeclado={respostaTeclado} cadastrar={cadastrar}
        alterar={alterar} exibir={exibir} remover={remover} obj={objUser} />

    },

    {

      path: "home",
      element: <Home usersList={users} removeSelecaoDevice={removeSelecaoDevice}
        selecionaDevice={selecionaDevice} btnEdicao={btnEdicao} exibirUsuarioAtivo={exibirUsuarioAtivo} objUser={objUser} />

    }


  ])


  return (
    <div>

      <RouterProvider router={router} />

    </div>


  );
}

export default App;
