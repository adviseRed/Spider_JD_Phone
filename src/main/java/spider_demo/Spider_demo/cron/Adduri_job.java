package spider_demo.Spider_demo.cron;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class Adduri_job {
	
	public static void main(String[] args) {
		
		try {
			Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
			
			defaultScheduler.start();
			
			String simpleName = AddRedis.class.getSimpleName();
			JobDetail jobDetail = new JobDetail(simpleName, Scheduler.DEFAULT_GROUP, AddRedis.class);
			//	指定每天凌晨一点执行任务
			CronTrigger trigger = new CronTrigger(simpleName, Scheduler.DEFAULT_GROUP, "00 00 01 ? * *");
			defaultScheduler.scheduleJob(jobDetail, trigger);
			
			
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
}
