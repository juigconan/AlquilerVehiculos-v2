package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IVehiculos;

public class Vehiculos implements IVehiculos {
	
	private static Vehiculos instancia;
	private List<Vehiculo> coleccionVehiculos;

	private Vehiculos() {
		coleccionVehiculos = new ArrayList<>();
	}
	
	static Vehiculos getInstancia() {
		if(instancia == null) {
			instancia = new Vehiculos();
		}
		return instancia;
	
	}

	@Override
	public void comenzar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Vehiculo> get() {
		return new ArrayList<>(coleccionVehiculos);
	}

	@Override
	public void insertar(Vehiculo vehiculo) throws OperationNotSupportedException {
		if (vehiculo == null) {
			throw new NullPointerException("ERROR: No se puede insertar un vehículo nulo.");
		}
		if (coleccionVehiculos.contains(vehiculo)) {
			throw new OperationNotSupportedException("ERROR: Ya existe un vehículo con esa matrícula.");
		}
		coleccionVehiculos.add(vehiculo);
	}

	@Override
	public Vehiculo buscar(Vehiculo vehiculo) {
		if (vehiculo == null) {
			throw new NullPointerException("ERROR: No se puede buscar un vehículo nulo.");
		}
		int indice = coleccionVehiculos.indexOf(vehiculo);
		return indice == -1 ? null : coleccionVehiculos.get(indice);
	}

	@Override
	public void borrar(Vehiculo vehculo) throws OperationNotSupportedException {
		if (vehculo == null) {
			throw new NullPointerException("ERROR: No se puede borrar un vehículo nulo.");
		}
		if (!coleccionVehiculos.contains(vehculo)) {
			throw new OperationNotSupportedException("ERROR: No existe ningún vehículo con esa matrícula.");
		}
		coleccionVehiculos.remove(vehculo);
	}

}
