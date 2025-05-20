package eventos.pdfEventos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

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

	public static void escribirEventosPdf(String nombreArchivo, List<Evento> eventos) {
		Document document = new Document();

		try {
			// Creamos un escritor de PDF
			PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
			document.open();

			// Título
			document.add(new Paragraph("Lista de Eventos", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
			document.add(Chunk.NEWLINE);

			// Crear tabla con 4 columnas
			PdfPTable table = new PdfPTable(4);
			table.addCell("Nombre");
			table.addCell("Fecha");
			table.addCell("Ubicación");
			table.addCell("Descripción");

			// Añadir cada evento a la tabla
			for (Evento evento : eventos) {
				table.addCell(evento.getNombre());
				table.addCell(evento.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
				table.addCell(evento.getUbicacion());
				table.addCell(evento.getDescripcion());
			}

			// Agregar la tabla al documento
			document.add(table);
			document.close();

			System.out.println("Archivo PDF generado en: " + new File(nombreArchivo).getAbsolutePath());
		} catch (Exception e) {
			System.out.println("Error escribiendo PDF: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		List<Evento> eventos = leerEventos("eventos.txt");
		escribirEventosPdf("eventos.pdf", eventos);

		// Mostrar ruta absoluta
		File archivo = new File("eventos.pdf");
		System.out.println("Ruta absoluta del PDF: " + archivo.getAbsolutePath());
	}
}