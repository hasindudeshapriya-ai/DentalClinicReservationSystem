document.addEventListener("DOMContentLoaded", function () {
    const chargeInput = document.getElementById("treatmentCharge");
    const discountInput = document.getElementById("discount");
    const paidInput = document.getElementById("amountPaid");

    function money(value) {
        return "Rs. " + (Number(value) || 0).toFixed(2);
    }

    function calculateBill() {
        if (!chargeInput || !discountInput || !paidInput) return;

        const charge = Math.max(parseFloat(chargeInput.value) || 0, 0);
        const discount = Math.max(parseFloat(discountInput.value) || 0, 0);
        const paid = Math.max(parseFloat(paidInput.value) || 0, 0);
        const total = Math.max(charge - discount, 0);
        const balance = Math.max(total - paid, 0);

        const set = (id, value) => {
            const el = document.getElementById(id);
            if (el) el.textContent = money(value);
        };

        set("displayCharge", charge);
        set("displayDiscount", discount);
        set("displayTotal", total);
        set("displayPaid", paid);
        set("displayBalance", balance);
    }

    [chargeInput, discountInput, paidInput].forEach(input => {
        if (input) input.addEventListener("input", calculateBill);
    });
    calculateBill();

    const params = new URLSearchParams(window.location.search);
    const error = params.get("error");
    const success = params.get("success");
    const errorBox = document.getElementById("errorMessage");

    if (error && errorBox) {
        errorBox.textContent = error;
        errorBox.classList.remove("hidden");
    }

    const message = document.getElementById("message");
    if (success === "1" && message) {
        message.textContent = "Bill created successfully.";
        message.className = "message success";
    }

    const tableBody = document.getElementById("billingTableBody");
    if (!tableBody) return;

    loadBillingTable();

    async function loadBillingTable() {
        try {
            const sessionResponse = await fetch("api/session", {
                cache: "no-store",
                credentials: "same-origin"
            });

            if (!sessionResponse.ok) {
                throw new Error("Unable to load login session (" +
                    sessionResponse.status + ")");
            }

            const session = await sessionResponse.json();

            if (!session.authenticated) {
                throw new Error("User session has expired.");
            }

            const currentRole =
                String(session.role || "").trim().toUpperCase();

            const canPrint =
                currentRole === "ADMIN" ||
                currentRole === "CASHIER";

            const billsResponse = await fetch(
                "BillingServlet?format=json",
                {
                    headers: {
                        "Accept": "application/json"
                    },
                    cache: "no-store",
                    credentials: "same-origin"
                }
            );

            if (!billsResponse.ok) {
                throw new Error(
                    "Unable to load bills (HTTP " +
                    billsResponse.status + ")"
                );
            }

            const bills = await billsResponse.json();

            if (!Array.isArray(bills) || bills.length === 0) {
                tableBody.innerHTML =
                    '<tr><td colspan="12" class="no-bills">' +
                    'No bills found.</td></tr>';
                return;
            }

            tableBody.innerHTML = bills.map(function (bill) {

                const status =
                    String(bill.status || "Unpaid");

                const lower = status.toLowerCase();

                let statusClass = "status-unpaid";

                if (lower === "paid") {
                    statusClass = "status-paid";
                } else if (
                    lower === "partial" ||
                    lower === "partially paid"
                ) {
                    statusClass = "status-partial";
                }

                const patient =
                    bill.patientName ||
                    ("Patient #" + bill.patientId);

                const dentist =
                    bill.dentistName ||
                    ("Dentist #" + bill.dentistId);

                const created =
                    bill.createdDate || "-";

                const billId =
                    encodeURIComponent(bill.billId);

                let printButton = "";

                if (canPrint) {
                    printButton = `
                        <a
                            class="action-print"
                            href="PrintBillServlet?id=${billId}"
                            target="_blank"
                            rel="noopener noreferrer"
                            title="Print Bill">
                            🖨 Print
                        </a>
                    `;
                }

                return `
                    <tr>
                        <td>${escapeHtml(bill.billId)}</td>
                        <td>${escapeHtml(bill.appointmentId)}</td>
                        <td>${escapeHtml(patient)}</td>
                        <td>${escapeHtml(dentist)}</td>
                        <td class="amount">
                            ${money(bill.treatmentCharge)}
                        </td>
                        <td>${money(bill.discount)}</td>
                        <td class="amount">
                            ${money(bill.totalAmount)}
                        </td>
                        <td class="paid">
                            ${money(bill.amountPaid)}
                        </td>
                        <td class="balance">
                            ${money(bill.balance)}
                        </td>
                        <td>
                            <span class="status ${statusClass}">
                                ${escapeHtml(status)}
                            </span>
                        </td>
                        <td>${escapeHtml(created)}</td>
                        <td>
                            <div class="action-links">
                                <a
                                    class="action-view"
                                    href="view-bill.html?id=${billId}">
                                    View
                                </a>

                                <a
                                    class="action-edit"
                                    href="edit-bill.html?id=${billId}">
                                    Edit
                                </a>

                                ${printButton}
                            </div>
                        </td>
                    </tr>
                `;
            }).join("");


            if (!canPrint) {
                document
                    .querySelectorAll(".action-print")
                    .forEach(function (button) {
                        button.remove();
                    });
            }

        } catch (error) {
            console.error("Billing load error:", error);

            tableBody.innerHTML =
                '<tr><td colspan="12" class="no-bills error-text">' +
                'Unable to load bills. Check the database connection ' +
                'and Tomcat.</td></tr>';
        }
    }

    function escapeHtml(value) {
        return String(value ?? "")
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
});
