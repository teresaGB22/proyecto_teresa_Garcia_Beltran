-- operacion ver pedidos
use creacionbd;
drop procedure if exists verPedidos;
delimiter $$
create procedure verPedidos(in id int)
begin
select v.id_venta, v.fecha_venta, v.total, v.estado_pedido, c.nombre, c.apellidos
from venta v
inner join producto_en_venta pev on v.id_venta = pev.venta_id
inner join producto p on pev.producto_id = p.id_producto
inner join cliente c on v.cliente_dni = c.dni
where p.proveedor_id = id
group by v.id_venta;
end$$
delimiter ;

call verPedidos(2);
select id_proveedor, dni from proveedor;
ALTER TABLE venta
ADD estado_pedido ENUM('PENDIENTE', 'CONFIRMADO', 'RECHAZADO', 'ENVIADO', 'ENTREGADO', 'CANCELADO') DEFAULT 'PENDIENTE';
DROP PROCEDURE IF EXISTS ventasProveedor;
DELIMITER $$
CREATE PROCEDURE ventasProveedor(IN id INT)
BEGIN
    SELECT v.id_venta, v.fecha_venta, v.total, c.nombre AS nombre_cliente, c.apellidos AS apellidos_cliente
    FROM venta v
    JOIN producto_en_venta pv ON v.id_venta = pv.venta_id
    JOIN producto p ON pv.producto_id = p.id_producto
    JOIN cliente c ON v.cliente_dni = c.dni
    WHERE p.proveedor_id = id
    GROUP BY v.id_venta;
END$$
DELIMITER ;
drop procedure if exists facturacion;
delimiter $$
create procedure facturacion(in id int)
begin
select 
        f.fecha_emision, 
        f.total, 
        concat(c.nombre, ' ', c.apellidos) as cliente_nombre_apellidos,
        c.email as cliente_email,
        v.metodo_pago,
        v.metodo_envio
    from factura f
    inner join cliente c on f.dni_cliente = c.dni
    inner join venta v on f.venta_id = v.id_venta
    where f.venta_id = id;
    
    -- Productos comprados con cantidad
   select
        p.nombre as producto,
        pev.cantidad
    from producto_en_venta pev
    inner join producto p on pev.producto_id = p.id_producto
    where pev.venta_id = id;
end$$
delimiter ;
DROP PROCEDURE IF EXISTS estadisticasProveedor;
DELIMITER $$
CREATE PROCEDURE estadisticasProveedor(IN proveedorId INT)
BEGIN
    SELECT 
    COUNT(DISTINCT v.id_venta) AS total_ventas,
    IFNULL(SUM(v.total), 0) AS ingresos,
    (
        SELECT p.nombre
        FROM producto p
        JOIN producto_en_venta pv ON pv.producto_id = p.id_producto
        JOIN venta v2 ON v2.id_venta = pv.venta_id
        WHERE p.proveedor_id = proveedor_id
        GROUP BY p.id_producto
        ORDER BY SUM(pv.cantidad) DESC
        LIMIT 1
    ) AS producto_mas_vendido
FROM venta v
JOIN producto_en_venta pv ON v.id_venta = pv.venta_id
JOIN producto p ON pv.producto_id = p.id_producto
WHERE p.proveedor_id = proveedor_id;
END$$
DELIMITER ;
ALTER TABLE proveedor ADD COLUMN imagen LONGBLOB;
alter table proveedor add column ruta_imagen varchar(300);