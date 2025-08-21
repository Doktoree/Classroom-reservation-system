import { useState } from "react";
import { login } from "../services/authService";

function LoginForm() {
  
    const [email, setEmail] = useState(null);
    const [password, setPassword] = useState(null);

  const handleLogin = async(event) =>{

        const user = {

            email: email,
            password: password

        }

        const response = await login(user);

        if(!response.ok){

            alert(response.message);
            return;
        }

        alert('Uspeo login!');

  } 

  return (
    <form>
      <div data-mdb-input-init className="form-outline mb-4">
        <input type="email" id="form2Example1" className="form-control" onChange={e => setEmail(e.target.value)}/>
        <label className="form-label" htmlFor="form2Example1">
          Email address
        </label>
      </div>

      <div data-mdb-input-init className="form-outline mb-4">
        <input type="password" id="form2Example2" className="form-control" onChange={e => setPassword(e.target.value)}/>
        <label className="form-label" htmlFor="form2Example2">
          Password
        </label>
      </div>

      <button
        type="button"
        data-mdb-button-init
        data-mdb-ripple-init
        className="btn btn-primary btn-block mb-4"
        onClick={handleLogin}
      >
        Sign in
      </button>
    </form>
  );
}

export default LoginForm;
