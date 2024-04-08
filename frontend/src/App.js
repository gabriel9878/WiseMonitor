import { useState } from 'react';
import { useEffect } from 'react';
import './App.css';
import Formulario from './Formulario';
import Tabela from './Tabela';

function App() {
  
  //Objeto User

  const user ={

    id : 0,
    login : '',
    senha: '',
    email : '',
    cpf:''

  }

  //UseState
  const [btnCadastrar,setBtnCadastrar] = useState(true);
  const [users,setUsers] = useState([]);
  const [objUser,setObjUser] = useState(user)

  //UseEffect
  useEffect(() =>{

    fetch("http://localhost:8080/exibicaoUsuarios")
      .then(resposta => resposta.json())
      .then(respostaJson => setUsers(respostaJson))

  }, []);

  const respostaTeclado = (e) =>{

      setObjUser({...objUser,[e.target.name] : e.target.value})

  }

  const cadastrar = () =>{

    fetch('http://localhost:8080/cadastroUsuario',{

      method:'post',
      body: JSON.stringify(objUser),
      headers:{

        'Content-type': 'application/json',
        'Accept' : 'application/json'

      }

    })
    .then(retorno => retorno.json())
      .then(retornoJson =>{

        if(retornoJson.mensagem !== undefined){

          alert(retornoJson.mensagem)

        }else{

          setUsers([...users,retornoJson])
          alert('Usuario cadastrado com sucesso')
          limparFormulario()
        }

        

      }) 

  }

  const alterar = () =>{

    fetch('http://localhost:8080/edicaoUsuario',{

      method:'put',
      body: JSON.stringify(objUser),
      headers:{

        'Content-type': 'application/json',
        'Accept' : 'application/json'

      }

    })
    .then(retorno => retorno.json())
      .then(retornoJson =>{

        if(retornoJson.mensagem !== undefined){

          alert(retornoJson.mensagem)

        }
        else{

          
          alert('Usuario editado com sucesso')

          let usersTemp = [...users]

          let indice = usersTemp.findIndex((u) =>{
  
            return u.id === objUser.id
  
          })
  
          usersTemp[indice] = objUser
  
          setUsers(usersTemp)

          limparFormulario()
        }

        

      }) 

  }

  const remover = () =>{

    fetch('http://localhost:8080/exclusaoUsuario-{id}' + objUser.id,{

      method:'delete',
      headers:{

        'Content-type': 'application/json',
        'Accept' : 'application/json'

      }

    })
    .then(retorno => retorno.json())
      .then(retornoJson =>{

        alert(retornoJson.mensagem)

        let usersTemp = [...users]

        let indice = usersTemp.findIndex((u) =>{

          return u.id === objUser.id

        })

        usersTemp.splice(indice,1)

        setUsers(usersTemp)

        limparFormulario()


      }) 
  }


  const limparFormulario = () =>{

    setObjUser(user)
    setBtnCadastrar(true)
  }

  const selecionar = (indice) =>{

    setObjUser(users[indice])
    setBtnCadastrar(false)


  }

  return (
    <div>
      
      <Formulario botao = {btnCadastrar} eventoTeclado = {respostaTeclado} cadastrar = {cadastrar}
      alterar = {alterar} remover = {remover}obj = {objUser} cancelar={limparFormulario}/>
      <Tabela usersList = {users} selecionarUsers ={selecionar}/>
    
     </div>
  );
}

export default App;
