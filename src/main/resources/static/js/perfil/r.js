    document.addEventListener("DOMContentLoaded", () => {
  const nombreInput = document.getElementById('nombre');
  const apellidoInput = document.getElementById('apellido');
  const guardarBtn = document.getElementById('guardarBtn');
  const configForm = document.getElementById('configForm');
  const errorMensaje = document.getElementById('errorMensaje');

  const nombreInicial = nombreInput.value.trim();
  const apellidoInicial = apellidoInput.value.trim();

  function validarCambios() {
    const nombreActual = nombreInput.value.trim();
    const apellidoActual = apellidoInput.value.trim();

    const nombreValido = nombreActual.length >= 3;
    const apellidoValido = apellidoActual.length >= 3;
    const sonDistintos = nombreActual !== nombreInicial || apellidoActual !== apellidoInicial;

    if (!nombreValido || !apellidoValido) {
      errorMensaje.style.display = 'block';
      errorMensaje.textContent = 'El nombre y apellido deben tener al menos 3 letras.';
      guardarBtn.disabled = true;
    } else if (!sonDistintos) {
      errorMensaje.style.display = 'block';
      errorMensaje.textContent = 'Debes modificar el nombre o apellido para guardar.';
      guardarBtn.disabled = true;
    } else {
      errorMensaje.style.display = 'none';
      guardarBtn.disabled = false;
    }
  }

  nombreInput.addEventListener('input', validarCambios);
  apellidoInput.addEventListener('input', validarCambios);

  configForm.addEventListener('submit', (e) => {
    if (guardarBtn.disabled) {
      e.preventDefault();
    }
  });

  validarCambios();

  // MENÚ LATERAL PARA MÓVILES
  const toggleBtn = document.querySelector(".toggle-menu-btn");
  const menuLateral = document.querySelector(".menu-lateral-separado");

  if (toggleBtn && menuLateral) {
    toggleBtn.addEventListener("click", () => {
      menuLateral.classList.toggle("abierto");
    });

    menuLateral.querySelectorAll('a').forEach(link => {
      link.addEventListener('click', () => {
        menuLateral.classList.remove("abierto");
      });
    });
  }
});