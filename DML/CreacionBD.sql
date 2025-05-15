drop database if exists CreacionBD;
create database CreacionBD;
use CreacionBD;

-- Tabla proveedor con campos adicionales para contacto e información fiscal/bancaria
create table proveedor(
    id_proveedor int primary key auto_increment,
    dni varchar(9) not null unique,
    nombre varchar(100) not null,
    apellidos varchar(200) not null,
    telefono varchar(20),          -- nuevo campo teléfono
    email varchar(200),            -- nuevo campo email
    direccion_fiscal varchar(300), -- nueva dirección fiscal
    cuenta_bancaria varchar(50)    -- nueva cuenta bancaria
);

create table promocion(
    id_promocion int primary key auto_increment,
    proveedor_id int not null,
    descripcion longtext,
    descuento decimal(5,2) not null,
    fecha_inicio date,
    fecha_fin date,
    activo boolean default true,
    foreign key(proveedor_id) references proveedor(id_proveedor)
);

create table producto(
    id_producto int primary key auto_increment,
    nombre varchar(100) not null,
    fecha_entrada date not null,
    stock int not null,
    es_online boolean not null,
    precio decimal(10,2),
    proveedor_id int not null,
    foreign key(proveedor_id) references proveedor(id_proveedor)
);

create table empleado(
    id_empleado int primary key auto_increment,
    usuario varchar(100) unique not null,
    contrasenya varchar(100) not null
);

create table cliente(
    dni varchar(9) primary key,
    nombre varchar(100) not null,
    apellidos varchar(200) not null,
    email varchar(200) unique not null
);

create table sorteo(
    id_sorteo int primary key auto_increment,
    resultado enum('GANADOR','PERDEDOR','NO PARTICIPÓ'),
    premio text,
    cliente_dni varchar(9) not null,
    foreign key(cliente_dni) references cliente(dni) on update cascade
);

create table venta(
    id_venta int primary key auto_increment,
    fecha_venta date not null,
    total decimal(8,2) not null,
    metodo_pago enum('TARJETA','EFECTIVO','DESPUES DE ENTREGA'),
    metodo_envio enum('24H','3 A 5 DIAS','7 A 12 DIAS'),
    coste_envio decimal(7,2) not null,
    empleado_id int,
    cliente_dni varchar(9),
    foreign key(empleado_id) references empleado(id_empleado),
    foreign key(cliente_dni) references cliente(dni) on update cascade
);

create table cupon(
    id_cupon int primary key auto_increment,
    descuento decimal(5,2) not null,
    usado boolean not null default false,
    fecha_uso date,
    venta_id int,
    cliente_dni varchar(9),
    promocion_id int,
    foreign key(cliente_dni) references cliente(dni) on update cascade,
    foreign key (venta_id) references venta(id_venta),
    foreign key(promocion_id) references promocion(id_promocion)
);

create table producto_en_venta(
    venta_id int not null,
    producto_id int not null,
    cantidad int not null,
    primary key(venta_id, producto_id),
    foreign key (venta_id) references venta(id_venta),
    foreign key(producto_id) references producto(id_producto)
);

create table factura(
    id_factura int auto_increment primary key,
    fecha_emision date not null,
    dni_cliente varchar(9),
    venta_id int,
    total decimal(10,2),
    foreign key(dni_cliente) references cliente(dni) on update cascade,
    foreign key(venta_id) references venta(id_venta)
);