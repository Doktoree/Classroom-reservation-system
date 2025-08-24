import React, { useEffect, useState } from 'react'
import { deleteClassroom, getClassroom, saveClassroom, updateClassroom } from '../../services/classroomService';
import { useNavigate } from 'react-router-dom';

function ClassroomForm({classroom}) {

    const [header, setHeader] = useState("Create new classroom");
    const [id, setId] = useState(null);
    const [classroomNumber, setClassroomNumber] = useState(null);
    const [capacity, setCapacity] = useState(1);
    const classroomTypes = ["AMPHITHEATER", "CLASSROOM", "COMPUTER_LAB"];
    const [classroomType, setClassroomType] = useState(classroomTypes[0]);
    const [numberOfComputers, setNumberOfComputers] = useState(0);
    const [headerButton, setHeaderButton] = useState("Create new classroom");
    const navigate = useNavigate();

    useEffect(()=>{

        if(classroom){
            fillForm();
            setHeader("Classroom information");
            setHeaderButton("Update classroom");
        }

        else{

          cleanForm();

        }
            

    },[classroom])

    function fillForm(){

        setId(classroom.id);
        setClassroomNumber(classroom.classRoomNumber);
        setCapacity(classroom.capacity);
        setClassroomType(classroom.classRoomType);
        setNumberOfComputers(classroom.numberOfComputers);

    }

    function cleanForm(){

      setId(null);
      setClassroomNumber(null);
      setCapacity(1);
      setClassroomType(classroomTypes[0]);
      setNumberOfComputers(0);
    }

   async function handleUpdateClassroom(e){

        e.preventDefault();

        const classroom = {

            "id":id,
            "classRoomNumber":classroomNumber,
            "capacity":capacity,
            "classRoomType":classroomType,
            "numberOfComputers":numberOfComputers

        };

        try {

            const data = await updateClassroom(classroom);
            alert(data.message);
            navigate("/classroom", { state: { view: true } });
            
        } catch (error) {

            alert(data.message);
            
        }

    }

    async function handleCreateClassroom(e){

        e.preventDefault();

        const classroom = {

            
            "classRoomNumber":classroomNumber,
            "capacity":capacity,
            "classRoomType":classroomType,
            "numberOfComputers":numberOfComputers

        };

        try {

            const data = await saveClassroom(classroom);
            alert(data.message);
            navigate(0);
           
            
        } catch (error) {

            alert(data.message);
            
        }

    }

    async function handleDeleteClassroom(e){

        e.preventDefault();

        try {
            const data = await deleteClassroom(classroom.id);
            alert(data.message);
            navigate("/classroom", { state: { view: true } });
        } catch (error) {

            alert(data.message);
            
        }

        


    }

  return (
    <form className="user-form">
      <h2>{header}</h2>

      {classroom && (

        <div>
          <label>ID:</label>
          <input type="text" name="id" required value={id || ''} readOnly={true} onChange={e => setId(e.target.value)} />
        </div>

      )}

        
      <div>
        <label>Classroom number:</label>
        <input type="text" name="classroomNumber" required value={classroomNumber || ''}  onChange={e => setClassroomNumber(e.target.value)} />
      </div>

      <div>
        <label>Capacity:</label>
        <input type="number" min = {1} name="capacity" required value={capacity}  onChange={e => setCapacity(e.target.value)} />
      </div>

      <div>
        <label>Classroom type:</label>
        <select name="type" value={classroomType || ''} required  onChange={e => setClassroomType(e.target.value)}>
          {classroomTypes.map((r) => (
            <option key={r} value={r}>
              {r}
            </option>
          ))}
        </select>
      </div>

      <div>
        <label>Number of computers:</label>
        <input type="number" min={0} name="numberOfComputers" required value={numberOfComputers}  onChange={e => setNumberOfComputers(e.target.value)} />
      </div>

      <button type="button" onClick={classroom?handleUpdateClassroom:handleCreateClassroom}>{headerButton}</button>
      {classroom && (

        <button type="button" onClick={handleDeleteClassroom}>Delete classroom</button>

      )}

      
    </form>
  )
}

export default ClassroomForm