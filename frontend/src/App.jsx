import { useState } from 'react'
import './App.css'
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom'
import LoginForm from './components/LoginForm'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <Router>

        <Routes>

          <Route path='/' element={<LoginForm/>}></Route>

        </Routes>

      </Router>
    </>
  )
}

export default App
