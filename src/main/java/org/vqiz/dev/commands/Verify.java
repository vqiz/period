package org.vqiz.dev.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.vqiz.dev.Main;

import java.util.ArrayList;
import java.util.List;

public class Verify extends ListenerAdapter {
     @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
         String command = event.getName();
         User user = event.getUser();
         Member member = event.getMember();
         if (command.equals("verify") && member.getRoles().contains(event.getGuild().getRoleById(Main.config.get("TeamRoleID")))){
             OptionMapping useroption = event.getOption("user");
             event.getGuild().addRoleToMember(event.getGuild().getMemberById(useroption.getAsUser().getId()), event.getGuild().getRoleById(Main.config.get("NEULINGROLEID"))).queue();
             Main.welcome.add(event.getGuild().getMemberById(useroption.getAsUser().getId()));
             Main.verify(event.getGuild());

         }
    }
    @Override
    public void onGuildReady(GuildReadyEvent event){
        List<CommandData> commandData = new ArrayList<>();
        OptionData optionData = new OptionData(OptionType.USER, "user", "verify a user");
        commandData.add(Commands.slash("verify", "verify a member").addOptions(optionData));
        event.getGuild().updateCommands().addCommands(commandData).queue();

    }
}
