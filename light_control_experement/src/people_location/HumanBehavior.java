package people_location;

import java.util.Date;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import s2h.util.jms.JmsStub;
import s2h.util.json.JsonUtils;

public class HumanBehavior {

	static JmsStub stub;

	static Log logger = LogFactory.getLog(HumanBehavior.class);

	protected static JmsStub getSender() {
		if (stub == null) {
			try {
				logger.info("create stub: " + stub);
				stub = new JmsStub(new ActiveMQConnectionFactory(
						"tcp://192.168.4.100:61616"));
			} catch (JMSException e) {
				logger.error(e.getMessage(), e);
			}
		}

		try {
			stub.send("foo", "NULL");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.info("renew stub");
			stub = null;
			return getSender();
		}
		return stub;
	}

	public static void main(String[] args) throws SchedulerException {
		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();
		JobDetail jobDetail = new JobDetail("myJob", null, DumbJob.class);
		jobs(jobDetail);

		for (Object k : jobDetail.getJobDataMap().keySet()) {
			String key = (String) k;
			int hour = -1;
			int min = -1;
			try {
				hour = Integer.parseInt(key.substring(0, 2));
			} catch (Exception e) {
			}
			try {
				min = Integer.parseInt(key.substring(2));
			} catch (Exception e) {
			}

			if (hour < 0 || min < 0) {
				continue;
			}

			Trigger trigger = TriggerUtils.makeDailyTrigger(hour, min);
			trigger.setStartTime(TriggerUtils.getEvenSecondDate(new Date()));
			trigger.setName("triggers = " + key);
			jobDetail.setName(jobDetail.getName() + " = " + key);
			sched.scheduleJob(jobDetail, trigger);
		}

	}

	private static void jobs(JobDetail jobDetail) {
		jobDetail.getJobDataMap().put("1913", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(
						JsonUtils.createBuilder().add("peoplelocation", "DOOR")
								.toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 19:13 is finished");
			}
		});

		jobDetail.getJobDataMap().put("1914", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM-2").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 19:14 is finished");
			}
		});
		jobDetail.getJobDataMap().put("1925", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "CURTAIN_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 19:25 is finished");
			}
		});
		jobDetail.getJobDataMap().put("1927", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "KITCHEN").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 19:27 is finished");
			}
		});
		jobDetail.getJobDataMap().put("2000", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 20:00 is finished");
			}
		});
		jobDetail.getJobDataMap().put("2046", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "KITCHEN").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 20:46 is finished");
			}
		});
		jobDetail.getJobDataMap().put("2049", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 20:49 is finished");
			}
		});
		jobDetail.getJobDataMap().put("2130", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 21:30 is finished");
			}
		});
		jobDetail.getJobDataMap().put("2200", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 22:00 is finished");
			}
		});
		jobDetail.getJobDataMap().put("2201", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "BEDROOM").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 22:01 is finished");
			}
		});
		jobDetail.getJobDataMap().put("2232", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "BEDROOM-2").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 22:32 is finished");
			}
		});
		jobDetail.getJobDataMap().put("2349", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "LIVINGROOM").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 23:49 is finished");
			}
		});
		jobDetail.getJobDataMap().put("0030", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "BEDROOM").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 00:30 is finished");
			}
		});
		jobDetail.getJobDataMap().put("1611", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM-2").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 19:14 is finished");
			}
		});
		jobDetail.getJobDataMap().put("0040", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "OUT").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 00:40 is finished");
			}
		});
		jobDetail.getJobDataMap().put("0700", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 07:00 is finished");
			}
		});
		jobDetail.getJobDataMap().put("0720", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "CURTAIN_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "FAN-N_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "FAN-S_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 07:20 is finished");
			}
		});
		jobDetail.getJobDataMap().put("0730", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "KITCHEN").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-N_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-S_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 07:30 is finished");
			}
		});
		jobDetail.getJobDataMap().put("0745", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-N_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-S_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "FAN-N_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "FAN-S_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 07:45 is finished");
			}
		});
		jobDetail.getJobDataMap().put("0815", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);;
				System.out.println("Work 08:15 is finished");
			}
		});
		jobDetail.getJobDataMap().put("0827", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM-2").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 08:27 is finished");
			}
		});
		jobDetail.getJobDataMap().put("0830", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "DOOR").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 08:30 is finished");
			}
		});
		jobDetail.getJobDataMap().put("0833", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "OUT").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 08:33 is finished");
			}
		});
		jobDetail.getJobDataMap().put("1210", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "DOOR").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 12:10 is finished");
			}
		});
		jobDetail.getJobDataMap().put("1211", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM-2").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 12:11 is finished");
			}
		});
		jobDetail.getJobDataMap().put("1221", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 12:21 is finished");
			}
		});
		jobDetail.getJobDataMap().put("1222", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "KITCHEN").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-N_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-S_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 12:22 is finished");
			}
		});
		jobDetail.getJobDataMap().put("1245", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-N_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-S_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 12:45 is finished");
			}
		});
		jobDetail.getJobDataMap().put("1315", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 13:15 is finished");
			}
		});
		jobDetail.getJobDataMap().put("1345", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("value", "TV_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDGROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 13:45 is finished");
			}
		});
		jobDetail.getJobDataMap().put("1430", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM-2").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 14:30 is finished");
			}
		});
		jobDetail.getJobDataMap().put("1445", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "DOOR").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 14:45 is finished");
			}
		});
		jobDetail.getJobDataMap().put("1447", new OurJob() {
			@Override
			public void execute() throws Exception {
				HumanBehavior.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "OUT").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 14:47 is finished");
			}
		});
	}
}
