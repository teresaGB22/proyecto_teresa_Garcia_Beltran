# proyecto_teresa_Garcia_Beltran

Gestión de tienda minorista:
permite controlar operaciones comerciales tales como administración de productos, clientes, empleados, proveedores, ventas, facturación, cupones y sorteos

Funciones principales:
Control de productos con campos: id_producto, nombre, fecha_entrada, stock, disponibilidad online (es_online), precio y proveedor asociado (proveedor_id).
Gestión completa de proveedores con datos personales, contacto (teléfono, email), dirección fiscal y cuenta bancaria.
Registro y autenticación de empleados para permitir acciones como registrar ventas y generar facturas.
Registro de ventas con fecha, total, método de pago y envío, coste, empleado responsable y cliente.
Emisión de facturas vinculadas a ventas y clientes, detallando total y fecha de emisión.
Gestión de promociones (tabla promocion) asociadas a proveedores.
Cupones vinculados a clientes, promociones y ventas, con control de uso y fecha.
Sorteos con participación de clientes, resultados y premios asociados.
Separación de roles mediante las tablas empleado y proveedor (usuarios para el sistema con contraseña).

--- Incluido:
javafx
Api iText y splf4 (descargar factura en pdf)

MENU
(boton)
1. Cliente
2. Empleado
3. Proveedor
0. Salir


-- App
*metodos para cliente*
metodo mostrarVentanaLoginCliente()
Inicio sesion Cliente
Introduce su dni(obligatorio con ingreso y registo)
Introduce tu nombre(obligatorio con registro)
Introduce tus Apellidos(obligatorio con registro)
Introduce tu correo(obligatorio con registro)
(boton)
Ingresar--> si no esta en la base de datos se le notifica que se registre metodo clienteExiste()
Registrar--> metodo registrarNuevoCliente() Ingresar todos los datos para registrarse y entrar
metodo mostrarMenuCliente()
(boton)
Ver catalogo de Productos
Buscar Producto por nombre
Usar Cupon de Descuento
Ver mis facturas
Participar en sortep
Consultar mis cupones
Volver
*metodos para proveedor*
metodo mostrarLoginProveedor()
Introduce tu dni(obligatorio en ingresar y Registrar)
Introduce tu nombre (obligatorio al registrar)
Introduce tus apellidos(obligatorio al registrar)
(boton)
Ingresar (metodo Proveedor Existe())
Registrar(el metodo registrarNuevoProveedor esta en la clase proveedor)
mmetodo mostrarMenuProveedor(Proveedor p)
(Boton)
Gestionar productos
Ver pedidos
Gestion de ventas
Actualizar perfil
Promociones y cupones
Volver

-- Cliente
*metodos directos a App*
metodo verCatalogoProductos()--> lista de productos 
metodo buscarProductoPorNombre()--> desde consulta sql buscarlo e imprimirlo
metodo usarCuponDecuento(dniCliente)--> Introducir el codigo del cupon y el monto de compra puede ser valido o no. Si se usa se actualizará que esta usado
metodo verFacturas(dniCliente)--> listado de facturas del cliente
metodo participarEnSorteo(dniCliente) --> Resultados y premio, si se participa no se podrá participar otra vez 
metodo consultarCuponesDisponibles()--> listado de cupones disponibles
*metodo dentro de ParticiparEnSorteo*
metodo obtenerPremiExistente(dni, coneccion c)--> premio de ese sorteo 
-- Proveedor
*metodos directos a App*
metodo registrarNuevoProveedor(dni, nombre, apellidos)-- > insertar el nuevo proveedor a la base de datos.
metodo gestionarPoductos()



