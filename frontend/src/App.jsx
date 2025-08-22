import { useState } from 'react'
import './App.css'
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom'
import LoginForm from './components/login/LoginForm'
import ReservationForm from './components/reservation/ReservationForm'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <Router>

        <Routes>

          <Route path='/' element={<LoginForm/>}></Route>
          <Route path='/reservation' element={<ReservationForm/>}></Route>
        </Routes>

      </Router>
    </>
  )
}

export default App
