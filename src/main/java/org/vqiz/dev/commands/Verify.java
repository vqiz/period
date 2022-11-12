package org.vqiz.dev.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.vqiz.dev.Main;

public class Verify extends ListenerAdapter {
     @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
         String command = event.getName();
         User user = event.getUser();
         Member member = event.getMember();
         if (command.equals("verify") && member.getRoles().contains(event.getGuild().getRoleById(Main.config.get("TeamRoleID")))){
             OptionMapping useroption = event.getOption("user");
             event.getGuild().addRoleToMember(event.getGuild().getMemberById(useroption.getAsUser().getId()), event.getGuild().getRoleById(Main.config.get("NEULINGROLEID"))).queue();


         }

     }
}
