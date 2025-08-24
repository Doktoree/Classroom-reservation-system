import React, { useEffect } from 'react'
import NavBarAdmin from '../navbar/NavBarAdmin'
import ClassroomForm from './ClassroomForm'
import { useLocation } from 'react-router-dom'
import ClassroomTable from './ClassroomTable';

function ClassroomPage() {

  const location = useLocation();
  let {view = false, classroom} = location.state || {};  

  return (
    <div>

    <NavBarAdmin></NavBarAdmin>

    {view ? <ClassroomTable /> : <ClassroomForm classroom={classroom} />}


    </div>
  )
}

export default ClassroomPage