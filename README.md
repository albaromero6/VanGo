# **Van&Go**

## **ÍNDICE**
1. [Autoría](#-autoría)  
2. [Introducción](#-introducción)  
3. [Objetivos](#-objetivos)  
4. [Tecnologías](#-tecnologías)
5. [Esquema de la Base de Datos](#-esquema-de-la-base-de-datos)  
6. [Funcionalidades implementadas](#-funcionalidades-implementadas)  
7. [Funcionalidades pendientes](#-funcionalidades-pendientes)  
8. [Histórico de cambios](#-histórico-de-cambios)  
9. [Bibliografía y recursos](#-bibliografía-y-recursos)  
10. [Vídeo](#-vídeo)

---

## 🔖 AUTORÍA

Alba Romero Almansa

---

## 🧭 INTRODUCCIÓN

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

## 🧩 ESQUEMA DE LA BASE DE DATOS

![ER](https://github.com/user-attachments/assets/f8d489af-2b50-4417-afab-a66e2105daa3)

![DED](https://github.com/user-attachments/assets/79dfc117-facd-4871-b5f1-79b1fd1451ac)

---

## ✅ FUNCIONALIDADES IMPLEMENTADAS

- Modelado de la base de datos.
- Estructura básica del proyecto con Spring Boot.
- Creación de modelos, repositorios, servicios y controladores en el backend.
- Diseño en Figma de las interfaces principales.
- Estilos CSS definidos para las ventanas principales de la aplicación.

---

## 🔄 FUNCIONALIDADES PENDIENTES

- Integración del frontend con el backend  
- Registro e inicio de sesión con JWT  
- Panel de control de usuarios y administradores  
- Valoraciones y comentarios de usuarios  
- Despliegue en AWS  
- Configuración de NGINX  
- Seguridad avanzada (roles, permisos)  
- Testing y validaciones
- Escaneo de seguridad con OWASP ZAP

---

## 📜 HISTÓRICO DE CAMBIOS

- `commit1` (Subida Back Inicial) – Estructura inicial del backend creada con Spring Boot
- `commit2` (Spring en Funcionamiento) – Arreglo de application.properties para conseguir lanzar Spring Boot  
- `commit3` (Subida Models) – Estructura inicial de las entidades
- `commit4` (Models Actualizados) – Models mejorados con validaciones y restricciones de los atributos
- `commit5` (Models Terminados) – Models terminados con todas sus relaciones y mejoras
- `commit6` (Subida Repositories) – Estructura inicial y final de los repositories
- `commit7` (Subida Services) – Estructura inicial y final de los servicios
- `commit8` (Subida Controllers) – Estructura inicial y final de los controllers
- `commit9` (Solución a Recursión Infinita) – Uso de @JsonManagedReference para evitar la recursión infinita en la sereialización del JSON

---

## 📚 BIBLIOGRAFÍA Y RECURSOS

- [Documentación oficial de Spring Boot](https://spring.io/projects/spring-boot)  
- [Documentación oficial de Angular](https://angular.io/)  
- [Figma](https://www.figma.com/)  
- [YouTube](https://www.youtube.com/)

---

## 🎥 VÍDEO

🔗 [Van&Go](https://vimeo.com/1080304449/fe6823ea07?share=copy)
