package handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import timer.ClockWatcher;


public class StatusCommandHandler extends BaseCommandHandler
{
    private static Logger m_logger = LoggerFactory.getLogger(StatusCommandHandler.class);


    public StatusCommandHandler()
    {

    }


    @Override
    public void go(SlashCommandInteractionEvent event)
    {
	m_logger.info("Processing Status Command.");

	getEventData(event);

	String message = "Downtime Bot is ";

	if (ClockWatcher.isRunning())
	{
	    message = message + "Running.";
	}
	else
	{
	    message = message + "Stopped.";
	}

	getChannel().sendMessage(message).queue();
    }


}
