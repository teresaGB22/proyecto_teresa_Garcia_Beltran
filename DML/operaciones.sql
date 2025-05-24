-- operacion ver pedidos
use creacionbd;

-- creacion del provedimiento verPedidos
-- modifico la tabla para que tenga estado_pedido enum
ALTER TABLE venta
ADD estado_pedido ENUM('PENDIENTE', 'CONFIRMADO', 'RECHAZADO', 'ENVIADO', 'ENTREGADO', 'CANCELADO') DEFAULT 'PENDIENTE';

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

-- creacion del procedimiento ventasProveedor
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

 -- creacion del procedimiento estadisticas para el proveedor
drop procedure if exists  estadisticasProveedor;
DELIMITER $$
create procedure estadisticasProveedor(in proveedorId int)
begin
    select 
    count(distinct v.id_venta) as total_ventas,
    ifnull(sum(v.total), 0) as ingresos,
    (
        select p.nombre
        from producto p
        join producto_en_venta pv on pv.producto_id = p.id_producto
        join venta v2 on v2.id_venta = pv.venta_id
        where p.proveedor_id = proveedor_id
        group by p.id_producto
        order by sum(pv.cantidad) desc
        limit 1
    ) as producto_mas_vendido
from venta v
join producto_en_venta pv on v.id_venta = pv.venta_id
join producto p on pv.producto_id = p.id_producto
where p.proveedor_id = proveedor_id;
end$$
delimiter ;

-- modificacion proveedor añadida columna imagen y su ruta
alter table proveedor add column imagen longblob;
alter table proveedor add column ruta_imagen varchar(300);

-- procedimiento de carga para cupones existentes
drop procedure if exists cargar_cupones_existentes;
delimiter $$
create procedure cargar_cupones_existentes(in id int)
begin
SELECT c.id_cupon, c.descuento, p.descripcion 
FROM cupon c JOIN promocion p 
ON c.promocion_id = p.id_promocion 
WHERE c.usado = false 
AND p.proveedor_id = id;
end$$
delimiter ;
select * from promocion where proveedor_id = 2;

-- procedimiento para usar el cupon en la venta
drop procedure if exists usar_cupon_en_venta;
delimiter $$

create procedure usar_cupon_en_venta(
    in p_id_cupon int,
    in p_id_venta int
)
begin
    declare v_descuento decimal(5,2);
    declare v_usado boolean;
    declare v_cliente_dni varchar(9);
    declare v_promocion_id int;
    declare v_proveedor_id int;

    select c.descuento, c.usado, c.cliente_dni, c.promocion_id, p.proveedor_id
    into v_descuento, v_usado, v_cliente_dni, v_promocion_id, v_proveedor_id
    from cupon c
    join promocion p on c.promocion_id = p.id_promocion
    where c.id_cupon = p_id_cupon;

    if v_usado then
        signal sqlstate '45000'
        set message_text = 'el cupón ya fue usado.';
    end if;

  
    if not exists (
        select 1 from venta 
        where id_venta = p_id_venta and cliente_dni = v_cliente_dni
    ) then
        signal sqlstate '45000'
        set message_text = 'el cupón no pertenece al cliente de la venta.';
    end if;

  
    if not exists (
        select 1
        from producto_en_venta pev
        join producto pr on pr.id_producto = pev.producto_id
        where pev.venta_id = p_id_venta and pr.proveedor_id = v_proveedor_id
    ) then
        signal sqlstate '45000'
        set message_text = 'el cupón no aplica a ningún producto en esta venta.';
    end if;

    update venta
    set total = greatest(0, total - v_descuento)
    where id_venta = p_id_venta;

    update cupon
    set usado = true,
        fecha_uso = curdate(),
        venta_id = p_id_venta
    where id_cupon = p_id_cupon;

end $$

delimiter ;

-- modificar cupon para que tenga el id del producto
alter table cupon add column producto_id int;


