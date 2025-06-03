-- Limpiar tablas en orden inverso a sus dependencias
DELETE FROM resenia;
DELETE FROM reserva;
DELETE FROM vehiculo;
DELETE FROM modelo;
DELETE FROM marca;
DELETE FROM sede;
DELETE FROM usuario;

-- Insertar usuarios
INSERT INTO usuario (nombre, apellido, email, password, telefono, direccion, fecha_nacimiento, registro, rol, dni) 
VALUES 
('Juan', 'Pérez', 'juan.perez@example.com', '$2a$12$3RRcj7gb47dTstQL2CzBT.1/VAEIOavZSEnyfC9pIR8SsSE1v2qfG', '600123456', 'Calle Mayor 123, Madrid', '1990-05-15', '2025-05-07', 'CLIENTE', '79954430J'),
('Ana', 'Gómez', 'ana.gomez@example.com', '$2a$12$3RRcj7gb47dTstQL2CzBT.1/VAEIOavZSEnyfC9pIR8SsSE1v2qfG', '611234567', NULL, NULL, '2025-04-20', 'ADMINISTRADOR', '19338585R'),
('Carlos', 'López', 'carlos.lopez@example.com', '$2a$12$3RRcj7gb47dTstQL2CzBT.1/VAEIOavZSEnyfC9pIR8SsSE1v2qfG', '622345678', 'Plaza España 7, Sevilla', '1995-03-10', '2025-03-15', 'CLIENTE', '32550686M');

-- Insertar marcas
INSERT INTO marca (nombre) 
VALUES 
('Volkswagen'), ('Mercedes-Benz'), ('Ford'), ('Peugeot'), ('Renault'), ('Citroën'), ('Fiat'), ('Toyota'), ('Hymer');

-- Insertar modelos
INSERT INTO modelo (nombre, id_mar) 
VALUES 
('Transporter', 1), ('California', 1), ('Sprinter', 2), ('Vito', 2), 
('Transit Custom', 3), ('Tourneo', 3), ('Boxer', 4), ('Jumpy', 4), 
('Trafic', 5), ('Master', 5), ('Berlingo', 6), ('SpaceTourer', 6), 
('Ducato', 7), ('Proace', 7), ('Land Cruiser', 8), ('Hilux', 8), 
('Grand Canyon', 9);

-- Insertar vehículos
INSERT INTO vehiculo (matricula, imagen, precio, anio, id_mod, pasajeros, puertas, transmision, combustible, detalles1, detalles2, detalles3, detalles4) 
VALUES 
('1234ABC', 'Catalogo1.png', 120.00, 2020, 1, 4, 4, 'MANUAL', 'DIESEL', 'Detalles15.jpg', 'Detalles12.jpg', 'Detalles9.jpg', 'Detalles7.jpg'),
('5678DEF', 'Catalogo2.png', 150.00, 2021, 2, 6, 5, 'AUTOMATICO', 'DIESEL', 'Detalles13.jpg', 'Detalles11.jpg', 'Detalles6.jpg', 'Detalles5.jpg'),
('9012GHI', 'Catalogo3.png', 90.00, 2019, 3, 2, 3, 'MANUAL', 'GASOLINA', 'Detalles14.jpg', 'Detalles10.jpg', 'Detalles7.jpg', 'Detalles4.jpg'),
('3456JKL', 'Catalogo4.png', 110.00, 2022, 4, 5, 5, 'AUTOMATICO', 'DIESEL', 'Detalles12.jpg', 'Detalles8.jpg', 'Detalles6.jpg', 'Detalles5.jpg'),
('7890MNP', 'Catalogo5.png', 95.00, 2023, 5, 2, 3, 'MANUAL', 'DIESEL', 'Detalles15.jpg', 'Detalles12.jpg', 'Detalles9.jpg', 'Detalles7.jpg'),
('2345QRS', 'Catalogo6.png', 130.00, 2021, 6, 6, 5, 'AUTOMATICO', 'GASOLINA', 'Detalles14.jpg', 'Detalles9.jpg', 'Detalles4.jpg', 'Detalles7.jpg'),
('6789TUV', 'Catalogo7.png', 100.00, 2020, 7, 3, 4, 'MANUAL', 'DIESEL', 'Detalles14.jpg', 'Detalles5.jpg', 'Detalles9.jpg', 'Detalles7.jpg'),
('8901WXY', 'Catalogo8.png', 115.00, 2022, 8, 4, 3, 'AUTOMATICO', 'GASOLINA', 'Detalles15.jpg', 'Detalles13.jpg', 'Detalles9.jpg', 'Detalles7.jpg'),
('4567ZZZ', 'Catalogo9.png', 160.00, 2023, 9, 4, 4, 'AUTOMATICO', 'DIESEL', 'Detalles12.jpg', 'Detalles4.jpg', 'Detalles9.jpg', 'Detalles7.jpg');

-- Insertar sedes
INSERT INTO sede (direccion, ciudad, telefono, imagen) 
VALUES 
('Calle de Vallehermoso, 78', 'Madrid', '912345678', 'sede_madrid.jpg'),
('Carrer de Pau Claris, 153', 'Barcelona', '933456789', 'sede_barcelona.jpg'),
('Calle Tetuán, 17', 'Sevilla', '954567890', 'sede_sevilla.jpg'),
('Carrer de Hospital, 68', 'Valencia', '961234567', 'sede_valencia.jpg'),
('Avenida de la Constitución, 15', 'Zaragoza', '976345678', 'sede_zaragoza.jpg'),
('Avenida de Andalucía, 25', 'Málaga', '952123456', 'sede_malaga.jpg'),
('Calle Gran Vía, 2', 'Bilbao', '944123456', 'sede_bilbao.jpg'),
('Praza do Obradoiro, 1', 'Santiago de Compostela', '981123456', 'sede_santiago.jpg'),
('Calle Santa Engracia, 12', 'Toledo', '925123456', 'sede_toledo.jpg');

-- Insertar reservas
INSERT INTO reserva (inicio, fin, total, estado, id_usu, id_veh, id_sed_lleg, id_sed_salid)    
VALUES
('2025-04-15', '2025-04-22', 380.00, 'FINALIZADA', 1, 5, 1, 3),
('2025-05-15', '2025-05-22', 400.00, 'FINALIZADA', 3, 7, 3, 1),
('2025-06-01', '2025-06-08', 460.00, 'CURSO', 1, 8, 4, 2),
('2025-06-02', '2025-06-09', 390.00, 'CURSO', 1, 2, 5, 3),
('2025-06-15', '2025-06-22', 450.00, 'RESERVADA', 1, 3, 4, 1),
('2025-06-20', '2025-06-27', 410.00, 'RESERVADA', 1, 4, 2, 5);

-- Insertar reseñas (solo para reservas finalizadas)
INSERT INTO resenia (comentario, puntuacion, fecha, id_reser)
VALUES
('Vehículo en excelente estado, todo perfecto.', 5, '2025-04-23', 1),
('Buena experiencia aunque la entrega fue un poco lenta.', 4, '2025-05-09', 2),
('Servicio aceptable, aunque el vehículo tenía algunos detalles.', 3, '2025-05-23', 3); 