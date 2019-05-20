package br.com.klayrocha.log;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;

public class TesteProdutorFilaLog {

	public static void main(String[] args) throws Exception {
		
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
		
		Destination fila = (Destination) context.lookup("LOG");
		
		MessageProducer producer = session.createProducer(fila);
		
		Message message = session.createTextMessage("INFO | Listening for connections at: 1000&wireFormat.maxFrameSize=104857600");
		producer.send(message, DeliveryMode.NON_PERSISTENT,3,5000);// O '3' é a prioridade, o consumidor vai ler primeiro de acordo com a prioridade
		
		session.close();
		connection.close();
		context.close();
	}
	
}
