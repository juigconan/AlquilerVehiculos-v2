package org.iesalandalus.programacion.alquilervehiculos.vista.texto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.utilidades.Entrada;

public class Consola {

	private static final String PATRON_FECHA = "dd/MM/yyyy";
	private static final String PATRON_MES = "MM/yyyy";
	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern(PATRON_FECHA);

	private Consola() {

	}

	public static void mostrarCabecera(String mensaje) {
		System.out.printf("%n%s%n", mensaje);
		StringBuilder subrayado = new StringBuilder();
		for (int i = 0; i < mensaje.length(); i++) {
			subrayado.append("-");
		}
		System.out.printf("%s%n%n", subrayado);
	}

	public static void mostrarMenu() {
		for (Accion accion : Accion.values()) {
			if (accion.ordinal() != 0) {
				System.out.printf("%s%n", accion);
			}
		}
		System.out.println(Accion.SALIR);
	}

	private static String leerCadena(String mensaje) {
		System.out.printf("Introduce %s", mensaje);
		return Entrada.cadena();
	}

	private static int leerEntero(String mensaje) {
		System.out.printf("Introduce %s", mensaje);
		return Entrada.entero();
	}

	private static LocalDate leerFecha(String mensaje, String patron) {
		System.out.printf("Introduce %s (%s):", mensaje, patron);
		LocalDate fecha = null;
		try {
			if (patron.equals(PATRON_FECHA) ) {
				fecha = LocalDate.parse(Entrada.cadena(), FORMATO_FECHA);
			}else if(patron.equals(PATRON_MES)){
				fecha = LocalDate.parse("01/" + Entrada.cadena(),FORMATO_FECHA);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("ERROR: La fecha introducida no es válida.");
		}

		return fecha;
	}

	public static Accion elegirAccion() {
		Accion opcion = null;
		do {
			try {
				opcion = Accion.get(leerEntero("la opcion deseada:"));
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		} while (opcion == null);

		return opcion;
	}

	public static Cliente leerCliente() {
		return new Cliente(leerNombre(), leerCadena("el dni del cliente:"), leerTelefono());
	}

	public static Cliente leerClienteDni() {
		return Cliente.getClienteConDni(leerCadena("el dni del cliente:"));
	}

	public static String leerNombre() {
		return leerCadena("el nombre del cliente:");
	}

	public static String leerTelefono() {
		return leerCadena("el teléfono del cliente:");
	}

	public static Vehiculo leerVehiculo() {
		mostrarMenuTiposVehiculos();
		return leerVehiculo(elegirTipoVehiculo());
	}

	private static void mostrarMenuTiposVehiculos() {
		mostrarCabecera("Tipos de vehículo:");
		for (TipoVehiculo tipoVehiculo : TipoVehiculo.values()) {
			System.out.printf("%s%n", tipoVehiculo);
		}
	}

	private static TipoVehiculo elegirTipoVehiculo() {
		return TipoVehiculo.get(leerEntero("el tipo de vehículo:") - 1);
	}

	private static Vehiculo leerVehiculo(TipoVehiculo tipoVehiculo) {
		Vehiculo vehiculoADevolver = null;
		String marca = leerCadena("la marca:");
		String modelo = leerCadena("el modelo:");
		String matricula = leerCadena("la matricula:");
		if (tipoVehiculo == TipoVehiculo.TURISMO) {
			vehiculoADevolver = new Turismo(marca, modelo, leerEntero("la cilidrada:"), matricula);
		}
		if (tipoVehiculo == TipoVehiculo.AUTOBUS) {
			vehiculoADevolver = new Autobus(marca, modelo, leerEntero("las plazas:"), matricula);
		}
		if (tipoVehiculo == TipoVehiculo.FURGONETA) {
			vehiculoADevolver = new Furgoneta(marca, modelo, leerEntero("el PMA:"), leerEntero("las plazas:"),
					matricula);
		}
		return vehiculoADevolver;
	}

	public static Vehiculo leerVehiculoMatricula() {
		return Vehiculo.getVehiculoConMatricula(leerCadena("la matricula del vehículo:"));

	}

	public static Alquiler leerAlquiler() {
		return new Alquiler(leerClienteDni(), leerVehiculoMatricula(), leerFecha("la fecha del alquiler",PATRON_FECHA));
	}

	public static LocalDate leerFechaDevolucion() {
		return leerFecha("la fecha de devolución",PATRON_FECHA);
	}

	public static LocalDate leerMes() {
		return leerFecha("el mes que desea mostrar",PATRON_MES);
		
	}

}
