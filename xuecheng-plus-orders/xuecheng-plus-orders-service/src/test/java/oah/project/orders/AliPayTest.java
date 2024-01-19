package oah.project.orders;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import oah.project.orders.config.AlipayConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * @ClassName AliPayTest
 * @Description TODO
 * @Author _oah
 * @Date 2024.01.03 16:00
 * @Version 1.0
 */
@SpringBootTest
public class AliPayTest {


	@Value("${pay.alipay.APP_ID}")
	String APP_ID;
	@Value("${pay.alipay.APP_PRIVATE_KEY}")
	String APP_PRIVATE_KEY;

	@Value("${pay.alipay.ALIPAY_PUBLIC_KEY}")
	String ALIPAY_PUBLIC_KEY;


	@Test
	public void queryPayResult() throws AlipayApiException {
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, APP_ID, APP_PRIVATE_KEY, "json", AlipayConfig.CHARSET, ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE); //获得初始化的AlipayClient
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		JSONObject bizContent = new JSONObject();
		bizContent.put("out_trade_no", "1742539207028400128");
		//bizContent.put("trade_no", "2014112611001004680073956707");
		request.setBizContent(bizContent.toString());
		AlipayTradeQueryResponse response = alipayClient.execute(request);
		String body = response.getBody();
		System.out.println(body);
//		if (response.isSuccess()) {
//			System.out.println("调用成功");
//			String resultJson = response.getBody();
//			//转map
//			Map resultMap = JSON.parseObject(resultJson, Map.class);
//			Map alipay_trade_query_response = (Map) resultMap.get("alipay_trade_query_response");
//			//支付结果
//			String trade_status = (String) alipay_trade_query_response.get("trade_status");
//			System.out.println(trade_status);
//		} else {
//			System.out.println("调用失败");
//		}
	}





}
