document.addEventListener("DOMContentLoaded", function () {
    loadReports();
});


async function loadReports() {

    console.log("Loading live report data...");

    try {

        const response = await fetch("api/reports", {
            method: "GET",
            headers: {
                "Accept": "application/json"
            }
        });

        console.log("Report API status:", response.status);

        if (!response.ok) {
            throw new Error(
                "Report API error: HTTP " + response.status
            );
        }

        const data = await response.json();

        console.log("Live report data:", data);



        if (data.error) {
            throw new Error(data.error);
        }



        const appointments = data.appointments || {};

        setText(
            "totalAppointments",
            appointments.total || 0
        );

        setText(
            "completedAppointments",
            appointments.completed || 0
        );

        setText(
            "pendingAppointments",
            appointments.pending || 0
        );

        setText(
            "cancelledAppointments",
            appointments.cancelled || 0
        );



        setText(
            "totalPatients",
            data.totalPatients || 0
        );


        const billing = data.billing || {};

        setMoney(
            "totalRevenue",
            billing.totalRevenue || 0
        );

        setMoney(
            "outstandingBalance",
            billing.outstandingBalance || 0
        );

        setMoney(
            "totalTreatmentCharges",
            billing.treatmentCharges || 0
        );

        setMoney(
            "totalDiscounts",
            billing.discounts || 0
        );

        setMoney(
            "totalBillAmount",
            billing.totalBillAmount || 0
        );

        setMoney(
            "totalPaid",
            billing.totalPaid || 0
        );

        setMoney(
            "reportBalance",
            billing.outstandingBalance || 0
        );


        loadRecentBills(
            data.recentBills || []
        );


        console.log("Report loaded successfully.");

    } catch (error) {

        console.error(
            "Unable to load report:",
            error
        );

        showReportError(
            "Unable to load live report data."
        );
    }
}



function setText(id, value) {

    const element =
        document.getElementById(id);

    if (element) {
        element.textContent = value;
    }
}



function setMoney(id, value) {

    const element =
        document.getElementById(id);

    if (!element) {
        return;
    }

    const amount =
        Number(value) || 0;

    element.textContent =
        "Rs. " +
        amount.toLocaleString("en-LK", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
}



function loadRecentBills(bills) {

    const tableBody =
        document.getElementById(
            "recentBillingBody"
        );

    if (!tableBody) {

        console.warn(
            "recentBillingBody element not found."
        );

        return;
    }


    tableBody.innerHTML = "";



    if (!bills || bills.length === 0) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="8"
                    class="no-data">
                    No billing records found.
                </td>
            </tr>
        `;

        return;
    }



    bills.forEach(function (bill) {

        const row =
            document.createElement("tr");


        row.innerHTML = `

            <td>
                ${escapeHtml(bill.billId)}
            </td>

            <td>
                ${escapeHtml(
                    bill.patientName || "-"
                )}
            </td>

            <td>
                ${escapeHtml(
                    bill.dentistName || "-"
                )}
            </td>

            <td>
                ${formatMoney(
                    bill.totalAmount
                )}
            </td>

            <td>
                ${formatMoney(
                    bill.amountPaid
                )}
            </td>

            <td>
                ${formatMoney(
                    bill.balance
                )}
            </td>

            <td>
                <span class="status-badge ${getStatusClass(
                    bill.status
                )}">
                    ${escapeHtml(
                        bill.status || "Unpaid"
                    )}
                </span>
            </td>

            <td>
                ${formatDate(
                    bill.createdAt
                )}
            </td>

        `;

        tableBody.appendChild(row);
    });
}



function getStatusClass(status) {

    const value =
        String(status || "")
            .toLowerCase()
            .trim();


    if (value === "paid") {
        return "status-paid";
    }


    if (
        value === "partial" ||
        value === "partially paid"
    ) {
        return "status-partial";
    }


    return "status-unpaid";
}



function formatMoney(value) {

    const amount =
        Number(value) || 0;

    return "Rs. " +
        amount.toLocaleString("en-LK", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
}



function formatDate(value) {

    if (!value) {
        return "-";
    }


    try {

        const date =
            new Date(value);


        if (isNaN(date.getTime())) {
            return value;
        }


        return date.toLocaleDateString(
            "en-LK",
            {
                year: "numeric",
                month: "short",
                day: "numeric"
            }
        );

    } catch (error) {

        return value;
    }
}


function escapeHtml(value) {

    if (
        value === null ||
        value === undefined
    ) {
        return "";
    }


    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}



function showReportError(message) {

    const tableBody =
        document.getElementById(
            "recentBillingBody"
        );


    if (tableBody) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="8"
                    class="error-message">
                    ${escapeHtml(message)}
                </td>
            </tr>
        `;
    }
}


function refreshReports() {

    loadReports();
}



function printReport() {

    window.print();
}