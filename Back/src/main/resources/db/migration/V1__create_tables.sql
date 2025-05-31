-- Crear tabla usuario
CREATE TABLE usuario (
    id_usu INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(45) NOT NULL,
    apellido VARCHAR(45) NOT NULL,
    email VARCHAR(45) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(15),
    registro DATE,
    rol ENUM('ADMINISTRADOR', 'CLIENTE') NOT NULL DEFAULT 'CLIENTE',
    dni VARCHAR(9) NOT NULL UNIQUE
);

-- Crear tabla marca
CREATE TABLE marca (
    id_mar INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(45) NOT NULL
);

-- Crear tabla modelo
CREATE TABLE modelo (
    id_mod INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(45) NOT NULL,
    id_mar INT,
    FOREIGN KEY (id_mar) REFERENCES marca(id_mar)
);

-- Crear tabla vehiculo
CREATE TABLE vehiculo (
    id_veh INT PRIMARY KEY AUTO_INCREMENT,
    matricula VARCHAR(10) NOT NULL UNIQUE,
    imagen VARCHAR(255),
    precio DECIMAL(10,2) NOT NULL,
    anio INT NOT NULL,
    id_mod INT,
    pasajeros INT NOT NULL,
    puertas INT NOT NULL,
    transmision ENUM('MANUAL', 'AUTOMATICO') NOT NULL,
    combustible ENUM('GASOLINA', 'DIESEL', 'ELECTRICO', 'HIBRIDO') NOT NULL,
    detalles1 VARCHAR(255),
    detalles2 VARCHAR(255),
    detalles3 VARCHAR(255),
    detalles4 VARCHAR(255),
    FOREIGN KEY (id_mod) REFERENCES modelo(id_mod)
);

-- Crear tabla sede
CREATE TABLE sede (
    id_sed INT PRIMARY KEY AUTO_INCREMENT,
    direccion VARCHAR(255) NOT NULL,
    ciudad VARCHAR(45) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    imagen VARCHAR(255)
);

-- Crear tabla reserva
CREATE TABLE reserva (
    id_res INT PRIMARY KEY AUTO_INCREMENT,
    inicio DATE NOT NULL,
    fin DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    estado ENUM('RESERVADA', 'CURSO', 'FINALIZADA', 'CANCELADA') NOT NULL,
    id_usu INT,
    id_veh INT,
    id_sed_lleg INT,
    id_sed_salid INT,
    FOREIGN KEY (id_usu) REFERENCES usuario(id_usu),
    FOREIGN KEY (id_veh) REFERENCES vehiculo(id_veh),
    FOREIGN KEY (id_sed_lleg) REFERENCES sede(id_sed),
    FOREIGN KEY (id_sed_salid) REFERENCES sede(id_sed)
);

-- Crear tabla resenia
CREATE TABLE resenia (
    id_rese INT PRIMARY KEY AUTO_INCREMENT,
    comentario TEXT,
    puntuacion INT NOT NULL,
    fecha DATE NOT NULL,
    id_reser INT,
    FOREIGN KEY (id_reser) REFERENCES reserva(id_res)
); 