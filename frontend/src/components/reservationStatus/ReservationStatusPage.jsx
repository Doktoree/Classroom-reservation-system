import React from 'react'
import NavBarAdmin from '../navbar/NavBarAdmin'
import ReservationStatusTable from './ReservationStatusTable'
import { useLocation } from 'react-router-dom';
import ReservationForm from '../reservation/ReservationForm';
import ReservationStatusForm from './ReservationStatusForm';

function ReservationStatusPage() {

  const location = useLocation();
  let {table = true, initialData} = location.state || {};  

  return (
    <div>
        <NavBarAdmin></NavBarAdmin>
        {table? <ReservationStatusTable/>:<ReservationStatusForm initialData={initialData}/>}
    </div>
  )
}

export default ReservationStatusPage