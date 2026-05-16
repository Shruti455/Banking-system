$(function () {

    console.log("wizard.js loaded");

    const wizard = document.getElementById('bankOpeningWizard');

    const steps = wizard.querySelectorAll('.wizard-step');
    const contents = wizard.querySelectorAll('.wizard-content');

    const prevBtn = wizard.querySelector('.wizard-prev');
    const nextBtn = wizard.querySelector('.wizard-next');
    const submitBtn = wizard.querySelector('.wizard-submit');

    const progressBar = document.getElementById('bankProgressBar');
    const stepMeta = document.getElementById('bankStepMeta');

    let currentStep = 1;
    const totalSteps = steps.length;

    window.customerId = null;

    // =========================
    // UPDATE WIZARD UI
    // =========================

    function updateWizard() {

        steps.forEach((step, i) => {

            step.classList.remove('active', 'completed');

            if (i + 1 === currentStep) {
                step.classList.add('active');
            }
            else if (i + 1 < currentStep) {
                step.classList.add('completed');
            }
        });

        contents.forEach(content => {

            content.classList.toggle(
                'active',
                Number(content.dataset.step) === currentStep
            );
        });

        progressBar.style.width =
            (currentStep / totalSteps) * 100 + "%";

        stepMeta.textContent =
            `Step ${currentStep} of ${totalSteps}`;

        prevBtn.disabled = currentStep === 1;

        if (currentStep === totalSteps) {

            nextBtn.style.display = "none";
            submitBtn.style.display = "inline-flex";
        }
        else {

            nextBtn.style.display = "inline-flex";
            submitBtn.style.display = "none";
        }
    }

    // =========================
    // LOAD REVIEW DATA
    // =========================

    function loadReviewData() {

        $("#summaryApplicant").text(
            $("#firstName").val() + " " +
            $("#lastName").val()
        );

        $("#summaryDob").text(
            $("#dob").val()
        );

        $("#summaryMobile").text(
            $("#mobileNumber").val()
        );

        $("#summaryEmail").text(
            $("#emailAddress").val()
        );

        $("#summaryAadhaar").text(
            $("#aadhaarNumber").val()
        );

        $("#summaryPan").text(
            $("#panNumber").val()
        );

        $("#summaryOccupation").text(
            $("#occupation").val()
        );

        $("#summaryAccountType").text(
            $("#accountType").val()
        );

        $("#summaryNominee").text(
            $("#nomineeName").val()
        );

        $("#summaryAddress").text(
            $("#addressLine1").val() + ", " +
            $("#city").val() + ", " +
            $("#state").val() + " - " +
            $("#postalCode").val()
        );

        $("#summaryAccountAndNominee").text(
            $("#accountType").val() +
            " Account | Nominee: " +
            $("#nomineeName").val()
        );
    }

    // =========================
    // MOBILE OTP SEND
    // =========================

    $("#btnSendMobileOtp").click(function () {

        const mobileNumber = $("#mobileNumber").val();

        if (!mobileNumber) {

            alert("Please enter mobile number");
            return;
        }

        $.ajax({

            type: "POST",

            url: "/account-opening/send-mobile-otp",

            data: {
                mobileNumber: mobileNumber
            },

            success: function (response) {

            console.log(response);

            // PEHLE PURANE MSG HIDE
            $("#mobileOtpStatus").hide();
            $("#mobileVerifyStatus").hide();

            // MOBILE EXISTS
            if (
                response.trim() ===
                "MOBILE ALREADY EXISTS"
            ) {

                $("#mobileOtpStatus").show();

                $("#mobileOtpStatus")
                    .html(
                        '<span class="text-danger">' +
                        ' Mobile number already exists' +
                        '</span>'
                    );

                return;
            }

            // OTP SENT
            $("#mobileOtp").prop("disabled", false);

            $("#btnVerifyMobileOtp")
                .prop("disabled", false)
                .show();

            $("#mobileOtpStatus").show();

            $("#mobileOtpStatus")
                .html(
                    '<span class="text-success">' +
                    ' OTP sent successfully' +
                    '</span>'
                );
        },

            error: function () {

                alert("Failed to send mobile OTP");
            }
        });
    });

    // =========================
    // MOBILE OTP VERIFY
    // =========================

    $("#btnVerifyMobileOtp").click(function () {

        const mobileNumber = $("#mobileNumber").val();

        const otp = $("#mobileOtp").val();

        if (!otp) {

            $("#mobileVerifyStatus").show();

            $("#mobileVerifyMessage")
                .text("Please enter OTP")
                .removeClass("text-success")
                .addClass("text-danger");

            return;
        }

        $.ajax({

            type: "POST",

            url: "/account-opening/verify-mobile-otp",

            data: {
                customerId: window.customerId,
                mobileNumber: mobileNumber,
                otp: otp
            },

            success: function (response) {

                $("#mobileVerifyStatus").show();

                if (response.trim() === "MOBILE VERIFIED") {

                    $("#mobileNumber").prop("readonly", true);

                    $("#mobileOtp").prop("readonly", true);

                    $("#btnVerifyMobileOtp").hide();

                    $("#mobileVerifyMessage")
                        .text(" Mobile number verified successfully")
                        .removeClass("text-danger")
                        .addClass("text-success");
                }else if (
                    response.trim() ===
                    "MOBILE ALREADY EXISTS"
                ) {

                    $("#mobileVerifyMessage")
                        .text(" Mobile number already exists")
                        .removeClass("text-success")
                        .addClass("text-danger");
                }else {

                    $("#mobileVerifyMessage")
                        .text(" Invalid OTP")
                        .removeClass("text-success")
                        .addClass("text-danger");
                }
            },

            error: function () {

                $("#mobileVerifyStatus").show();

                $("#mobileVerifyMessage")
                    .text(" OTP verification failed")
                    .removeClass("text-success")
                    .addClass("text-danger");
            }
        });
    });

    // =========================
    // EMAIL OTP SEND
    // =========================

    $("#btnSendEmailOtp").click(function () {

        const email = $("#emailAddress").val();

        if (!email) {

            alert("Please enter email address");
            return;
        }

        $.ajax({

            type: "POST",

            url: "/account-opening/send-email-otp",

            data: {
                email: email
            },

            success: function (response) {

            console.log(response);

            $("#emailOtpStatus").hide();
            $("#emailVerifyStatus").hide();

            if (
                response.trim() ===
                "EMAIL ALREADY EXISTS"
            ) {

                $("#emailOtpStatus").show();

                $("#emailOtpStatus")
                    .html(
                        '<span class="text-danger">' +
                        ' Email already exists' +
                        '</span>'
                    );

                return;
            }

            $("#emailOtp").prop("disabled", false);

            $("#btnVerifyEmailOtp")
                .prop("disabled", false)
                .show();

            $("#emailOtpStatus").show();

            $("#emailOtpStatus")
                .html(
                    '<span class="text-success">' +
                    ' OTP sent successfully' +
                    '</span>'
                );
        },

            error: function () {

                alert("Failed to send email OTP");
            }
        });
    });

    // =========================
    // EMAIL OTP VERIFY
    // =========================

    $("#btnVerifyEmailOtp").click(function () {

        const email = $("#emailAddress").val();

        const otp = $("#emailOtp").val();

        if (!otp) {

            $("#emailVerifyStatus").show();

            $("#emailVerifyMessage")
                .text("Please enter OTP")
                .removeClass("text-success")
                .addClass("text-danger");

            return;
        }

        $.ajax({

            type: "POST",

            url: "/account-opening/verify-email-otp",

            data: {
                customerId: window.customerId,
                email: email,
                otp: otp
            },

            success: function (response) {

                $("#emailVerifyStatus").show();

                if (response.trim() === "EMAIL VERIFIED") {

                    $("#emailAddress")
                        .prop("readonly", true);

                    $("#emailOtp")
                        .prop("readonly", true);

                    $("#btnVerifyEmailOtp").hide();

                    $("#emailVerifyMessage")
                        .text(" Email address verified successfully")
                        .removeClass("text-danger")
                        .addClass("text-success");
                }
                else if (
                    response.trim() ===
                    "EMAIL ALREADY EXISTS"
                ) {

                    $("#emailVerifyMessage")
                        .text(" Email already exists")
                        .removeClass("text-success")
                        .addClass("text-danger");
                }else {

                    $("#emailVerifyMessage")
                        .text(" Invalid OTP")
                        .removeClass("text-success")
                        .addClass("text-danger");
                }
            },

            error: function () {

                $("#emailVerifyStatus").show();

                $("#emailVerifyMessage")
                    .text(" Email verification failed")
                    .removeClass("text-success")
                    .addClass("text-danger");
            }
        });
    });

    // =========================
    // AADHAAR OTP SEND
    // =========================

    $("#btnSendAadhaarOtp").click(function () {

        const aadhaarNumber =
            $("#aadhaarNumber").val();

        if (!aadhaarNumber) {

            alert("Please enter Aadhaar number");
            return;
        }

        $.ajax({

            type: "POST",

            url: "/account-opening/send-aadhaar-otp",

            data: {
                aadhaarNumber: aadhaarNumber
            },

            success: function (response) {

            console.log("AADHAAR OTP :", response);

            $("#aadhaarOtp")
                .prop("disabled", false);

            $("#btnVerifyAadhaarOtp")
                .prop("disabled", false)
                .show();

            $("#aadhaarOtpStatus").show();

            $("#aadhaarVerifyStatus").hide();

            $("#aadhaarNumber")
                .prop("readonly", false);

            $("#aadhaarOtp")
                .prop("readonly", false);
        },

            error: function () {

                alert("Failed to send Aadhaar OTP");
            }
        });
    });


    // =========================
    // AADHAAR OTP VERIFY
    // =========================

    $("#btnVerifyAadhaarOtp").click(function () {

        const aadhaarNumber =
            $("#aadhaarNumber").val();

        const otp =
            $("#aadhaarOtp").val();

        if (!otp) {

            $("#aadhaarVerifyStatus").show();

            $("#aadhaarVerifyMessage")
                .text("Please enter OTP")
                .removeClass("text-success")
                .addClass("text-danger");

            return;
        }

        $.ajax({

            type: "POST",

            url: "/account-opening/verify-aadhaar-otp",

            data: {
                aadhaarNumber: aadhaarNumber,
                otp: otp
            },

            success: function (response) {

                $("#aadhaarVerifyStatus").show();

                if (
                    response.trim() ===
                    "AADHAAR VERIFIED"
                ) {

                    $("#aadhaarNumber")
                        .prop("readonly", true);

                    $("#aadhaarOtp")
                        .prop("readonly", true);

                    $("#btnVerifyAadhaarOtp")
                        .hide();

                    $("#aadhaarVerifyMessage")
                        .text(" Aadhaar verified successfully")
                        .removeClass("text-danger")
                        .addClass("text-success");
                }
                else {

                    $("#aadhaarVerifyMessage")
                        .text(" Invalid OTP")
                        .removeClass("text-success")
                        .addClass("text-danger");
                }
            },

            error: function () {

                $("#aadhaarVerifyStatus").show();

                $("#aadhaarVerifyMessage")
                    .text(" Aadhaar verification failed")
                    .removeClass("text-success")
                    .addClass("text-danger");
            }
        });
    });

    // =========================
    // STEP 1 DATA
    // =========================

    function getStep1Data() {

        const formData = new FormData();

        formData.append("title", $("#title").val());
        formData.append("firstName", $("#firstName").val());
        formData.append("lastName", $("#lastName").val());
        formData.append("dob", $("#dob").val());
        formData.append("gender", $("#gender").val());
        formData.append("motherName", $("#motherName").val());
        formData.append("fatherName", $("#fatherName").val());
        formData.append("maritalStatus", $("#maritalStatus").val());
        formData.append("occupation", $("#occupation").val());
        formData.append("citizenship", $("#citizenship").val());

        const file = $("#photograph")[0].files[0];

        if (file) {
            formData.append("photograph", file);
        }

        return formData;
    }

    // =========================
    // STEP 3 DATA
    // =========================

    function getStep3Data() {

        const formData = new FormData();

        formData.append("aadhaarNumber", $("#aadhaarNumber").val());
        formData.append("aadhaarOtp", $("#aadhaarOtp").val());
        formData.append("panNumber", $("#panNumber").val());
        formData.append("addressLine1", $("#addressLine1").val());
        formData.append("city", $("#city").val());
        formData.append("state", $("#state").val());
        formData.append("postalCode", $("#postalCode").val());

        return formData;
    }

    // =========================
    // STEP 4 DATA
    // =========================

    function getStep4Data() {

        const formData = new FormData();

        formData.append("accountType", $("#accountType").val());
        formData.append("nomineeOpted", $("#nomineeOpted").val());
        formData.append("nomineeName", $("#nomineeName").val());
        formData.append("nomineeRelation", $("#nomineeRelation").val());
        formData.append("nomineeDob", $("#nomineeDob").val());

        return formData;
    }

    // =========================
    // NEXT BUTTON
    // =========================

    nextBtn.addEventListener("click", function () {

        // STEP 1
        if (currentStep === 1) {

            const data = getStep1Data();

            $.ajax({

                type: "POST",

                url: "/account-opening/step1",

                data: data,

                processData: false,

                contentType: false,

                success: function (id) {

                    window.customerId = id;

                    currentStep++;

                    updateWizard();
                },

                error: function (xhr) {

                    alert("Step 1 failed");

                    console.log(xhr.responseText);
                }
            });

            return;
        }

        // STEP 2
        if (currentStep === 2) {

            currentStep++;

            updateWizard();

            return;
        }

        // STEP 3
        if (currentStep === 3) {

            if (!window.customerId) {

                alert("Customer ID missing");
                return;
            }

            const data = getStep3Data();

            $.ajax({

                type: "POST",

                url: `/account-opening/step3/${window.customerId}`,

                data: data,

                processData: false,

                contentType: false,

                success: function () {

                    currentStep++;

                    updateWizard();
                },

                error: function (xhr) {

                    alert("Step 3 failed");

                    console.log(xhr.responseText);
                }
            });

            return;
        }

        // STEP 4
        if (currentStep === 4) {

            if (!window.customerId) {

                alert("Customer ID missing");
                return;
            }

            const data = getStep4Data();

            $.ajax({

                type: "POST",

                url: `/account-opening/step4/${window.customerId}`,

                data: data,

                processData: false,

                contentType: false,

                success: function () {

                    loadReviewData();

                    currentStep++;

                    updateWizard();
                },

                error: function (xhr) {

                    alert("Step 4 failed");

                    console.log(xhr.responseText);
                }
            });

            return;
        }

        // DEFAULT
        currentStep++;

        updateWizard();
    });

    // =========================
    // PREVIOUS BUTTON
    // =========================

    prevBtn.addEventListener("click", function () {

        if (currentStep > 1) {

            currentStep--;

            updateWizard();
        }
    });

    // =========================
    // INIT
    // =========================

    // =========================
    // FINAL SUBMIT
    // =========================

    $("#bankOpeningWizard").submit(function (e) {

        // PAGE RELOAD ROKTA HAI
        e.preventDefault();

        // customerId check
        if (!window.customerId) {

            alert("Customer ID missing");

            return;
        }

        // BACKEND API CALL
        $.ajax({

            type: "POST",

            url: `/account-opening/submit/${window.customerId}`,

            success: function (response) {

            console.log(response);

            localStorage.setItem(
                "customerName",
                $("#firstName").val() + " " +
                $("#lastName").val()
            );

            localStorage.setItem(
                "accountNumber",
                response.accountNumber
            );

            localStorage.setItem(
                "crn",
                response.crn
            );

            localStorage.setItem(
                "accountType",
                response.accountType
            );

            window.location.href =
                "/account-opening/success";
        },

            error: function () {

                alert("Application submit failed");
            }
        });
    });

    updateWizard();

});