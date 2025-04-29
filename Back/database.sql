CREATE TABLE usuario (
    idUsu INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rol ENUM('administrador', 'cliente') DEFAULT 'cliente'
); 