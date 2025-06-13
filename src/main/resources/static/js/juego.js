document.addEventListener('DOMContentLoaded', () => {
    // ===== Carrusel de miniaturas e imagen principal =====
    const wrapper = document.querySelector('.miniaturas-wrapper');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    const portada = document.getElementById('portada');
    const miniaturas = document.querySelectorAll('.miniatura-img');
    const scrollStep = 130;

    if (prevBtn) prevBtn.addEventListener('click', () => {
        wrapper.scrollLeft -= scrollStep;
    });

    if (nextBtn) nextBtn.addEventListener('click', () => {
        wrapper.scrollLeft += scrollStep;
    });

    miniaturas.forEach(img => {
        img.addEventListener('click', () => {
            const nuevaRuta = img.getAttribute('data-ruta-imagen');
            if (nuevaRuta) {
                portada.src = nuevaRuta + '?v=' + Date.now();
                miniaturas.forEach(i => i.classList.remove('selected'));
                img.classList.add('selected');
            }
        });
    });

    const portadaActual = portada.getAttribute('src');
    miniaturas.forEach(img => {
        if (img.getAttribute('data-ruta-imagen') === portadaActual) {
            img.classList.add('selected');
        }
    });

    // ===== Valoración de estrellas =====
    const dataEl = document.getElementById('juego-data');
    const juegoId = dataEl?.dataset.juegoId;
    const userLogged = dataEl?.dataset.userLogged === 'true';
    const tieneJuego = dataEl?.dataset.tieneJuego === 'true';
    const valoracionMedia = parseFloat(dataEl?.dataset.valoracionMedia || '0');
    const valoracionUsuario = dataEl?.dataset.valoracionUsuario;

    function pintarEstrellas(selector, valor) {
        document.querySelectorAll(`${selector} .estrella`).forEach((estrella, i) => {
            const starIndex = i + 1;
            let fill = 0;
            if (valor >= starIndex) {
                fill = 100;
            } else if (valor > i && valor < starIndex) {
                fill = (valor - i) * 100;
            }
            estrella.style.setProperty('--fill-percent', `${fill}%`);
        });
    }

    pintarEstrellas('.valoracion-media .valoracion', valoracionMedia);
    if (valoracionUsuario && valoracionUsuario !== 'null') {
        pintarEstrellas('.valoracion-usuario .valoracion', parseFloat(valoracionUsuario));
    }

    if (userLogged && tieneJuego) {
        document.querySelectorAll('.valoracion-usuario .estrella').forEach(estrella => {
            estrella.style.cursor = 'pointer';
            estrella.addEventListener('click', async () => {
                const valor = parseInt(estrella.getAttribute('data-valor'));
                const formData = new URLSearchParams({ juegoId, valoracion: valor });
                try {
                    const res = await fetch('/valoracion/cpost', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                        body: formData
                    });
                    if (res.redirected) {
                        window.location.href = res.url;
                    } else if (res.ok) {
                        alert('Valoración enviada. ¡Gracias!');
                        pintarEstrellas('.valoracion-usuario .valoracion', valor);
                        const nuevaMedia = parseFloat(await res.text());
                        if (!isNaN(nuevaMedia)) {
                            pintarEstrellas('.valoracion-media .valoracion', nuevaMedia);
                        }
                    } else {
                        alert('Error enviando valoración.');
                    }
                } catch {
                    alert('Error de red al enviar valoración.');
                }
            });
        });
    }
});
