package handler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


public abstract class BaseCommandHandler implements ICommandHandler
{
    private MessageChannel m_channel;

    private Member m_member;

    private String m_nameOnServer = "";

    private String m_adminMention = "";

    private SimpleDateFormat m_dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
    {
	m_dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }


    @Override
    public abstract void go(SlashCommandInteractionEvent event);


    public void getEventData(SlashCommandInteractionEvent event)
    {
	m_channel = event.getChannel();

	m_member = event.getMember();

	m_adminMention = getAdminMention(event);

	String m_nameOnServer = m_member.getEffectiveName();

	// log time and name of user
	String message = m_adminMention + " " + event.getCommandId() + " command issued by: " + m_nameOnServer;

	event.getHook().sendMessage(message).queue();
    }


    public MessageChannel getChannel()
    {
	return m_channel;
    }


    public Member getMember()
    {
	return m_member;
    }


    public String getNameOnServer()
    {
	return m_nameOnServer;
    }


    public String getAdminMention()
    {
	return m_adminMention;
    }


    public String getAdminMention(SlashCommandInteractionEvent event)
    {
	Role role = event.getGuild().getRolesByName("Administrator", true).get(0);

	if (null != role)
	{
	    m_adminMention = role.getAsMention();
	}

	return m_adminMention;
    }


    public String now()
    {
	return m_dateFormatter.format(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
    }

    public Long getMillisToMidnight()
    {
	Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	c.add(Calendar.DAY_OF_YEAR, 1);
	c.set(Calendar.HOUR_OF_DAY, 0);
	c.set(Calendar.MINUTE, 0);
	c.set(Calendar.SECOND, 0);
	c.set(Calendar.MILLISECOND, 0);

	return c.getTimeInMillis() - System.currentTimeMillis();
    }
}
