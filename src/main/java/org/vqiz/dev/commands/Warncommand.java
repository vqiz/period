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
import org.vqiz.dev.utils.Warn;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Warncommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        String command = event.getName();
        User user = event.getUser();
        Member member = event.getMember();
        if (command.equals("warn") && member.getRoles().contains(event.getGuild().getRoleById(Main.config.get("TeamRoleID")))){
            OptionMapping useroption = event.getOption("user");
            OptionMapping reasonoption = event.getOption("reason");
            Main.addwarn(event.getGuild().getMemberById(useroption.getAsUser().getId()), reasonoption.getAsString(), event.getMember());
            event.deferReply().setEphemeral(true).queue();
            event.getHook().sendMessage("Du hasst den user " + useroption.getAsUser().getAsMention() + " erfolgreich verwarnt weiter infos siehst du im log channel").queue();
        }
        if (command.equals("getwarns") && member.getRoles().contains(event.getGuild().getRoleById(Main.config.get("TeamRoleID")))){
            OptionMapping useroption = event.getOption("user");
            event.deferReply().setEphemeral(true).queue();
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Warns of " + useroption.getAsUser());
            String ss = "";
            for (int i = 1; i<4;i++){
                if (Main.getwarns(event.getGuild().getMemberById(useroption.getAsUser().getId()), i).equals(null)) {
                    return;
                }
                Warn warn = Main.getwarns(event.getGuild().getMemberById(useroption.getAsUser().getId()), i);
                ss = ss + "Warn " + i + " \n Reason : " + warn.reason + "\n Time : " + warn.time + "\n Warned from : " + event.getGuild().getMemberById(useroption.getAsUser().getId()).getAsMention();


            }
            builder.setDescription(ss);
            builder.setFooter("Warn");
            builder.setColor(Color.red);


            event.getHook().sendMessageEmbeds(builder.build()).queue();
        }

    }
    @Override
    public void onGuildReady(GuildReadyEvent event){
        List<CommandData> commandData = new ArrayList<>();
        OptionData optionData = new OptionData(OptionType.USER, "user", "warn a user");
        OptionData optionData1 = new OptionData(OptionType.STRING, "reason", "the reason of the warn");
        commandData.add(Commands.slash("warn", "warn a member").addOptions(optionData).addOptions(optionData1));
        OptionData user = new OptionData(OptionType.USER, "user", "get the warns of an user");
        commandData.add(Commands.slash("getwarns", "get warns of an user").addOptions(user));
        event.getGuild().updateCommands().addCommands(commandData).queue();

    }

}
