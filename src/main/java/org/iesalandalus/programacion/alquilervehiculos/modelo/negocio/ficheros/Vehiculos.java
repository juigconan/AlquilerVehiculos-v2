package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.DocumentBuilder;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IVehiculos;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Vehiculos implements IVehiculos {

	private static Vehiculos instancia;
	private List<Vehiculo> coleccionVehiculos;
	private static final File FICHERO_VEHICULOS = new File(String.format("datos%svehiculos.xml", File.separator));
	private static final String RAIZ = "vehiculos";
	private static final String VEHICULO = "vehiculo";
	private static final String MARCA = "marca";
	private static final String MODELO = "modelo";
	private static final String MATRICULA = "matricula";
	private static final String CILINDRADA = "cilindrada";
	private static final String PLAZAS = "plazas";
	private static final String PMA = "pma";
	private static final String TIPO = "tipo";
	private static final String TURISMO = "turismo";
	private static final String AUTOBUS = "autobus";
	private static final String FURGONETA = "furgoneta";

	private Vehiculos() {
		coleccionVehiculos = new ArrayList<>();
	}

	static Vehiculos getInstancia() {
		if (instancia == null) {
			instancia = new Vehiculos();
		}
		return instancia;

	}

	@Override
	public void comenzar() {
		Document documento = UtilidadesXml.leerXmlDeFichero(FICHERO_VEHICULOS);
		if(documento != null) {
			leerDom(documento);
			System.out.println("Documento leido correctamente.");
		}else {
			System.out.println("ERROR: El documento no es correcto");
		}
	}

	private void leerDom(Document documentoXml) {

		NodeList vehiculos = documentoXml.getElementsByTagName(VEHICULO);

		for (int i = 0; i < vehiculos.getLength(); i++) {
			Node vehiculo = vehiculos.item(i);
			if (vehiculo.getNodeType() == Node.ELEMENT_NODE) {
				try {
					insertar(getVehiculo((Element) vehiculo));
				} catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
					System.out.printf("%s%s%d", e.getMessage(), " Fallo en la posicion: ", i);
				}
			}
		}

	}

	private Vehiculo getVehiculo(Element elemento) {
		Vehiculo vehiculo = null;
		String tipo = elemento.getAttribute(TIPO);
		String marca = elemento.getAttribute(MARCA);
		String modelo = elemento.getAttribute(MODELO);
		String matricula = elemento.getAttribute(MATRICULA);
		if (tipo.equals(TURISMO)) {
			vehiculo = new Turismo(marca, modelo, Integer.parseInt(elemento.getAttribute(CILINDRADA)), matricula);
		} else if (tipo.equals(FURGONETA)) {
			vehiculo = new Furgoneta(marca, modelo, Integer.parseInt(elemento.getAttribute(PMA)),
					Integer.parseInt(elemento.getAttribute(PLAZAS)), matricula);
		} else if (tipo.equals(AUTOBUS)) {
			vehiculo = new Autobus(marca, modelo, Integer.parseInt(elemento.getAttribute(PLAZAS)), matricula);
		} else {
			throw new IllegalArgumentException("ERROR: El tipo del vehículo no es válido.");
		}
		return vehiculo;
	}

	@Override
	public void terminar() {
		UtilidadesXml.escribirXmlAFichero(crearDom(), FICHERO_VEHICULOS);
	}

	private Document crearDom() {
		DocumentBuilder constructor = UtilidadesXml.crearConstructorDocumentoXml();
		Document documentoXml = null;
		if (constructor != null) {
			documentoXml = constructor.newDocument();
			documentoXml.appendChild(documentoXml.createElement(RAIZ));
			for (Vehiculo vehiculo : coleccionVehiculos) {
				Element elementoVehiclo = crearElemento(documentoXml, vehiculo);
				documentoXml.getDocumentElement().appendChild(elementoVehiclo);
			}
		}
		return documentoXml;
	}

	private static Element crearElemento(Document documentoXML, Vehiculo vehiculo) {
		Element elementoVehiculo = documentoXML.createElement(VEHICULO);
		elementoVehiculo.setAttribute(MARCA, vehiculo.getMarca());
		elementoVehiculo.setAttribute(MODELO, vehiculo.getModelo());
		elementoVehiculo.setAttribute(MATRICULA, vehiculo.getMatricula());
		if(vehiculo instanceof Turismo turismo) {
			elementoVehiculo.setAttribute(CILINDRADA, Integer.toString(turismo.getCilindrada()));
			elementoVehiculo.setAttribute(TIPO, TURISMO);
		}
		else if(vehiculo instanceof Autobus autobus) {
			elementoVehiculo.setAttribute(PLAZAS, Integer.toString(autobus.getPlazas()));
			elementoVehiculo.setAttribute(TIPO, AUTOBUS);
		}
		else if(vehiculo instanceof Furgoneta furgoneta) {
			elementoVehiculo.setAttribute(PLAZAS, Integer.toString(furgoneta.getPlazas()));
			elementoVehiculo.setAttribute(PMA, Integer.toString(furgoneta.getPma()));
			elementoVehiculo.setAttribute(TIPO, FURGONETA);
		}
		return elementoVehiculo;
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
