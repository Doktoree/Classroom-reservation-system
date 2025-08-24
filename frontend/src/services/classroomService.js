import fetchWithAuth from "./authService";

const apiUrl = "http://localhost:8080/api/classroom/";

export async function getClassroom(id) {
   
  const response = await fetchWithAuth(apiUrl + id);

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}

export async function saveClassroom(classroom) {
   
  const response = await fetchWithAuth(apiUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(classroom),
  });

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}

export async function deleteClassroom(id) {
   
  console.log(JSON.stringify(id));


  const response = await fetchWithAuth(apiUrl + id, {
    method: "DELETE"
  });

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}


export async function updateClassroom(classroom) {
   
  console.log(JSON.stringify(classroom));


  const response = await fetchWithAuth(apiUrl, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(classroom),
  });

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}


export async function getAllClassrooms() {
   
  const response = await fetchWithAuth(apiUrl + "all");

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}
