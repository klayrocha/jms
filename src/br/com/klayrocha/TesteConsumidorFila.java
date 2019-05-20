package br.com.klayrocha;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class TesteConsumidorFila {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		connection.start();
		
		//Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
		Session session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);
		
		Destination fila = (Destination) context.lookup("financeiro");
		
		MessageConsumer consumer = session.createConsumer(fila);
		
		
		consumer.setMessageListener(new MessageListener() {
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					message.acknowledge();// Quando é CLIENT_ACKNOWLEDGE
					System.out.println("Recebendo msg : "+ textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//Message message = consumer.receive();
		//System.out.println("Recebendo msg : "+ message);
		
		new Scanner(System.in).nextLine();
		
		session.close();
		connection.close();
		context.close();
	}

}
