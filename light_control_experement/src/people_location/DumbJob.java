package people_location;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DumbJob implements Job {

	static Log logger = LogFactory.getLog(DumbJob.class);

	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		int hour = ctx.getFireTime().getHours();
		int min = ctx.getFireTime().getMinutes();

		String key = ((hour < 10) ? "0" + hour : "" + hour)
				+ ((min < 10) ? "0" + min : "" + min);

		logger.info(key);

		OurJob job = (OurJob) ctx.getJobDetail().getJobDataMap().get(key);
		if (job == null) {
			logger.warn("job is null, with the key: " + key);
			return;
		}
		try {
			job.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
