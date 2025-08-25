import fetchWithAuth from "./authService";

const apiUrl = "http://classroomreservationsystem.dmfvg0frgje3czah.italynorth.azurecontainer.io:8080/api/student-organization/";

export async function getAllStudentOrganizations() {
  const response = await fetchWithAuth(apiUrl);

  const data = await response.json();
  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}