//const apiUrl = "http://classroomreservationsystem.dmfvg0frgje3czah.italynorth.azurecontainer.io:8080/api/auth";
const apiUrl = "http://localhost:8080/api/auth";

const fetchWithAuth = async (url, options = {}) => {
  const token = localStorage.getItem("token");
  const headers = {
    "Content-Type": "application/json",
    ...options.headers,
    Authorization: `Bearer ${token}`,
  };

  const response = await fetch(`${url}`, {
    ...options,
    headers,
  });

  return response;
};

export default fetchWithAuth;

export async function login(user) {
  const response = await fetch(apiUrl + "/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(user),
  });

  if (!response.ok) {
    throw new Error(response.message);
  }

  const data = await response.json();

  return data;
}

export async function register(user) {
  const response = await fetch(apiUrl + "/register", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(user),
  });

  if (!response.ok) {
    throw new Error(response.message);
  }

  const data = await response.json();

  return data;
}