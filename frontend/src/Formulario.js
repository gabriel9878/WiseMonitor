function Formulario({ botao,eventoTeclado,cadastrar,alterar,remover,obj,cancelar }) {

    return (

        <form>

            <input type='text' value={obj.login} onChange= {eventoTeclado} name = 'login' placeholder="Login" className="form-control" />
            <input type='text' value={obj.senha} onChange= {eventoTeclado} name = 'senha' placeholder="senha" className="form-control" />
            <input type='text' value={obj.cpf} onChange= {eventoTeclado} name = 'cpf' placeholder="CPF" className="form-control" />
            <input type='text' value={obj.email}onChange= {eventoTeclado} name = 'email' placeholder="email" className="form-control" />
           

            {
                botao ?

                    <input type="button" onClick={cadastrar} value="Cadastrar" className="btn btn-primary" />
                    :
                    <div>

                        <input type="button" onClick = {alterar}value="Alterar" className="btn btn-primary" />
                        <input type="button" onClick = {remover}value="Remover" className="btn btn-primary" />
                        <input type="button" onClick = {cancelar} value="Cancelar" className="btn btn-secondary" />

                    </div>

            }



        </form>

    )

}

export default Formulario;