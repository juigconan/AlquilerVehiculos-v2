package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.memoria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IAlquileres;

public class Alquileres implements IAlquileres {

	private List<Alquiler> coleccionAlquileres;

	public Alquileres() {
		coleccionAlquileres = new ArrayList<>();
	}

	@Override
	public List<Alquiler> get() {
		return new ArrayList<>(coleccionAlquileres);
	}

	@Override
	public List<Alquiler> get(Cliente cliente) {
		List<Alquiler> alquileresCliente = new ArrayList<>();
		for (Alquiler alquiler : coleccionAlquileres) {
			if (alquiler.getCliente().equals(cliente)) {
				alquileresCliente.add(alquiler);
			}
		}
		return alquileresCliente;
	}

	@Override
	public List<Alquiler> get(Vehiculo turismo) {
		List<Alquiler> alquileresTurismo = new ArrayList<>();
		for (Alquiler alquiler : coleccionAlquileres) {
			if (turismo.equals(alquiler.getVehiculo())) {
				alquileresTurismo.add(alquiler);
			}
		}
		return alquileresTurismo;
	}

	@Override
	public int getCantidad() {
		return coleccionAlquileres.size();
	}

	private void comprobarAlquiler(Cliente cliente, Vehiculo vehiculo, LocalDate fechaAlquiler)
			throws OperationNotSupportedException {
		for (Alquiler alquiler : coleccionAlquileres) {
			LocalDate fechaDevolucion = alquiler.getFechaDevolucion();
			Cliente esteCliente = alquiler.getCliente();
			Vehiculo esteVehiculo = alquiler.getVehiculo();
			if (fechaDevolucion == null) {
				if (esteCliente.equals(cliente)) {
					throw new OperationNotSupportedException("ERROR: El cliente tiene otro alquiler sin devolver.");
				}
				if (esteVehiculo.equals(vehiculo)) {
					throw new OperationNotSupportedException("ERROR: El turismo está actualmente alquilado.");
				}
				// Si la fecha de devolución no es anterior, asi nos ahorramos una comprobación
			} else if (!fechaDevolucion.isBefore(fechaAlquiler)) {
				if (esteCliente.equals(cliente)) {
					throw new OperationNotSupportedException("ERROR: El cliente tiene un alquiler posterior.");
				}
				if (esteVehiculo.equals(vehiculo)) {
					throw new OperationNotSupportedException("ERROR: El turismo tiene un alquiler posterior.");
				}
			}

		}

	}

	@Override
	public void devolver(Cliente cliente, LocalDate fechaDevolucion) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede devolver un alquiler de un cliente nulo.");
		}
		Alquiler alquiler = getAlquilerAbierto(cliente);
		if (alquiler == null) {
			throw new OperationNotSupportedException("ERROR: No existe ningún alquiler abierto para ese cliente.");
		}

		buscar(alquiler).devolver(fechaDevolucion);
	}

	private Alquiler getAlquilerAbierto(Cliente cliente) {
		Alquiler alquilerADevolver = null;
		Iterator<Alquiler> iterador = coleccionAlquileres.iterator();
		while (alquilerADevolver == null && iterador.hasNext()) {
			Alquiler alquiler = iterador.next();
			if (alquiler.getCliente().equals(cliente) && alquiler.getFechaDevolucion() == null) {
				alquilerADevolver = alquiler;
			}
		}
		return alquilerADevolver;
	}

	@Override
	public void devolver(Vehiculo vehiculo, LocalDate fechaDevolucion) throws OperationNotSupportedException {
		if (vehiculo == null) {
			throw new NullPointerException("ERROR: No se puede devolver un alquiler de un vehículo nulo.");
		}
		Alquiler alquiler = getAlquilerAbierto(vehiculo);
		if (alquiler == null) {
			throw new OperationNotSupportedException("ERROR: No existe ningún alquiler abierto para ese vehículo.");
		}
		buscar(alquiler).devolver(fechaDevolucion);
	}

	private Alquiler getAlquilerAbierto(Vehiculo vehiculo) {
		Alquiler alquilerADevolver = null;
		Iterator<Alquiler> iterador = coleccionAlquileres.iterator();
		while (alquilerADevolver == null && iterador.hasNext()) {
			Alquiler alquiler = iterador.next();
			if (alquiler.getVehiculo().equals(vehiculo) && alquiler.getFechaDevolucion() == null) {
				alquilerADevolver = alquiler;
			}
		}
		return alquilerADevolver;
	}

	@Override
	public void insertar(Alquiler alquiler) throws OperationNotSupportedException {
		if (alquiler == null) {
			throw new NullPointerException("ERROR: No se puede insertar un alquiler nulo.");
		}
		comprobarAlquiler(alquiler.getCliente(), alquiler.getVehiculo(), alquiler.getFechaAlquiler());
		coleccionAlquileres.add(alquiler);
	}

	@Override
	public Alquiler buscar(Alquiler alquiler) {
		if (alquiler == null) {
			throw new NullPointerException("ERROR: No se puede buscar un alquiler nulo.");
		}
		int indice = coleccionAlquileres.indexOf(alquiler);
		return indice == -1 ? null : coleccionAlquileres.get(indice);
	}

	@Override
	public void borrar(Alquiler alquiler) throws OperationNotSupportedException {
		if (alquiler == null) {
			throw new NullPointerException("ERROR: No se puede borrar un alquiler nulo.");
		}
		if (!coleccionAlquileres.contains(alquiler)) {
			throw new OperationNotSupportedException("ERROR: No existe ningún alquiler igual.");
		}
		coleccionAlquileres.remove(alquiler);
	}

}
