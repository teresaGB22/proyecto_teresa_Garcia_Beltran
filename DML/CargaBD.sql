USE CreacionBD;

SET FOREIGN_KEY_CHECKS = 0;

-- PROVEEDORES (agregando teléfono, email, dirección fiscal y cuenta bancaria)
INSERT INTO proveedor (dni, nombre, apellidos, telefono, email, direccion_fiscal, cuenta_bancaria) VALUES
('12345678A', 'Juan', 'Pérez López', '600123456', 'juan.perez@mail.com', 'Calle Falsa 123, Ciudad', 'ES7620770024003102575766'),
('23456789B', 'Ana', 'Gómez Ruiz', '600234567', 'ana.gomez@mail.com', 'Av. Siempre Viva 742, Ciudad', 'ES9121000418450200051332'),
('34567890C', 'Carlos', 'Fernández Ortega', '600345678', 'carlos.fernandez@mail.com', 'Plaza Mayor 10, Ciudad', 'ES7912345678901234567890'),
('45678901D', 'Lucía', 'Martínez Díaz', '600456789', 'lucia.martinez@mail.com', 'Paseo del Prado 5, Ciudad', 'ES8000000000000000000000'),
('56789012E', 'Pedro', 'Sánchez Morales', '600567890', 'pedro.sanchez@mail.com', 'Ronda de la Universidad 7, Ciudad', 'ES8400491500051234567890');

-- PROMOCIONES
INSERT INTO promocion (proveedor_id, descripcion, descuento, fecha_inicio, fecha_fin, activo) VALUES
(1, 'Descuento primavera laptops', 10.00, '2024-03-01', '2024-05-31', TRUE),
(2, 'Oferta periféricos', 15.00, '2024-03-15', '2024-04-30', TRUE),
(3, 'Silla + cámara: combo office', 20.00, '2024-02-20', '2024-06-01', TRUE),
(4, 'Tecnología para gamers', 25.00, '2024-04-01', '2024-06-15', TRUE),
(5, 'Almacenamiento al mejor precio', 12.50, '2024-03-10', '2024-05-10', TRUE);

-- PRODUCTOS
INSERT INTO producto (nombre, fecha_entrada, stock, es_online, precio, proveedor_id) VALUES
('Laptop Dell', '2024-03-15', 20, TRUE, 1200.00, 1),
('Mouse Logitech', '2024-03-18', 50, TRUE, 25.99, 1),
('Monitor LG', '2024-04-01', 15, TRUE, 300.00, 2),
('Teclado Corsair', '2024-03-22', 25, TRUE, 80.00, 2),
('Silla Oficina', '2024-02-28', 10, FALSE, 150.00, 3),
('Mesa Gamer', '2024-04-05', 5, FALSE, 250.00, 4),
('Tablet Samsung', '2024-04-10', 12, TRUE, 450.00, 4),
('Disco Duro 1TB', '2024-03-20', 30, TRUE, 70.00, 5),
('Auriculares JBL', '2024-03-25', 40, TRUE, 60.00, 5),
('Cámara Logitech', '2024-03-29', 18, TRUE, 90.00, 3);

-- EMPLEADOS
INSERT INTO empleado (usuario, contrasenya) VALUES
('empleado1', '1234'),
('empleado2', '2345'),
('empleado3', 'abcd'),
('empleado4', 'qwerty'),
('empleado5', 'admin');

-- CLIENTES
INSERT INTO cliente (dni, nombre, apellidos, email) VALUES
('11111111A', 'Luis', 'Torres Gil', 'luis@gmail.com'),
('22222222B', 'Marta', 'Navarro López', 'marta@gmail.com'),
('33333333C', 'Jorge', 'Serrano Ruiz', 'jorge@gmail.com'),
('44444444D', 'Carmen', 'Ibáñez Pérez', 'carmen@gmail.com'),
('55555555E', 'Diego', 'Cano Vega', 'diego@gmail.com'),
('66666666F', 'Pilar', 'Rey Castillo', 'pilar@gmail.com'),
('77777777G', 'Elena', 'Romero Lara', 'elena@gmail.com'),
('88888888H', 'Hugo', 'Santos Morales', 'hugo@gmail.com'),
('99999999I', 'Raúl', 'Marín Rojas', 'raul@gmail.com'),
('10101010J', 'Nuria', 'Molina Jiménez', 'nuria@gmail.com'),
('12121212K', 'Iván', 'Peña Bravo', 'ivan@gmail.com'),
('13131313L', 'Alba', 'Delgado León', 'alba@gmail.com'),
('14141414M', 'David', 'Campos Ortega', 'david@gmail.com'),
('15151515N', 'Sofía', 'Ruiz Nieto', 'sofia@gmail.com'),
('16161616O', 'Noelia', 'Cruz Vargas', 'noelia@gmail.com');

