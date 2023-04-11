package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.DocumentBuilder;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IAlquileres;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Alquileres implements IAlquileres {

	private static Alquileres instancia;
	private List<Alquiler> coleccionAlquileres;
	private static final File FICHERO_ALQUILERES = new File(String.format("datos%salquileres.xml", File.separator));
	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final String RAIZ = "alquileres";
	private static final String ALQUILER = "alquiler";
	private static final String CLIENTE = "cliente";
	private static final String VEHICULO = "vehiculo";
	private static final String FECHA_ALQUILER = "fechaAlquiler";
	private static final String FECHA_DEVOLUCION = "fechaDevolucion";

	public Alquileres() {
		coleccionAlquileres = new ArrayList<>();
	}

	static Alquileres getInstancia() {
		if (instancia == null) {
			instancia = new Alquileres();
		}
		return instancia;
	}

	@Override
	public void comenzar() {
		Document documento = UtilidadesXml.leerXmlDeFichero(FICHERO_ALQUILERES);
		if(documento != null) {
			leerDom(documento);
			System.out.println("Documento de alquileres leido correctamente.");
		}else {
			System.out.println("ERROR: El documento de alquileres no es correcto");
		}
	}

	private void leerDom(Document documentoXml) {

		NodeList alquileres = documentoXml.getElementsByTagName(ALQUILER);

		for (int i = 0; i < alquileres.getLength(); i++) {
			Node alquiler = alquileres.item(i);
			if (alquiler.getNodeType() == Node.ELEMENT_NODE) {
				try {
					insertar(getAlquiler((Element) alquiler));
				} catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
					System.out.printf("%s%s%d", e.getMessage(), " Fallo en la posicion: ", i);
				}
			}
		}

	}

	private Alquiler getAlquiler(Element elemento) {
		String dniCliente = elemento.getAttribute(CLIENTE);
		String matriculavehiculo = elemento.getAttribute(VEHICULO);
		String fechaAlquiler = elemento.getAttribute(FECHA_ALQUILER);
		String fechaDevolucion = elemento.getAttribute(FECHA_DEVOLUCION);
		Cliente cliente = Cliente.getClienteConDni(dniCliente);
		cliente = Clientes.getInstancia().buscar(cliente);
		Vehiculo vehiculo = Vehiculo.getVehiculoConMatricula(matriculavehiculo);
		vehiculo = Vehiculos.getInstancia().buscar(vehiculo);
		if(cliente == null) {
			throw new NullPointerException("ERROR: El cliente no existe.");
		}
		if(vehiculo == null) {
			throw new NullPointerException("ERROR: El vehiculo no existe.");
		}
		Alquiler alquiler = new Alquiler(cliente, vehiculo, LocalDate.parse(fechaAlquiler, FORMATO_FECHA));
		if (!fechaDevolucion.isEmpty()) {
			try {
				alquiler.devolver(LocalDate.parse(fechaDevolucion, FORMATO_FECHA));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return alquiler;
	}

	@Override
	public void terminar() {
		UtilidadesXml.escribirXmlAFichero(crearDom(), FICHERO_ALQUILERES);
	}

	private Document crearDom() {
		DocumentBuilder constructor = UtilidadesXml.crearConstructorDocumentoXml();
		Document documentoXml = null;
		if (constructor != null) {
			documentoXml = constructor.newDocument();
			documentoXml.appendChild(documentoXml.createElement(RAIZ));
			for (Alquiler alquiler : coleccionAlquileres) {
				Element elementoCliente = crearElemento(documentoXml, alquiler);
				documentoXml.getDocumentElement().appendChild(elementoCliente);
			}
		}
		return documentoXml;
	}

	private static Element crearElemento(Document documentoXML, Alquiler alquiler) {
		Element elementoAlquiler = documentoXML.createElement(ALQUILER);
		elementoAlquiler.setAttribute(CLIENTE, alquiler.getCliente().getDni());
		elementoAlquiler.setAttribute(VEHICULO, alquiler.getVehiculo().getMatricula());
		elementoAlquiler.setAttribute(FECHA_ALQUILER, alquiler.getFechaAlquiler().format(FORMATO_FECHA));
		if (alquiler.getFechaDevolucion() != null) {
			elementoAlquiler.setAttribute(FECHA_DEVOLUCION, alquiler.getFechaDevolucion().format(FORMATO_FECHA));
		}
		return elementoAlquiler;
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
					throw new OperationNotSupportedException("ERROR: El vehículo está actualmente alquilado.");
				}
				// Si la fecha de devolución no es anterior, asi nos ahorramos una comprobación
			} else if (!fechaDevolucion.isBefore(fechaAlquiler)) {
				if (esteCliente.equals(cliente)) {
					throw new OperationNotSupportedException("ERROR: El cliente tiene un alquiler posterior.");
				}
				if (esteVehiculo.equals(vehiculo)) {
					throw new OperationNotSupportedException("ERROR: El vehículo tiene un alquiler posterior.");
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
