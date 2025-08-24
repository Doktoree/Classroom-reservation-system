
import { useEffect, useState } from 'react'
import Navbar from '../navbar/NavbarUser'
import ReservationForm from './ReservationForm'
import { getAllReservationsFromUser, getReservation } from '../../services/reservationService'
import { useLocation } from 'react-router-dom';
import ReservationTable from './ReservationTable';
import NavBarAdmin from '../navbar/NavBarAdmin';

function ReservationPage() {

  const [reservations,setReservations] = useState([]);
  const location = useLocation();
  let {create, reservation} = location.state || {};
  const [isAdmin, setIsAdmin] = useState(false);

  useEffect(() => {

    async function fetchData(){

        const user = JSON.parse(localStorage.getItem("user"));
        const reservation = await getAllReservationsFromUser(user.id);
        setReservations(reservation.dtoT);

    }

    const user = JSON.parse(localStorage.getItem("user"));

    if(user.role === "ADMIN")
        setIsAdmin(true);

    if(!create)
        fetchData();

  },[])

  return (
    <div>

    {isAdmin && (<NavBarAdmin></NavBarAdmin>)}
    {!isAdmin && <Navbar></Navbar>}

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