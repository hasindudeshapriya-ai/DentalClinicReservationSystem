document.addEventListener("DOMContentLoaded", function () {

    const params = new URLSearchParams(window.location.search);
    const billId = params.get("id");

    const loadingMessage =
        document.getElementById("loadingMessage");

    const billContent =
        document.getElementById("billContent");

    const errorMessage =
        document.getElementById("errorMessage");

    if (!billId) {
        showError("Bill ID is missing.");
        return;
    }

    fetch("ViewBillServlet?id=" + encodeURIComponent(billId))
        .then(response => {

            if (!response.ok) {
                throw new Error("Unable to load bill.");
            }

            return response.json();
        })
        .then(bill => {

            document.getElementById("billId").textContent =
                bill.billId;

            document.getElementById("appointmentId").textContent =
                bill.appointmentId;

            document.getElementById("patientName").textContent =
                bill.patientName || ("Patient #" + bill.patientId);

            document.getElementById("dentistName").textContent =
                bill.dentistName || ("Dentist #" + bill.dentistId);

            document.getElementById("createdDate").textContent =
                bill.createdDate || "-";

            document.getElementById("treatmentCharge").textContent =
                "Rs. " + Number(bill.treatmentCharge).toFixed(2);

            document.getElementById("discount").textContent =
                "Rs. " + Number(bill.discount).toFixed(2);

            document.getElementById("total").textContent =
                "Rs. " + Number(bill.total).toFixed(2);

            document.getElementById("amountPaid").textContent =
                "Rs. " + Number(bill.amountPaid).toFixed(2);

            document.getElementById("balance").textContent =
                "Rs. " + Number(bill.balance).toFixed(2);


            const statusBadge =
                document.getElementById("statusBadge");

            statusBadge.textContent = bill.status;

            statusBadge.className = "status";

            if (bill.status === "Paid") {
                statusBadge.classList.add("status-paid");
            }
            else if (bill.status === "Partial") {
                statusBadge.classList.add("status-partial");
            }
            else {
                statusBadge.classList.add("status-unpaid");
            }


            document.getElementById("editBillButton").href =
                "edit-bill.html?id=" + bill.billId;


            loadingMessage.style.display = "none";
            billContent.style.display = "block";

        })
        .catch(error => {

            console.error(error);

            loadingMessage.style.display = "none";

            showError(
                "Unable to load bill details. Please try again."
            );
        });


    function showError(message) {

        errorMessage.textContent = message;
        errorMessage.style.display = "block";
    }

});



async function setupBillPrintAccess() {
    const button = document.getElementById("printBillButton");
    if (!button) return;

    try {
        const response = await fetch("api/session", { cache: "no-store" });
        if (!response.ok) return;
        const session = await response.json();
        const role = String(session.role || "").toUpperCase();
        button.style.display = (role === "ADMIN" || role === "CASHIER") ? "inline-block" : "none";
    } catch (error) {
        console.warn("Unable to determine bill print access.", error);
    }
}

function printCurrentBill() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get("id");
    if (!id) {
        alert("Bill ID is missing.");
        return;
    }
    window.open("PrintBillServlet?id=" + encodeURIComponent(id), "_blank", "noopener");
}

document.addEventListener("DOMContentLoaded", setupBillPrintAccess);
