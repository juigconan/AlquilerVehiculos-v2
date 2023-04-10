package org.iesalandalus.programacion.alquilervehiculos.vista.texto;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.vista.Vista;

public class VistaTexto extends Vista {

	public VistaTexto() {
		Accion.setVista(this);
	}

	@Override
	public void comenzar() {
		Accion accion;
		do {
			Consola.mostrarCabecera("Menu principal");
			Consola.mostrarMenu();
			accion = Consola.elegirAccion();
			accion.ejecutar();
		} while (accion != Accion.SALIR);
	}

	@Override
	public void terminar() {
		getControlador().terminar();
		System.out.print("¡Gracias por utilizar nuestra aplicacion!");
	}

	public void insertarCliente() {
		Consola.mostrarCabecera("Insertar cliente");
		try {
			getControlador().insertarCliente(Consola.leerCliente());
			System.out.println("Cliente insertado correctamente");
		} catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void insertarVehiculo() {
		try {
			getControlador().insertarVehiculo(Consola.leerVehiculo());
			System.out.println("Vehículo insertado correctamente.");
		} catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}

	}

	public void insertarAlquiler() {
		Consola.mostrarCabecera("Insertar alquiler");
		try {
			getControlador().insertarAlquiler(Consola.leerAlquiler());
			System.out.println("Alquiler insertado correctamente.");
		} catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}

	}

	public void buscarCliente() {
		Consola.mostrarCabecera("Buscar cliente");
		Cliente clienteABuscar = Consola.leerClienteDni();
		Cliente cliente = null;
		try {
			cliente = getControlador().buscar(clienteABuscar);
		} catch (IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(cliente != null ? cliente.toString() : "No existe el cliente");
	}

	public void buscarVehiculo() {
		Consola.mostrarCabecera("Buscar vehículo");
		Vehiculo vehiculoABuscar = Consola.leerVehiculoMatricula();
		Vehiculo vehiculo = null;
		try {
			vehiculo = getControlador().buscar(vehiculoABuscar);
		} catch (IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(vehiculo != null ? vehiculo.toString() : "No existe el vehículo");
	}

	public void buscarAlquiler() {
		Consola.mostrarCabecera("Buscar alquiler");
		Alquiler alquilerABuscar = Consola.leerAlquiler();
		Alquiler alquiler = null;
		try {
			alquiler = getControlador().buscar(alquilerABuscar);
		} catch (IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(alquiler != null ? alquiler.toString() : "No existe el alquiler");
	}

	public void modificarCliente() {
		Consola.mostrarCabecera("Modificar cliente");
		System.out.printf("Inroduce los datos del cliente: %n%n");
		try {
			Cliente cliente = Consola.leerClienteDni();
			String nombre = Consola.leerNombre();
			String telefono = Consola.leerTelefono();
			getControlador().modificarCliente(cliente, nombre, telefono);
			System.out.println("Cliente modificado correctamente.");
		} catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void devolverAlquilerCliente() {
		Consola.mostrarCabecera("Devolver alquiler por cliente");
		try {
			getControlador().devolverAlquiler(Consola.leerClienteDni(), Consola.leerFechaDevolucion());
			System.out.println("Alquiler devuelto correctamente.");
		} catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void devolverAlquilerVehiculo() {
		Consola.mostrarCabecera("Devolver alquiler por vehículo");
		try {
			getControlador().devolverAlquiler(Consola.leerVehiculoMatricula(), Consola.leerFechaDevolucion());
			System.out.println("Alquiler devuelto correctamente.");
		} catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void borrarCliente() {
		Consola.mostrarCabecera("Borrar cliente");
		try {
			getControlador().borrarCliente(Consola.leerCliente());
			System.out.println("Cliente borrado correctamente.");
		} catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void borrarVehiculo() {
		Consola.mostrarCabecera("Borrar vehículo");
		try {
			getControlador().borrarVehiculo(Consola.leerVehiculo());
			System.out.println("Turismo borrado correctamente");
		} catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void borrarAlquiler() {
		Consola.mostrarCabecera("Borrar alquiler");
		try {
			getControlador().borrarAlquiler(Consola.leerAlquiler());
			System.out.println("Alquiler borrado correctamente.");
		} catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void listarClientes() {
		Consola.mostrarCabecera("Listado de clientes");
		List<Cliente> listaClientes = getControlador().getClientes();
		listaClientes.sort(Comparator.comparing(Cliente::getNombre).thenComparing(Cliente::getDni));
		StringBuilder listado = new StringBuilder();
		for (Cliente cliente : listaClientes) {
			listado.append(cliente).append(System.getProperty("line.separator"));
		}
		System.out.println(listado.isEmpty() ? "No existe ningún cliente." : listado);
	}

	public void listarVehiculos() {
		Consola.mostrarCabecera("Listado de vehíulos");
		List<Vehiculo> listaVehiculos = getControlador().getVehiculos();
		listaVehiculos.sort(Comparator.comparing(Vehiculo::getMarca).thenComparing(Vehiculo::getModelo)
				.thenComparing(Vehiculo::getMatricula));
		StringBuilder listado = new StringBuilder();
		for (Vehiculo vehículo : listaVehiculos) {
			listado.append(vehículo).append(System.getProperty("line.separator"));
		}
		System.out.println(listado.isEmpty() ? "No existe ningún vehíulo." : listado);
	}

	public void listarAlquileres() {
		Consola.mostrarCabecera("Listado de alquileres");
		Comparator<Cliente> comparadorCliente = Comparator.comparing(Cliente::getNombre).thenComparing(Cliente::getDni);
		List<Alquiler> listaAlquileres = getControlador().getAlquileres();
		listaAlquileres.sort(Comparator.comparing(Alquiler::getFechaAlquiler).thenComparing(Alquiler::getCliente,
				comparadorCliente));
		StringBuilder listado = new StringBuilder();
		for (Alquiler alquiler : listaAlquileres) {
			listado.append(alquiler).append(System.getProperty("line.separator"));
		}
		System.out.println(listado.isEmpty() ? "No existe ningún alquiler." : listado);
	}

	public void listarAlquileresCliente() {
		Consola.mostrarCabecera("Listado de alquileres del cliente");
		StringBuilder listado = new StringBuilder();
		Cliente clienteABuscar = null;
		try {
			clienteABuscar = Consola.leerClienteDni();
		} catch (IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
		Comparator<Cliente> comparadorCliente = Comparator.comparing(Cliente::getNombre).thenComparing(Cliente::getDni);
		List<Alquiler> listaAlquileres = getControlador().getAlquileresCliente(clienteABuscar);
		listaAlquileres.sort(Comparator.comparing(Alquiler::getFechaAlquiler).thenComparing(Alquiler::getCliente,
				comparadorCliente));
		for (Alquiler alquiler : listaAlquileres) {
			listado.append(alquiler).append(System.getProperty("line.separator"));
		}

		if (clienteABuscar != null) {
			System.out.println(listado.isEmpty() ? "Este cliente no tiene ningún alquiler." : listado);
		}

	}

	public void listarAlquileresVehiculo() {
		Consola.mostrarCabecera("Listado de alquileres del vehículo");
		StringBuilder listado = new StringBuilder();
		Vehiculo vehiculoABuscar = null;
		try {
			vehiculoABuscar = Consola.leerVehiculoMatricula();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Comparator<Vehiculo> comparadorVehiculo = Comparator.comparing(Vehiculo::getMarca)
				.thenComparing(Vehiculo::getModelo).thenComparing(Vehiculo::getMatricula);
		List<Alquiler> listaAlquileres = getControlador().getAlquileresVehiculo(vehiculoABuscar);
		listaAlquileres.sort(Comparator.comparing(Alquiler::getFechaAlquiler).thenComparing(Alquiler::getVehiculo,
				comparadorVehiculo));
		for (Alquiler alquiler : listaAlquileres) {
			listado.append(alquiler).append(System.getProperty("line.separator"));
		}
		if (vehiculoABuscar != null) {
			System.out.println(listado.isEmpty() ? "Este vehículo no tiene ningún alquiler." : listado);
		}
	}

	public void mostrarEstadisticaMensualesTipoVehiculo() {
		Map<TipoVehiculo, Integer> mapaEstadisticas = inicializaEstadisticas();
		try {
			LocalDate fecha = Consola.leerMes();
			for (Alquiler alquiler : getControlador().getAlquileres()) {
				if (alquiler.getFechaAlquiler().getMonth().equals(fecha.getMonth())
						&& alquiler.getFechaAlquiler().getYear() == fecha.getYear()) {
					mapaEstadisticas.put(TipoVehiculo.get(alquiler.getVehiculo()), mapaEstadisticas.get(TipoVehiculo.get(alquiler.getVehiculo())) + 1);
				}
			}
		} catch (IllegalArgumentException e) {
			mapaEstadisticas = null;
		}

		if (mapaEstadisticas == null) {
			System.out.println("ERROR: No es posible mostrar las estadisticas, la fecha no es válida.");
		} else {
			for (Map.Entry<TipoVehiculo, Integer> entrada : mapaEstadisticas.entrySet()) {
				TipoVehiculo tipo = entrada.getKey();
				Integer numero = entrada.getValue();
				System.out.printf("%s --> %d%n", tipo, numero);
			}
		}

	}

	private Map<TipoVehiculo, Integer> inicializaEstadisticas() {
		Map<TipoVehiculo,Integer> mapaEstadisticas = new EnumMap<>(TipoVehiculo.class);
		for (int i = 0; i < TipoVehiculo.values().length; i++) {
			mapaEstadisticas.put(TipoVehiculo.get(i), 0);
		}
		return mapaEstadisticas;
	}

}
