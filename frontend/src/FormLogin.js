import { Link, useNavigate } from 'react-router-dom';

function FormLogin({ obj, eventoTeclado, setObjUDto, userDto, limpaObjUDto}) {

    const navigate = useNavigate()
    //const [errorMessage, setErrorMessage] = useState('')

    const login = (e) => {
        e.preventDefault();

        fetch("http://127.0.0.1:8080/login", {
            method: 'post',
            body: JSON.stringify(obj),
            headers: {
                'Content-type': 'application/json',
                'Accept': 'application/json'  // pode ser */* também
            }
        })
            .then(async (response) => {
                
                const text = await response.text()

                if (!response.ok) {
                    // Trata erro: text pode ser mensagem de erro ou HTML
                    throw new Error(text || `Erro HTTP ${response.status}`);
                }

                // guarda token no localStorage/sessionStorage
                // localStorage.setItem('token', token);

                return text;
            })
            .then((text) => {
                // Sucesso no login
                navigate("home");
                setObjUDto(userDto);
            })
            .catch((erro) => {
                alert('Erro ao realizar login: ' + erro.message);
                console.error(erro);
            });
    }

    return (

        <form>

            <div>

                <header>

                    <h1>Wise Monitor</h1>

                </header>

            </div>
            <input type='text' value={obj.login} onChange={eventoTeclado} name='login' placeholder="Login" className="form form-control" required />

            <input type='password' value={obj.senha} onChange={eventoTeclado} name='senha' placeholder="Senha" className="form form-control" required />

            <button type= "button" id="primario" onClick={login}>Entrar </button>
            <button type = "button" id="secundario"><Link to="/cadastrar">Cadastrar</Link> </button>

        </form>

    )

}

export default FormLogin