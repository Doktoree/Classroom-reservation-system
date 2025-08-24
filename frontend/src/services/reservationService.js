import fetchWithAuth from "./authService";

const apiUrl = "http://localhost:8080/api/reservation/";

export async function getAllAvailableClassrooms(reservation) {

  const response = await fetchWithAuth(apiUrl + "availableClassrooms", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(reservation),
  });

  const data = await response.json();
  console.log(JSON.stringify(data));
  if (!response.ok) {
    throw new Error(data.message);
  }


  return data;
}

export async function saveReservation(reservation) {
   
  const response = await fetchWithAuth(apiUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(reservation),
  });

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}

export async function getReservation(id) {
   
  const response = await fetchWithAuth(apiUrl + id);

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}

export async function getAllReservationsFromUser(id, page=0) {
   
  const response = await fetchWithAuth(apiUrl + "user/" + id + "?pageNumber=" + page);

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}

export async function updateReservation(reservation) {
   
  const response = await fetchWithAuth(apiUrl, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(reservation),
  });

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}