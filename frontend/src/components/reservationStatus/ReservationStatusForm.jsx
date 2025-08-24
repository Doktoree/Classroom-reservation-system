import React, { useState } from 'react'
import ReservationForm from '../reservation/ReservationForm'
import { approveReservation, rejectReservation } from '../../services/reservationStatusService';
import { updateReservation } from '../../services/reservationService';
import { useNavigate } from 'react-router-dom';

function ReservationStatusForm({initialData}) {

  const [reservationFormMethods, setReservationFormMethods] = useState(null);
  const [rejectingReason, setRejectingReason] = useState(initialData.rejectingReason);
  const navigate = useNavigate();
  
  async function approve(e){
    e.preventDefault();

    const fullReservation = {
    id: initialData.id,
    reservationDto:reservationFormMethods.getCurrentData(),
    status: initialData.status,
    rejectingReason: initialData.rejectingReason
};
    try{
      console.log(JSON.stringify(fullReservation));
      const data = await approveReservation(fullReservation);
      await updateReservation(fullReservation.reservationDto);
      alert(data.message);
      navigate("/navbarAdmin");
    } catch (error) {
      alert(data.message);
    }
    
    

  }

  async function reject(e){
    e.preventDefault();
     const fullReservation = {
    id: initialData.id,
    reservationDto:reservationFormMethods.getCurrentData(),
    status: initialData.status,
    rejectingReason: rejectingReason
};
    try {
      if(!rejectingReason){
        alert("Rejecting reason can't be empty!");
        return;
      }
      const data = await rejectReservation(fullReservation);
      await updateReservation(fullReservation.reservationDto);
      alert(data.message);
      navigate("/navbarAdmin");
    } catch (error) {
      alert(data.message);
    }
    

  }
  return (
    <div>

        <form className="user-form">
            <label>Reservation status</label>
            <input type="text" readOnly={true} value={initialData.status} className="status-input" />

            <label>Rejecting reason</label>
            <input type="text" value={initialData.rejectingReason} onChange={e => setRejectingReason(e.target.value)} className="status-input" />
        


      {<ReservationForm onlyview = {false} initialData={initialData.reservationDto} noWrapper={true} onChange={setReservationFormMethods}></ReservationForm> }
        <button onClick={approve} className="reservation-button" >Approve reservation</button>
        <button onClick={reject} className="reservation-button" >Reject reservation</button>
        </form>
    

    </div>
  )
}

export default ReservationStatusForm