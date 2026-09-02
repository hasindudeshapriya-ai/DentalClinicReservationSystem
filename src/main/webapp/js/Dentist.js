document.addEventListener("DOMContentLoaded", function () {

    const form = document.querySelector("form");

    if (!form) {
        return;
    }

    form.addEventListener("submit", function (event) {

        const phoneInput = form.querySelector(
            'input[name="phone"]'
        );

        if (!phoneInput) {
            return;
        }

        
        if (!validatePhone(phoneInput)) {

            event.preventDefault();

            phoneInput.focus();
        }

    });

});


function validatePhone(phoneInput) {

    const phone = phoneInput.value.trim();

    if (phone === "") {

        showPhoneError(
            phoneInput,
            "Phone number is required."
        );

        return false;
    }

    if (!/^[0-9]{10}$/.test(phone)) {

        showPhoneError(
            phoneInput,
            "Phone number must contain exactly 10 digits."
        );

        return false;
    }

    clearPhoneError(phoneInput);

    return true;
}


function showPhoneError(input, message) {

    input.classList.add("input-error");

    let error =
        input.parentElement.querySelector(".field-error");

    if (!error) {

        error = document.createElement("small");

        error.className = "field-error";

        input.parentElement.appendChild(error);
    }

    error.textContent = message;
}


function clearPhoneError(input) {

    input.classList.remove("input-error");

    const error =
        input.parentElement.querySelector(".field-error");

    if (error) {
        error.textContent = "";
    }
}