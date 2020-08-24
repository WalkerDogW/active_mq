package site.java.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;
import java.io.IOException;

/**
 * @author shkstart
 * @create 2020-08-22 10:49
 */
@RestController
public class ConsumerController {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Value("${spring.activemq.topicName}")
    private String topicName;

    @RequestMapping("receiveFromTopic")
    public void sendMessage() throws JMSException, IOException {
        String topicName = "myTopic_new";

        //通过链接工厂获取connection并启动
        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        connection.start();

        //创建会话，参数为事务，签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //创建目的地
        Topic topic = session.createTopic(topicName);

        //创建消费者
        MessageConsumer messageConsumer = session.createConsumer(topic);




        //2、通过监听的方式来消费消息（异步非阻塞  监听器 onMessage()）
        messageConsumer.setMessageListener((message)->{
            if(message !=null && (message instanceof  TextMessage)){
                TextMessage textMessage = (TextMessage)message;
                try {
                    System.out.println("消费者1：topic: "+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //保证控制台不关闭
        System.in.read();





        //关闭资源
        messageConsumer.close();
        session.close();
        connection.close();
    }


    @RequestMapping("receiveFromTopic2")
    public void sendMessage2() throws JMSException, IOException {
        String topicName = "myTopic_new";

        //通过链接工厂获取connection并启动
        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        connection.start();

        //创建会话，参数为事务，签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //创建目的地
        Topic topic = session.createTopic(topicName);

        //创建消费者
        MessageConsumer messageConsumer = session.createConsumer(topic);




        //2、通过监听的方式来消费消息（异步非阻塞  监听器 onMessage()）
        messageConsumer.setMessageListener((message)->{
            if(message !=null && (message instanceof  TextMessage)){
                TextMessage textMessage = (TextMessage)message;
                try {
                    System.out.println("消费者2：topic: "+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //保证控制台不关闭
        System.in.read();





        //关闭资源
        messageConsumer.close();
        session.close();
        connection.close();
    }


    //持久化
    @RequestMapping("receiveFromTopic3")
    public void sendMessage3() throws JMSException, IOException {
        String topicName = "myTopic_durable";

        //通过链接工厂获取connection并启动
        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
//        connection.start();

        //使用z3来订阅myTopic_durable这个主题
        connection.setClientID("z3");

        //创建会话，参数为事务，签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //创建目的地
        Topic topic = session.createTopic(topicName);

        //创建消费者
//        MessageConsumer messageConsumer = session.createConsumer(topic);
        //创建主题订阅者
        TopicSubscriber durableSubscriber = session.createDurableSubscriber(topic, "remark...");

        connection.start();


        Message message = durableSubscriber.receive();
        while(message != null){
            TextMessage textMessage =  (TextMessage)message;
            System.out.println("***收到持续化的topic："+textMessage.getText());
            message = durableSubscriber.receive(5000L);
            System.out.println("message:...");
        }


        //关闭资源
        durableSubscriber.close();
        session.close();
        connection.close();
    }
}
