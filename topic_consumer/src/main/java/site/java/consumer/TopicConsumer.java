package site.java.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author shkstart
 * @create 2020-08-21 20:25
 */
@Component
public class TopicConsumer {

    // 使用JmsListener配置消费者监听的队列
    @JmsListener(destination = "myTopic")
    public void handleMessage1(String msg) {
        System.out.println("消费者1成功接收topic消息成功：" + msg);
    }

    // 使用JmsListener配置消费者监听的队列
    @JmsListener(destination = "myTopic")
    public void handleMessage2(String msg) {
        System.out.println("消费者2成功接收topic消息成功：" + msg);
    }

}
