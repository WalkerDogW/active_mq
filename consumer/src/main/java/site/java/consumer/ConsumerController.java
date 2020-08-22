package site.java.consumer;

import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("receiveFromQueue2")
    public void sendMessage2() throws JMSException, IOException {
        //通过链接工厂获取connection并启动
        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        connection.start();

        //创建会话，参数为事务，签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //创建目的地
        Queue myQueue2 = session.createQueue("myQueue2");

        //创建消费者
        MessageConsumer messageConsumer = session.createConsumer(myQueue2);

        /*

        //1、同步阻塞方式receive()
        //订阅者或接受者调用MessageConsumer的receive()方法来接收消息，
        //receive方法在能够接收消息之前一直阻塞
        while(true){
//            会阻塞，一直等待
//            TextMessage textMessage = (TextMessage)messageConsumer.receive();

            TextMessage textMessage = (TextMessage)messageConsumer.receive(4000L);
            if(textMessage != null){
                System.out.println("消费者："+textMessage.getText());
            }else{
                break;
            }
        }


         */


        //2、通过监听的方式来消费消息（异步）
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if(message !=null && (message instanceof  TextMessage)){
                    TextMessage textMessage = (TextMessage)message;
                    try {
                        System.out.println("消费者："+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//        //保证控制台不关闭
//        System.in.read();





        //关闭资源
        messageConsumer.close();
        session.close();
        connection.close();
    }

}
