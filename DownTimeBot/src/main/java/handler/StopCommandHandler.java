package handler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import timer.ClockWatcher;


public class StopCommandHandler implements ICommandHandler
{
    private static Logger m_logger = LoggerFactory.getLogger(StopCommandHandler.class);

    public StopCommandHandler()
    {

    }


    @Override
    public void go(SlashCommandInteractionEvent event)
    {
	// check things out

	Member member = event.getMember();

	String serverName = member.getEffectiveName();

	Date today = Calendar.getInstance(TimeZone.getTimeZone("GMT +0")).getTime();
	// "yyyy-MM-dd"
	SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
	dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT +0"));

	// log time and name of user
	String message = "Stop command issued at: " + today.toString() + " by: " + serverName;

	event.getHook().sendMessage(message).queue();

	// Stop timer/worker
	ClockWatcher.stop();
    }


}