-- modificar venta que sea enum metodo_pago y metodo_envio con estas opciones
alter table venta modify metodo_pago enum(
    'TARJETA', 'EFECTIVO', 'DESPUES DE ENTREGA', 'PAYPAL', 'BIZUM'
);
alter table venta modify metodo_envio enum(
    '24H', '3 A 5 DIAS', '7 A 12 DIAS', 'URGENTE', 'RECOGIDA EN TIENDA'
);
-- modificar sorteo para que el dni del cliente sea nulo, no tiene porque participar si todavia no ha participado
ALTER TABLE sorteo
MODIFY COLUMN cliente_dni VARCHAR(9) NULL;
-- modificar sorteo para que tenga la columna del id del proveedor
ALTER TABLE sorteo
ADD COLUMN proveedor_id INT NOT NULL;
update sorteo 
set proveedor_id = 1 
where proveedor_id not in (select id_proveedor from proveedor);
describe sorteo;
describe proveedor;
-- añadir la foreign key de proveedor a sortep
ALTER TABLE sorteo
ADD CONSTRAINT fk_proveedor_sorteo FOREIGN KEY (proveedor_id) REFERENCES proveedor(id_proveedor) ON UPDATE CASCADE;


describe empleado;

-- procedimiento para revisar los pedidos que hay
drop procedure if exists revisarPedidos;
delimiter $$
create procedure revisarPedidos(in id int)
begin
 select 
        v.id_venta, 
        v.fecha_venta, 
        v.total, 
        v.estado_pedido, 
        c.dni, c.nombre, c.apellidos, c.email, v.metodo_envio, v.metodo_pago
    from venta as v
    inner join cliente as c on v.cliente_dni = c.dni
    where v.empleado_id = id;

end$$
delimiter ;
call revisarPedidos(2);
describe venta;
-- modificada tabla venta para que el coste del envio sea double no nula y por defecto 0
ALTER TABLE venta MODIFY coste_envio decimal(7,2) NOT NULL DEFAULT 0.00;

-- creacion del procedimiento facturacion
drop procedure if exists facturacion;
delimiter $$

create procedure facturacion(in id int)
begin
  select 
    f.id_factura,
    f.fecha_emision, 
    f.total as total, 
    f.total_con_descuento as total_con_descuento,
    c.dni,
    concat(c.nombre, ' ', c.apellidos) as cliente_nombre_apellidos,
    c.email as cliente_email,
    v.metodo_pago,
    v.metodo_envio,
    p.nombre as proveedor_nombre,
    p.direccion_fiscal as proveedor_direccion,
    p.telefono as proveedor_telefono,
    p.email as proveedor_email,
    p.cuenta_bancaria as proveedor_cuenta_bancaria,
    prod.nombre as producto,
    pev.cantidad,
    prod.precio,                
    prod.precio_descuento
  from factura f
  left join venta v on f.venta_id = v.id_venta
  left join cliente c on v.cliente_dni = c.dni
  left join producto_en_venta pev on pev.venta_id = v.id_venta
  left join producto prod on pev.producto_id = prod.id_producto
  left join proveedor p on prod.proveedor_id = p.id_proveedor
  where f.venta_id = id;
end $$

delimiter ;

describe venta;

-- trigger de inicializacion (al insertar una nueva venta)

drop trigger if exists antes_insertar_venta;
delimiter &&
create trigger antes_de_insertar
before insert on venta
for each row
begin
if new.estado_pedido is null then
set new.estado_pedido = 'PENDIENTE';
end if;

if new.coste_envio is null then
set new.coste_envio = 0.00;
end if;

if new.fecha_venta is null then
set new.fecha_venta = curdate();
end if;
end&&
delimiter ;

-- añadir el id del producto a la promocion
alter table promocion add column producto_id int;

update promocion set producto_id = 1 where producto_id is null ;

alter table promocion modify column producto_id int not null;
-- añadir como fk de producto
alter table promocion
add constraint fk_promocion_producto
  foreign key (producto_id) references producto(id_producto);
  
  -- triger antes de insertar promocion
 
drop trigger if exists promocion_antes_insertar;
delimiter &&

