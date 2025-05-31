# Documentación de la Base de Datos

### Tablas

1. **usuario**
   - Almacena información de usuarios (clientes y administradores)
   - Campos principales: id_usu, nombre, apellido, email, password, rol, dni

2. **marca**
   - Catálogo de marcas de vehículos
   - Campos principales: id_mar, nombre

3. **modelo**
   - Modelos de vehículos asociados a marcas
   - Campos principales: id_mod, nombre, id_mar

4. **vehiculo**
   - Información detallada de cada vehículo
   - Campos principales: id_veh, matricula, precio, id_mod, características

5. **sede**
   - Ubicaciones de las oficinas
   - Campos principales: id_sed, direccion, ciudad, telefono

6. **reserva**
   - Registro de alquileres
   - Campos principales: id_res, inicio, fin, total, estado, id_usu, id_veh

7. **resenia**
   - Comentarios y valoraciones de los clientes
   - Campos principales: id_rese, comentario, puntuacion, id_reser

## Migraciones

- `V1__create_tables.sql`: Creación de la estructura de tablas
- `V2__insert_initial_data.sql`: Datos iniciales


## Notas de Implementación

- Se utiliza Flyway para el control de versiones de la base de datos
- Las contraseñas están encriptadas con BCrypt
- Se mantiene un historial de cambios en la base de datos 