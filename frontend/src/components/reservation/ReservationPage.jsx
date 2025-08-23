
import { useEffect, useState } from 'react'
import Navbar from '../navbar/NavbarUser'
import ReservationForm from './ReservationForm'
import { getAllReservationsFromUser, getReservation } from '../../services/reservationService'
import { useLocation } from 'react-router-dom';
import ReservationTable from './ReservationTable';

function ReservationPage() {

  const [reservations,setReservations] = useState([]);
  const location = useLocation();
  let {create, reservation} = location.state || {};

  useEffect(() => {

    async function fetchData(){

        const user = JSON.parse(localStorage.getItem("user"));
        console.log("User id:" + user.id);
        const reservation = await getAllReservationsFromUser(user.id);
        setReservations(reservation.dtoT);

    }

    if(!create)
        fetchData();

  },[])

  return (
    <div>

    <Navbar></Navbar>

    {
        create && (
            <ReservationForm onlyview={!create}></ReservationForm>
        )
    }

    {
        !create && !reservation && (

            <ReservationTable reservations={reservations}></ReservationTable>


        )

        
    }

    {
        reservation &&(

            <ReservationForm initialData={reservation}></ReservationForm>


        )

        
    }

    
    
    </div>
  )
}

export default ReservationPage