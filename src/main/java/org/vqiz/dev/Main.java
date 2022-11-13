package org.vqiz.dev;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManager;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.vqiz.dev.commands.Verify;
import org.vqiz.dev.commands.Warncommand;
import org.vqiz.dev.listener.GuildChannelUpdate;
import org.vqiz.dev.listener.MESSAGERECIVE;
import org.vqiz.dev.mysql.DatabaseManager;
import org.vqiz.dev.mysql.Table;
import org.vqiz.dev.utils.Warn;

import java.awt.*;
import java.security.UnrecoverableEntryException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static ArrayList<Member> welcome;
    public static ArrayList<Member> incalls;
    public static ShardManager bot;
    public static Dotenv config;
    public static DatabaseManager db;
    public static Table messages = new Table("dcuserstats", "ID TEXT, XP TEXT");
    public static Table warns = new Table("dcuserwarns", "ID TEXT, WARN1 TEXT, WARN2 TEXT, WARN3 TEXT, WARN1FROM TEXT, WARN2FROM TEXT, WARN3FROM TEXT, WARNREASON1 TEXT, WARNREASON2 TEXT, WARNREASON3 TEXT,WARN1TIME TEXT, WARN2TIME TEXT, WARN3TIME TEXT");
    public static void main(String[] args) {
        System.out.println("Starting System up !");
        config = Dotenv.load();
        db = new DatabaseManager(config.get("MYSQLHOST"),"3306", config.get("MYSQLUSER"), config.get("MYSQLDB"), config.get("MYSQLPASSWORD")).setAsync(false);
        messages.create(db);
        warns.create(db);
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
        bot = builder.build();
        bot.addEventListener(new GuildChannelUpdate());
        bot.addEventListener(new MESSAGERECIVE());
        bot.addEventListener(new Verify());
        bot.addEventListener(new Warncommand());
        for (Member member : bot.getGuildById(config.get("GUILDID")).getMembers()){
            checkuser(member, bot.getGuildById(config.get("GUILDID")));
        }
        start();
        for (Member member : bot.getGuildById(config.get("GUILDID")).getMembers()){
            if (!warns.dataexist("ID", member.getId())){
                warns.insert("'" + member.getId() +"','null','null','null','null','null','null','null','null','null','null','null','null'");
                System.out.println("Added " + member.getNickname() + " to database");
            }

        }
    }
    public static Warn getwarns(Member member, int wich){
        
            if (!warns.getString(member.getId(), "ID", "WARN" + wich).equals("null")){
                Warn warn = new Warn();
                warn.member = member;
                warn.reason = warns.getString(member.getId(), "ID", "WARNREASON" + wich);
                warn.banner = bot.getGuildById(config.get("GUILDID")).getMemberById(warns.getString(member.getId(), "ID", "WARN" + wich + "FROM"));
                warn.time = warns.getString(member.getId(), "ID", "WARN" + wich +"TIME");
                return warn;
            }
            return null;




    }
    public static void addwarn(Member member, String reason, Member from){
        for (int i = 1; i < 4; i++){
            if (warns.getString(member.getId(), "ID", "WARN" + i).equals("null")){
                warns.setString(member.getId(), "ID", "WARN" + i, "true");
                warns.setString(member.getId(), "ID", "WARN" + i + "FROM", from.getId());
                warns.setString(member.getId(), "ID", "WARNREASON" + i, reason);
                warns.setString(member.getId(), "ID", "WARN"+ i + "TIME", LocalTime.now().toString());
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("WARN");
                builder.setColor(Color.RED);
                builder.setDescription("Das Teammitglied " + from.getAsMention() + " hat " + member.getAsMention() + " gewarnt ! \n  es ist sein " + i + " warn. \n Grund : " + reason + "\n Zeit : " + LocalTime.now());
                builder.setFooter("MODERATION SYSTEM");
                bot.getGuildById(config.get("GUILDID")).getTextChannelById(config.get("warnlogchannelid")).sendMessageEmbeds(builder.build()).queue();

                break;
            }
            if (i == 3){
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("WARN");
                builder.setColor(Color.RED);
                builder.setDescription(from.getAsMention() + " der user " + member.getAsMention() + " hat bereits 3 verwarnungen ereicht !");
                builder.setFooter("MODERATION SYSTEM");
                bot.getGuildById(config.get("GUILDID")).getTextChannelById(config.get("warnlogchannelid")).sendMessageEmbeds(builder.build()).queue();
            }

        }
    }
    public static void verify(Guild guild){
        if (welcome.size() == 3){
            String memberstring = "";
            for (Member member : welcome){
                memberstring = memberstring + " " + member.getAsMention();
                welcome.remove(member);
            }
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle(config.get("welcometitle"));
            builder.setDescription(config.get("welcomedescription").replace("[user]", memberstring));
            builder.setFooter(config.get("welcomefooter"));
            guild.getTextChannelById(config.get("LabereckenChannelid")).sendMessageEmbeds(builder.build()).queue();
        }

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
    public static void checkuser(Member member, Guild guild){
        if (member.getRoles().contains(guild.getRoleById(config.get("VERTRAUTERROLEID")))){
            messages.setInt(member.getId(), "ID", "XP", Integer.parseInt(config.get("REQUIREDXPVERTRAUTER")));
        }
        if (member.getRoles().contains(guild.getRoleById(config.get("BAUERROLEID")))){
            messages.setInt(member.getId(), "ID", "XP", Integer.parseInt(config.get("REQUIREDXPBAUER")));
        }
        if (member.getRoles().contains(guild.getRoleById(config.get("RITTERROLEID")))){
            messages.setInt(member.getId(), "ID", "XP", Integer.parseInt(config.get("REQUIREDXPRITTER")));
        }
        if (member.getRoles().contains(guild.getRoleById(config.get("BARONROLEID")))){
            messages.setInt(member.getId(), "ID", "XP", Integer.parseInt(config.get("REQUIREDXPBARON")));
        }
        if (member.getRoles().contains(guild.getRoleById(config.get("GRAFROLEID")))){
            messages.setInt(member.getId(), "ID", "XP", Integer.parseInt(config.get("REQUIREDXPGRAF")));
        }
        if (member.getRoles().contains(guild.getRoleById(config.get("HERZOGROLEID")))){
            messages.setInt(member.getId(), "ID", "XP", Integer.parseInt(config.get("REQUIREDXPHERZOG")));
        }
        if (member.getRoles().contains(guild.getRoleById(config.get("KÖNIGROLEID")))){
            messages.setInt(member.getId(), "ID", "XP", Integer.parseInt(config.get("REQUIREDXPKÖNIG")));
        }
        if (member.getRoles().contains(guild.getRoleById(config.get("KAISERROLEID")))){
            messages.setInt(member.getId(), "ID", "XP", Integer.parseInt(config.get("REQUIREDXPKAISER")));
        }









    }
    public static void roleuser(Member member, Guild guild){
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPVERTRAUTER")) && !member.getRoles().contains(guild.getRoleById(config.get("VERTRAUTERROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("VERTRAUTERROLEID"))).queue();
            Dmsuseronlvlup(member, "Vertrauter");

        }
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPBAUER")) && !member.getRoles().contains(guild.getRoleById(config.get("BAUERROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("BAUERROLEID"))).queue();
            Dmsuseronlvlup(member, "Bauer");

        }
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPRITTER")) && !member.getRoles().contains(guild.getRoleById(config.get("RITTERROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("RITTERROLEID"))).queue();
            Dmsuseronlvlup(member, "Ritter");

        }
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPBARON")) && !member.getRoles().contains(guild.getRoleById(config.get("BARONROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("BARONROLEID"))).queue();
            Dmsuseronlvlup(member, "Baron");
        }
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPGRAF")) && !member.getRoles().contains(guild.getRoleById(config.get("GRAFROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("GRAFROLEID"))).queue();
            Dmsuseronlvlup(member, "Graf");

        }
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPHERZOG")) && !member.getRoles().contains(guild.getRoleById(config.get("HERZOGROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("HERZOGROLEID"))).queue();
            Dmsuseronlvlup(member, "Herzog");

        }
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPKÖNIG")) && !member.getRoles().contains(guild.getRoleById(config.get("KÖNIGROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("KÖNIGROLEID"))).queue();
            Dmsuseronlvlup(member, "König");

        }
        if (messages.getInt(member.getId(), "ID", "XP") >Integer.parseInt(config.get("REQUIREDXPKAISER")) && !member.getRoles().contains(guild.getRoleById(config.get("KAISERROLEID")))){
            guild.addRoleToMember(member, guild.getRoleById(config.get("KAISERROLEID"))).queue();
            Dmsuseronlvlup(member, "Kaiser");


        }

    }
    public static void Dmsuseronlvlup(Member member, String NAME){
        if (Boolean.parseBoolean(config.get("DMUSERONLEVELUP"))) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle(config.get("LVLUPEMBEDTITLE"));
            builder.setColor(Color.GREEN);
            builder.setDescription(config.get("LVLUPEMBEDDESCRIPTION").replace("[user]", member.getUser().getAsMention()).replace("[rank]", NAME));
            builder.setFooter(config.get("LVLUPEMBEDFOOTER"));
            try {
                builder.setThumbnail(config.get("LVLUPEMBEDTHUMP"));
            } catch (Exception e) {
                System.out.println("NONE or invalid Embed THumpnial !");
            }
            sendMessage(member.getUser(), builder.build());

        }
    }
    public static void sendMessage(User user, MessageEmbed content) {
        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessageEmbeds(content))
                .queue();
    }
}