import { Link, useNavigate } from 'react-router-dom';
import { useState } from 'react';

function FormLogin({ obj, eventoTeclado,userDto,limpaObjUDto }) {

    const navigate = useNavigate()
    //const [errorMessage, setErrorMessage] = useState('')

    const login = (e) => {

            e.preventDefault()
            
            fetch("http://localhost:8080/login",

                {

                    method: 'post',
                    body: JSON.stringify(obj),
                    headers: {

                        'Content-type': 'application/json',
                        'Accept': 'application/json'

                    }

                }).then(retorno=> retorno.json())
                .then(retornoJson => {

                    if (retornoJson.mensagem !== undefined) {
                        
                        
                        alert(retornoJson.mensagem)

                    }
                    else{

                        
                        navigate("home")
                        setObjUDto(userDto)
                    }
                    
                    

                })
       
    }

    return (

        <form>

            <div>

                <header>

                    <h1>Wise Finder</h1>

                </header>

            </div>
            <input type='text' value={obj.login} onChange={eventoTeclado} name='login' placeholder="Login" className="form form-control" required />

            <input type='password' value={obj.senha} onChange={eventoTeclado} name='senha' placeholder="Senha" className="form form-control" required />

            <button id="primario" onClick={login}>Entrar </button>
            <button type = "button" id="secundario"><Link to="/cadastrar">Cadastrar</Link> </button>




        </form>

    )

}

export default FormLogin