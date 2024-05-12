import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import FormLogin from './FormLogin';
import FormCadastro from './FormCadastro';
import reportWebVitals from './reportWebVitals';
import { createBrowserRouter, Router, RouterProvider} from 'react-router-dom';

//Configuração do router


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  
<App/>
  
);

reportWebVitals();
