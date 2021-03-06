/**
 * 
 */
package com.osm.web.async;

import java.util.concurrent.Callable;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.osm.web.async.DeferredResultHolder;
import com.osm.web.async.MockQueue;


/**
 * @author ouShiming
 *
 */
@RestController
@RequestMapping("/order")
public class AsyncController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MockQueue mockQueue;
	
	@Autowired
	private DeferredResultHolder deferredResultHolder;
	
	@RequestMapping("/deferredResult")
	public DeferredResult<String> deferredResultOrder() throws Exception {
		logger.info("主线程开始");
		String orderNumber = RandomStringUtils.randomNumeric(8);
		mockQueue.setPlaceOrder(orderNumber);
		DeferredResult<String> result = new DeferredResult<>();
		deferredResultHolder.getMap().put(orderNumber, result);
		logger.info("主线程返回");
		return result;
	}
	
	
	
	@RequestMapping("/callable")
	public Callable<String> callableOrder() throws Exception {
		logger.info("主线程开始");
		Callable<String> result = new Callable<String>() {
			@Override
			public String call() throws Exception {
				logger.info("副线程开始");
				Thread.sleep(1000);
				logger.info("副线程返回");
				return "success";
			}
		};
		logger.info("主线程返回");
		
		return result;
	}
}
