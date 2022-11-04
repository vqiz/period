package org.vqiz.dev.listener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.vqiz.dev.Main;

public class MESSAGERECIVE extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Main.createuser(event.getMember());
        Main.messages.setInt(event.getMember().getId(), "ID", "XP", Main.messages.getInt(event.getMember().getId(), "ID", "XP") + Integer.parseInt(Main.config.get("XPPROMESSAGE")));
        Main.roleuser(event.getMember(), event.getGuild());


    }
}
