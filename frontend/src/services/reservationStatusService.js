import fetchWithAuth from "./authService";

const apiUrl = "http://localhost:8080/api/reservation-status/";

export async function getAllReservationStatuses(page=0) {
   
  const response = await fetchWithAuth(apiUrl + "?pageNumber=" + page);

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}

export async function getReservationStatus(id) {
   
  const response = await fetchWithAuth(apiUrl + id);

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}

export async function approveReservation(reservation) {
   
  console.log(JSON.stringify(reservation));
  console.log("APIIIIIIIIII: " + apiUrl+"approve");
  const response = await fetchWithAuth(apiUrl+"approve", {
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

export async function rejectReservation(reservation) {
   
  console.log(JSON.stringify(reservation));
  const response = await fetchWithAuth(apiUrl+"reject", {
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

export async function getAllReservationStatusByStatus(page=0, reservationStatus) {
   

  console.log(JSON.stringify(reservationStatus)); 
  const response = await fetchWithAuth(apiUrl+"status/" + "?pageNumber=" + page, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(reservationStatus),
  });

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message);
  }

  return data;
}