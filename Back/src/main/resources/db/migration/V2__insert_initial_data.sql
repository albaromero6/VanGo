-- Limpiar tablas en orden inverso a sus dependencias
DELETE FROM resenia;
DELETE FROM reserva;
DELETE FROM vehiculo;
DELETE FROM modelo;
DELETE FROM marca;
DELETE FROM sede;
DELETE FROM usuario;

-- Insertar usuarios
INSERT INTO usuario (nombre, apellido, email, password, telefono, registro, rol, dni) 
VALUES 
('Juan', 'Pérez', 'juan.perez@example.com', '$2a$12$hBT3/pJjoKp9p26YgjO.Pu0C8JFJ6WNtj3d1IBNSK0NRltIA4CK6K', '600123456', '2025-05-07', 'CLIENTE', '79954430J'),
('Ana', 'Gómez', 'ana.gomez@example.com', '$2a$12$hBT3/pJjoKp9p26YgjO.Pu0C8JFJ6WNtj3d1IBNSK0NRltIA4CK6K', '611234567', '2025-04-20', 'ADMINISTRADOR', '19338585R'),
('Carlos', 'López', 'carlos.lopez@example.com', '$2a$12$hBT3/pJjoKp9p26YgjO.Pu0C8JFJ6WNtj3d1IBNSK0NRltIA4CK6K', '622345678', '2025-03-15', 'CLIENTE', '32550686M');

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
('2025-05-12', '2025-05-19', 380.00, 'RESERVADA', 1, 5, 1, 3),
('2025-05-14', '2025-05-21', 520.00, 'CURSO', 2, 6, 2, 4),
('2025-05-16', '2025-05-23', 400.00, 'FINALIZADA', 3, 7, 3, 1),
('2025-05-18', '2025-05-25', 460.00, 'RESERVADA', 1, 8, 4, 2);

-- Insertar reseñas (corregidos los IDs para que coincidan con las reservas)
INSERT INTO resenia (comentario, puntuacion, fecha, id_reser) 
VALUES 
('Vehículo en excelente estado, todo perfecto.', 5, '2025-05-20', 1),
('Buena experiencia aunque la entrega fue un poco lenta.', 4, '2025-05-22', 2),
('Servicio aceptable, aunque el vehículo tenía algunos detalles.', 3, '2025-05-24', 3),
('Muy satisfecho con el alquiler, repetiré sin duda.', 5, '2025-05-26', 4); 