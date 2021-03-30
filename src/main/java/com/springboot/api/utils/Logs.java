package com.springboot.api.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logs {
	Logger logger = Logger.getLogger("CuentasLog");
	FileHandler fh;

	long total;

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public void crearLogs(long tiempoUno, long tiempoDos, String mensaje, String tipo) {
		try {
			fh = new FileHandler("/home/gonet/logs/cuentas.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			total = tiempoDos - tiempoUno;
			LocalDateTime now = LocalDateTime.now();

			logger.info(String.valueOf(total) + " ms | mensaje: " + mensaje + " | " + dtf.format(now) + " | " + tipo);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("/home/gonet/logs/externo.log", true);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			LocalDateTime now = LocalDateTime.now();
			bufferedWriter.newLine();
			bufferedWriter.write(
					String.valueOf(total) + " ms | mensaje: " + mensaje + " | " + dtf.format(now) + " | " + tipo);

			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
