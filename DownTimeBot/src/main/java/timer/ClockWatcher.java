package timer;

import java.util.Timer;
import java.util.TimerTask;


public class ClockWatcher
{
    public static final Long ONE_DAY = (long) 24 * 60 * 60 * 1000;

    // for debugging
    public static final Long HALF_MINUTE = (long) 30 * 1000;

    // timer
    private static Timer m_timer = null;


    public static void start(TimerTask task, Long delay, Long period)
    {
	// if timer not already running, start timer
	if (null == m_timer)
	{
	    m_timer = new Timer();

	    // for debugging
	    m_timer.schedule(task, delay, period);
	}
    }

    public static void stop()
    {
	// stop timer
	if (null != m_timer)
	{
	    // clean up
	    m_timer.cancel();

	    m_timer = null;
	}


    }


}
