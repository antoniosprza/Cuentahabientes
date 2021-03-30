package com.springboot.api.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.springboot.api.exception.ResourceNotFoundException;
import com.springboot.api.model.Cuentahabientes;
import com.springboot.api.repository.CuentahabientesRepository;
import com.springboot.api.utils.EncriptadorAES;
import com.springboot.api.utils.Logs;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET })
public class CuentahabientesController {

	@Value("${spring.rabbitmq.host}")
	private String host;

	@Value("${spring.rabbitmq.template.default-receive-queue}")
	private String QUEUE;

	@Autowired
	private CuentahabientesRepository cuentaRepository;

	EncriptadorAES objeto = new EncriptadorAES();

	Logs log = new Logs();
	long tiempoUno;
	long tiempoDos;

	@GetMapping("cuentahabientes/{id}")
	public String enviarMensaje(@PathVariable(value = "id") Long id) throws ResourceNotFoundException, Exception {
		Cuentahabientes cuentas = cuentaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Id: " + id + " no encontrado "));

		ArrayList<String> datos = new ArrayList<>();
		datos.add(String.valueOf(cuentas.getId()));
		datos.add(cuentas.getCliente());
		datos.add(cuentas.getNumeroCuenta());
		datos.add(cuentas.getUsuario());
		datos.add(cuentas.getClave());
		datos.add(cuentas.getUltimoAcceso());

		String enviar = "";

		for (String cadena : datos) {
			enviar += cadena.concat("|");
		}

		enviar = objeto.encriptar(enviar, "claveSecreta");

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		tiempoUno = System.currentTimeMillis();

		try {
			channel.queueDeclare(QUEUE, false, false, false, null);
			channel.basicPublish("", QUEUE, null, enviar.getBytes());
			tiempoDos = System.currentTimeMillis();
		} finally {
			channel.close();
			connection.close();
		}

		log.crearLogs(tiempoUno, tiempoDos, enviar, "enviado");
		return "Mensaje enviado";
	}

	@GetMapping("recibirMensaje")
	public String recibirMensaje() throws Exception {

		Cuentahabientes cuentas = new Cuentahabientes();

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		tiempoUno = System.currentTimeMillis();
		try {
			channel.queueDeclare(QUEUE, false, false, false, null);

			//System.out.println(" [*] Esperando mensajes. Para salir presione CTRL + C");

			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws UnsupportedEncodingException {
					String message = "";
					try {
						message = new String(body, "UTF-8");
						tiempoDos = System.currentTimeMillis();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					System.out.println(String.format("Mensaje recibido  «%s»", message));
					cuentas.setMensajeEncriptado(new String(body, "UTF-8"));
				}
			};

			channel.basicConsume(QUEUE, true, consumer);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e);
		} finally {
			channel.close();
			connection.close();
		}

		/*String contenido = "";
		contenido = objeto.desencriptar(cuentas.getMensajeEncriptado().toString(), "claveSecreta");
		System.out.println(contenido);*/

		log.crearLogs(tiempoUno, tiempoDos, cuentas.getMensajeEncriptado(), "recibido");

		return "Mensaje recibido";
	}

}
