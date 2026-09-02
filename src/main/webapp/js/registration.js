document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("registrationForm");
    const role = document.getElementById("role");
    const dentistFields = document.getElementById("dentistFields");
    const specialization = document.getElementById("specialization");
    const phone = document.getElementById("phone");

    const successModal = document.getElementById("successModal");
    const errorModal = document.getElementById("errorModal");
    const errorModalText = document.getElementById("errorModalText");
    const errorModalClose = document.getElementById("errorModalClose");
    const message = document.getElementById("message");

    function updateDentistFields() {
        const isDentist = role.value === "DENTIST";
        dentistFields.style.display = isDentist ? "block" : "none";
        specialization.required = isDentist;
        
        phone.required = true;

        if (!isDentist) {
            specialization.value = "";
            clearFieldError(specialization, "specializationError");
        }
    }

    role.addEventListener("change", updateDentistFields);
    updateDentistFields();

 
    phone.addEventListener("input", function () {
        clearFieldError(phone, "phoneError");
    });

    function setFieldError(input, errorId, text) {
        input.classList.add("input-invalid");
        document.getElementById(errorId).textContent = text;
    }

    function clearFieldError(input, errorId) {
        input.classList.remove("input-invalid");
        document.getElementById(errorId).textContent = "";
    }

    function validateForm() {
        let valid = true;

        const fullName = document.getElementById("fullName");
        const username = document.getElementById("username");
        const email = document.getElementById("email");
        const password = document.getElementById("password");

        
        if (fullName.value.trim() === "") {
            setFieldError(fullName, "fullNameError", "Full name is required.");
            valid = false;
        } else if (!/^[A-Za-z.\s]+$/.test(fullName.value.trim())) {
            setFieldError(fullName, "fullNameError", "Use letters, spaces and dots only.");
            valid = false;
        } else {
            clearFieldError(fullName, "fullNameError");
        }

        
        if (username.value.trim() === "") {
            setFieldError(username, "usernameError", "Username is required.");
            valid = false;
        } else if (!/^[A-Za-z0-9_.-]{3,30}$/.test(username.value.trim())) {
            setFieldError(username, "usernameError", "Use 3-30 letters, numbers, dot, underscore or hyphen.");
            valid = false;
        } else {
            clearFieldError(username, "usernameError");
        }

        
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (email.value.trim() === "") {
            setFieldError(email, "emailError", "Email is required.");
            valid = false;
        } else if (!emailPattern.test(email.value.trim())) {
            setFieldError(email, "emailError", "Enter a valid email address.");
            valid = false;
        } else {
            clearFieldError(email, "emailError");
        }

       
        if (password.value === "") {
            setFieldError(password, "passwordError", "Password is required.");
            valid = false;
        } else if (password.value.length < 6) {
            setFieldError(password, "passwordError", "Password must contain at least 6 characters.");
            valid = false;
        } else {
            clearFieldError(password, "passwordError");
        }

        
        if (role.value === "") {
            setFieldError(role, "roleError", "Please select a role.");
            valid = false;
        } else {
            clearFieldError(role, "roleError");
        }

        
        if (phone.value.trim() === "") {
            setFieldError(
                phone,
                "phoneError",
                "Phone number is required."
            );
            valid = false;
        } else if (!/^[0-9]{10}$/.test(phone.value.trim())) {
            setFieldError(
                phone,
                "phoneError",
                "Phone number must contain exactly 10 digits (numbers only)."
            );
            valid = false;
        } else {
            clearFieldError(phone, "phoneError");
        }

       
        if (role.value === "DENTIST") {
            if (specialization.value.trim() === "") {
                setFieldError(
                    specialization,
                    "specializationError",
                    "Specialization is required for dentists."
                );
                valid = false;
            } else if (!/^[A-Za-z0-9&.,'()\-\s]+$/.test(specialization.value.trim())) {
                setFieldError(
                    specialization,
                    "specializationError",
                    "Enter a valid specialization."
                );
                valid = false;
            } else {
                clearFieldError(specialization, "specializationError");
            }

        }

        return valid;
    }

    form.addEventListener("submit", function (event) {
        if (!validateForm()) {
            event.preventDefault();
            return;
        }

    });

    function showErrorPopup(text) {
        errorModalText.textContent = text;
        errorModal.classList.add("show");
        errorModal.setAttribute("aria-hidden", "false");
    }

    errorModalClose.addEventListener("click", function () {
        errorModal.classList.remove("show");
        errorModal.setAttribute("aria-hidden", "true");
    });

   
    const params = new URLSearchParams(window.location.search);
    const error = params.get("error");
    const success = params.get("success");

    if (error) {
        showErrorPopup(error);
        message.textContent = error;
        message.className = "message error";
        history.replaceState({}, document.title, window.location.pathname);
    }

    if (success) {
        successModal.classList.add("show");
        successModal.setAttribute("aria-hidden", "false");

        history.replaceState({}, document.title, window.location.pathname);

        setTimeout(function () {
            window.location.href = "Login.html";
        }, 2200);
    }
});
