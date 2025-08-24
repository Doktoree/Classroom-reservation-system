import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom';

function NavBarAdmin() {
  const [showDropdownUser, setShowDropdownUser] = useState(false);
  const [showDropdownReservation, setShowDropdownReservation] = useState(false);
  const [showDropdownClassroom, setShowDropdownClassroom] = useState(false);
  const navigate = useNavigate();

  function handleLogout(){

    localStorage.removeItem("user");
    localStorage.removeItem("token");
    navigate("/");

  }

  return (
    <nav className="navbar">
    <div className="navbar-left">
      <div className="navbar-logo">Classroom reservation system</div>
      <ul className="navbar-links">
        <li 
          className="navbar-link dropdown"
          onMouseEnter={() => setShowDropdownUser(true)}
          onMouseLeave={() => setShowDropdownUser(false)}
        >
          User
          {showDropdownUser && (
            <ul className="dropdown-menu">
              <li>
                <Link className="dropdown-link" to="/user" state={{view:true}}>User information</Link>
              </li>
              <li>
                <Link className="dropdown-link" to="/user">Register user</Link>
              </li>
            </ul>
          )}
        </li>
        <li>
                <Link className="navbar-link" to="/reservationStatus">Reservation management</Link>
        </li>
        <li 
          className="navbar-link dropdown"
          onMouseEnter={() => setShowDropdownClassroom(true)}
          onMouseLeave={() => setShowDropdownClassroom(false)}
        >
          Classroom management
          {showDropdownClassroom && (
            <ul className="dropdown-menu">
              <li>
                <Link className="dropdown-link" to="/classroom" state={{view:false}}>Create new classroom</Link>
              </li>
              <li>
                <Link className="dropdown-link" to="/classroom" state={{view:true}}>View classrooms</Link>
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

export default NavBarAdmin