
# LevelUpGames 🎮

LevelUpGames es una aplicación web para la gestión y visualización de juegos, desarrollada como proyecto final de grado.

## 🚀 Tecnologías utilizadas

- **Java**: para la lógica de negocio y backend.
- **Bootstrap & CSS**: para el diseño y estilos responsivos.
- **HTML & JavaScript**: para la estructura y funcionalidad del frontend.
- **Supabase**: base de datos y autenticación en la nube.
- **Docker**: para contenerización y despliegue de la aplicación.

## 🛠️ Funcionalidades principales

- Registro e inicio de sesión de usuarios.
- CRUD (crear, leer, actualizar y eliminar) juegos.
- Interfaz responsive con Bootstrap.
- Persistencia de datos usando Supabase.
- Despliegue y ejecución en contenedor Docker.

## ⚙️ Instalación y ejecución

1. Clona el repositorio:

   ```bash
   git clone https://github.com/CarloosCT/LevelUpGames.git
   cd LevelUpGames
   ```

2. Construye la imagen Docker:

   ```bash
   docker build -t levelupgames .
   ```

3. Ejecuta el contenedor:

   ```bash
   docker run -p 8080:8080 levelupgames
   ```

4. Accede a `http://localhost:8080` en tu navegador.

## 👥 Colaboradores

- [@alinnnn02](https://github.com/alinnnn02)
- [@CarloosCT](https://github.com/CarloosCT)

## 📄 Licencia

Este proyecto está bajo la licencia MIT.
