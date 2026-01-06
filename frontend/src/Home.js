import { useState } from 'react';
import { useCallback } from 'react';
import { useEffect } from "react";
import { json, Link, useNavigate } from 'react-router-dom';


function Home({

    usersList, user, ObjUDto, objUser, userDto, setObjUser,setObjUDto,
    selecionaUsuarioAtivo, salvaUsuarioAtivo, alterarUsuario,trataRespostaJson

}) {

    const navigate = useNavigate()

    const device = {

        id: 0,
        nome: '',
        user: user

    }

    const deviceDto = {

        nome: ''

    }


    const [btnsSelecao, setBtnsSelecao] = useState([])
    const [userDevices, setUserDevices] = useState([])
    const [objDDto, setObjDDto] = useState(deviceDto)
    const [objDevice, setObjDevice] = useState(device)
    const [initialize, setInitialize] = useState(false)

    const respostaTeclado = (e) => {

        setObjDDto({ ...objDDto, [e.target.name]: e.target.value })

    }


    const selecionaDispositivoUsuario = (dispositivo, index) => {

        setObjDevice(dispositivo);
        alert("Dispositivo Selecionado: " + dispositivo.id)
        const tempBtnsSelecao = [...btnsSelecao];
        tempBtnsSelecao[index] = false;
        setBtnsSelecao(tempBtnsSelecao);

    }

    const removeSelecaoDevices = () => {


        let tamBtns = objUser.dispositivos?.length
        setObjDevice(device)
        alert("removeseleça" + JSON.stringify(tamBtns)) 
        //alert("Tamanho dispositivos removendoSeleção : " + tamBtns)
        //alert(tamBtns + JSON.stringify(objUser.dispositivos) )

        if (tamBtns > 0) {

            const tempBtns = new Array(tamBtns).fill(true)
            //tempBtnsSelecao.fill(true)

            setBtnsSelecao(tempBtns)

        }
        //alert( "Botoes de seleção na remoçãoDevice: " + JSON.stringify(btnsSelecao))
    }


    const logoff = (e) => {

        e.preventDefault()

        fetch("http://127.0.0.1:8080/logoff",

            {

                method: 'get',
                headers: {

                    'Content-type': 'application/json',
                    'Accept': 'application/json'

                }

            }).then(trataRespostaJson)
            .then(retornoJson => {


                alert(retornoJson.mensagem)
                navigate("/")
                setObjUDto(userDto)
                setObjUser(user)
                


            })



    }

    const cadastrarDispositivo = () => {

        fetch("http://127.0.0.1:8080/cadastroDispositivo",

            {

                method: 'post',
                body: JSON.stringify(objDDto),
                headers: {

                    'Content-type': 'application/json',
                    'Accept': 'application/json'

                }

            }).then(trataRespostaJson)
            .then(respostaJson => {

                if (respostaJson.mensagem !== undefined) {

                    alert(respostaJson.mensagem)
                    return

                }

                let tempDevices = ([...objUser.dispositivos,respostaJson])
                
                setObjUser({ ...objUser, dispositivos: tempDevices })
                setBtnsSelecao([...btnsSelecao, true])

                salvaUsuarioAtivo()
                //removeSelecaoDevices()




            })


    }

    /*const selecionarDispositivos = () => {

        fetch("http://localhost:8080/exibicaoDispositivos", {


            method: 'get',
            headers: {

                'Content-type': 'application/json',
                'Accept': 'application/json'

            }

        }).then(trataRespostaJson)
            .then(respostaJson => {

                setDevices(respostaJson)

            })


    }*/



    const removerDispositivo = () => {

        fetch('http://127.0.0.1:8080/exclusaoDispositivo/' + objDevice.id, {

            method: 'delete',
            headers: {

                'Content-type': 'application/json',
                'Accept': 'application/json'
            }

        }).then(trataRespostaJson)
            .then(respostaJson => {

                if (respostaJson.mensagem !== undefined) {

                    alert(respostaJson.mensagem)

                }

                else {


                    let devicesTemp = [...objUser.dispositivos]

                    let indice = devicesTemp.findIndex((d) => {

                        return d.id === objDevice.id

                    })

                    devicesTemp.splice(indice, 1)

                    //alert("Dispositivos após remoção: " + JSON.stringify(devicesTemp))
                    setObjUser({ ...objUser, dispositivos: devicesTemp })
                    salvaUsuarioAtivo()

                    /*for(let i = 0; i < objUser.dispositivos.length;i++){

                        alert(JSON.stringify("Dispositivo " + i +  objUser.dispositivos[i]))

                    }*/

                    let tempBtnsSelecao = [...btnsSelecao]
                    tempBtnsSelecao.splice(indice, 1)

                    //alert("tempBtnsApósRemoção: " + JSON.stringify(tempBtnsSelecao))

                    setBtnsSelecao(tempBtnsSelecao)

                    //alert("BtnsSeleção Após Remoção: " + JSON.stringify(tempBtnsSelecao))

                }

            })


    }

    // Usando useCallback para memorizar a função
  const listarDispositivosUsuario = useCallback(() => {
    fetch('http://127.0.0.1:8080/exibicaoDispositivosUsuario/' + objUser.id, {
      method: 'get',
      headers: {
        'Content-type': 'application/json',
        'Accept': 'application/json'
      }
    })
     .then(trataRespostaJson)
     .then(retornoJson => {
        if (retornoJson.mensagem!== undefined) {
          alert(retornoJson.mensagem);
          return;
        }
        setObjUser({...objUser,dispositivos:retornoJson})
        removeSelecaoDevices();
        
      });
  }, [objUser,removeSelecaoDevices])


  useEffect(() => {

            selecionaUsuarioAtivo();
            /*salvaUsuarioAtivo()
            removeSelecaoDevices()
            alert("Usuario Logado: " + JSON.stringify(objUser));
            setInitialize(true);*/

    }, []);

    useEffect(() => {

        if (objUser.dispositivos?.length > 0) {
            const tempBtns = new Array(objUser.dispositivos.length).fill(true);
            setBtnsSelecao(tempBtns);
        }

    }, [objUser.dispositivos]);

    return (

        <table className="table" >

            <thead>

                <tr>

                    <th id="primeira">ID</th>
                    <th>Login</th>
                    <th>Email</th>
                    <th>CPF</th>
                    <th id="ultima">Dispositivos</th>


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
                                <form>
                                    <h2>Cadastre um dispositivo</h2>
                                    <li type="none"><input type="text" value={objDDto.nome} name="nome" onChange={respostaTeclado} required /></li>
                                    <li type="none"><button id="primario" onClick={() => cadastrarDispositivo()}>Adicionar dispositivo</button></li>

                                </form>


                                {objUser.dispositivos?.map((dispositivo, index) => (

                                    <li key={index} type="none">{"Dispositivo " + (index + 1)}

                                        {btnsSelecao[index] ?

                                            (<button type = "button" id="primario" onClick={() => selecionaDispositivoUsuario(dispositivo, index)} >Selecionar</button>

                                            ) :

                                            (
                                                <div>

                                                    {"Nome: " + dispositivo.nome}
                                                    <br />
                                                    {"\nid:" + dispositivo.id}
                                                    <br />

                                                    <button type = "button" id="primario">Editar</button>
                                                    <button type = "button" id="secundario" onClick={() => removerDispositivo()}>Remover</button>
                                                    <button type = "button" id="secundario" onClick={() => removeSelecaoDevices()}>Cancelar</button>

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
            <button type = "button" id="primario">Editar</button>
            <button type = "button" id="secundario" onClick={logoff}>Sair</button>

        </table>



    )

}

export default Home;