-- SORTEOS
INSERT INTO sorteo (resultado, premio, cliente_dni) VALUES
('GANADOR', 'Auriculares', '11111111A'),
('PERDEDOR', NULL, '22222222B'),
('GANADOR', 'Tablet', '33333333C'),
('NO PARTICIPÓ', NULL, '44444444D'),
('PERDEDOR', NULL, '55555555E'),
('GANADOR', 'Vale 100€', '66666666F'),
('PERDEDOR', NULL, '77777777G'),
('GANADOR', 'Mouse Gamer', '88888888H'),
('PERDEDOR', NULL, '99999999I'),
('NO PARTICIPÓ', NULL, '10101010J');

-- VENTAS
INSERT INTO venta (fecha_venta, total, metodo_pago, metodo_envio, coste_envio, empleado_id, cliente_dni) VALUES
('2024-04-01', 150.00, 'TARJETA', '24H', 5.00, 1, '11111111A'),
('2024-04-02', 80.50, 'EFECTIVO', '3 A 5 DIAS', 3.00, 2, '22222222B'),
('2024-04-03', 120.99, 'TARJETA', '7 A 12 DIAS', 2.50, 3, '33333333C'),
('2024-04-04', 99.90, 'EFECTIVO', '3 A 5 DIAS', 4.00, 4, '44444444D'),
('2024-04-05', 65.00, 'TARJETA', '24H', 5.00, 1, '55555555E'),
('2024-04-06', 40.99, 'DESPUES DE ENTREGA', '24H', 2.50, 2, '66666666F'),
('2024-04-07', 199.99, 'TARJETA', '24H', 5.00, 3, '77777777G'),
('2024-04-08', 89.50, 'EFECTIVO', '7 A 12 DIAS', 3.00, 1, '88888888H'),
('2024-04-09', 130.00, 'TARJETA', '3 A 5 DIAS', 4.00, 4, '99999999I'),
('2024-04-10', 58.75, 'TARJETA', '24H', 2.00, 5, '10101010J'),
('2024-04-11', 74.60, 'EFECTIVO', '3 A 5 DIAS', 3.50, 2, '12121212K'),
('2024-04-12', 160.30, 'TARJETA', '24H', 5.00, 3, '13131313L'),
('2024-04-13', 200.00, 'TARJETA', '7 A 12 DIAS', 4.50, 4, '14141414M'),
('2024-04-14', 110.00, 'EFECTIVO', '3 A 5 DIAS', 3.00, 1, '15151515N'),
('2024-04-15', 99.99, 'DESPUES DE ENTREGA', '24H', 2.50, 2, '16161616O');

-- CUPONES
INSERT INTO cupon (descuento, cliente_dni, promocion_id) VALUES
(5.00, '11111111A', 1),
(10.00, '22222222B', 2),
(7.50, '33333333C', 3),
(8.00, '44444444D', 4),
(12.50, '55555555E', 5),
(15.00, '66666666F', 1),
(20.00, '77777777G', 3),
(5.00, '88888888H', 2),
(10.00, '99999999I', 4),
(15.00, '10101010J', 5),
(7.00, '12121212K', 2),
(12.00, '13131313L', 4),
(13.50, '14141414M', 1),
(8.00, '15151515N', 3),
(9.99, '16161616O', 5);

-- PRODUCTOS EN VENTA
INSERT INTO producto_en_venta (venta_id, producto_id, cantidad) VALUES 
(1, 1, 1), (1, 2, 2), (2, 3, 1), (3, 1, 1),
(3, 4, 1), (4, 5, 1), (5, 6, 1), (6, 7, 1),
(7, 8, 2), (8, 9, 1), (9, 10, 1), (10, 1, 1),
(11, 2, 1), (12, 3, 2), (13, 4, 1), (14, 5, 1),
(15, 6, 1), (5, 3, 1), (6, 9, 2), (7, 10, 1), (7, 7, 1), (8, 6, 1);

-- FACTURAS
INSERT INTO factura (fecha_emision, dni_cliente, venta_id, total) VALUES
('2024-04-01', '11111111A', 1, 150.00),
('2024-04-02', '22222222B', 2, 80.50),
('2024-04-03', '33333333C', 3, 120.99),
('2024-04-04', '44444444D', 4, 99.90),
('2024-04-05', '55555555E', 5, 65.00),
('2024-04-06', '66666666F', 6, 40.99),
('2024-04-07', '77777777G', 7, 199.99),
('2024-04-08', '88888888H', 8, 89.50),
('2024-04-09', '99999999I', 9, 130.00),
('2024-04-10', '10101010J', 10, 58.75),
('2024-04-11', '12121212K', 11, 74.60),
('2024-04-12', '13131313L', 12, 160.30),
('2024-04-13', '14141414M', 13, 200.00),
('2024-04-14', '15151515N', 14, 110.00),
('2024-04-15', '16161616O', 15, 99.99);

SET FOREIGN_KEY_CHECKS = 1;