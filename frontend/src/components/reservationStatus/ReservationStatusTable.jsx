import React, { useEffect, useState } from 'react'
import { getAllReservationStatusByStatus, getAllReservationStatuses, getReservationStatus } from '../../services/reservationStatusService';
import { useNavigate } from 'react-router-dom';

function ReservationStatusTable() {

  const [header,setHeader] = useState("All reservations");
  const [reservationStatuses, setReservationStatuses] = useState([]);
  const [pageNumber, setPageNumber] = useState(0);
  const [selectedOption, setSelectedOption] = useState("PENDING");
  const navigate = useNavigate();

  useEffect(() => {

    async function fetchReservationStatuses(){

        const data = await getAllReservationStatuses(pageNumber);
        console.log(JSON.stringify(data));
        setReservationStatuses(data.dtoT);
        setPageNumber(pageNumber+1);

    }

    fetchReservationStatuses();

  },[]);

  async function handleClick(reservationStatusId){
  
      const response = await getReservationStatus(reservationStatusId);
      const reservationS = response.dtoT;
      console.log(JSON.stringify(reservationS));
      navigate("/reservationStatus",{state:{table:false, initialData: reservationS}});
  
  }

  async function handleSelectChange(e){

    const newStatus = e.target.value;
    setSelectedOption(newStatus);
    setPageNumber(0);

  try {
    const reservationStatus = { status: newStatus };
    const data = await getAllReservationStatusByStatus(0, reservationStatus);
    setReservationStatuses(data.dtoT);
  } catch (error) {
    alert(error.message);
  }

  }
  

  return (
    <div className="reservationTable-container">
      <select value={selectedOption} onChange={handleSelectChange} className="reservationTable-select">
        <option value="PENDING">PENDING</option>
        <option value="APPROVED">APPROVED</option>
        <option value="REJECTED">REJECTED</option>
      </select>
      <h3 className="reservationTable-title">{header}</h3>
      <table className="reservationTable-table">
        <thead>
          <tr>
            <th>Date</th>
            <th>User name</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {reservationStatuses.map((rs) => (
            <tr
              key={rs.id}
              onClick={() => handleClick(rs.id)}
              className="reservationTable-row"
            >
              <td>{rs.reservationDto.date}</td>
              <td>{rs.reservationDto.user.employee.name + " " + rs.reservationDto.user.employee.lastName}</td>
              <td>{rs.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
      
    </div>
  )
}

export default ReservationStatusTable