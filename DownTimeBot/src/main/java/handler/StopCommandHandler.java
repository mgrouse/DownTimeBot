package handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import timer.ClockWatcher;


public class StopCommandHandler extends BaseCommandHandler
{
    private static Logger m_logger = LoggerFactory.getLogger(StopCommandHandler.class);

    public StopCommandHandler()
    {

    }


    @Override
    public void go(SlashCommandInteractionEvent event)
    {
	// check things out
	getEventData(event);

	// Stop timer/worker
	ClockWatcher.stop();
    }

}
