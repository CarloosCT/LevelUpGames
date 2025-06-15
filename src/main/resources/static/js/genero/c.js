function validarGenero() {
  var expresion = /^[a-zA-ZáéíóúüÁÉÍÓÚÜñÑçÇ ]{3,}$/;
  var inputNombre = document.getElementById("idGenero");
  var errorMensaje = document.getElementById("errorNombre");
  var nombre = inputNombre.value.trim();

  if (expresion.test(nombre)) {
    inputNombre.style.border = "";
    errorMensaje.style.display = "none";
    return true;
  } else {
    inputNombre.style.border = "2px solid red";
    errorMensaje.style.display = "inline";
    return false;
  }
}