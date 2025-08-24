import React, { useEffect, useState } from 'react'
import './UserForm.css';
import { register } from '../../services/authService';

function UserForm({initialData, readOnly = false}) {

  const [id, setId] = useState(null);
  const [email, setEmail] = useState(null);
  const [password, setPassword] = useState(null);
  const [role, setRole] = useState("USER");
  const [employeeId, setEmployeeId] = useState(null);  
  const [firstName, setFirstName] = useState(null);
  const [lastName, setLastName] = useState(null);
  const [academicRank, setAcademicRank] = useState(null);

  const roles = ["USER","ADMIN"];

  useEffect(() => {

    function fillForm(){

        if(initialData){

            setId(initialData.id);
            setEmail(initialData.email);
            setPassword(initialData.password);
            setRole(initialData.role);
            setEmployeeId(initialData.employeeId);
            setFirstName(initialData.name);
            setLastName(initialData.lastName);
            setAcademicRank(initialData.academicRank);

        }


    }

    if(readOnly)
      fillForm();

    if(!readOnly)
      clearForm();
    
      

    

  },[readOnly,initialData])

  function clearForm(){

    setId(null);
    setEmail(null);
    setPassword(null);
    setRole("USER");
    setEmployeeId(null);
    setFirstName(null);
    setLastName(null);
    setAcademicRank(null);

  }

  async function handleRegisterUser(e){

    e.preventDefault();

    try {

      const registeredUser = {

        email: email,
        password: password,
        role: role,
        employeeId: employeeId

      };

      console.log(JSON.stringify(registeredUser));

      const data = await register(registeredUser);
      console.log(data);
      clearForm();
      alert(data);
      
    } catch (error) {

      alert(data);
      
    }

  }



  return (
    <form className="user-form">
      <h2>User Form</h2>
      {readOnly && (

        <div>
        <label>ID:</label>
        <input type="text" name="id" required value={id || ''} readOnly={readOnly} onChange={e => setId(e.target.value)} />
      </div>

      )}
      

      <div>
        <label>Email:</label>
        <input type="email" name="email" required value={email || ''} readOnly={readOnly} onChange={e => setEmail(e.target.value)} />
      </div>

      <div>
        <label>Password:</label>
        <input type="password" name="password" required value={password || ''} readOnly={readOnly} onChange={e => setPassword(e.target.value)} />
      </div>

      <div>
        <label>Role:</label>
        <select name="role" value={role || ''} required disabled={readOnly} onChange={e => setRole(e.target.value)}>
          {roles.map((r) => (
            <option key={r} value={r}>
              {r}
            </option>
          ))}
        </select>
      </div>

      <div>
        <label>Employee ID:</label>
        <input type="text" name="employeeId" required value={employeeId || ''} readOnly={readOnly} onChange={e => setEmployeeId(e.target.value)} />
      </div>

        {readOnly && (

          <div>
            <div>
              <label>First name:</label>
              <input type="text" name="firstName" required value={firstName || ''} readOnly={readOnly} onChange={e => setFirstName(e.target.value)} />
            </div>

          <div>
              <label>Last name:</label>
             <input type="text" name="lastName" required value={lastName || ''} readOnly={readOnly} onChange={e => setLastName(e.target.value)} />
          </div>

          <div>
            <label>Academic rank:</label>
            <input type="text" name="lastName" required value={academicRank || ''} readOnly={readOnly} onChange={e => setAcademicRank(e.target.value)} />
          </div>
      </div>

        )}

        {!readOnly && (

          <button onClick={handleRegisterUser}>Register user</button>

        )}
      
    </form>
  )
}

export default UserForm