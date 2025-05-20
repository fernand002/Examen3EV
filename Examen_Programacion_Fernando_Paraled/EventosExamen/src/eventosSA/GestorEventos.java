package eventosSA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GestorEventos {

	public static List<Evento> leerEventos(String nombreArchivo) {
		List<Evento> eventos = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		try {
			List<String> lineas = Files.readAllLines(Paths.get(nombreArchivo));
			for (String linea : lineas) {
				String[] partes = linea.split(",", 4);
				if (partes.length == 4) {
					String nombre = partes[0];
					LocalDateTime fecha = LocalDateTime.parse(partes[1], formatter);
					String ubicacion = partes[2];
					String descripcion = partes[3];
					eventos.add(new Evento(nombre, fecha, ubicacion, descripcion));
				}
			}
		} catch (IOException e) {
			System.out.println("Error leyendo el archivo: " + e.getMessage());
		}

		return eventos;
	}

	public static void escribirEventos(String nombreArchivo, List<Evento> eventos) {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(nombreArchivo))) {
			for (Evento evento : eventos) {
				writer.write(evento.toString());
				writer.newLine();
			}
		} catch (IOException e) {
			System.out.println("Error escribiendo el archivo: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		List<Evento> eventos = leerEventos("eventos.txt");
		escribirEventos("salida_eventos.txt", eventos);

//Esto mostrara un mensaje por pantalla indicandonos donde se encuentra el archivo generado :)

		File archivo = new File("salida_eventos.txt");
		System.out.println("El archivo esta en: " + archivo.getAbsolutePath());
	}
}
