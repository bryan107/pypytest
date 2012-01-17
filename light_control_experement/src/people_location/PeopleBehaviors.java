package people_location;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import s2h.util.jms.JmsStub;
import s2h.util.json.JsonUtils;

public class PeopleBehaviors {

	static Log logger = LogFactory.getLog(PeopleBehaviors.class);

	static JmsStub stub;

	public static void main(String[] args) throws JMSException {

		execute(19, 13, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "DOOR").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 19:13 is finished");
			}
		});

		execute(19, 14, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM-2").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 19:14 is finished");
			}
		});

		execute(19, 17, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 19:17 is finished");
			}
		});
		execute(19, 25, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "CURTAIN_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 19:25 is finished");
			}
		});
		execute(19, 27, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "KITCHEN").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 19:27 is finished");
			}
		});
		execute(20, 0, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 20:00 is finished");

			}
		});
		execute(20, 46, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "KITCHEN").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 20:46 is finished");
			}
		});
		execute(20, 49, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 20:49 is finished");
			}
		});
		execute(21, 30, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_DOWN").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 21:30 is finished");
			}
		});
		execute(22, 0, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 22:00 is finished");
			}
		});
		execute(22, 1, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "BEDROOM").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 20:01 is finished");
			}
		});
		execute(22, 32, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "BEDROOM-2").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 22:32 is finished");
			}
		});
		execute(23, 49, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "LIVINGROOM").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 23:49 is finished");
			}
		});
		execute(0, 30, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "BEDROOM").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 00:30 is finished");
			}
		});
		execute(0, 40, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "OUT").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 00:40 is finished");
			}
		});
		execute(7, 0, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 07:00 is finished");
			}
		});
		execute(7, 20, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "CURTAIN_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "FAN-N_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "FAN-S_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 07:20 is finished");
			}
		});
		execute(7, 30, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "KITCHEN").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-N_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-S_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 07:30 is finished");
			}
		});
		execute(7, 45, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-N_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-S_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "FAN-N_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "FAN-S_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 07:45 is finished");
			}
		});
		execute(8, 15, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);;
				System.out.println("Work 08:15 is finished");
			}
		});
		execute(8, 27, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM-2").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 08:27 is finished");
			}
		});
		execute(8, 30, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "DOOR").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 08:30 is finished");
			}
		});
		execute(8, 33, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "OUT").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 08:33 is finished");
			}
		});
		execute(12, 10, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "DOOR").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 12:10 is finished");
			}
		});
		execute(12, 11, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM-2").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 12:11 is finished");
			}
		});
		execute(12, 21, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_ON").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 12:21 is finished");
			}
		});
		execute(12, 22, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "KITCHEN").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-N_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-S_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 12:22 is finished");
			}
		});
		execute(12, 45, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-N_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "AIR-PUMP-S_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 12:45 is finished");
			}
		});
		execute(13, 15, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "LIVINGROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_VOL_UP").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				System.out.println("Work 13:15 is finished");
			}
		});

		execute(13, 45, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("value", "TV_OFF").toJson(), "ssh.COMMAND");
				Thread.sleep(3000);
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDGROOM").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 13:45 is finished");
			}
		});

		execute(14, 30, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "BEDROOM-2").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 14:30 is finished");
			}
		});
		execute(14, 45, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "DOOR").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 14:45 is finished");
			}
		});
		execute(14, 47, new Job() {
			@Override
			public void execute() throws Exception {
				PeopleBehaviors.getSender().send(JsonUtils.createBuilder().add("peoplelocation", "OUT").toJson(), "ssh.CONTEXT");
				Thread.sleep(3000);
				System.out.println("Work 14:47 is finished");
			}
		});


		// if (true) {
		// return;
		// }
		//
		// SchedulerFactory schedFact = new
		// org.quartz.impl.StdSchedulerFactory();
		//
		// Scheduler sched = schedFact.getScheduler();
		//
		// sched.start();
		//
		// JobDetail jobDetail = new JobDetail("myJob", null, AJob.class);
		//
		// Trigger trigger = TriggerUtils.makeDailyTrigger(14, 40);
		// trigger.setStartTime(TriggerUtils.getEvenSecondDate(new Date()));
		// trigger.setName("myTrigger");
		//
		// sched.scheduleJob(jobDetail, trigger);

		new Thread(){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(5000);
						System.out.println(new Date());
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
	}

	protected static JmsStub getSender() {
		if(stub == null){
			try {
				logger.info("create stub: "+ stub);
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

	private static void execute(int hour, int min, final Job job) {
		logger.info("set job: " + job);
		Date now = new Date();
		Calendar date = Calendar.getInstance();
		date.set(Calendar.HOUR_OF_DAY, hour);
		date.set(Calendar.MINUTE, min);
//
//		if((now.getHours() * 100 + now.getMinutes() >  hour * 100 + min)){
//			date.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH)+1);
//		}

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					job.execute();
				} catch (Exception e) {
				}
			}
		}, date.getTime());

	}
}
