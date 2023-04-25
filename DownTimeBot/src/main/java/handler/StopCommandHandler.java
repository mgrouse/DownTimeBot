package handler;

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

	String mention = CommandHandlerHelper.getAdminMention(event);

	// log time and name of user
	String message = mention + " Stop command issued by: " + serverName;

	event.getHook().sendMessage(message).queue();

	// Stop timer/worker
	ClockWatcher.stop();
    }

}
