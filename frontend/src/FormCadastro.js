import { Outlet } from "react-router-dom";




function FormCadastro({ eventoTeclado,cadastrar,alterar,remover,obj}) {


    
    return (

        <form>
            <h1>Insira os seus dados abaixo</h1>
            
            <input type='text' value={obj.login} onChange= {eventoTeclado} name = 'login' placeholder="Login" className="form-control" />
            <input type='text' value={obj.senha} onChange= {eventoTeclado} name = 'senha' placeholder="senha" className="form-control" />
            <input type='text' value={obj.cpf} onChange= {eventoTeclado} name = 'cpf' placeholder="CPF" className="form-control" />
            <input type='text' value={obj.email}onChange= {eventoTeclado} name = 'email' placeholder="email" className="form-control" />

            <input type="button" onClick={cadastrar} value="Cadastrar" className="btn btn-primary" />
                    


         
        </form>

    )

}

export default FormCadastro;