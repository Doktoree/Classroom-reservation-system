import { useNavigate } from "react-router-dom";
import { getAllReservationsFromUser, getReservation } from "../../services/reservationService";
import "./ReservationTable.css";
import { useEffect, useState } from "react";

function ReservationTable() {

const navigate = useNavigate();
const [reservations, setReservations] = useState([]);
const [pageNumber, setPageNumber] = useState(0);
const [isHidden, setIsHidden] = useState(false);

useEffect(()=>{

  async function fetchReservations(){

    const user = JSON.parse(localStorage.getItem("user"));
    const reservations2 = await getAllReservationsFromUser(user.id, pageNumber);
    setReservations(reservations2.dtoT);
    setPageNumber(pageNumber+1);

  }

  fetchReservations();

},[]);

async function handleClick(reservationId){

    const response = await getReservation(reservationId);
    const reservation = response.dtoT;
    console.log(reservation);
    navigate("/reservation",{state:{reservation:reservation}});

}

async function handleNextPage(e){

  try {
    e.preventDefault();
    const user = JSON.parse(localStorage.getItem("user"));
    const data = await getAllReservationsFromUser(user.id, pageNumber);
    const reservationData = data.dtoT;
    setReservations([...reservations, ...reservationData]);
    setPageNumber(pageNumber+1);
  } catch (error) {
    setIsHidden(true);
  }
  


}

return (
    <div className="reservationTable-container">
      <h3 className="reservationTable-title">Your reservations</h3>
      <table className="reservationTable-table">
        <thead>
          <tr>
            <th>Date</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th>Reservation Purpose</th>
          </tr>
        </thead>
        <tbody>
          {reservations.map((res) => (
            <tr
              key={res.id}
              onClick={() => handleClick(res.id)}
              className="reservationTable-row"
            >
              <td>{res.date}</td>
              <td>{res.startTime}</td>
              <td>{res.endTime}</td>
              <td>{res.reservationPurpose}</td>
            </tr>
          ))}
        </tbody>
      </table>
      {
        !isHidden && (
          <button className="reservationTable-button" onClick={handleNextPage}>Next page</button>
        )
      }
      
    </div>
  );
}

export default ReservationTable