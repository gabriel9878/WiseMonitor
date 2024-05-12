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
    email: '',
    cpf: ''

  }

  //UseState
  const [btnCadastrar, setBtnCadastrar] = useState(true);
  const [users, setUsers] = useState([]);
  const [objUser, setObjUser] = useState(user)

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
          atualizarFormulario()
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

          atualizarFormulario()

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

          atualizarFormulario()

        }

      })
  }


  const atualizarFormulario = () => {

    setObjUser(user)
    setBtnCadastrar(true)

  }

  const selecionarUsers = (id) => {

    setObjUser(users[id])
    setBtnCadastrar(false)


  }

  const router = createBrowserRouter([
    {
      path: "/",
      element: <FormLogin eventoTeclado={respostaTeclado} obj = {objUser} />,

    },
    
    {
      path: "cadastrar",
      element: <FormCadastro botao={setBtnCadastrar} eventoTeclado={respostaTeclado} cadastrar={cadastrar}
        alterar={alterar} exibir={exibir} remover={remover} obj={objUser} />

    },

    {

      path: "home",
      element: <Home/>

    }


  ])


  return (
    <div>

      <RouterProvider router={router} />

    </div>


  );
}

export default App;
