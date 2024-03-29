/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.github.mgrouse.downtimebot;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dispatcher.CommandDispatcher;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import service.ServiceFactory;


public class DownTimeBot implements EventListener
{
    private static Logger m_logger = LoggerFactory.getLogger(DownTimeBot.class);


    private JDABuilder jdaBuilder;

    private JDA jda;

    public static void main(String[] args)
    {
	DownTimeBot bot = new DownTimeBot();

	bot.init();
    }


    public DownTimeBot()
    {

    }

    @Override
    public void onEvent(GenericEvent event)
    {
	if (event instanceof ReadyEvent)
	{
	    run();
	}
    }

    public void init()
    {
	// create JDA Builder
	jdaBuilder = JDABuilder.createLight(Secret.BotToken, EnumSet.noneOf(GatewayIntent.class));
	jdaBuilder.addEventListeners(this, new CommandDispatcher());
	try
	{
	    jda = jdaBuilder.build();
	    // optionally block until JDA is ready
	    jda.awaitReady();
	}
	catch (Exception e)
	{
	    e.printStackTrace();

	    // leave this here!
	    // DO NOT move to finally
	    // This means you Michael!!!
	    cleanUp();
	}
	finally
	{

	}
    }


    public void run()
    {
	m_logger.info("DownTimeBot is running...");
	jdaBuilder.setStatus(OnlineStatus.ONLINE);
	jdaBuilder.setActivity(Activity.playing("D&D"));

	// other JDA stuff? memmory cache?

	uploadCommands();

	// get a sheets service to kick off the authentication service for new
	// credentials
	// if we need them, with out needing to issue a Discord command
	ServiceFactory.getSheetsService();

	// would there be a way to await some sort of
	// shut down command? or re-start command?
    }

    private void uploadCommands()
    {
	// Create the /commands
	List<SlashCommandData> slashCmds = new ArrayList<SlashCommandData>();

	// Start
	SlashCommandData data = Commands.slash("start", "Starts the repeating timer to update DownTime.");

//      OptionData opts = new OptionData(OptionType.STRING, "id", "The numbers at the end of the PC's DNDB URL.");
//	opts.setRequired(true);
//	data.addOptions(opts);

	slashCmds.add(data);

	// Stop
	data = Commands.slash("stop", "Stops the repeating timer, DownTime will not be updated.");

//	OptionData opts = new OptionData(OptionType.STRING, "id", "The numbers at the end of the PC's DNDB URL.");
//	opts.setRequired(true);
//	data.addOptions(opts);

	slashCmds.add(data);

	// Status
	data = Commands.slash("status", "Returns the status of Downtime Bot, either \"Running\" or \"Stopped\". ");


	slashCmds.add(data);

	// add the commands
	CommandListUpdateAction action = jda.updateCommands();
	action.addCommands(slashCmds);
	action.queue();

    }

    public void cleanUp()
    {
	jda.shutdown();
    }
}
