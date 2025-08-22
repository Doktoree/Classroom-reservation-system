import fetchWithAuth from "./authService";

const apiUrl = "http://localhost:8080/api/department/";

export async function getAllDepartments() {
  const response = await fetchWithAuth(apiUrl);

  const data = await response.json();
  console.log(JSON.stringify(data));
  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}