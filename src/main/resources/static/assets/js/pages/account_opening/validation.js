console.log("validation.js loaded");

$(function () {
  const wizard = document.getElementById('bankOpeningWizard');

  window.validator = $(wizard).validate({
    ignore: [],
    errorElement: 'div',
    errorClass: 'invalid-feedback',

    highlight: function (element) {
      $(element).addClass('is-invalid');
    },

    unhighlight: function (element) {
      $(element).removeClass('is-invalid');
    },

    errorPlacement: function (error, element) {
      error.insertAfter(element);
    },

    rules: {
      title: "required",
      firstName: "required",
      lastName: "required",
      dob: "required",
      gender: "required",
      motherName: "required",
      fatherName: "required",
      maritalStatus: "required",
      occupation: "required",
      citizenship: "required",

      mobileNumber: { required: true, digits: true, minlength: 10 },
      emailAddress: { required: true, email: true },

      aadhaarNumber: { required: true, digits: true, minlength: 12, maxlength: 12 },
      panNumber: { required: true, minlength: 10, maxlength: 10 },

      addressLine1: "required",
      city: "required",
      state: "required",
      postalCode: { required: true, digits: true, minlength: 6, maxlength: 6 },

      accountType: "required",
      nomineeOpted: "required",

      nomineeName: {
        required: () => $('#nomineeOpted').val() === 'Yes'
      },
      nomineeRelation: {
        required: () => $('#nomineeOpted').val() === 'Yes'
      },
      nomineeDob: {
        required: () => $('#nomineeOpted').val() === 'Yes'
      },

      declarationConsent: "required",
      termsConsent: "required"
    }
  });

  window.validateStep = function (step) {
    let valid = true;

    $(`.wizard-content[data-step="${step}"]`)
      .find('input, select, textarea')
      .each(function () {
        if (!window.validator.element(this)) {
          valid = false;
        }
      });

    return valid;
  };
});


$(document).ready(function () {

    // Mobile OTP
    $("#btnSendMobileOtp").click(function () {

        // status show
        $("#mobileOtpStatus").show();

        // enable input + button
        $("#mobileOtp").prop("disabled", false);
        $("#btnVerifyMobileOtp").prop("disabled", false);

    });

    // Email OTP
    $("#btnSendEmailOtp").click(function () {

        // status show
        $("#emailOtpStatus").show();

        // enable input + button
        $("#emailOtp").prop("disabled", false);
        $("#btnVerifyEmailOtp").prop("disabled", false);

    });

});