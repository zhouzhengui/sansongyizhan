package cn.stylefeng.guns.core.schedue.spring;

import cn.stylefeng.guns.modular.suninggift.entity.QueueSchedule;
import cn.stylefeng.guns.modular.suninggift.service.*;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 乐芃收单系统的异步通知
 * @author guodong
 *
 */
@Slf4j
@Component
public class OrderDisposeTask implements InitializingBean {
	
	SimpleDateFormat sdf_t = new SimpleDateFormat("yyyyMMdd HHmmss");
	
	private static final String SENDPOST = "sendPost";

	@Autowired
	private QueueScheduleService queueScheduleService;

	@Autowired
	SysConfigService sysConfigService;

	@Autowired
	OperatorAPIService operatorAPIService;

	@Autowired
	OrderInfoService orderInfoService;

	@Autowired
	InInterfaceService inInterfaceService;

	@Autowired
	QimenService qimenService;

	public LinkedBlockingQueue<QueueSchedule> notifyQueue = new LinkedBlockingQueue<>(1000);

	public boolean isGetStart = false;

	public boolean isPostStart = false;
	
	private long sleepTime = 200;

	private final int ADDNEXTNOTIFYTIME = 5;//同笔订单间隔5秒查一次 单位（秒）

	private final long SYNC_DATABASE_FREQUENCY = 30 * 1000L;//间隔30秒查同步一次数据库 单位（毫秒）

	private final int SYNC_DATABASE_TIME_SCOPE = 24 * 60;//同步一次数据库的时间范围 单位（分）

	private final int SYNC_DATABASE_RESTART = 30;//同步一次数据库的时间范围 单位（分）


//	public synchronized boolean addNotifyBean(QueueSchedule bean) {
//		boolean isSuccess = false;
//		try {
//			//如果队列已满，会等待两秒
//			isSuccess = notifyQueue.offer(bean, 10, TimeUnit.SECONDS);
//		}catch (Exception e) {
//
//		}
//
//		//如果两秒后都无法插入队列，重新读取所有异步通知表
//		if(! isSuccess) {
//			notifyQueue = new LinkedBlockingQueue<>(100);
//			notifyQueue.offer(bean);
//			readNotifyToQueue();
//		}
//
//		return isSuccess;
//	}
	
	//每小时30分0秒触发重启线程
//	@Scheduled(cron = "0 */30 * * * *")
	public void restartJob(){
		log.info("订单定时任务多线程启动");
		isGetStart = false;
		try {
			Thread.sleep(5000);//延迟读取数据库尚未同步的状态
		} catch (Exception e) {
		}
		isGetStart = true;
		//开一个线程循环1小时
//		startGetNotify();  //重启获取异步通知的进程
		boolean b = readNotifyToQueue();
		log.info("初始化队列"+b);
		isPostStart = false;
		try {
			Thread.sleep(500);
		} catch (Exception e) {
		}
		
		isPostStart = true;
		//开5个线程循环0.5小时
		startPostNotify(2);  //重启处理异步通知的进程
	}

	/**
	 *
	 */
//	public void startGetNotify() {
//		log.info("=================================启用每分钟读取数据库尚未成功的订单状态");
//		new Thread() {
//			public void run() {
//				//while (isGetStart) {
//				Calendar c = Calendar.getInstance();
//				c.setTime(new Date());
//				c.add(Calendar.MINUTE, 60);//1个小时
//				Date eqDate = c.getTime();
//				while (eqDate.getTime() > new Date().getTime()) {
//					try {
//						Thread.sleep(SYNC_DATABASE_FREQUENCY);//每分钟读一次数据库的尚未同步状态
//					} catch (Exception e) {
//					}
//					log.info("每分钟读一次数据库的尚未同步状态========");
//					readNotifyToQueue();
//				}
//			};
//		}.start();
//	}
	
