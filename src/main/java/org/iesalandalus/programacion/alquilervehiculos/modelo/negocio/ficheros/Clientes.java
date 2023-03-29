package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IClientes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Clientes implements IClientes {
	
	private static final File FICHERO_CLIENTES = new File(String.format("datos%sclientes.xml",File.separator));
	private static final String RAIZ = "clientes";
	private static final String CLIENTE = "cliente";
	private static final String NOMBRE = "nombre";
	private static final String DNI = "dni";
	private static final String TELEFONO = "telefono";
	private static Clientes instancia;
	private List<Cliente> coleccionClientes;

	private Clientes() {
		coleccionClientes = new ArrayList<>();
	}
	
	static Clientes getInstancia(){
		if(instancia == null) {
			instancia = new Clientes();
		}
		return instancia;
	}

	@Override
	public void comenzar() {	
		leerDom(UtilidadesXml.leerXmlDeFichero(FICHERO_CLIENTES));
	}
	
	private void leerDom(Document documentoXml) {
		
		NodeList clientes = documentoXml.getElementsByTagName(CLIENTE);
		
		for(int i = 0; i < clientes.getLength(); i++) {
			Node cliente = clientes.item(i);
			if(cliente.getNodeType() == Node.ELEMENT_NODE) {
				String nombre = ((Element)cliente).getAttribute(NOMBRE);
				String dni = ((Element)cliente).getAttribute(DNI);
				String telefono= ((Element)cliente).getAttribute(TELEFONO);
				try {
					insertar(new Cliente(nombre, dni, telefono));
				} catch (OperationNotSupportedException e) {
					System.out.printf("%s%s%d",e.getMessage(),"Fallo en la posicion ",i);
				}
			}
		}
		
	}

	@Override
	public void terminar() {
		
	}

	@Override
	public List<Cliente> get() {
		return new ArrayList<>(coleccionClientes);
	}

	@Override
	public void insertar(Cliente cliente) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede insertar un cliente nulo.");
		}
		if (coleccionClientes.contains(cliente)) {
			throw new OperationNotSupportedException("ERROR: Ya existe un cliente con ese DNI.");
		}
		coleccionClientes.add(cliente);
	}

	@Override
	public Cliente buscar(Cliente cliente) {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede buscar un cliente nulo.");
		}
		int indice = coleccionClientes.indexOf(cliente);
		return indice == -1 ? null : coleccionClientes.get(indice);
	}

	@Override
	public void borrar(Cliente cliente) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede borrar un cliente nulo.");
		}
		if (!coleccionClientes.contains(cliente)) {
			throw new OperationNotSupportedException("ERROR: No existe ningún cliente con ese DNI.");
		}
		coleccionClientes.remove(cliente);
	}

	@Override
	public void modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException {
		if (cliente == null) {
			throw new NullPointerException("ERROR: No se puede modificar un cliente nulo.");
		}
		if (!coleccionClientes.contains(cliente)) {
			throw new OperationNotSupportedException("ERROR: No existe ningún cliente con ese DNI.");
		}
		if (nombre != null && !nombre.isBlank()) {
			cliente.setNombre(nombre);
		}
		if (telefono != null && !telefono.isBlank()) {
			cliente.setTelefono(telefono);
		}
	}

}
