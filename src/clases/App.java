package clases;

import java.sql.SQLException;
import java.util.Scanner;

public class App {

	public  void menu() throws SQLException{
		Scanner scanner = new Scanner(System.in);
		int opcion = -1;

		while (opcion != 0) {
			try {
				System.out.println("--Bienvenido--");
				System.out.println("Inserte su tipo de usuario:");
				System.out.println("1. Cliente\n2. Empleado\n3. Administrador\n0. Salir");

				opcion = Integer.parseInt(scanner.nextLine());

				switch (opcion) {
				case 1:
					menuCliente(scanner);
					break;
				case 2:
					menuEmpleado(scanner);
					break;
				case 3:
					menuAdministrador(scanner);
					break;
				case 0:
					System.out.println("Saliendo del sistema...");
					break;
				default:
					System.out.println("Opción no válida.");
				}
			} catch (NumberFormatException e) {
				System.err.println("Error, debe ingresar un número.");
			}
		}
	}

	private void menuCliente(Scanner scanner) throws SQLException {
		Cliente cliente = new Cliente();
		int opcion;
		do {
			System.out.println("-- Menú Cliente --");
			System.out.println("1. Ver catálogo de productos");
			System.out.println("2. Buscar producto por nombre");
			System.out.println("3. Usar cupón de descuento");
			System.out.println("4. Ver facturas asociadas a mi correo");
			System.out.println("5. Participar en sorteo (si dispone de cupón válido)");
			System.out.println("6. Consultar mis cupones disponibles");
			System.out.println("0. Volver al menú principal");

			opcion = Integer.parseInt(scanner.nextLine());

			switch (opcion) {
			case 1:
				System.out.println("Mostrando catálogo...");
				try {
					cliente.verCatalogoProductos();
				}catch(SQLException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				
				try {
					cliente.buscarProductoPorNombre();
				}catch(SQLException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				try {
					cliente.usarCuponDescuento();
					
				}catch(SQLException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				try {
					cliente.verFacturas();
				}catch(SQLException e) {
					e.printStackTrace();
				}
				break;
			case 5:
				try {
					cliente.participarEnSorteo();
				}catch(SQLException e) {
					e.printStackTrace();
				}
				break;
			case 6:
				try {
					cliente.consultarCuponesDisponibles();
				}catch(SQLException e) {
					e.printStackTrace();
				}
				break;
			case 0:
				break;
			default:
				System.out.println("Opción no válida.");
			}

		} while (opcion != 0);
	}

	private void menuEmpleado(Scanner scanner) {
		int opcion;
		do {
			System.out.println("-- Menú Empleado --");
			System.out.println("1. Registrar nueva venta");
			System.out.println("2. Buscar productos disponibles");
			System.out.println("3. Ver historial de ventas registradas por mí");
			System.out.println("4. Consultar datos de clientes");
			System.out.println("5. Generar factura de una venta existente");
			System.out.println("0. Volver al menú principal");

			opcion = Integer.parseInt(scanner.nextLine());

			switch (opcion) {
			case 1:
				System.out.println("Registrando venta...");
				break;
			case 2:
				System.out.println("Buscando productos...");
				break;
			case 3:
				System.out.println("Mostrando historial de ventas...");
				break;
			case 4:
				System.out.println("Consultando clientes...");
				break;
			case 5:
				System.out.println("Generando factura...");
				break;
			case 0:
				break;
			default:
				System.out.println("Opción no válida.");
			}

		} while (opcion != 0);
	}

	private void menuAdministrador(Scanner scanner) {
		int opcion;
		do {
			System.out.println("-- Menú Administrador --");
			System.out.println("1. Gestión de productos");
			System.out.println("2. Gestión de proveedores");
			System.out.println("3. Gestión de empleados");
			System.out.println("4. Gestión de ventas");
			System.out.println("5. Gestión de facturación");
			System.out.println("6. Gestión de cupones");
			System.out.println("7. Gestión de sorteos");
			System.out.println("0. Volver al menú principal");

			opcion = Integer.parseInt(scanner.nextLine());

			switch (opcion) {
			case 1:
				System.out.println("Accediendo a gestión de productos...");
				break;
			case 2:
				System.out.println("Accediendo a gestión de proveedores...");
				break;
			case 3:
				System.out.println("Accediendo a gestión de empleados...");
				break;
			case 4:
				System.out.println("Accediendo a gestión de ventas...");
				break;
			case 5:
				System.out.println("Accediendo a gestión de facturación...");
				break;
			case 6:
				System.out.println("Accediendo a gestión de cupones...");
				break;
			case 7:
				System.out.println("Accediendo a gestión de sorteos...");
				break;
			case 0:
				break;
			default:
				System.out.println("Opción no válida.");
			}

		} while (opcion != 0);
	}

	public static void main(String[] args) {
		try {
			new App().menu();
		}catch(SQLException e) {
			e.printStackTrace();
		}

	}

}