create trigger promocion_antes_insertar
before insert on promocion
for each row
begin
  if new.fecha_inicio is null then
    set new.fecha_inicio = curdate();
  end if;
  
  if new.fecha_fin is null then
    set new.fecha_fin = date_add(curdate(), interval 30 day);
  end if;
end&&

delimiter ;
-- triger para despues de insertar la promocion
drop trigger if exists promocion_despues_insertar;
delimiter $$
create trigger promocion_despues_insertar
after insert on promocion
for each row
begin
  update producto
  set precio_descuento = precio * (1 - new.descuento / 100)
  where id_producto = new.producto_id;

  insert into cupon (promocion_id, descuento, usado)
  values (new.id_promocion, new.descuento, false);
end$$
delimiter ;
-- añadir culomna a producto para el precio descontado

ALTER TABLE producto ADD COLUMN precio_descuento DECIMAL(10,2);
describe promocion;
describe cupon;


describe venta;
describe factura;
-- procedimiento para finalizar una venta
drop procedure if exists finalizar_venta;
delimiter $$

create procedure finalizar_venta (
    in p_venta_id int,
    in p_cupon_id int,
    in p_usar_cupon boolean
)
begin
    declare v_total decimal(10,2) default 0;
    declare v_descuento decimal(5,2) default 0;
    declare v_total_con_descuento decimal(10,2);
    declare v_dni_cliente varchar(9);

    select ifnull(sum(coalesce(p.precio_descuento, p.precio) * pev.cantidad), 0)
    into v_total
    from producto_en_venta pev
    join producto p on p.id_producto = pev.producto_id
    where pev.venta_id = p_venta_id;

    select cliente_dni into v_dni_cliente
    from venta
    where id_venta = p_venta_id;

    if v_dni_cliente is null then
        signal sqlstate '45000' set message_text = 'venta sin cliente válido';
    end if;

    if p_usar_cupon = true then
        select pr.descuento
        into v_descuento
        from cupon c
        join promocion pr on c.promocion_id = pr.id_promocion
        where c.id_cupon = p_cupon_id and c.usado = false
        limit 1;

        set v_total_con_descuento = v_total * (1 - v_descuento / 100);

        update cupon
        set usado = true, fecha_uso = curdate(), venta_id = p_venta_id
        where id_cupon = p_cupon_id;
    else
        set v_total_con_descuento = v_total;
    end if;

    update venta
    set total = v_total
    where id_venta = p_venta_id;

    insert into factura (venta_id, fecha_emision, total, total_con_descuento, dni_cliente)
    values (p_venta_id, curdate(), v_total, v_total_con_descuento, v_dni_cliente);

    select p_venta_id as id_venta, v_total as total, v_total_con_descuento as total_con_descuento;
end$$

delimiter ;

-- funcion para ver si existe factura
drop function if exists existe_factura;
delimiter %%
create function existe_factura(id int) returns int
not deterministic
reads sql data
begin
  declare existe int;
  select count(*) into existe from factura where venta_id = id;
  return existe;
end%%

delimiter ;
-- añadida columna total_con_descuento
alter table factura add column total_con_descuento decimal(10,2);
-- funcion para obtener los el id del cupon disponible
drop function if exists obtener_cupon_disponible;
delimiter $$

create function obtener_cupon_disponible() 
returns int
deterministic
reads sql data
begin
    declare cupon_id int;

    select c.id_cupon
    into cupon_id
    from cupon c
    inner join promocion p on c.promocion_id = p.id_promocion
    where c.usado = false and p.activo = true
    limit 1;

    return cupon_id;
end$$

delimiter ;
-- consultas de cliente
select f.dni_cliente, count(*) as cantidad_facturas, sum(f.total_con_descuento) as total_pagado
from factura f
group by f.dni_cliente
order by total_pagado desc
limit 1;
-- cosulta productos vendidos
select 
    p.nombre, 
    sum(pev.cantidad) as total_vendido, 
    pr.descuento 
