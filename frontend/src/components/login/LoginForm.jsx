import { useState } from "react";
import { login } from "../../services/authService";
import { useNavigate } from "react-router-dom";
import './LoginForm.css';

function LoginForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (event) => {
    const user = {
      email: email,
      password: password,
    };


    try {
      const data = await login(user);
      localStorage.setItem("token", data.token);
      localStorage.setItem("user", JSON.stringify(data.user));
      alert("Login succesful!");
      navigate("/navbar");
    } catch (err) {
      alert(err);
    }
  };

  return (
    <form className="login-form">
  <div className="login-form-group">
    <label className="login-form-label" htmlFor="email">
      Email address
    </label>
    <input
      type="email"
      id="email"
      className="login-form-input"
      onChange={(e) => setEmail(e.target.value)}
    />
  </div>

  <div className="login-form-group">
    <label className="login-form-label" htmlFor="password">
      Password
    </label>
    <input
      type="password"
      id="password"
      className="login-form-input"
      onChange={(e) => setPassword(e.target.value)}
    />
  </div>

  <button
    type="button"
    className="login-form-button"
    onClick={handleLogin}
  >
    Sign in
  </button>
</form>
  );
}

export default LoginForm;
