package evenos.eventosExcel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evento {
	private String nombre;
	private LocalDateTime fecha;
	private String ubicacion;
	private String descripcion;

	public Evento(String nombre, LocalDateTime fecha, String ubicacion, String descripcion) {
		this.nombre = nombre;
		this.fecha = fecha;
		this.ubicacion = ubicacion;
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return nombre;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return nombre + " | " + fecha.format(formatter) + " | " + ubicacion + " | " + descripcion;
	}
}
