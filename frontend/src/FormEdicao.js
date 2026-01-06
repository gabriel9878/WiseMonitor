import { Link, useNavigate } from 'react-router-dom';

function FormEdicao({ eventoTeclado, alterarUsuario, obj }) {

    return (

        <form>

            <h1>Insira os seus dados abaixo</h1>
            
            <input type='text' value={obj.login} name='login' placeholder="Login" className="form-control" disabled />
            <input type='text' value={obj.cpf} onChange= {eventoTeclado} name = 'cpf' placeholder="CPF" className="form-control" required />
            <input type='text' value={obj.email} onChange= {eventoTeclado} name = 'email' placeholder="email" className="form-control" required />

            <button type = "button" id = "primario" onClick={alterarUsuario}>Editar</button>
            <button type = "button" id="secundario"><Link to="/home">Retornar</Link> </button>
         
        </form>

    )

}

export default FormEdicao;