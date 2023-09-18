package handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


public class StatusCommandHandler extends BaseCommandHandler
{
    private static Logger m_logger = LoggerFactory.getLogger(StatusCommandHandler.class);


    public StatusCommandHandler()
    {

    }


    @Override
    public void go(SlashCommandInteractionEvent event)
    {
	getEventData(event);


    }


}
