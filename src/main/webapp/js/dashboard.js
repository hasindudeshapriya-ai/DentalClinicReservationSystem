/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


document.addEventListener("DOMContentLoaded", function () {

    loadDashboard();

    
    setInterval(loadDashboard, 30000);
});


async function loadDashboard() {

    try {

        const response =
            await fetch("api/dashboard", {
                cache: "no-store"
            });

        if (!response.ok) {

            console.error(
                "Dashboard API error:",
                response.status
            );

            return;
        }

        const data =
            await response.json();

        if (!data.success) {
            return;
        }



        const cards =
            document.querySelectorAll(
                ".card-content h2"
            );

        if (cards.length >= 4) {

            cards[0].textContent =
                data.totalPatients;

            cards[1].textContent =
                data.totalAppointments;

            cards[2].textContent =
                data.totalDentists;

            cards[3].textContent =
                "Rs. " +
                Number(data.totalRevenue)
                    .toLocaleString(
                        "en-LK",
                        {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2
                        }
                    );
        }



        const appointmentStats =
            document.querySelectorAll(
                ".appointment-stat strong"
            );

        if (appointmentStats.length >= 3) {

            appointmentStats[0].textContent =
                data.todayScheduled;

            appointmentStats[1].textContent =
                data.todayCompleted;

            appointmentStats[2].textContent =
                data.todayCancelled;
        }



        const overviewNumbers =
            document.querySelectorAll(
                ".overview-number"
            );

        if (overviewNumbers.length >= 4) {

            overviewNumbers[0].textContent =
                data.pendingAppointments;

            overviewNumbers[1].textContent =
                data.completedAppointments;

            overviewNumbers[2].textContent =
                data.outstandingBills;

            overviewNumbers[3].textContent =
                data.totalDentists;
        }


        const emptyMessage =
            document.querySelector(
                ".empty-message"
            );

        if (emptyMessage) {

            if (data.todayTotal > 0) {

                emptyMessage.innerHTML = `
                    <div>📅</div>

                    <strong>
                        ${data.todayTotal}
                        appointment(s) today
                    </strong>

                    <p>
                        Today's appointment schedule
                        is available.
                    </p>
                `;

            } else {

                emptyMessage.innerHTML = `
                    <div>📅</div>

                    <strong>
                        No appointments today
                    </strong>

                    <p>
                        There are currently no
                        appointments scheduled for today.
                    </p>
                `;
            }
        }



        const subtitle =
            document.querySelector(
                ".topbar p"
            );

        if (subtitle) {

            if (data.role === "DENTIST") {

                subtitle.textContent =
                    "Overview of your appointments, patients and payments.";

            } else if (data.role === "CASHIER") {

                subtitle.textContent =
                    "Overview of clinic patients, appointments and billing.";

            } else {

                subtitle.textContent =
                    "Welcome back! Here's an overview of your dental clinic.";
            }
        }
        
        if (data.role === "DENTIST") {

    const cardLabels =
        document.querySelectorAll(
            ".card-content span"
        );

    if (cardLabels.length >= 4) {

        cardLabels[0].textContent =
            "My Patients";

        cardLabels[1].textContent =
            "My Appointments";

        cardLabels[2].textContent =
            "My Profile";

        cardLabels[3].textContent =
            "My Payments";
    }
}

    } catch (error) {

        console.error(
            "Dashboard loading failed:",
            error
        );
    }
}