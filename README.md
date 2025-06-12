# **Van&Go**

## **ÍNDICE**
1. [Autoría](#-autoría)  
2. [Descripción](#-introducción)  
3. [Objetivos](#-objetivos)  
4. [Tecnologías](#-tecnologías)
5. [Esquema de la Base de Datos](#-esquema-de-la-base-de-datos)  
6. [Bibliografía y recursos](#-bibliografía-y-recursos)  
7. [Vídeo](#-vídeo)

---

## 🔖 AUTORÍA

Alba Romero Almansa

---

## 🧭 DESCRIPCIÓN

Van&Go es una innovadora plataforma web diseñada para facilitar el alquiler de campers y caravanas de forma flexible, cómoda y accesible. Nuestro objetivo es ofrecer a los viajeros la libertad de disfrutar de la carretera sin preocuparse por tarifas rígidas o requisitos poco prácticos.

---

## 🎯 OBJETIVOS

- Facilitar el acceso al alquiler de campers y caravanas a través de una plataforma intuitiva y accesible.
- Ofrecer un sistema de alquiler flexible.
- Maximizar la disponibilidad y rentabilidad de los vehículos.
- Garantizar una experiencia de alquiler segura con verificación y valoraciones.
- Promover un turismo en carretera accesible y sostenible.
- Desarrollar una plataforma escalable.
- Brindar atención al cliente efectiva.
- Integrar herramientas de búsqueda avanzada y filtros personalizados.
- Fomentar una comunidad de viajeros y propietarios.
- Establecer alianzas estratégicas con el sector camper.
- Aplicar estrategias de marketing digital para posicionar la marca Van&Go.

---

## 🛠️ TECNOLOGÍAS

### Bases de Datos y Gestión
- MySQL
- Workbench
- ERD Plus
- Beekeeper Studio

### Back-End y API
- Spring Boot
- Spring Security
- JWT

### Front-End y Diseño
- Angular 17
- SCSS
- CSS3
- HTML5

### Infraestructura y Despliegue
- AWS
- NGINX

### Otras Herramientas
- Git
- GitHub
- Postman
- Figma
- Figjam

---

## 🧩 ESQUEMAS DE LA BASE DE DATOS

![ER](https://github.com/albaromero6/VanGo/blob/main/docs/ER.png)


![DED](https://github.com/albaromero6/VanGo/blob/main/docs/DED.png)

---

## 📖 Tutorial de Uso de la Aplicación Van&Go

### 1. Página Principal (Home)
Al entrar a la aplicación, llegarás a la página principal donde encontrarás una presentación general de la plataforma y acceso a todas las secciones principales.

![Página Principal](ruta/a/tu/captura-home.png)

---

### 2. Catálogo de Vehículos  
**Ruta:** `/catalogo`  
Aquí verás todos los campers y caravanas disponibles para alquilar. Puedes ordenar los vehículos por precio (ascendente o descendente) y ver detalles básicos.

![Catálogo de Vehículos](ruta/a/tu/captura-catalogo.png)

---

### 3. Detalles del Vehículo  
**Ruta:** `/detalles/:id`  
Al seleccionar un vehículo del catálogo, accederás a su página de detalles, donde podrás ver fotos, características completas, precios y disponibilidad.

![Detalles del Vehículo](ruta/a/tu/captura-detalles.png)

---

### 4. Sistema de Reservas  
**Ruta:** `/reserva/:id`  
Después de elegir un vehículo, podrás seleccionar fechas de alquiler, ver el precio total y realizar la reserva.

![Sistema de Reservas](ruta/a/tu/captura-reserva.png)

---

### 5. Perfil de Usuario  
**Ruta:** `/perfil`  
En tu perfil puedes ver y modificar tus datos personales, gestionar reservas, y ver el historial de alquileres. Si eres administrador, tendrás acceso a un botón para entrar al panel de control.

![Perfil de Usuario](ruta/a/tu/captura-perfil.png)

---

### 6. Secciones Informativas  
- **Por qué elegirnos:** `/eligenos` — Ventajas de usar Van&Go.  
- **Rutas:** `/rutas` — Sugerencias de destinos para viajeros.  
- **Contacto:** `/contacto` — Formulario para contactar con soporte.  
- **Ubicación:** `/ubicacion` — Información de dónde encontrarnos.

![Secciones Informativas](ruta/a/tu/captura-informacion.png)

---

### 7. Sistema de Usuarios  
- **Registro:** `/registro` para crear una nueva cuenta.  
- **Login:** `/login` para iniciar sesión.  
- **Comentarios:** `/comments/:id` para ver y dejar valoraciones sobre vehículos.

![Sistema de Usuarios](ruta/a/tu/captura-login-registro.png)

---

### 8. Panel de Administración (Solo administradores)  
Accede desde el perfil, botón "Panel de control".  
**Ruta:** `http://localhost:8080/admin/panel`  
Requiere autenticación con token. Permite gestionar usuarios, vehículos, sedes, reservas, reseñas, marcas y modelos.

![Panel de Administración](ruta/a/tu/captura-admin.png)

---

### Consejos de Navegación
- Usa el menú superior para moverte entre secciones principales.  
- El logo de Van&Go siempre te llevará a la página principal.  
- Para reservar: visita el catálogo, elige un vehículo, revisa detalles y realiza la reserva.  
- Para usuarios: regístrate o inicia sesión para acceder a todas las funcionalidades y gestionar tu perfil.  
- Para administradores: accede al panel de control para gestionar la plataforma.

---


## 📘 Bitácora

A continuación se detallan las tareas principales realizadas durante el desarrollo del proyecto, indicando la fecha de cada una de ellas.



| Tarea                                                                                           | Fecha        |
|-------------------------------------------------------------------------------------------------|--------------|
| Inicio del backend con Spring Boot y estructura base                                            | 25-04-2025   |
| Creación de modelos, repositorios, servicios y controladores                                    | 30-04-2025   |
| Añadidas validaciones en modelos y datos iniciales en `import.sql`                              | 07-05-2025   |
| Configuración de seguridad con JWT y variables de entorno                                       | 09-05-2025   |
| Configuración de roles para los endpoints                                                       | 13-05-2025   |
| Creación del proyecto Angular 17 y estructura inicial del frontend                              | 15-05-2025   |
| Desarrollo de secciones del sitio: Steps, Application, Hero, Reviews, etc.                      | 18-05-2025   |
| Implementación de login funcional, nombre de usuario en navbar, y enlaces                       | 23-05-2025   |
| Integración con Google Maps y obtención de ubicaciones desde el backend                         | 26-05-2025   |
| Implementación de edición de vehículos con `editMode` y uso de SweetAlert                       | 29-05-2025   |
| Implementación de internacionalización y mejoras en navbar y documentación                      | 01-06-2025   |
| Funcionalidad de reservas en perfil, eliminar cuenta y mejoras varias                           | 04-06-2025   |
| Sección de comentarios completa con CRUD y mejoras en estilo                                    | 06-06-2025   |
| Diseño responsive en varias secciones del sitio                                                 | 08-06-2025   |
| Desarrollo del panel de administración con gestión de usuarios, marcas, modelos, etc.           | 10-06-2025   |
| Resolución de errores globales y ajustes finales de rutas para producción                       | 11-06-2025   |
| Actualización de documentación (README) y pruebas finales                                       | 12-06-2025   |


---


## 📚 BIBLIOGRAFÍA Y RECURSOS

- [Documentación oficial de Spring Boot](https://spring.io/projects/spring-boot)  
- [Documentación oficial de Angular](https://angular.io/)
- [Documentación JWT](https://jwt.io/introduction)
- [Documentación Spring Security](https://docs.spring.io/spring-security/reference/getting-spring-security.html)
- [Figma](https://www.figma.com/)  
- [YouTube](https://www.youtube.com/)

---

## 🎥 VÍDEO

🔗 [Van&Go](https://vimeo.com/1080304449/fe6823ea07?share=copy)
