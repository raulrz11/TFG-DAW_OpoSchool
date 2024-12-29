INSERT INTO usuario(nombre, username, email, password, is_active)
    VALUES ('Elizabeth', 'admin', 'eli@gmail.com', '$2a$12$CgtKjUarTJIOlkZimeT6CuuS1uOgIR4F7rzg3w7s/jFR/Wr9RAhgK', true);
INSERT INTO usuario_rols (usuario_id, rols)
    VALUES (1, 'ADMIN');
INSERT INTO usuario(nombre, username, email, password, is_active)
    VALUES ('Erik', 'eten18', 'erik@gmail.com', '$2a$12$CgtKjUarTJIOlkZimeT6CuuS1uOgIR4F7rzg3w7s/jFR/Wr9RAhgK', true);
INSERT INTO usuario_rols (usuario_id, rols)
    VALUES (2, 'PROFESOR');
INSERT INTO usuario(nombre, username, email, password, is_active)
    VALUES ('Raul', 'rrodriguez11', 'raul@gmail.com', '$2a$12$CgtKjUarTJIOlkZimeT6CuuS1uOgIR4F7rzg3w7s/jFR/Wr9RAhgK', true);
INSERT INTO usuario_rols (usuario_id, rols)
    VALUES (3, 'ALUMNO');
INSERT INTO usuario(nombre, username, email, password, is_active)
    VALUES ('Eva', 'ecortes77', 'eva@gmail.com', '$2a$12$CgtKjUarTJIOlkZimeT6CuuS1uOgIR4F7rzg3w7s/jFR/Wr9RAhgK', true);
INSERT INTO usuario_rols (usuario_id, rols)
    VALUES (4, 'ALUMNO');
/*
admin
admin1
*/
/*PROFESORES*/
INSERT INTO profesor(nombre, apellidos, email, telefono, dni, is_deleted, usuario_id)
    VALUES ('Elizabeth', 'Luna Coleto', 'eli@gmail.com', '696203345', '87563412P', false, 1);
INSERT INTO profesor(nombre, apellidos, email, telefono, dni, is_deleted, usuario_id)
    VALUES ('Erik', 'Ten Hag', 'erik@gmail.com', '605392210', '30164739K', false, 2);
/*GRUPOS*/
INSERT INTO grupo(nombre, fecha_clase, hora_clase, duracion_clase_string, profesor_id)
    VALUES ('A', '2024-12-10', '18:00', 'PT2H30M', 1);
INSERT INTO grupo(nombre, fecha_clase, hora_clase, duracion_clase_string, profesor_id)
    VALUES ('B', '2024-12-16', '16:45', 'PT1H30M', 2);
/*ALUMNOS*/
INSERT INTO alumno(nombre, apellidos, email, telefono, dni, is_deleted, grupo_id, usuario_id)
    VALUES ('Raul', 'Rodriguez Luna', 'raul@gmail.com', '722397635', '54230901W', false, 1, 3);
INSERT INTO alumno(nombre, apellidos, email, telefono, dni, is_deleted, grupo_id, usuario_id)
    VALUES ('Eva', 'Cortes Garcia', 'eva@gmail.com', '603568823', '09125910L', false, 2, 4);
/*TAREAS*/
INSERT INTO tarea(titulo, descripcion, fecha_entrega, archivo_url, grupo_id)
    VALUES ('Prueba Inicial', 'Prueba para ver tu nivel', '2024-12-16', NULL, 1);
INSERT INTO tarea(titulo, descripcion, fecha_entrega, archivo_url, grupo_id)
    VALUES ('Practica de vocabulario', 'Completa la ficha de vocabulario', '2024-12-18', NULL, 1);
INSERT INTO tarea(titulo, descripcion, fecha_entrega, archivo_url, grupo_id)
    VALUES ('Examen Inicial', 'Primer examen para evaluar el nivel', '2024-12-20', NULL, 2);
INSERT INTO tarea(titulo, descripcion, fecha_entrega, archivo_url, grupo_id)
    VALUES ('Tarea de gramatica', 'Completa la dicha de gramatica', '2024-12-24', NULL, 2);
/*SUBSCRIPCION*/
INSERT INTO subscripcion(fecha_inicio, fecha_fin, estado, precio, num_tarjeta, fecha_expiracion, codigo_seguridad, alumno_id, created_at, updated_at, deleted_at)
    VALUES ('2024-12-12', '2024-12-30', 0, 50.00, '1234567890123456', '2025-12-01', '123', 1, '2024-11-17 17:30:00', '2024-11-17 17:30:00', NULL);
INSERT INTO subscripcion(fecha_inicio, fecha_fin, estado, precio, num_tarjeta, fecha_expiracion, codigo_seguridad, alumno_id, created_at, updated_at, deleted_at)
    VALUES ('2024-12-15', '2024-12-29', 2, 50.00, '1234567890123456', '2025-12-01', '123', 2, '2024-11-17 17:30:00', '2024-11-17 17:30:00', NULL);