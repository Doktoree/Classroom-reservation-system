import { useState } from 'react'
import './App.css'
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom'
import LoginForm from './components/login/LoginForm'
import ReservationPage from './components/reservation/ReservationPage'
import UserPage from './components/user/UserPage'
import Navbar from './components/navbar/NavbarUser'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <Router>

        <Routes>

          <Route path='/' element={<LoginForm/>}></Route>
          <Route path='/user' element={<UserPage/>}></Route>
          <Route path='/navbar' element={<Navbar/>}></Route>
          <Route path='/reservation' element={<ReservationPage/>}></Route>
        </Routes>

      </Router>
    </>
  )
}

export default App
