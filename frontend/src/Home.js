import { useEffect } from "react";
import { Link, useNavigate } from 'react-router-dom';

function Home({ usersList, limpaObjUDto, objUser, selecionaUsuarioAtivo, btnEdicao, selecionaDevice, removeSelecaoDevice }) {

    const navigate = useNavigate()
    useEffect(() => {

        selecionaUsuarioAtivo()

    }, [])

    const logoff = (e) => {

        e.preventDefault()

        fetch("http://localhost:8080/logoff",

            {

                method: 'get',
                headers: {

                    'Content-type': 'application/json',
                    'Accept': 'application/json'

                }

            }).then(retorno => retorno.json())
            .then(retornoJson => {


                alert(retornoJson.mensagem)
                
                navigate("/")
                limpaObjUDto()


            })



    }

    return (

        <table className="table" >

            <thead>

                <tr>

                    <th id ="primeira">ID</th>
                    <th>Login</th>
                    <th>Email</th>
                    <th>CPF</th>
                    <th>Dispositivos</th>
                    <th id = "ultima">Selecionar</th>


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

            <button id="secundario" onClick={logoff}>Sair</button>

        </table>



    )

}

export default Home;