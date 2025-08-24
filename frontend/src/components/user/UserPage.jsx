import React, { useEffect, useState } from 'react'
import Navbar from '../navbar/NavbarUser'
import UserForm from './UserForm'
import { getEmployeeById } from '../../services/employeeService';
import NavBarAdmin from '../navbar/NavBarAdmin';
import { useLocation } from 'react-router-dom';

function UserPage() {

  const [fullUser, setFullUser] = useState(null);
  const [isAdmin, setIsAdmin] = useState(false);
  const user = JSON.parse(localStorage.getItem("user"));
  let location = useLocation();
  let {view = false} = location.state || {};

  useEffect(() => {
    

    async function fetchEmployee(){

      const data = await getEmployeeById(user.employeeId);
      const employee = data.dtoT;
      setFullUser({...user,...employee});
     
    }

    if(user.role === "ADMIN")
      setIsAdmin(true);

    console.log("User page view: " + view);

    if(view)
      fetchEmployee();

    

  },[location]);
 
  return (

    
    <div>
      {isAdmin && <NavBarAdmin></NavBarAdmin>}
      {!isAdmin && <Navbar></Navbar> }
        
      <UserForm initialData={fullUser} readOnly={view}></UserForm>
    </div>
  )
}

export default UserPage