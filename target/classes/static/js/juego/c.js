let archivosImagenes = [];

const imagenesInput = document.getElementById('imagenes');
const imagenesPreview = document.getElementById('imagenesPreview');
const portadaIndexInput = document.getElementById('portadaIndex');

// Manejador de carga de imágenes
imagenesInput.addEventListener('change', (e) => {
  const nuevosArchivos = Array.from(e.target.files);

  if (archivosImagenes.length + nuevosArchivos.length > 5) {
    alert('Solo puedes subir hasta 5 imágenes.');
    return;
  }

  archivosImagenes = archivosImagenes.concat(nuevosArchivos);
  actualizarPreview();
  imagenesInput.value = ''; // Limpiar input visual
});

function actualizarPreview() {
  imagenesPreview.innerHTML = '';

  archivosImagenes.forEach((archivo, index) => {
    const reader = new FileReader();
    reader.onload = function (e) {
      const container = document.createElement('div');
      container.style.position = 'relative';
      container.style.width = '100px';
      container.style.height = '100px';
      container.style.borderRadius = '4px';
      container.style.overflow = 'hidden';
      container.style.cursor = 'pointer';

      const img = document.createElement('img');
      img.src = e.target.result;
      img.style.width = '100%';
      img.style.height = '100%';
      img.style.objectFit = 'cover';
      container.appendChild(img);

      // Botón eliminar
      const btnEliminar = document.createElement('button');
      btnEliminar.innerHTML = '&times;';
      btnEliminar.type = 'button';
      btnEliminar.title = 'Eliminar imagen';
      Object.assign(btnEliminar.style, {
        position: 'absolute',
        top: '2px',
        right: '2px',
        background: 'rgba(255, 0, 0, 0.8)',
        color: 'white',
        border: 'none',
        borderRadius: '50%',
        width: '20px',
        height: '20px',
        cursor: 'pointer',
        fontSize: '16px',
        lineHeight: '18px',
        padding: '0'
      });

      btnEliminar.onclick = (event) => {
        event.stopPropagation();
        archivosImagenes.splice(index, 1);
        if (portadaIndexInput.value == index.toString()) {
          portadaIndexInput.value = '';
        }
        actualizarPreview();
      };

      // Seleccionar como portada al hacer clic
      container.onclick = () => {
        portadaIndexInput.value = index;

        // Resaltar la nueva portada
        document.querySelectorAll('.preview-container div').forEach(cont => {
          cont.style.border = '2px solid #ccc';
        });
        container.style.border = '2px solid #ffcc00';
      };

      container.appendChild(btnEliminar);
      container.appendChild(img);
      imagenesPreview.appendChild(container);
    };
    reader.readAsDataURL(archivo);
  });
}

// Validación del formulario
function validarFormulario() {
  let valido = true;

  // Validar nombre
  const inputNombre = document.getElementById("idNombre");
  const nombre = inputNombre.value.trim();
  const nombreExp = /^[a-zA-ZáéíóúüÁÉÍÓÚÜñÑçÇ0-9 .,'\-]{3,}$/;
  let errorNombre = document.getElementById("error-nombre");

  if (!errorNombre) {
    errorNombre = document.createElement("small");
    errorNombre.id = "error-nombre";
    errorNombre.style.color = "red";
    inputNombre.parentNode.appendChild(errorNombre);
  }

  if (!nombreExp.test(nombre)) {
    inputNombre.style.border = "2px solid red";
    errorNombre.textContent = "El nombre debe tener al menos 3 caracteres válidos.";
    errorNombre.style.display = "inline";
    valido = false;
  } else {
    inputNombre.style.border = "";
    errorNombre.style.display = "none";
  }

  // Validar precio
  const inputPrecio = document.getElementById("precio");
  const precio = parseFloat(inputPrecio.value);
  let errorPrecio = document.getElementById("error-precio");

  if (!errorPrecio) {
    errorPrecio = document.createElement("small");
    errorPrecio.id = "error-precio";
    errorPrecio.style.color = "red";
    inputPrecio.parentNode.appendChild(errorPrecio);
  }

  if (isNaN(precio) || precio < 0) {
    inputPrecio.style.border = "2px solid red";
    errorPrecio.textContent = "Introduce un precio válido mayor o igual a 0.";
    errorPrecio.style.display = "inline";
    valido = false;
  } else {
    inputPrecio.style.border = "";
    errorPrecio.style.display = "none";
  }

  // Validar descripción
  const inputDesc = document.getElementById("idDesc");
  const desc = inputDesc.value.trim();
  let errorDesc = document.getElementById("error-desc");

  if (!errorDesc) {
    errorDesc = document.createElement("small");
    errorDesc.id = "error-desc";
    errorDesc.style.color = "red";
    inputDesc.parentNode.appendChild(errorDesc);
  }

  if (desc.length < 10) {
    inputDesc.style.border = "2px solid red";
    errorDesc.textContent = "La descripción debe tener al menos 10 caracteres.";
    errorDesc.style.display = "inline";
    valido = false;
  } else {
    inputDesc.style.border = "";
    errorDesc.style.display = "none";
  }

  // Validar géneros
  const checkboxes = document.querySelectorAll('input[name="generosIds"]:checked');
  const errorGeneros = document.getElementById('error-generos');

  if (checkboxes.length === 0) {
    errorGeneros.style.display = "block";
    valido = false;
  } else {
    errorGeneros.style.display = "none";
  }

  // Validar que haya imágenes
  if (!archivosImagenes.length) {
    alert("Debes seleccionar al menos una imagen.");
    return false;
  }

  // Validar que se haya elegido una portada
  const portadaIndex = parseInt(portadaIndexInput.value);
  if (isNaN(portadaIndex) || portadaIndex < 0 || portadaIndex >= archivosImagenes.length) {
    alert("Selecciona una imagen como portada.");
    return false;
  }

  // Rellenar el campo file con todas las imágenes
  const dataTransfer = new DataTransfer();
  archivosImagenes.forEach(file => dataTransfer.items.add(file));
  imagenesInput.files = dataTransfer.files;

  return valido;
}