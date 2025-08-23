import React, { useEffect, useState } from 'react'
import Navbar from '../navbar/NavbarUser'
import UserForm from './UserForm'
import { getEmployeeById } from '../../services/employeeService';

function UserPage() {

  const [fullUser, setFullUser] = useState(null);

  useEffect(() => {

    async function fetchEmployee(){

      const user = JSON.parse(localStorage.getItem("user"));
      const data = await getEmployeeById(user.employeeId);
      const employee = data.dtoT;
      setFullUser({...user,...employee});
     
    }

    fetchEmployee();

  },[]);
 
  if(!fullUser){

    return(
      <div>
        <Navbar />
        <p>Loading user data...</p>
      </div>

    )

  }

  return (
    <div>
        <Navbar></Navbar>
        <UserForm initialData={fullUser} readOnly={true}></UserForm>
    </div>
  )
}

export default UserPage