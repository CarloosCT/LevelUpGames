document.addEventListener('DOMContentLoaded', function () {
  let archivosImagenes = [];
  const imagenesInput = document.getElementById('imagenes');
  const imagenesActualesPreview = document.getElementById('imagenesActualesPreview');

  const portadaIndexInput = document.getElementById('portadaIndex') || (() => {
    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'portadaIndex';
    input.id = 'portadaIndex';
    document.querySelector('form').appendChild(input);
    return input;
  })();

  let portadaTipo = null; // 'new' o 'existing'
  let portadaAnteriorId = null;

  const botonGuardar = document.querySelector('input[type="submit"]');

  imagenesInput.addEventListener('change', (e) => {
    const nuevosArchivos = Array.from(e.target.files);
    const imagenesActualesCount = document.querySelectorAll('#imagenesActualesPreview .preview-img-wrapper').length;
    const imagenesNuevasCount = archivosImagenes.length;
    const espacioDisponible = 5 - (imagenesActualesCount + imagenesNuevasCount);

    if (nuevosArchivos.length > espacioDisponible) {
      alert(`Solo puedes añadir hasta ${espacioDisponible} imagen(es) más.`);
      return;
    }

    archivosImagenes = archivosImagenes.concat(nuevosArchivos);
    actualizarPreview();
    imagenesInput.value = '';
  });

  function actualizarPreview() {
    const container = document.getElementById('imagenesPreview');
    container.innerHTML = '';

    archivosImagenes.forEach((archivo, index) => {
      const reader = new FileReader();
      reader.onload = function (e) {
        const wrapper = document.createElement('div');
        wrapper.className = 'preview-img-wrapper';
        wrapper.style.position = 'relative';

        const img = document.createElement('img');
        img.src = e.target.result;
        img.className = 'preview-img';
        img.title = 'Haz clic para portada';
        wrapper.appendChild(img);

        const btnEliminar = document.createElement('button');
        btnEliminar.innerHTML = '&times;';
        btnEliminar.type = 'button';
        btnEliminar.className = 'delete-btn';
        btnEliminar.onclick = () => {
          archivosImagenes.splice(index, 1);
          actualizarPreview();

          if (portadaTipo === 'new' && portadaIndexInput.value == index) {
            portadaIndexInput.value = '';
            portadaTipo = null;
            mostrarMensajePortadaRequerida();
            bloquearBotonGuardar(true);
          }
        };

        wrapper.onclick = (event) => {
          event.stopPropagation();
          seleccionarPortada(wrapper, index);
        };

        wrapper.appendChild(btnEliminar);
        container.appendChild(wrapper);
      };
      reader.readAsDataURL(archivo);
    });

    const mensajePortada = document.getElementById('mensaje-portada');
    if (mensajePortada) {
      mensajePortada.style.display = archivosImagenes.length > 0 ? 'block' : 'none';
    }
  }

  function seleccionarPortada(wrapper, index = null) {
    const mensajePortadaRequerida = document.getElementById('mensaje-portada-requerida');

    if (portadaIndexInput.value) {
      if (portadaTipo === 'existing') {
        portadaAnteriorId = portadaIndexInput.value;
      } else {
        portadaAnteriorId = null;
      }
    }

    document.querySelectorAll('.preview-img-wrapper').forEach(w => w.classList.remove('portada-seleccionada'));
    wrapper.classList.add('portada-seleccionada');

    if (index !== null) {
      portadaIndexInput.value = index;
      portadaTipo = 'new';
    } else {
      const inputID = wrapper.querySelector('input[type="hidden"]');
      portadaIndexInput.value = inputID ? inputID.value : '';
      portadaTipo = 'existing';
    }

    if (portadaAnteriorId && portadaAnteriorId !== portadaIndexInput.value) {
      agregarAEliminar(portadaAnteriorId);
      portadaAnteriorId = null;
    }

    if (mensajePortadaRequerida) mensajePortadaRequerida.style.display = 'none';
    bloquearBotonGuardar(false);
  }

  function agregarAEliminar(id) {
    const eliminadas = Array.from(document.querySelectorAll('input[name="imagenesAEliminar"]'))
      .map(i => i.value);

    if (!eliminadas.includes(id)) {
      const inputEliminar = document.createElement('input');
      inputEliminar.type = 'hidden';
      inputEliminar.name = 'imagenesAEliminar';
      inputEliminar.value = id;
      document.querySelector('form').appendChild(inputEliminar);
    }
  }

  function eliminarImagenActual(btn) {
    const wrapper = btn.parentElement;
    const inputID = wrapper.querySelector('input[type="hidden"]');
    const idExistente = inputID ? inputID.value : null;

    if (!confirm("¿Eliminar esta imagen?")) return;

    const esPortada = wrapper.classList.contains('portada-seleccionada');

    if (idExistente) agregarAEliminar(idExistente);
    wrapper.remove();

    if (esPortada) {
      portadaIndexInput.value = '';
      mostrarMensajePortadaRequerida();
      bloquearBotonGuardar(true);
    }
  }

  function mostrarMensajePortadaRequerida() {
    const container = document.getElementById('imagenesActualesPreview').parentElement;
    let mensaje = document.getElementById('mensaje-portada-requerida');

    if (!mensaje) {
      mensaje = document.createElement('div');
      mensaje.id = 'mensaje-portada-requerida';
      mensaje.style.color = 'red';
      mensaje.style.marginTop = '10px';
      mensaje.style.fontWeight = 'bold';
      mensaje.innerText = '⚠️ Debes seleccionar una imagen como portada.';
      container.appendChild(mensaje);
    } else {
      mensaje.style.display = 'block';
    }
  }

  function bloquearBotonGuardar(bloquear) {
    if (botonGuardar) {
      botonGuardar.disabled = bloquear;
      botonGuardar.style.opacity = bloquear ? '0.6' : '1';
      botonGuardar.style.cursor = bloquear ? 'not-allowed' : 'pointer';
    }
  }

  function validarFormulario() {
    const nombre = document.getElementById('idNombre').value.trim();
    if (nombre === '') {
      alert('El nombre es obligatorio.');
      return false;
    }

    const precio = document.getElementById('precio').value;
    if (precio === '' || parseFloat(precio) < 0) {
      alert('El precio debe ser mayor o igual a 0.');
      return false;
    }

    const descripcion = document.getElementById('idDesc').value.trim();
    if (descripcion === '') {
      alert('La descripción es obligatoria.');
      return false;
    }

    const checkboxes = document.querySelectorAll('input[name="generosIds"]:checked');
    const errorGeneros = document.getElementById('error-generos');
    if (checkboxes.length === 0) {
      errorGeneros.style.display = 'block';
      alert('Selecciona al menos un género.');
      return false;
    } else {
      errorGeneros.style.display = 'none';
    }

    const portadaSeleccionada = document.querySelector('.portada-seleccionada');
    if (!portadaSeleccionada) {
      alert("Debes seleccionar una imagen como portada.");
      return false;
    }

    const dataTransfer = new DataTransfer();
    archivosImagenes.forEach(file => dataTransfer.items.add(file));
    imagenesInput.files = dataTransfer.files;

    return true;
  }

  window.seleccionarPortada = seleccionarPortada;
  window.eliminarImagenActual = eliminarImagenActual;
  window.validarFormulario = validarFormulario;
});
