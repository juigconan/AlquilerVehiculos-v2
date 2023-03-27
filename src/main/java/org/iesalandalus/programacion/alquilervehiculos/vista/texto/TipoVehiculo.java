package org.iesalandalus.programacion.alquilervehiculos.vista.texto;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;

public enum TipoVehiculo {
	TURISMO("Turismo"), AUTOBUS("Autobus"), FURGONETA("Furgoneta");

	String nombre;

	private TipoVehiculo(String nombre) {
		this.nombre = nombre;
	}

	private static boolean esOrdinalValido(int ordinal) {
		return (ordinal >= 0 && ordinal < Accion.values().length);
	}

	public static TipoVehiculo get(int ordinal) {
		if (!esOrdinalValido(ordinal)) {
			throw new IllegalArgumentException("ERROR: Tipo no válido.");
		}
		return TipoVehiculo.values()[ordinal];
	}

	public static TipoVehiculo get(Vehiculo vehiculo) {
		if (vehiculo == null) {
			throw new NullPointerException("ERROR: El vehiculo no puede ser nulo.");
		}
		int ordinal = -1;
		if (vehiculo instanceof Turismo) {
			ordinal = TipoVehiculo.TURISMO.ordinal();
		}
		if (vehiculo instanceof Autobus) {
			ordinal = TipoVehiculo.AUTOBUS.ordinal();
		}
		if (vehiculo instanceof Furgoneta) {
			ordinal = TipoVehiculo.FURGONETA.ordinal();
		}
		return get(ordinal);
	}

	@Override
	public String toString() {
		return String.format("%d.- %s", ordinal() + 1, nombre);
	}

}
