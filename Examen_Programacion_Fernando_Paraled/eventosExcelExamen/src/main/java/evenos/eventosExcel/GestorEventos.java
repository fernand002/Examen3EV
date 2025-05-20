package evenos.eventosExcel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

	public static void escribirEventosExcel(String nombreArchivo, List<Evento> eventos) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Eventos");

		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Nombre");
		header.createCell(1).setCellValue("Fecha");
		header.createCell(2).setCellValue("Ubicación");
		header.createCell(3).setCellValue("Descripción");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		for (int i = 0; i < eventos.size(); i++) {
			Evento e = eventos.get(i);
			Row row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(e.getNombre());
			row.createCell(1).setCellValue(e.getFecha().format(formatter));
			row.createCell(2).setCellValue(e.getUbicacion());
			row.createCell(3).setCellValue(e.getDescripcion());
		}

		try (FileOutputStream fileOut = new FileOutputStream(nombreArchivo)) {
			workbook.write(fileOut);
			workbook.close();
			System.out.println("Archivo Excel generado en: " + new File(nombreArchivo).getAbsolutePath());
		} catch (IOException e) {
			System.out.println("Error escribiendo Excel: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		List<Evento> eventos = leerEventos("eventos.txt");
		escribirEventos("salida_eventos.txt", eventos);
		escribirEventosExcel("eventos.xlsx", eventos);

		File archivo = new File("eventos.xlsx");
		System.out.println("Ruta absoluta del Excel: " + archivo.getAbsolutePath());
	}
}