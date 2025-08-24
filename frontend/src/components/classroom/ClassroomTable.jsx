import React, { useEffect, useState } from 'react'
import { getAllClassrooms, getClassroom } from '../../services/classroomService';
import { useNavigate } from 'react-router-dom';

function ClassroomTable() {

  const navigate = useNavigate();
  const [classrooms, setClassrooms] = useState([]);

  useEffect(() => {

    async function fetchClassroom(){

        const data = await getAllClassrooms();
        setClassrooms(data.dtoT);

    }

    fetchClassroom();

  },[]);

  async function handleClick(classroomId){
  
      const response = await getClassroom(classroomId);
      const classroom = response.dtoT;
      console.log(classroom);
      navigate("/classroom",{state:{classroom:classroom}});
  
  }


  return (
    <div className="reservationTable-container">
      <h3 className="reservationTable-title">All classrooms</h3>
      <table className="reservationTable-table">
        <thead>
          <tr>
            <th>Classroom number</th>
            <th>Classroom type</th>
            <th>Capacity</th>
            <th>Number of computers</th>
          </tr>
        </thead>
        <tbody>
          {classrooms.map((classr) => (
            <tr
              key={classr.id}
              onClick={() => handleClick(classr.id)}
              className="reservationTable-row"
            >
              <td>{classr.classRoomNumber}</td>
              <td>{classr.classRoomType}</td>
              <td>{classr.capacity}</td>
              <td>{classr.numberOfComputers}</td>
            </tr>
          ))}
        </tbody>
      </table>
      
    </div>
  )
}

export default ClassroomTable