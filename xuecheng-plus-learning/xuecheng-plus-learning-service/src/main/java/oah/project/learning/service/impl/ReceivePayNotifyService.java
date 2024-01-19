package oah.project.learning.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import oah.project.base.exception.XueChengPlusException;
import oah.project.learning.config.PayNotifyConfig;
import oah.project.learning.service.MyCourseTablesService;
import oah.project.messagesdk.model.po.MqMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static oah.project.learning.config.PayNotifyConfig.PAYNOTIFY_QUEUE;

/**
 * @ClassName ReceivePayNotifyService
 * @Description 接收消息通知处理类
 * @Author _oah
 * @Date 2024.01.04 17:10
 * @Version 1.0
 */
@Service
@Slf4j
public class ReceivePayNotifyService {


	@Autowired
	private MyCourseTablesService myCourseTablesService;


	@RabbitListener(queues = PayNotifyConfig.PAYNOTIFY_QUEUE)
	public void receive(Message message) {

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 解析出消息
		byte[] body = message.getBody();
		String jsonString = new String(body);

		// 转成对象
		MqMessage mqMessage = JSON.parseObject(jsonString, MqMessage.class);
		// 解析消息的内容
		// chooseCourseId（选课id）
		String chooseCourseId = mqMessage.getBusinessKey1();
		// 订单类型
		String orderType = mqMessage.getBusinessKey2();
		// 学习中心服务只要购买课程类的支付订单的结果
		if(orderType.equals("60201")) {
			// 根据消息内容，更新选课记录、向我的课程表插入记录
			boolean b = myCourseTablesService.saveChooseCourseSuccess(chooseCourseId);
			if(!b) {
				XueChengPlusException.cast("保存选课记录状态失败");
			}
		}
	}

}
