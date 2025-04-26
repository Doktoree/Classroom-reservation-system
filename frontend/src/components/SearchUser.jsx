import React, { useState } from "react";
import { findUserById, saveUser } from "../services/userService";


function SearchUser() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("ADMIN");
  const [employeeId, setEmployeeId] = useState(null);
  const [ispis,setIspis] = useState("");
  const [employeeIdSearch, setEmployeeIdSearch] = useState(null);
  const [ispisSearch, setIspisSearch] = useState("");

    const saveUserHandler = async(event) => {

        event.preventDefault();

        const user = {

            "email":email,
            "password":password,
            "role":role,
            "employeeId":employeeId

        }

        const response = await saveUser(user);
        const odgovor = JSON.stringify(response.message);
        setIspis(odgovor);

    

    }

    const searchEmployeeHandler = async (e) => {

        try {
          e.preventDefault();

          const response = await findUserById(employeeIdSearch);
          const odgovor = JSON.stringify(response.dto);
          setIspisSearch(odgovor);
        } catch (error) {
          setIspisSearch(error.message);
        }
       

    }

  return (
    <div>
      <h1>Employee service</h1>

      <label htmlFor="email"> Unesite mejl:</label>
      <input type="email" name="email" id="email" onChange={(e) => setEmail(e.target.value)} required />
      <br />
       <label htmlFor="password">Unesite lozinku: </label>
       <input type="password" name="password" id="password" onChange={(e) => setPassword(e.target.value)} required />
       <br />
       <label htmlFor="role">Odaberite rolu: </label>
       <select name="roles" id="role" onChange={(e) => setRole(e.target.value)}>

         <option value="ADMIN">ADMIN</option>
         <option value = "USER">USER</option>   

       </select>
        <br />
        <label htmlFor="employee">Enter employeeId: </label>
        <input type="number" name="employee" id="employee" onChange={(e) => setEmployeeId(e.target.value)} required/>
        <br />

        <button onClick={saveUserHandler}>Save</button>
        
        <h1>{ispis}</h1>

        <br />
        <label htmlFor="employeeId">Enter employeeId:</label>
        <input type="number" name="employeeId" id="employeeId" onChange={(e) => setEmployeeIdSearch(e.target.value)} required/>
        <br></br>
        <button onClick={searchEmployeeHandler}>Search Employee:</button>
        <br />

        <h1>{ispisSearch}</h1>

    </div>
  );
}

export default SearchUser;
