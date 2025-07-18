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
('Alba', 'Romero', 'alba.romero@example.com', '$2a$12$3RRcj7gb47dTstQL2CzBT.1/VAEIOavZSEnyfC9pIR8SsSE1v2qfG', '600000001', 'Calle Luna 45, Málaga', '1996-10-09', '2025-05-07', 'ADMINISTRADOR', '52261075S'),
('Luis', 'Martínez', 'luis.martinez@example.com', '$2a$12$3RRcj7gb47dTstQL2CzBT.1/VAEIOavZSEnyfC9pIR8SsSE1v2qfG', '611000002', 'Av. Reina Sofía 10, Murcia', '1992-07-18', '2025-04-21', 'CLIENTE', '17620168Y'),
('María', 'Fernández', 'maria.fernandez@example.com', '$2a$12$3RRcj7gb47dTstQL2CzBT.1/VAEIOavZSEnyfC9pIR8SsSE1v2qfG', '622000003', 'Calle del Sol 21, Granada', '1988-01-30', '2025-04-22', 'CLIENTE', '43873119K'),
('Javier', 'Ruiz', 'javier.ruiz@example.com', '$2a$12$3RRcj7gb47dTstQL2CzBT.1/VAEIOavZSEnyfC9pIR8SsSE1v2qfG', '633000004', 'Plaza de la Paz 7, Logroño', '1990-06-05', '2025-04-23', 'CLIENTE', '02835332F'),
('Sara', 'López', 'sara.lopez@example.com', '$2a$12$3RRcj7gb47dTstQL2CzBT.1/VAEIOavZSEnyfC9pIR8SsSE1v2qfG', '644000005', 'Calle Real 88, León', '1993-09-14', '2025-04-24', 'CLIENTE', '89087733Q'),
('Diego', 'Sánchez', 'diego.sanchez@example.com', '$2a$12$3RRcj7gb47dTstQL2CzBT.1/VAEIOavZSEnyfC9pIR8SsSE1v2qfG', '655000006', 'Camino Viejo 12, Alicante', '1991-03-09', '2025-04-25', 'CLIENTE', '10968994H'),
('Lucía', 'García', 'lucia.garcia@example.com', '$2a$12$3RRcj7gb47dTstQL2CzBT.1/VAEIOavZSEnyfC9pIR8SsSE1v2qfG', '666000007', 'Av. de la Estación 5, Oviedo', '1994-12-01', '2025-04-26', 'CLIENTE', '33429685J');

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
INSERT INTO vehiculo (imagen, precio, anio, id_mod, pasajeros, puertas, transmision, combustible, detalles1, detalles2, detalles3, detalles4) 
VALUES 
('Catalogo1.png', 120.00, 2020, 1, 4, 4, 'MANUAL', 'DIESEL', 'Detalles15.jpg', 'Detalles12.jpg', 'Detalles9.jpg', 'Detalles7.jpg'),
('Catalogo2.png', 150.00, 2021, 2, 6, 5, 'AUTOMATICO', 'DIESEL', 'Detalles13.jpg', 'Detalles11.jpg', 'Detalles6.jpg', 'Detalles5.jpg'),
('Catalogo3.png', 90.00, 2019, 3, 2, 3, 'MANUAL', 'GASOLINA', 'Detalles14.jpg', 'Detalles10.jpg', 'Detalles7.jpg', 'Detalles4.jpg'),
('Catalogo4.png', 110.00, 2022, 4, 5, 5, 'AUTOMATICO', 'DIESEL', 'Detalles12.jpg', 'Detalles8.jpg', 'Detalles6.jpg', 'Detalles5.jpg'),
('Catalogo5.png', 95.00, 2023, 5, 2, 3, 'MANUAL', 'DIESEL', 'Detalles15.jpg', 'Detalles12.jpg', 'Detalles9.jpg', 'Detalles7.jpg'),
('Catalogo6.png', 130.00, 2021, 6, 6, 5, 'AUTOMATICO', 'GASOLINA', 'Detalles14.jpg', 'Detalles9.jpg', 'Detalles4.jpg', 'Detalles7.jpg'),
('Catalogo7.png', 100.00, 2020, 7, 3, 4, 'MANUAL', 'DIESEL', 'Detalles14.jpg', 'Detalles5.jpg', 'Detalles9.jpg', 'Detalles7.jpg'),
('Catalogo8.png', 115.00, 2022, 8, 4, 3, 'AUTOMATICO', 'GASOLINA', 'Detalles15.jpg', 'Detalles13.jpg', 'Detalles9.jpg', 'Detalles7.jpg'),
('Catalogo9.png', 160.00, 2023, 9, 4, 4, 'AUTOMATICO', 'DIESEL', 'Detalles12.jpg', 'Detalles4.jpg', 'Detalles9.jpg', 'Detalles7.jpg'),
('Catalogo10.png', 125.00, 2020, 10, 5, 4, 'MANUAL', 'DIESEL', 'Detalles10.jpg', 'Detalles12.jpg', 'Detalles7.jpg', 'Detalles5.jpg'),
('Catalogo11.png', 140.00, 2021, 11, 4, 4, 'AUTOMATICO', 'GASOLINA', 'Detalles11.jpg', 'Detalles13.jpg', 'Detalles6.jpg', 'Detalles4.jpg'),
('Catalogo12.png', 105.00, 2018, 12, 6, 5, 'MANUAL', 'DIESEL', 'Detalles13.jpg', 'Detalles12.jpg', 'Detalles5.jpg', 'Detalles3.jpg'),
('Catalogo13.png', 150.00, 2022, 13, 3, 3, 'AUTOMATICO', 'DIESEL', 'Detalles14.jpg', 'Detalles9.jpg', 'Detalles8.jpg', 'Detalles4.jpg'),
('Catalogo14.png', 135.00, 2023, 14, 5, 4, 'MANUAL', 'GASOLINA', 'Detalles15.jpg', 'Detalles10.jpg', 'Detalles7.jpg', 'Detalles6.jpg'),
('Catalogo15.png', 170.00, 2024, 15, 4, 4, 'AUTOMATICO', 'DIESEL', 'Detalles14.jpg', 'Detalles11.jpg', 'Detalles9.jpg', 'Detalles6.jpg');

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
('2025-04-15', '2025-04-22', 760.00, 'FINALIZADA', 2, 5, 1, 3),
('2025-05-15', '2025-05-22', 800.00, 'FINALIZADA', 4, 7, 3, 1),
('2025-03-10', '2025-03-17', 650.00, 'FINALIZADA', 2, 6, 2, 1),
('2025-02-01', '2025-02-07', 780.00, 'FINALIZADA', 3, 6, 1, 4),
('2025-01-10', '2025-01-15', 650.00, 'FINALIZADA', 4, 6, 3, 2),
('2025-06-01', '2025-06-05', 920.00, 'CURSO', 2, 8, 4, 2),
('2025-06-02', '2025-06-09', 1200.00, 'CURSO', 2, 2, 5, 3),
('2025-06-15', '2025-06-22', 720.00, 'RESERVADA', 2, 3, 4, 1),
('2025-06-20', '2025-06-27', 880.00, 'RESERVADA', 2, 4, 2, 5),
('2025-04-01', '2025-04-08', 800.00, 'FINALIZADA', 2, 10, 1, 3),
('2025-04-10', '2025-04-15', 700.00, 'FINALIZADA', 4, 11, 3, 2),
('2025-02-20', '2025-02-25', 950.00, 'FINALIZADA', 3, 12, 4, 1),
('2025-03-05', '2025-03-10', 870.00, 'FINALIZADA', 2, 13, 2, 4),
('2025-03-15', '2025-03-20', 920.00, 'FINALIZADA', 4, 14, 5, 2),
('2025-03-25', '2025-03-30', 990.00, 'FINALIZADA', 3, 15, 1, 5),
('2025-06-25', '2025-07-02', 1100.00, 'CURSO', 4, 10, 3, 4),
('2025-07-10', '2025-07-15', 890.00, 'RESERVADA', 2, 11, 2, 1),
('2025-06-08', '2025-06-12', 950.00, 'CURSO', 5, 12, 3, 1),
('2025-06-09', '2025-06-13', 790.00, 'CURSO', 6, 13, 2, 4);

-- Insertar reseñas (solo para reservas finalizadas)
INSERT INTO resenia (comentario, puntuacion, fecha, id_reser)
VALUES
('Buena experiencia aunque la entrega fue un poco lenta.', 4, '2025-05-09', 2),
('Servicio aceptable, aunque el vehículo tenía algunos detalles.', 3, '2025-05-23', 3),
('Todo genial, el vehículo estaba limpio y bien equipado.', 5, '2025-02-08', 4),
('Buena atención al cliente, aunque el GPS no funcionaba bien.', 4, '2025-01-16', 5),
('Vehículo en perfectas condiciones, repetiría sin duda.', 5, '2025-04-09', 10),
('Todo bien, aunque el aire acondicionado no funcionaba.', 3, '2025-04-16', 11),
('Excelente atención en la sede y muy buen coche.', 5, '2025-02-26', 12),
('Vehículo cómodo pero el depósito no estaba lleno.', 4, '2025-03-11', 13),
('La furgoneta estaba limpia y en muy buen estado.', 5, '2025-03-21', 14),
('El proceso de recogida fue algo lento, pero buen servicio.', 4, '2025-03-31', 15),
('Muy buena relación calidad-precio, repetiré.', 5, '2025-04-09', 1);