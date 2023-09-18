package dispatcher;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mgrouse.downtimebot.Secret;

import handler.StartCommandHandler;
import handler.StatusCommandHandler;
import handler.StopCommandHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class CommandDispatcher extends ListenerAdapter
{
    private static Logger m_logger = LoggerFactory.getLogger(CommandDispatcher.class);

    public CommandDispatcher()
    {

    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
	Boolean isEphemeral = false;

	// We only have 3 seconds until Discord sends "App not responding"
	// defer reply cancels that message
	event.deferReply(isEphemeral).queue();

	// only allow calling DownTime bot from the "correct" channel
	if (event.getChannel().getName().contentEquals(Secret.ADMIN_CHANNEL) &&
	// and either Admin or Me
		(isAdmin(event.getMember()) || isMichael(event.getMember()))//
	)


	{
	    switch (event.getName())
	    {
		case "start":
		{
		    StartCommandHandler startHandler = new StartCommandHandler();
		    startHandler.go(event);
		    break;
		}
		case "stop":
		{
		    StopCommandHandler stopHandler = new StopCommandHandler();
		    stopHandler.go(event);
		    break;
		}
		case "status":
		{
		    StatusCommandHandler stausHandler = new StatusCommandHandler();
		    stausHandler.go(event);
		    break;
		}
//	    case "file":
//	    {
//		StatusCommandHandler stausHandler = new StatusCommandHandler();
//		stausHandler.go(event);
//		break;
//	    }
//	    case "notify-add-remove":
//	    {
//		StatusCommandHandler stausHandler = new StatusCommandHandler();
//		stausHandler.go(event);
//		break;
//	    }
		default:
		{
		    m_logger.info("Unknown Slash Command. CommandDispatcher.java");
		    event.getHook().sendMessage("DownTime does not recognize the slash command " + event.getName())
			    .queue();
		}
	    }// switch
	} // if ((channel)&&(Admin))
	else
	{
	    event.getHook().sendMessage(" " + event.getName()).queue();
	}
    }

    private Boolean isAdmin(Member member)
    {
	Boolean retVal = false;

	List<Role> memberRoles = member.getRoles();

	for (Role role : memberRoles)
	{
	    if (role.getName().contentEquals("Administrator"))
	    {
		retVal = true;
		break;
	    }
	}

	return retVal;
    }

    private Boolean isMichael(Member member)
    {
	Boolean retVal = false;

	if (member.getUser().getName().contentEquals("Go1denScarab"))
	{
	    retVal = true;
	}

	return retVal;
    }
}
