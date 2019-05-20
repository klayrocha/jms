package br.com.klayrocha;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;

import org.apache.activemq.ActiveMQConnectionFactory;

import br.com.klayrocha.modelo.Pedido;

public class TesteConsumidorTopicoComercial {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		InitialContext context = new InitialContext();
		//ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		//A partir da versão 5.12.2 o ActiveMQ precisa de uma configuração explicita para permitir a serializacao e deserializacao.
				
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		factory.setTrustAllPackages(true);
		
		
		Connection connection = factory.createConnection();
		connection.setClientID("comercial");
		
		connection.start();
		
		Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
		
		Topic topico = (Topic) context.lookup("loja");
		
		MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");
		
		
		consumer.setMessageListener(new MessageListener() {
			public void onMessage(Message message) {
				//TextMessage textMessage = (TextMessage) message;
				ObjectMessage objectMessage = (ObjectMessage) message;
				try {
					//System.out.println(textMessage.getText());
					Pedido pedido = (Pedido) objectMessage.getObject();
					System.out.println(pedido.toString());
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
