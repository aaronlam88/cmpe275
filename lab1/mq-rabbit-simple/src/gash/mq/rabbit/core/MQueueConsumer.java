package gash.mq.rabbit.core;

import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;

public class MQueueConsumer extends MQueueBase {
	private String queue;

	MQueueConsumer(Channel channel, String queue) {
		super(channel);
		setQueueBasename(queue);
	}

	public List<String> retrieve() throws Exception {
		ArrayList<String> r = new ArrayList<String>();
		synchronized (channel) {
			while (true) {
				// note the consumer does a manual ack to recover from errors
				GetResponse response = channel.basicGet(getQueue(), false);
				if (response == null)
					break;

				try {
					AMQP.BasicProperties props = response.getProps();
					byte[] body = response.getBody();
					r.add(new String(body));
					long deliveryTag = response.getEnvelope().getDeliveryTag();
					channel.basicAck(deliveryTag, false);
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}

		return r;
	}
}