	public void startPostNotify(int threads) {
		log.info("重启处理异步通知的进程");
		for(int i = 0; i< threads; i++) {
			try{Thread.sleep(500);}catch(Exception e) {}
			
			new Thread() {
				public void run() {
					//while(isPostStart) {
					Calendar c = Calendar.getInstance();
					c.setTime(new Date());
					c.add(Calendar.MINUTE, SYNC_DATABASE_RESTART);
					Date eqDate = c.getTime();
					while (eqDate.getTime() > new Date().getTime()) {
						try{Thread.sleep(sleepTime);}catch(Exception e) {}

						//队列当中拿出一个bean进行处理，如果处理不成功继续加入队列尾部，处理成功就丢弃
						QueueSchedule notify = notifyQueue.poll();
						if(notify == null) {
							continue;
						}
//						log.info("======="+Thread.currentThread().getName());
						try {
							Date date = new Date();
							if(notify.getNextNotifyTime().getTime() > date.getTime()){
//								log.info(Thread.currentThread().getName()  + "尚未到下次发送时间" + JSONObject.toJSONString(notify));
								notifyQueue.offer(notify);
								continue;//尚未到下次发送时间
							}
							System.err.println(sdf_t.format(notify.getUpdateTime())+"时间" + sdf_t.format(date.getTime()));


							String resp = "fail";
							JSONObject responReslut = new JSONObject();
							notify.setNotifyCount(notify.getNotifyCount()+1);

							//TODO 编写逻辑
                            resp = "";//aa(notify);

							if(StringUtils.isNotBlank(resp)){
								//转小写
								resp = resp.toLowerCase();
							}

							if("success".equals(resp) || "0000".equals(responReslut.getString("code")) ||
									"0".equals(responReslut.getString("respCode"))) {
								notify.setStatus(1);
								notify.setResponseData(resp);
								notify.setSuccessTime(new Date());
								notify.setUpdateTime(notify.getSuccessTime());
								queueScheduleService.updateById(notify);
								log.info(">>>>>>>> resp:"+resp + " || outTradeNo:" + notify.getOutTradeNo());
								continue;
							}else {
								//更新下一次通知的时间
								notify = updateNotifyNext(notify);
								//重新入队继续通知
//								log.info("1队列长度" + notifyQueue.size());
								notifyQueue.offer(notify);
//								log.info("2队列长度" + notifyQueue.size());
							}
						}catch(Exception e) {
							log.info("异步通知处理出现错误  outTradeNo="+notify.getOutTradeNo() , e);
							//更新下一次通知的时间
							notify = updateNotifyNext(notify);
						}
					}
				}
			}.start();
		}
	}
	
	/**
	 * 获取下次通知时间和创建时间之间的毫秒差
	 * @param notify
	 * @return
	 */
	private long getIntervalNotifyTime(QueueSchedule notify) {
		if(notify.getCreateTime() == null || notify.getNextNotifyTime() == null) {
			return 0;
		}
		return notify.getNextNotifyTime().getTime() - notify.getCreateTime().getTime();
	};
	
	/**
	 * 更新下一次通知的时间
	 * @param notify
	 * @return
	 */
	public QueueSchedule updateNotifyNext(QueueSchedule notify) {
		notify.setStatus(0);
		notify.setUpdateTime(new Date());
		Calendar c = Calendar.getInstance();
		c.setTime(notify.getUpdateTime());
		c.add(Calendar.SECOND, ADDNEXTNOTIFYTIME);
		notify.setNextNotifyTime(c.getTime());
		queueScheduleService.updateById(notify);
		return notify;
	}
	
	/**
	 * 读取数据库数据到队列
	 * @return
	 */
	public boolean readNotifyToQueue(){
		try {
			Calendar c = Calendar.getInstance();
			//c.add(Calendar.HOUR_OF_DAY, -6);//两小时内
			c.add(Calendar.MINUTE, -SYNC_DATABASE_TIME_SCOPE);
			Date eqDate = c.getTime();//当前时间往前推一天

//			c = Calendar.getInstance();
//			c.add(Calendar.MINUTE, -5);//数据库只捞5分钟前的数据/3分钟之后的新数据不再自动塞入队列
//			Date stDate = c.getTime();//当前时间往前推5分钟

			//读取 NextNotifyTime > 一小时以内的数据
			QueryWrapper<QueueSchedule> example = new QueryWrapper();
//			example.le("next_notify_time", stDate);
			example.ge("create_time", eqDate);
			example.eq("status",0);

			List<QueueSchedule> notifyList = queueScheduleService.list();// agentPayNotifyMapper.selectByExampleWithBLOBs(example);
			log.info("同步数据条数" + notifyList.size());
			notifyQueue.clear();
			notifyQueue.addAll(notifyList);
			return true;
		}catch(Exception e) {
			return false;
		}
	}



	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("初始化的时候执行一次");
//		restartJob();
	}

	/**
	 * 添加信息到队列
	 * @param queueSchedule
	 * @return
	 */
	public Boolean addQueue(QueueSchedule queueSchedule){
		return notifyQueue.offer(queueSchedule);
	}

}


