/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener("DOMContentLoaded", function () {

    loadAppointmentData();

});


async function loadAppointmentData() {

    try {

        const response =
            await fetch("AppointmentServlet", {
                method: "GET",
                cache: "no-store"
            });

        if (!response.ok) {

            throw new Error(
                "Failed to load appointment data"
            );
        }

        const data =
            await response.json();

        if (!data.success) {

            console.error(
                data.message
            );

            return;
        }

        loadPatients(data.patients);

        loadDentists(data.dentists);

    } catch (error) {

        console.error(
            "Appointment data loading error:",
            error
        );

        showDropdownError();
    }
}


function loadPatients(patients) {

    const patientSelect =
        document.querySelector(
            'select[name="patientId"]'
        );

    if (!patientSelect) {
        return;
    }

    patientSelect.innerHTML =
        '<option value="">-- Select Patient --</option>';

    if (!patients || patients.length === 0) {

        patientSelect.innerHTML +=
            '<option value="" disabled>' +
            'No patients available' +
            '</option>';

        return;
    }

    patients.forEach(function (patient) {

        const option =
            document.createElement("option");

        option.value =
            patient.patientId;

        option.textContent =
            patient.firstName +
            " " +
            patient.lastName;

        patientSelect.appendChild(option);
    });
}


function loadDentists(dentists) {

    const dentistSelect =
        document.querySelector(
            'select[name="dentistId"]'
        );

    if (!dentistSelect) {
        return;
    }

    dentistSelect.innerHTML =
        '<option value="">-- Select Dentist --</option>';

    if (!dentists || dentists.length === 0) {

        dentistSelect.innerHTML +=
            '<option value="" disabled>' +
            'No dentists available' +
            '</option>';

        return;
    }

    dentists.forEach(function (dentist) {

        const option =
            document.createElement("option");

        option.value =
            dentist.dentistId;

        option.textContent =
            dentist.dentistName;

        dentistSelect.appendChild(option);
    });
}


function showDropdownError() {

    const patientSelect =
        document.querySelector(
            'select[name="patientId"]'
        );

    const dentistSelect =
        document.querySelector(
            'select[name="dentistId"]'
        );

    if (patientSelect) {

        patientSelect.innerHTML =
            '<option value="">Unable to load patients</option>';
    }

    if (dentistSelect) {

        dentistSelect.innerHTML =
            '<option value="">Unable to load dentists</option>';
    }
}
