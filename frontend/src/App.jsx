import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";


import SearchUser from './components/SearchUser';

function App() {
  

  return (
    <>

    <Router>

      <div>

      <Routes>

        <Route path = "/" element = {<SearchUser></SearchUser>}></Route>

      </Routes>

      </div>

    </Router>
    
    </>
  )
}

export default App
