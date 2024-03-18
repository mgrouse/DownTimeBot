package handler;


import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mgrouse.downtimebot.Secret;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import timer.ClockWatcher;
import timer.DownTimeTracker;


public class StartCommandHandler extends BaseCommandHandler
{
    private static Logger m_logger = LoggerFactory.getLogger(StartCommandHandler.class);


    public StartCommandHandler()
    {
    }


    @Override
    public void go(SlashCommandInteractionEvent event)
    {
	m_logger.info("Processing Start Command.");

	// check things out
	getEventData(event);

	// Create the task
	MyTask myTask = new MyTask();

	// Start timer/worker
	if (Secret.isDebug())
	{
	    // For debugging.
	    ClockWatcher.start(myTask, (long) 10, ClockWatcher.HALF_MINUTE);
	}
	else
	{
	    ClockWatcher.start(myTask, getMillisToMidnight(), ClockWatcher.ONE_DAY);
	}

	getHook().sendMessage("The timer has been started.").queue();
	m_logger.info("The timer has been started.");
    }


    private class MyTask extends TimerTask
    {

	public MyTask()
	{
	}

	private String makeMessage()
	{
	    // Create the message to announce a run each time

	    // ======
	    // Yeah Private inner classes get to do this kinda stuff!!!
	    // ======
	    return getAdminMention() + " DownTime ran at: " + now();
	}


	@Override
	public void run()
	{
	    // these need to be "getChanel()" as WebHooks only last 15 minutes
	    // and if this is prod this will run once a day.
	    try
	    {
		getChannel().sendMessage(makeMessage()).queue();
		DownTimeTracker.addDownTime();
	    }
	    catch (Exception e)
	    {
		getChannel().sendMessage(e.getMessage()).queue();
	    }
	}
    }


}
