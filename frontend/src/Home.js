import { useEffect } from "react";

function Home({ usersList, objUser, exibirUsuarioAtivo, btnEdicao, selecionaDevice, removeSelecaoDevice }) {

    useEffect(() => {

        exibirUsuarioAtivo()

    }, [])

    return (

        <table className="table" >

            <thead>

                <tr>

                    <th>ID</th>
                    <th>Login</th>
                    <th>Email</th>
                    <th>CPF</th>
                    <th>Dispositivos</th>
                    <th>Selecionar</th>


                </tr>


            </thead>

            <tbody>

                {

                    <tr>

                        <td>{objUser.id}</td>
                        <td>{objUser.login}</td>
                        <td>{objUser.email}</td>
                        <td>{objUser.cpf}</td>
                        <td>
                            <ul>

                                {objUser.dispositivos.map((dispositivo, index) => (

                                    <li key={index}>{dispositivo.nome}

                                        {btnEdicao ?

                                            (<button onClick={() => selecionaDevice(index)} >Selecionar</button>

                                            ) :

                                            (
                                            <div>
                                            <button>Rastrear</button>
                                            <button>Editar</button>
                                            <button>Deletar</button>
                                            <button onClick={() => removeSelecaoDevice()}>Cancelar</button>
                                            </div>
                                            )
                                        }

                                    </li>











                                ))}

                            </ul>
                        </td>

                    </tr>








                }

            </tbody>

        </table>



    )

}

export default Home;