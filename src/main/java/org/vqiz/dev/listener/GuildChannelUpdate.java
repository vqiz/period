package org.vqiz.dev.listener;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.vqiz.dev.Main;

public class GuildChannelUpdate extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Main.createuser(event.getMember());
        if (!event.getMember().getVoiceState().inAudioChannel()) {
            Main.incalls.remove(event.getMember());
        } else {
            if (!Main.incalls.contains(event.getMember())){
                Main.incalls.add(event.getMember());
            }

        }
    }
}
