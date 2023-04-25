package handler;


import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mgrouse.downtimebot.Secret;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import timer.ClockWatcher;
import timer.DownTimeTracker;


public class StartCommandHandler extends TimerTask implements ICommandHandler
{
    private static Logger m_logger = LoggerFactory.getLogger(StartCommandHandler.class);

    private MessageChannel m_channel;

    private String m_mention;

    public StartCommandHandler()
    {

    }


    @Override
    public void go(SlashCommandInteractionEvent event)
    {
	// check things out

	Member member = event.getMember();

	m_mention = CommandHandlerHelper.getAdminMention(event);

	String serverName = member.getEffectiveName();

	// log time and name of user
	String message = m_mention + " Start command issued by: " + serverName;

	event.getHook().sendMessage(message).queue();

	m_channel = event.getChannel();

	// Start timer/worker
	if (Secret.isProduction)
	{
	    ClockWatcher.start(this, CommandHandlerHelper.millisToMidnight(), ClockWatcher.ONE_DAY);
	}
	else
	{
	    // For debugging.
	    ClockWatcher.start(this, (long) 10, ClockWatcher.HALF_MINUTE);
	}
    }


    @Override
    public void run()
    {
	DownTimeTracker.addDownTime();

	String message = m_mention + " DownTime ran at: " + CommandHandlerHelper.now();

	m_channel.sendMessage(message).queue();
    }

}
