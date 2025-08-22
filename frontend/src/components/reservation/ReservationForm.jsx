import React, { useEffect, useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { getAllDepartments } from "../../services/departmentService";
import { getAllStudentOrganizations } from "../../services/studentOrganizationService";
import {
  getAllAvailableClassrooms,
  saveReservation,
} from "../../services/reservationService";
import './ReservationForm.css';

function ReservationForm() {
  const reservationTypes = [
    "EXAM",
    "COLLOQUIUM",
    "COUNCIL",
    "COURSE",
    "DEPARTMENT_MEETING",
    "OTHER_MEETING",
    "OTHER_WORKSHOP",
    "STUDENT_ORGANIZATION",
  ];

  const councilTypes = ["NNV", "OAS", "MAS", "DAS"];
  const [selectedOption, setSelectedOption] = useState(null);
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedStartingTime, setSelectedStartingTime] = useState(null);
  const [selectedEndingTime, setSelectedEndingTime] = useState(null);
  const [subjectName, setSubjectName] = useState(null);
  const [selectedDepartment, setSelectedDepartment] = useState(null);
  const [departments, setDepartments] = useState([]);
  const [selectedCouncilType, setSelectedCouncilType] = useState(null);
  const [shortDescription, setShortDescription] = useState(null);
  const [studentOrganizations, setStudentOrganizations] = useState([]);
  const [selectedStudentOrganization, setSelectedStudentOrganization] =
    useState(null);
  const [otherWorkshopName, setOtherWorkshopName] = useState(null);
  const [participants, setParticipants] = useState([
    { firstName: "", lastName: "" },
  ]);
  const [selectedClassrooms, setSelectedClassrooms] = useState([]);
  const [classrooms, setClassrooms] = useState([]);
  const [showClassrooms, setShowClassrooms] = useState(false);

  useEffect(() => {
    async function fetchData() {
      const dep = await getAllDepartments();
      setDepartments(dep.dtoT);
      const studOrg = await getAllStudentOrganizations();
      setStudentOrganizations(studOrg.dtoT);
    }

    fetchData();
  }, []);



  const fetchAvailableClassrooms = async () => {

    if(selectedStartingTime > selectedEndingTime){

        alert("Start time must be before end time!");
        return;
    }


    if (selectedDate && selectedStartingTime && selectedEndingTime) {
      const reservation = {
        date: formatDate(selectedDate),
        startTime: formatTime(selectedStartingTime),
        endTime: formatTime(selectedEndingTime),
      };

      try {
        const result = await getAllAvailableClassrooms(reservation);
        setClassrooms(result.dtoT);
        setShowClassrooms(true);
      } catch (error) {
        setClassrooms([]);
        setShowClassrooms(false);
        alert(error);
      }
    }
  };

  function cleanForm () {

    setSelectedOption(null);
    setSelectedDate(null);
    setSelectedStartingTime("");
    setSelectedEndingTime("");
    setSelectedClassrooms([]);
    setClassrooms([]);
    setSubjectName(null);
    setSelectedCouncilType(null);
    setShortDescription(null);
    setOtherWorkshopName(null);
    setSelectedDepartment(null);
    setSelectedStudentOrganization(null);
    setParticipants([]);
    setShowClassrooms(false);

  }

  const handleParticipantChange = (index, field, value) => {
    const newParticipants = [...participants];
    newParticipants[index][field] = value;
    setParticipants(newParticipants);
  };

  const addParticipant = () => {
    setParticipants([...participants, { firstName: "", lastName: "" }]);
  };

  const removeParticipant = (index) => {
    const newParticipants = participants.filter((_, i) => i !== index);
    setParticipants(newParticipants);
  };

  function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");

    return `${year}-${month}-${day}`;
  }

  function formatTime(time) {
    if (!time) return null;
    return time + ":00";
  }

  const createReservation = async () => {



    const formattedDate = formatDate(selectedDate);
    const formattedStartTime = formatTime(selectedStartingTime);
    const formattedEndTime = formatTime(selectedEndingTime);
    const formatedClassrooms = selectedClassrooms.map((id) => ({ id }));
    const reservation = {
      reservationPurpose: selectedOption,
      date: formattedDate,
      startTime: formattedStartTime,
      endTime: formattedEndTime,
      user: JSON.parse(localStorage.getItem("user")),
      classrooms: formatedClassrooms,
      subjectName: subjectName,
      councilType: selectedCouncilType,
      shortDescription: shortDescription,
      name: otherWorkshopName,
      department: selectedDepartment,
      studentOrganization: selectedStudentOrganization,
      workshopParticipants: participants,
    };

    const data = await saveReservation(reservation);
    alert(data.message);
    cleanForm();
  };

  const handleCheckboxChange = (id) => {
    if (selectedClassrooms.includes(id)) {
      setSelectedClassrooms(selectedClassrooms.filter((c) => c !== id));
    } else {
      setSelectedClassrooms([...selectedClassrooms, id]);
    }
  };

  return (
  <form className="reservation-form">
    <label>Please select type of reservation:</label>
    <select
      className="reservation-select"
      value={selectedOption}
      onChange={(e) => setSelectedOption(e.target.value)}
    >
      {reservationTypes.map((opt, index) => (
        <option key={index} value={opt}>
          {opt}
        </option>
      ))}
    </select>

    <label>Please select date of your reservation:</label>
    <DatePicker
      className="reservation-input"
      selected={selectedDate}
      onChange={(date) => setSelectedDate(date)}
      dateFormat="dd/MM/yyyy"
      placeholderText="--Date--"
      required
    />

    <label>Please enter starting time of your reservation:</label>
    <input
      className="reservation-input"
      type="time"
      onChange={(e) => setSelectedStartingTime(e.target.value)}
      value={selectedStartingTime}
      required
    />

    <label>Please enter ending time of your reservation:</label>
    <input
      className="reservation-input"
      type="time"
      onChange={(e) => setSelectedEndingTime(e.target.value)}
      value={selectedEndingTime}
      required
    />

    <button className="reservation-button" onClick={fetchAvailableClassrooms}>
      Check available classrooms
    </button>

    {showClassrooms && (
      <>
        <h3>Pick classrooms for reservation:</h3>
        <div className="classroom-list">
          {classrooms.map((classroom) => (
            <label key={classroom.id}>
              <input
                type="checkbox"
                value={classroom.id}
                checked={selectedClassrooms.includes(classroom.id)}
                onChange={(e) => handleCheckboxChange(parseInt(e.target.value))}
              />
              {classroom.classRoomNumber}
            </label>
          ))}
        </div>
      </>
    )}

    {(selectedOption === "EXAM" ||
      selectedOption === "COURSE" ||
      selectedOption === "COLLOQUIUM") && (
      <div>
        <label>Please enter subject name:</label>
        <input
          className="reservation-input"
          value={subjectName}
          onChange={(e) => setSubjectName(e.target.value)}
          required
        />
      </div>
    )}

    {selectedOption === "DEPARTMENT_MEETING" && (
      <div>
        <label>Please pick department:</label>
        <select
          className="reservation-select"
          value={selectedDepartment}
          onChange={(e) => setSelectedDepartment(e.target.value)}
          required
        >
          {departments.map((dep, index) => (
            <option key={index} value={dep.id}>
              {dep.name}
            </option>
          ))}
        </select>
      </div>
    )}

    {selectedOption === "COUNCIL" && (
      <div>
        <label>Please pick council type:</label>
        <select
          className="reservation-select"
          value={selectedCouncilType}
          onChange={(e) => setSelectedCouncilType(e.target.value)}
          required
        >
          {councilTypes.map((cou, index) => (
            <option key={index} value={cou}>
              {cou}
            </option>
          ))}
        </select>
      </div>
    )}

    {selectedOption === "OTHER_MEETING" && (
      <div>
        <label>Please enter short description:</label>
        <textarea
          className="reservation-textarea"
          id="shortDescription"
          name="shortDescription"
          rows="4"
          cols="50"
          onChange={(e) => setShortDescription(e.target.value)}
          required
        ></textarea>
      </div>
    )}

    {selectedOption === "STUDENT_ORGANIZATION" && (
      <div>
        <label>Please pick student organization:</label>
        <select
          className="reservation-select"
          value={selectedStudentOrganization}
          onChange={(e) => setSelectedStudentOrganization(e.target.value)}
          required
        >
          <option value="">--Select Organization--</option>
          {studentOrganizations.map((so, index) => (
            <option key={index} value={so.id}>
              {so.name}
            </option>
          ))}
        </select>
      </div>
    )}

    {selectedOption === "OTHER_WORKSHOP" && (
      <div>
        <label>Please enter workshop name:</label>
        <input
          className="reservation-input"
          value={otherWorkshopName}
          onChange={(e) => setOtherWorkshopName(e.target.value)}
          required
        />
        <div className="participants">
          <label>Participants:</label>
          {participants.map((participant, index) => (
            <div className="participant-row" key={index}>
              <input
                type="text"
                placeholder="First Name"
                value={participant.firstName}
                className="reservation-input"
                onChange={(e) =>
                  handleParticipantChange(index, "firstName", e.target.value)
                }
              />
              <input
                type="text"
                placeholder="Last Name"
                value={participant.lastName}
                className="reservation-input"
                onChange={(e) =>
                  handleParticipantChange(index, "lastName", e.target.value)
                }
              />
              <button type="button" onClick={() => removeParticipant(index)}>
                Remove
              </button>
            </div>
          ))}
          <button type="button" onClick={addParticipant}>
            Add Participant
          </button>
        </div>
      </div>
    )}
    <br />
    <button className="reservation-button" onClick={createReservation}>
      Create reservation
    </button>
  </form>
);

}

export default ReservationForm;
