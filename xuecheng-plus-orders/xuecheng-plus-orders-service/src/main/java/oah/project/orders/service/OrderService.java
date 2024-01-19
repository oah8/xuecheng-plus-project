package oah.project.orders.service;

import oah.project.messagesdk.model.po.MqMessage;
import oah.project.orders.model.dto.AddOrderDto;
import oah.project.orders.model.dto.PayRecordDto;
import oah.project.orders.model.dto.PayStatusDto;
import oah.project.orders.model.po.XcPayRecord;

/**
 * @ClassName OrderService
 * @Description 订单相关的service接口
 * @Author _oah
 * @Date 2024.01.03 19:11
 * @Version 1.0
 */
public interface OrderService {

	/**
	 * 创建商品订单
	 * @param userId
	 * @param addOrderDto 订单信息
	 * @return
	 */
	public PayRecordDto createOrder(String userId, AddOrderDto addOrderDto);


	/**
	 * 查询支付记录
	 * @param payNo 交易记录号
	 * @return
	 */
	public XcPayRecord getPayRecordByPayNo(String payNo);



	/**
	 * 请求支付宝查询支付结果
	 * @param payNo 支付记录id
	 * @return 支付记录信息
	 */
	public PayRecordDto queryPayResult(String payNo);


	/**
	 * 保存支付状态
	 * @param payStatusDto
	 */
	public void saveAliPayStatus(PayStatusDto payStatusDto);


	/**
	 * 发送通知结果
	 * @param message
	 */
	public void notifyPayResult(MqMessage message);



}






















