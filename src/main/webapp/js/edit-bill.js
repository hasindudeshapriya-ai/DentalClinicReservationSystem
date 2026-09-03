document.addEventListener("DOMContentLoaded", function () {

    const params = new URLSearchParams(window.location.search);
    const billId = params.get("id");

    const form = document.getElementById("editBillForm");
    const loading = document.getElementById("loadingMessage");
    const message = document.getElementById("formMessage");

    if (!billId) {
        showMessage("Bill ID is missing.", "error");
        return;
    }

    loadBill();


    function loadBill() {

        fetch("ViewBillServlet?id=" + encodeURIComponent(billId))

            .then(response => {

                if (!response.ok) {
                    throw new Error("Failed to load bill");
                }

                return response.json();

            })

            .then(bill => {

                document.getElementById("billId").value =
                    bill.billId;

                document.getElementById("appointmentId").value =
                    bill.appointmentId;

                document.getElementById("patientName").value =
                    bill.patientName;

                document.getElementById("dentistName").value =
                    bill.dentistName;

                document.getElementById("treatmentCharge").value =
                    bill.treatmentCharge;

                document.getElementById("discount").value =
                    bill.discount;

                document.getElementById("amountPaid").value =
                    bill.amountPaid;

                document.getElementById("status").value =
                    bill.status;

                updateSummary();

                loading.style.display = "none";
                form.style.display = "block";

            })

            .catch(error => {

                console.error(error);

                loading.textContent =
                    "Unable to load bill details.";
            });
    }


    document.getElementById("treatmentCharge")
        .addEventListener("input", updateSummary);

    document.getElementById("discount")
        .addEventListener("input", updateSummary);

    document.getElementById("amountPaid")
        .addEventListener("input", updateSummary);


    function updateSummary() {

        const charge =
            parseFloat(
                document.getElementById("treatmentCharge").value
            ) || 0;

        const discount =
            parseFloat(
                document.getElementById("discount").value
            ) || 0;

        const paid =
            parseFloat(
                document.getElementById("amountPaid").value
            ) || 0;

        const total =
            Math.max(0, charge - discount);

        const balance =
            Math.max(0, total - paid);


        document.getElementById("summaryCharge")
            .textContent =
            "Rs. " + charge.toFixed(2);

        document.getElementById("summaryDiscount")
            .textContent =
            "Rs. " + discount.toFixed(2);

        document.getElementById("summaryTotal")
            .textContent =
            "Rs. " + total.toFixed(2);

        document.getElementById("summaryPaid")
            .textContent =
            "Rs. " + paid.toFixed(2);

        document.getElementById("summaryBalance")
            .textContent =
            "Rs. " + balance.toFixed(2);


        const status =
            document.getElementById("status");

        if (paid <= 0) {
            status.value = "Unpaid";
        }
        else if (paid >= total) {
            status.value = "Paid";
        }
        else {
            status.value = "Partial";
        }
    }


    form.addEventListener("submit", function (event) {

        event.preventDefault();

        const charge =
            parseFloat(
                document.getElementById("treatmentCharge").value
            ) || 0;

        const discount =
            parseFloat(
                document.getElementById("discount").value
            ) || 0;

        const paid =
            parseFloat(
                document.getElementById("amountPaid").value
            ) || 0;

        const total =
            Math.max(0, charge - discount);


        if (discount > charge) {
            showMessage(
                "Discount cannot be greater than the treatment charge.",
                "error"
            );
            return;
        }

        if (paid > total) {
            showMessage(
                "Amount paid cannot be greater than the total.",
                "error"
            );
            return;
        }


        const data = new URLSearchParams();

        data.append(
            "billId",
            document.getElementById("billId").value
        );

        data.append(
            "treatmentCharge",
            charge
        );

        data.append(
            "discount",
            discount
        );

        data.append(
            "amountPaid",
            paid
        );

        data.append(
            "status",
            document.getElementById("status").value
        );


        fetch("UpdateBillServlet", {

            method: "POST",

            headers: {
                "Content-Type":
                    "application/x-www-form-urlencoded"
            },

            body: data.toString()

        })

        .then(response => response.json())

        .then(result => {

            if (result.success) {

                showMessage(
                    "Bill updated successfully.",
                    "success"
                );

                setTimeout(function () {

                    window.location.href =
                        "billing.html";

                }, 1000);

            }
            else {

                showMessage(
                    result.message ||
                    "Unable to update bill.",
                    "error"
                );
            }

        })

        .catch(error => {

            console.error(error);

            showMessage(
                "Server error while updating bill.",
                "error"
            );
        });

    });


    function showMessage(text, type) {

        message.textContent = text;
        message.className =
            "form-message " + type;
    }

});
