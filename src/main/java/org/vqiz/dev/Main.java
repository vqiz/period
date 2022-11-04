package org.vqiz.dev;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManager;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.vqiz.dev.mysql.DatabaseManager;
import org.vqiz.dev.mysql.Table;

import java.awt.*;
import java.security.UnrecoverableEntryException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static ArrayList<Member> incalls;
    public static ShardManager bot;
    public static Dotenv config;
    public static DatabaseManager db;
    public static Table messages = new Table("dcuserstats", "ID TEXT, XP TEXT");
    public static void main(String[] args) {
        System.out.println("Starting System up !");
        config = Dotenv.load();
        db = new DatabaseManager(config.get("MYSQLHOST"),"3306", config.get("MYSQLUSER"), config.get("MYSQLDB"), config.get("MYSQLPASSWORD")).setAsync(false);
        messages.create(db);
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(config.get("TOKTEN"));
        builder.setActivity(Activity.listening(config.get("STATUS")));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.enableIntents(GatewayIntent.GUILD_BANS);
        builder.enableIntents(GatewayIntent.GUILD_WEBHOOKS);
        builder.enableIntents(GatewayIntent.GUILD_INVITES);
        builder.enableIntents(GatewayIntent.GUILD_VOICE_STATES);
        builder.enableIntents(GatewayIntent.GUILD_PRESENCES);
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES);
        builder.enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS);
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);

    }
    public static void start(){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable yourTaskRunner = new Runnable() {
            public void run() {
                for (Member member : incalls){
                    if (!member.getVoiceState().isSelfMuted() && member.getVoiceState().getChannel().getId() != config.get("AFKCHANNELID")){
                        messages.setInt(member.getId(), "ID", "XP", messages.getInt(member.getId(), "ID", "XP") + Integer.parseInt(config.get("XPPROMINUTEIMCALL")));
                        roleuser(member, bot.getGuildById(config.get("GUILDID")));
                    }


                }




            }
        };
        scheduler.scheduleAtFixedRate(yourTaskRunner, 0, 60, TimeUnit.SECONDS);
    }
    public static void createuser(Member member){
        if (!messages.dataexist("ID", member.getId())){
            messages.insert("'" + member.getId() +"','0'");
        }
    }
    public static void roleuser(Member member, Guild guild){
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPVERTRAUTER")) && !member.getRoles().contains(guild.getRoleById(config.get("VERTRAUTERROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("VERTRAUTERROLEID"))).queue();
            if (Boolean.parseBoolean(config.get("DMUSERONLEVELUP"))){
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(config.get("LVLUPEMBEDTITLE"));
                builder.setColor(Color.GREEN);
                builder.setDescription(config.get("LVLUPEMBEDDESCRIPTION").replace("[user]", member.getUser().getAsMention()).replace("[rank]", "Vertrauter"));
                builder.setFooter(config.get("LVLUPEMBEDFOOTER"));
                try {
                    builder.setThumbnail(config.get("LVLUPEMBEDTHUMP"));
                }catch (Exception e){
                    System.out.println("NONE or invalid Embed THumpnial !");
                }
                sendMessage(member.getUser(), builder.build());

            }

        }
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPBAUER")) && !member.getRoles().contains(guild.getRoleById(config.get("BAUERROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("BAUERROLEID"))).queue();
            if (Boolean.parseBoolean(config.get("DMUSERONLEVELUP"))){
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(config.get("LVLUPEMBEDTITLE"));
                builder.setColor(Color.GREEN);
                builder.setDescription(config.get("LVLUPEMBEDDESCRIPTION").replace("[user]", member.getUser().getAsMention()).replace("[rank]", "Bauer"));
                builder.setFooter(config.get("LVLUPEMBEDFOOTER"));
                try {
                    builder.setThumbnail(config.get("LVLUPEMBEDTHUMP"));
                }catch (Exception e){
                    System.out.println("NONE or invalid Embed THumpnial !");
                }
                sendMessage(member.getUser(), builder.build());

            }

        }
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPRITTER")) && !member.getRoles().contains(guild.getRoleById(config.get("RITTERROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("RITTERROLEID"))).queue();
            if (Boolean.parseBoolean(config.get("DMUSERONLEVELUP"))){
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(config.get("LVLUPEMBEDTITLE"));
                builder.setColor(Color.GREEN);
                builder.setDescription(config.get("LVLUPEMBEDDESCRIPTION").replace("[user]", member.getUser().getAsMention()).replace("[rank]", "Ritter"));
                builder.setFooter(config.get("LVLUPEMBEDFOOTER"));
                try {
                    builder.setThumbnail(config.get("LVLUPEMBEDTHUMP"));
                }catch (Exception e){
                    System.out.println("NONE or invalid Embed THumpnial !");
                }
                sendMessage(member.getUser(), builder.build());

            }

        }
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPBARON")) && !member.getRoles().contains(guild.getRoleById(config.get("BARONROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("BARONROLEID"))).queue();
            if (Boolean.parseBoolean(config.get("DMUSERONLEVELUP"))){
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(config.get("LVLUPEMBEDTITLE"));
                builder.setColor(Color.GREEN);
                builder.setDescription(config.get("LVLUPEMBEDDESCRIPTION").replace("[user]", member.getUser().getAsMention()).replace("[rank]", "Ritter"));
                builder.setFooter(config.get("LVLUPEMBEDFOOTER"));
                try {
                    builder.setThumbnail(config.get("LVLUPEMBEDTHUMP"));
                }catch (Exception e){
                    System.out.println("NONE or invalid Embed THumpnial !");
                }
                sendMessage(member.getUser(), builder.build());

            }

        }
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPGRAF")) && !member.getRoles().contains(guild.getRoleById(config.get("GRAFROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("GRAFROLEID"))).queue();
            if (Boolean.parseBoolean(config.get("DMUSERONLEVELUP"))){
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(config.get("LVLUPEMBEDTITLE"));
                builder.setColor(Color.GREEN);
                builder.setDescription(config.get("LVLUPEMBEDDESCRIPTION").replace("[user]", member.getUser().getAsMention()).replace("[rank]", "Graf"));
                builder.setFooter(config.get("LVLUPEMBEDFOOTER"));
                try {
                    builder.setThumbnail(config.get("LVLUPEMBEDTHUMP"));
                }catch (Exception e){
                    System.out.println("NONE or invalid Embed THumpnial !");
                }
                sendMessage(member.getUser(), builder.build());

            }

        }
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPHERZOG")) && !member.getRoles().contains(guild.getRoleById(config.get("HERZOGROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("HERZOGROLEID"))).queue();
            if (Boolean.parseBoolean(config.get("DMUSERONLEVELUP"))){
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(config.get("LVLUPEMBEDTITLE"));
                builder.setColor(Color.GREEN);
                builder.setDescription(config.get("LVLUPEMBEDDESCRIPTION").replace("[user]", member.getUser().getAsMention()).replace("[rank]", "Herzog"));
                builder.setFooter(config.get("LVLUPEMBEDFOOTER"));
                try {
                    builder.setThumbnail(config.get("LVLUPEMBEDTHUMP"));
                }catch (Exception e){
                    System.out.println("NONE or invalid Embed THumpnial !");
                }
                sendMessage(member.getUser(), builder.build());

            }

        }
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPKÖNIG")) && !member.getRoles().contains(guild.getRoleById(config.get("KÖNIGROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("KÖNIGROLEID"))).queue();
            if (Boolean.parseBoolean(config.get("DMUSERONLEVELUP"))){
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(config.get("LVLUPEMBEDTITLE"));
                builder.setColor(Color.GREEN);
                builder.setDescription(config.get("LVLUPEMBEDDESCRIPTION").replace("[user]", member.getUser().getAsMention()).replace("[rank]", "König"));
                builder.setFooter(config.get("LVLUPEMBEDFOOTER"));
                try {
                    builder.setThumbnail(config.get("LVLUPEMBEDTHUMP"));
                }catch (Exception e){
                    System.out.println("NONE or invalid Embed THumpnial !");
                }
                sendMessage(member.getUser(), builder.build());

            }

        }
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPKAISER")) && !member.getRoles().contains(guild.getRoleById(config.get("KAISERROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("KAISERROLEID"))).queue();
            if (Boolean.parseBoolean(config.get("DMUSERONLEVELUP"))){
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(config.get("LVLUPEMBEDTITLE"));
                builder.setColor(Color.GREEN);
                builder.setDescription(config.get("LVLUPEMBEDDESCRIPTION").replace("[user]", member.getUser().getAsMention()).replace("[rank]", "Kaiser"));
                builder.setFooter(config.get("LVLUPEMBEDFOOTER"));
                try {
                    builder.setThumbnail(config.get("LVLUPEMBEDTHUMP"));
                }catch (Exception e){
                    System.out.println("NONE or invalid Embed THumpnial !");
                }
                sendMessage(member.getUser(), builder.build());

            }

        }

    }
    public static void sendMessage(User user, MessageEmbed content) {
        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessageEmbeds(content))
                .queue();
    }
}