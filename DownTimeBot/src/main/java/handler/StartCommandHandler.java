package handler;


import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mgrouse.downtimebot.Secret;

import net.dv8tion.jda.api.entities.MessageChannel;
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
	// check things out
	getEventData(event);

	// Create the message
	String message = getAdminMention() + " DownTime ran at: " + now();

	// Create the task
	MyTask myTask = new MyTask(getChannel(), message);

	// Start timer/worker
	if (Secret.isProduction)
	{
	    ClockWatcher.start(myTask, getMillisToMidnight(), ClockWatcher.ONE_DAY);
	}
	else
	{
	    // For debugging.
	    ClockWatcher.start(myTask, (long) 10, ClockWatcher.HALF_MINUTE);
	}
    }


    private class MyTask extends TimerTask
    {
	private MessageChannel m_channel;

	private String m_message = "";

	public MyTask(MessageChannel channel, String message)
	{
	    m_channel = channel;
	    m_message = message;
	}

	@Override
	public void run()
	{
	    DownTimeTracker.addDownTime();

	    m_channel.sendMessage(m_message).queue();
	}
    }

//    @Override
//    public void run()
//    {
//	DownTimeTracker.addDownTime();
//
//	String message = m_mention + " DownTime ran at: " + CommandHandlerHelper.now();
//
//	m_channel.sendMessage(message).queue();
//    }


}
