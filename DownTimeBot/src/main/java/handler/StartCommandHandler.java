package handler;


import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import timer.ClockWatcher;
import timer.DownTimeTracker;


public class StartCommandHandler extends TimerTask implements ICommandHandler
{
    private static Logger m_logger = LoggerFactory.getLogger(StartCommandHandler.class);

    private SlashCommandInteractionEvent m_event;


    public StartCommandHandler()
    {

    }


    @Override
    public void go(SlashCommandInteractionEvent event)
    {
	m_event = event;

	// check things out

	Member member = m_event.getMember();

	String serverName = member.getEffectiveName();

	Date today = Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime();

	// log time and name of user
	String message = "Start command issued at: " + today.toString() + " by: " + serverName;

	m_event.getHook().sendMessage(message.toString()).queue();


	// For debugging. Start timer/worker
	ClockWatcher.start(this, (long) 10, ClockWatcher.HALF_MINUTE);


	// Start timer/worker
	// ClockWatcher.start(this, ClockWatcher.millisToMidnight(),
	// ClockWatcher.ONE_DAY);
    }


    @Override
    public void run()
    {
	DownTimeTracker.addDownTime();

	Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

	String message = "@Administrator DownTime ran at: " + c.getTime().toString();

	m_event.getHook().sendMessage(message).queue();
    }

}
