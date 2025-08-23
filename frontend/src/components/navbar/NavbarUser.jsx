import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom';
import './NavbarUser.css';

function Navbar() {

  const [showDropdown, setShowDropdown] = useState(false);
  const navigate = useNavigate();

  function handleLogout(){

    localStorage.removeItem("user");
    localStorage.removeItem("token");
    navigate("/")

  }

  return (
    <nav className="navbar">
    <div className="navbar-left">
      <div className="navbar-logo">Classroom reservation system</div>
      <ul className="navbar-links">
        <li>
          <Link className="navbar-link" to="/user">User</Link>
        </li>
        <li 
          className="navbar-link dropdown"
          onMouseDownCapture={() => setShowDropdown(true)}
          onMouseLeave={() => setShowDropdown(false)}
        >
          Reservations
          {showDropdown && (
            <ul className="dropdown-menu">
              <li>
                <Link className="dropdown-link" to="/reservation" state={{create:true}}>Create reservation</Link>
              </li>
              <li>
                <Link className="dropdown-link" to="/reservation" state={{create:false}}>View reservations</Link>
              </li>
            </ul>
          )}
        </li>
      </ul>
    </div>
    <div className="navbar-right">
      <button className="navbar-link logout-button" onClick={handleLogout}>
        Logout
      </button>
    </div>
  </nav>
  )
}

export default Navbar