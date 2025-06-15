function validarGenero() {
  const expresion = /^[a-zA-ZáéíóúüÁÉÍÓÚÜñÑçÇ ]{3,}$/;
  const inputNombre = document.getElementById("idGenero");
  const errorMensaje = document.getElementById("errorNombre");
  const nombre = inputNombre.value.trim();

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