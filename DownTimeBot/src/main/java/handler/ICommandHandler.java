package handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface ICommandHandler
{
    public void go(SlashCommandInteractionEvent event);
    
}
