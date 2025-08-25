import fetchWithAuth from "./authService";

const apiUrl = "http://classroomreservationsystem.dmfvg0frgje3czah.italynorth.azurecontainer.io:8080/api/employee/";

export async function getEmployeeById(id) {
   
  const response = await fetchWithAuth(apiUrl + id);

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}