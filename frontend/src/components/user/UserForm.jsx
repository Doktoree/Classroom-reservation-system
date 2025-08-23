import React, { useEffect, useState } from 'react'
import './UserForm.css';

function UserForm({initialData, readOnly = false}) {

  const [id, setId] = useState(null);
  const [email, setEmail] = useState(null);
  const [password, setPassword] = useState(null);
  const [role, setRole] = useState(null);
  const [employeeId, setEmployeeId] = useState(null);  
  const [firstName, setFirstName] = useState(null);
  const [lastName, setLastName] = useState(null);
  const [academicRank, setAcademicRank] = useState(null);

  const roles = ["USER","ADMIN"];

  useEffect(() => {

    function fillForm(){

        if(initialData){

            console.log("Smooooooth: " + JSON.stringify(initialData));

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

    fillForm();

  },[])

  return (
    <form className="user-form">
      <h2>User Form</h2>
      <div>
        <label>ID:</label>
        <input type="text" name="id" required value={id || ''} readOnly={readOnly} onChange={e => setId(e.target.value)} />
      </div>

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
    </form>
  )
}

export default UserForm