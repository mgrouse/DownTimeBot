package handler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


public class CommandHandlerHelper
{

    private static String m_adminMention = "";


    private static SimpleDateFormat m_dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
    {
	m_dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }


    public static String getAdminMention(SlashCommandInteractionEvent event)
    {
	Role role = event.getGuild().getRolesByName("Administrator", true).get(0);

	if (null != role)
	{
	    m_adminMention = role.getAsMention();
	}

	return m_adminMention;
    }


    public static String now()
    {
	return m_dateFormatter.format(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
    }

    public static Long millisToMidnight()
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
