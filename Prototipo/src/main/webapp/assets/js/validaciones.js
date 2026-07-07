(function () {
    "use strict";

    function mensajeCampo(campo) {
        if (campo.validity.valueMissing) return "Este campo es obligatorio.";
        if (campo.validity.tooShort) return "Ingrese al menos " + campo.minLength + " caracteres.";
        if (campo.validity.tooLong) return "No supere " + campo.maxLength + " caracteres.";
        if (campo.validity.typeMismatch) return "Ingrese un formato válido.";
        if (campo.validity.patternMismatch) return campo.title || "El formato ingresado no es válido.";
        if (campo.validity.rangeUnderflow || campo.validity.rangeOverflow) return "El valor está fuera del rango permitido.";
        return "";
    }

    function obtenerError(campo) {
        var grupo = campo.closest(".form-group") || campo.parentElement;
        if (!grupo) return null;

        var error = grupo.querySelector(".field-error");
        if (!error) {
            error = document.createElement("p");
            error.className = "field-error";
            error.setAttribute("aria-live", "polite");
            grupo.appendChild(error);
        }
        return error;
    }

    function pintarCampo(campo) {
        if (!(campo instanceof HTMLInputElement || campo instanceof HTMLSelectElement || campo instanceof HTMLTextAreaElement)) {
            return true;
        }

        var error = obtenerError(campo);
        var mensaje = mensajeCampo(campo);
        if (mensaje) {
            campo.classList.add("is-invalid");
            if (error) error.textContent = mensaje;
            return false;
        }

        campo.classList.remove("is-invalid");
        if (error) error.textContent = "";
        return true;
    }

    function validarFechas(formulario) {
        var inicio = formulario.querySelector('input[name="fechaInicio"]');
        var fin = formulario.querySelector('input[name="fechaFin"]');
        if (!inicio || !fin || !inicio.value || !fin.value) return true;

        if (fin.value < inicio.value) {
            fin.setCustomValidity("La fecha de fin no puede ser anterior a la fecha de inicio.");
            pintarCampo(fin);
            return false;
        }
        fin.setCustomValidity("");
        pintarCampo(fin);
        return true;
    }

    function validarConfirmacion(formulario) {
        var nueva = formulario.querySelector("#contrasenaNueva");
        var confirmacion = formulario.querySelector("#confirmacion");
        if (!nueva || !confirmacion || !confirmacion.value) return true;

        if (nueva.value !== confirmacion.value) {
            confirmacion.setCustomValidity("Las contraseñas no coinciden.");
            pintarCampo(confirmacion);
            return false;
        }
        confirmacion.setCustomValidity("");
        pintarCampo(confirmacion);
        return true;
    }

    document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll("form[data-validate]").forEach(function (formulario) {
            var campos = formulario.querySelectorAll("input, select, textarea");

            campos.forEach(function (campo) {
                ["input", "blur", "change"].forEach(function (evento) {
                    campo.addEventListener(evento, function () {
                        if (campo.id === "confirmacion") validarConfirmacion(formulario);
                        if (campo.name === "fechaInicio" || campo.name === "fechaFin") validarFechas(formulario);
                        pintarCampo(campo);
                    });
                });
            });

            formulario.addEventListener("submit", function (evento) {
                var valido = true;
                campos.forEach(function (campo) {
                    if (!pintarCampo(campo)) valido = false;
                });
                if (!validarConfirmacion(formulario)) valido = false;
                if (!validarFechas(formulario)) valido = false;

                if (!valido || !formulario.checkValidity()) {
                    evento.preventDefault();
                    var primero = formulario.querySelector(".is-invalid, :invalid");
                    if (primero) primero.focus();
                }
            });
        });
    });
})();
