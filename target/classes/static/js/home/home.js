const searchInput = document.getElementById('search');
const generoLinks = document.querySelectorAll('.genero-link');
const generoHidden = document.getElementById('genero-hidden');
const filterForm = document.getElementById('filter-form');

let lastSearch = searchInput.value;

generoLinks.forEach(link => {
  link.addEventListener('click', function (e) {
    e.preventDefault();
    generoHidden.value = this.dataset.genero || "";
    filterForm.submit();
  });
});

searchInput.addEventListener('input', debounce(() => {
  const currentValue = searchInput.value.trim();
  if (currentValue !== lastSearch) {
    lastSearch = currentValue;
    filterForm.submit();
  }
}, 800));

function debounce(callback, delay) {
  let timeout;
  return function (...args) {
    clearTimeout(timeout);
    timeout = setTimeout(() => callback.apply(this, args), delay);
  };
}

document.addEventListener('DOMContentLoaded', function () {
  const generoActual = generoHidden.value;

  generoLinks.forEach(link => {
    link.classList.remove('active');
    if (link.dataset.genero === generoActual) {
      link.classList.add('active');
    }
    if (!generoActual && link.dataset.genero === "") {
      link.classList.add('active');
    }
  });
});
