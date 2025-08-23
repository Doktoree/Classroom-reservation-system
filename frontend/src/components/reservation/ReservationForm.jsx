import React, { useEffect, useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { getAllDepartments } from "../../services/departmentService";
import { getAllStudentOrganizations } from "../../services/studentOrganizationService";
import {
  getAllAvailableClassrooms,
  saveReservation,
} from "../../services/reservationService";
import "./ReservationForm.css";

function ReservationForm({ initialData, onlyview = true }) {
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
  const [selectedStudentOrganization, setSelectedStudentOrganization] = useState(null);
  const [otherWorkshopName, setOtherWorkshopName] = useState(null);
  const [participants, setParticipants] = useState([
    { firstName: "", lastName: "" },
  ]);
  const [selectedClassrooms, setSelectedClassrooms] = useState([]);
  const [classrooms, setClassrooms] = useState([]);
  const [showClassrooms, setShowClassrooms] = useState(false);
  const [headerText, setHeaderText] = useState("Pick classrooms for reservation:");
  const [headerSubjectName, setHeaderSubjectName] = useState("Please enter subject name:");
  const [headerReservationType, setReservationType] = useState("Please select type of reservation:");
  const [headerDate, setHeaderDate] = useState("Please select date of your reservation:");
  const [headerStartTime, setHeaderStartTime] = useState("Please enter start time of your reservation:");
  const [headerEndTime, setHeaderEndTime] = useState("Please enter end time of your reservation:");
  const [headerDepartment, setHeaderDepartment] = useState("Please pick department:");
  const [headerCouncil, setHeaderCouncil] = useState("Please pick council type:");
  const [headerOtherMeeting, setHeaderOtherMeeting] = useState("Please enter short description:");
  const [headerStudentOrganization, setHeaderStudentOrganization] = useState("Please pick student organization:");
  const [headerOtherWorkshop, setHeaderOtherWorkshop] = useState("Enter workshop name:");

  useEffect(() => {
    async function fetchData() {
      const dep = await getAllDepartments();
      setDepartments(dep.dtoT);
      const studOrg = await getAllStudentOrganizations();
      setStudentOrganizations(studOrg.dtoT);
    }

    fetchData();
  }, []);

  useEffect(() => {
    if (initialData) {
      console.log("Ima ga!");
      setSelectedOption(initialData.reservationPurpose);
      setSelectedDate(initialData.date);
      setSelectedStartingTime(initialData.startTime);
      setSelectedEndingTime(initialData.endTime);
      setClassrooms(initialData.classrooms);
      setSubjectName(initialData.subjectName);
      setSelectedCouncilType(initialData.councilType);
      setShortDescription(initialData.shortDescription);
      setOtherWorkshopName(initialData.name);
      setSelectedDepartment(initialData.department);
      setSelectedStudentOrganization(initialData.studentOrganization);
      setParticipants(initialData.workshopParticipants);
      setShowClassrooms(true);
      setHeaderText("Classrooms reserved:");
      setHeaderSubjectName("Subject name:");
      setReservationType("Reservation type:");
      setHeaderDate("Date of reservation:")
      setHeaderStartTime("Start time of reservation:");
      setHeaderEndTime("End time of reservation:");
      setHeaderDepartment("Department:");
      setHeaderCouncil("Council:");
      setHeaderOtherMeeting("Short description:");
      setHeaderStudentOrganization("Student organization:");
      setOtherWorkshopName("Workshop name:");
    }

    console.log("Nema ga!");
  }, [initialData]);

  const fetchAvailableClassrooms = async (e) => {

    e.preventDefault();

    if (selectedStartingTime > selectedEndingTime) {
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

  function cleanForm() {
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

  const createReservation = async (e) => {

    e.preventDefault();
    const formattedDate = formatDate(selectedDate);
    const formattedStartTime = formatTime(selectedStartingTime);
    const formattedEndTime = formatTime(selectedEndingTime);
    const formatedClassrooms = selectedClassrooms.map((id) => ({ id }));
    const formatedDepartments = {id: selectedDepartment};
    const formatedStudentOrganization = {id: selectedStudentOrganization}
    const formatedWorkshopParticipants = participants;
    

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
      department: formatedDepartments,
      studentOrganization: formatedStudentOrganization,
      workshopParticipants: formatedWorkshopParticipants,
    };

    console.log(JSON.stringify(reservation));

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
      <label>{headerReservationType}</label>
      <select
        className="reservation-select"
        value={selectedOption}
        onChange={(e) => setSelectedOption(e.target.value)}
        disabled={onlyview}
      >
        {reservationTypes.map((opt, index) => (
          <option key={index} value={opt}>
            {opt}
          </option>
        ))}
      </select>

      <label>{headerDate}</label>
      <DatePicker
        className="reservation-input"
        selected={selectedDate}
        onChange={(date) => setSelectedDate(date)}
        dateFormat="dd/MM/yyyy"
        placeholderText="--Date--"
        disabled={onlyview}
        required
      />

      <label>{headerStartTime}</label>
      <input
        className="reservation-input"
        type="time"
        onChange={(e) => setSelectedStartingTime(e.target.value)}
        value={selectedStartingTime}
        readOnly={onlyview}
        required
      />

      <label>{headerEndTime}</label>
      <input
        className="reservation-input"
        type="time"
        onChange={(e) => setSelectedEndingTime(e.target.value)}
        value={selectedEndingTime}
        readOnly={onlyview}
        required
      />

      {!onlyview && (
        <button
          className="reservation-button"
          onClick={fetchAvailableClassrooms}
          readOnly
        >
          Check available classrooms
        </button>
      )}

      {showClassrooms && (
        <>
          <h3>{headerText}</h3>
          <div className="classroom-list">
            {classrooms.map((classroom) => (
              <label key={classroom.id}>
                <input
                  type="checkbox"
                  value={classroom.id}
                  checked={selectedClassrooms.includes(classroom.id)}
                  onChange={(e) =>
                    handleCheckboxChange(parseInt(e.target.value))
                  }
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
          <label>{headerSubjectName}</label>
          <input
            className="reservation-input"
            value={subjectName}
            onChange={(e) => setSubjectName(e.target.value)}
            required
            readOnly={onlyview}
          />
        </div>
      )}

      {selectedOption === "DEPARTMENT_MEETING" && (
        <div>
          <label>{headerDepartment}</label>
          <select
            className="reservation-select"
            value={selectedDepartment}
            onChange={(e) => setSelectedDepartment(parseInt(e.target.value))}
            required
            readOnly={onlyview}
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
          <label>{headerCouncil}</label>
          <select
            className="reservation-select"
            value={selectedCouncilType}
            onChange={(e) => setSelectedCouncilType(e.target.value)}
            required
            disabled={onlyview}
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
          <label>{headerOtherMeeting}</label>
          <textarea
            className="reservation-textarea"
            id="shortDescription"
            name="shortDescription"
            rows="4"
            cols="50"
            onChange={(e) => setShortDescription(e.target.value)}
            required
            value={shortDescription}
            readOnly={onlyview}
          ></textarea>
        </div>
      )}

      {selectedOption === "STUDENT_ORGANIZATION" && (
        <div>
          <label>{headerStudentOrganization}</label>
          <select
            className="reservation-select"
            value={selectedStudentOrganization.name}
            onChange={(e) => setSelectedStudentOrganization(e.target.value)}
            required
            disabled={onlyview}
          >
            
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
          <label>{headerOtherWorkshop}</label>
          <input
            className="reservation-input"
            value={otherWorkshopName}
            onChange={(e) => setOtherWorkshopName(e.target.value)}
            required
            readOnly={onlyview}
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
      {!onlyview && (
        <button className="reservation-button" type="button" onClick={createReservation}>
          Create reservation
        </button>
      )}
    </form>
  );
}

export default ReservationForm;
