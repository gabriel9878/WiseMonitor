function Tabela({usersList,selecionarUsers}) {

    return (


        <table className="table">

            <thead>

                <tr>

                    <th>#</th>
                    <th>Login</th>
                    <th>Email</th>
                    <th>CPF</th>
                    <th>Dispositivos</th>
                    <th>Selecionar</th>


                </tr>


            </thead>

            <tbody>

                {

                    usersList.map((obj, indice) => (

                        <tr key = {indice}>

                            <td>{indice + 1}</td>
                            <td>{obj.login}</td>
                            <td>{obj.email}</td>
                            <td>{obj.cpf}</td>
                            <td>
                                <ul>

                                    {obj.dispositivos.map((dispositivo, index) => (
                                        <li key={index}>{dispositivo.nome}</li>
                                    ))}

                                </ul>
                            </td>
                            <td><button onClick={() => {selecionarUsers(indice)}}className="btn btn-success">Selecionar </button> </td>

                        </tr>



                    )


                    )

                }

            </tbody>

        </table>



    )

}

export default Tabela;