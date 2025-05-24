USE CreacionBD;

SET FOREIGN_KEY_CHECKS = 0;

insert into proveedor (dni, nombre, apellidos, telefono, email, direccion_fiscal, cuenta_bancaria) values
('12345678a', 'juan', 'pérez lópez', '600123456', 'juanp@example.com', 'calle falsa 123, madrid', 'es1234567890123456789012'),
('87654321b', 'maría', 'gómez ruiz', '611223344', 'mariag@example.com', 'av. constitución 12, sevilla', 'es9876543210987654321098'),
('23456789c', 'luis', 'fernández garrido', '622334455', 'luisf@example.com', 'paseo del prado 45, valencia', 'es5647382910123456789012'),
('34567890d', 'ana', 'martínez lozano', '633445566', 'anam@example.com', 'ronda sur 77, murcia', 'es9081726354839201827364');

insert into promocion (proveedor_id, descripcion, descuento, fecha_inicio, fecha_fin, activo) values
(1, 'descuento de verano', 10.00, '2025-06-01', '2025-06-30', true),
(2, 'oferta especial en electrónica', 15.00, '2025-05-01', '2025-05-15', false),
(3, 'rebajas de primavera', 5.50, '2025-03-15', '2025-04-15', true),
(4, 'liquidación de stock', 20.00, '2025-01-01', '2025-01-31', false);
insert into producto (nombre, fecha_entrada, stock, es_online, precio, proveedor_id) values
('teclado mecánico', '2025-05-20', 50, true, 59.99, 1),
('monitor 24"', '2025-05-15', 30, false, 129.99, 2),
('ratón inalámbrico', '2025-05-22', 100, true, 25.50, 1),
('impresora multifunción', '2025-04-10', 20, false, 89.95, 3),
('portátil 15"', '2025-03-12', 15, true, 699.00, 4),
('disco ssd 1tb', '2025-04-25', 80, true, 109.90, 3);

insert into empleado (usuario, contrasenya) values
('jlopez', 'abc123'),
('mgomez', 'def456'),
('cmartin', 'ghi789'),
('rnavarro', 'xyz321');

insert into cliente (dni, nombre, apellidos, email) values
('00000000a', 'carlos', 'ruiz fernández', 'carlosr@example.com'),
('11111111b', 'lucía', 'navarro díaz', 'lucian@example.com'),
('22222222c', 'mario', 'gutiérrez pérez', 'mariog@example.com'),
('33333333d', 'sofia', 'moreno lópez', 'sofiam@example.com'),
('44444444e', 'alberto', 'sánchez torres', 'albertos@example.com');

insert into sorteo (resultado, premio, cliente_dni) values
('ganador', 'vale de 50€', '00000000a'),
('perdedor', null, '11111111b'),
('no participó', null, '22222222c'),
('ganador', 'producto gratis', '33333333d'),
('perdedor', null, '44444444e');
insert into venta (fecha_venta, total, metodo_pago, metodo_envio, coste_envio, empleado_id, cliente_dni) values
('2025-05-01', 159.98, 'tarjeta', '24h', 4.99, 1, '00000000a'),
('2025-05-02', 89.95, 'efectivo', '3 a 5 dias', 2.99, 2, '11111111b'),
('2025-05-03', 25.50, 'despues de entrega', '7 a 12 dias', 0.00, 3, '22222222c'),
('2025-05-04', 699.00, 'tarjeta', '24h', 5.99, 4, '33333333d');
insert into cupon (descuento, usado, fecha_uso, venta_id, cliente_dni, promocion_id) values
(10.00, true, '2025-05-01', 1, '00000000a', 1),
(5.00, false, null, null, '11111111b', 3),
(20.00, true, '2025-05-04', 4, '33333333d', 4);

insert into producto_en_venta (venta_id, producto_id, cantidad) values
(1, 1, 1),
(1, 3, 2),
(2, 4, 1),
(3, 3, 1),
(4, 5, 1);

insert into factura (fecha_emision, dni_cliente, venta_id, total) values
('2025-05-01', '00000000a', 1, 159.98),
('2025-05-02', '11111111b', 2, 89.95),
('2025-05-03', '22222222c', 3, 25.50),
('2025-05-04', '33333333d', 4, 699.00);

SET FOREIGN_KEY_CHECKS = 1;