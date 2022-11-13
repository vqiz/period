package org.vqiz.dev.commands;

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

public class Warn extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        String command = event.getName();
        User user = event.getUser();
        Member member = event.getMember();
        if (command.equals("warn") && member.getRoles().contains(event.getGuild().getRoleById(Main.config.get("TeamRoleID")))){
            OptionMapping useroption = event.getOption("user");


        }
    }
    @Override
    public void onGuildReady(GuildReadyEvent event){


    }

}