from producto p 
join promocion pr on p.id_producto = pr.producto_id 
join producto_en_venta pev on p.id_producto = pev.producto_id 
join cupon c on c.promocion_id = pr.id_promocion 
where pr.activo = true 
  and c.usado = true 
group by p.id_producto, p.nombre, pr.descuento
limit 0, 1000;
-- clientes participaron en sorteos
select s.cliente_dni
from sorteo s
left join venta v on s.cliente_dni = v.cliente_dni
where v.id_venta is null and s.cliente_dni is not null;
-- productos no vendidos de los proveeores
select pr.id_proveedor, pr.nombre
from proveedor pr
where not exists (
    select 1 
    from producto p 
    join producto_en_venta pev on p.id_producto = pev.producto_id
    where p.proveedor_id = pr.id_proveedor
);
-- ventas con mas productos 
select v.id_venta, count(distinct p.proveedor_id) as total_proveedores
from venta v
join producto_en_venta pev on v.id_venta = pev.venta_id
join producto p on p.id_producto = pev.producto_id
group by v.id_venta
having total_proveedores > 1;
-- actualizo como cancelado las ventas sin productos asociados
update venta
set estado_pedido = 'cancelado'
where id_venta not in (
    select distinct venta_id from producto_en_venta
);
-- actualizo el stock de productos
update producto p
set p.stock = p.stock - (
    select ifnull(sum(pev.cantidad), 0)
    from producto_en_venta pev
    where pev.producto_id = p.id_producto
)
where exists (
    select 1 from producto_en_venta pev where pev.producto_id = p.id_producto
);
-- eliminar productos sin stock
delete from producto
where stock = 0
and id_producto not in (select producto_id from producto_en_venta)
and id_producto not in (select producto_id from promocion);
-- triger para actualizar el stock
drop trigger if exists actualizar_stock_despues_insertar;
delimiter &&

create trigger actualizar_stock_despues_insertar
after insert on producto_en_venta
for each row
begin
    update producto
    set stock = stock - new.cantidad
    where id_producto = new.producto_id;
end&&
delimiter ;
-- trigger para eliminar productos sin stock
drop trigger if exists eliminar_productos_sin_stock;
delimiter &&

create trigger eliminar_productos_sin_stock
after update on producto
for each row
begin
    if new.stock = 0 then
        delete from producto
        where id_producto = new.id_producto
        and id_producto not in (select producto_id from producto_en_venta)
        and id_producto not in (select producto_id from promocion);
    end if;
end&&
delimiter ;
-- consultas de sorteo
select id_sorteo, premio 
from sorteo where 
resultado = 'NO PARTICIPÓ';
select * from proveedor where dni = '87654321A';
select * from sorteo where proveedor_id = 5;
select id_sorteo, resultado, premio from sorteo where resultado is not null;
-- vista ciente_proveedor
drop view if exists clientes_proveedor;
create or replace view clientes_proveedor as
select distinct v.cliente_dni, p.proveedor_id
from venta v
inner join producto_en_venta pev on v.id_venta = pev.venta_id
inner join producto p on pev.producto_id = p.id_producto;

-- vista de producto_en_venta
create or replace view vista_productos_en_venta as
select pev.cantidad, p.id_producto, p.nombre, p.precio
from producto_en_venta pev
join producto p on pev.producto_id = p.id_producto;

-- vista productos cantidad
create view vista_productos_cantidad as
select p.id_producto, p.nombre, p.precio, sum(pev.cantidad) as total_cantidad
from producto p
join producto_en_venta pev on p.id_producto = pev.producto_id
group by p.id_producto, p.nombre, p.precio;
select id_factura, fecha_emision, total, total_con_descuento from factura where dni_cliente = '11111111b';
select * from venta where id_venta = 63;
SELECT * FROM factura WHERE venta_id = 54;

select existe_factura(54);
describe factura;
select f.dni_cliente, prv.cantidad from factura as f inner join venta as v 
on f.venta_id = v.id_venta 
inner join producto_en_venta as prv
on prv.venta_id = v.id_venta where f.dni_cliente = '12345678c';
