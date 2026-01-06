import { Outlet } from "react-router-dom";
import { Link, useNavigate } from 'react-router-dom';



function FormCadastro({ eventoTeclado,cadastrarUsuario,obj}) {

    return (

        <form>

            <h1>Insira os seus dados abaixo</h1>
            
            <input type='text' value={obj.login} onChange= {eventoTeclado} name = 'login' placeholder="Login" className="form-control" required />
            <input type='text' value={obj.senha} onChange= {eventoTeclado} name = 'senha' placeholder="senha" className="form-control" required />
            <input type='text' value={obj.cpf} onChange= {eventoTeclado} name = 'cpf' placeholder="CPF" className="form-control" required />
            <input type='text' value={obj.email} onChange= {eventoTeclado} name = 'email' placeholder="email" className="form-control" required />

            <button type = "button" id = "primario" onClick={cadastrarUsuario}>Cadastrar</button>
            <button type = "button" id="secundario"><Link to="/">Retornar</Link> </button>
         
        </form>

    )

}

export default FormCadastro;