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

  if (!response.ok) {
    throw new Error(data.message);
  }


  return data;
}

export async function saveReservation(reservation) {
   
  console.log(JSON.stringify(reservation));


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