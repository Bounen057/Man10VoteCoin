package man10.red.man10votingcoin;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
//import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Man10VotingCoin extends JavaPlugin {
    //////////////////////////////////////////////////
    //   変数の準備 説明
    //////////////////////////////////////////////////
    //  投票番号関係
    public HashMap<Integer,ArrayList<String>> vote_JoinedPlayer = new HashMap<Integer, ArrayList<String>>();
    public ArrayList<String> vote_JoinedPlayer_list = new ArrayList<>();//Coinチャンスに参加した人
    public ArrayList<Date> vote_time = new ArrayList<>();
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public int vote_number=0;
    public ArrayList<String> vote_voter = new ArrayList<>();//投票した人

    public String PluginName ="§d§l[§f§k§lXX§a§lH§2§la§e§lp§d§lp§b§ly§6§lCoin§f§k§lXX§a§l]";

    CustomConfig yml = new CustomConfig(this);
    MVC_command MVC_command = new MVC_command(this,yml);

    public CustomConfig config;
    public CustomConfig voting;
    @Override
    public void onEnable() {
        config = new CustomConfig(this);
        voting = new CustomConfig(this, "voting.yml");
        config.saveDefaultConfig();
        voting.saveDefaultConfig();

        getCommand("man10votecoin").setExecutor(this);
    }

    /////////////////////////////////
    //  Commands
    /////////////////////////////////
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                MVC_command.help((Player)sender);
            }
            if (args[0].equalsIgnoreCase("on")) {
                MVC_command.on((Player)sender);
            }
            if (args[0].equalsIgnoreCase("off")) {
                MVC_command.off((Player)sender);
            }
            if (args[0].equalsIgnoreCase("stats")) {
                MVC_command.stats((Player)sender);
            }
            if (args[0].equalsIgnoreCase("register")) {
                MVC_command.register((Player)sender);
            }
        }
        if(args.length == 2) {
            if (args[0].equalsIgnoreCase("voting")) {
                if(sender instanceof Player){
                    ((Player)sender).sendMessage(PluginName+"§c§lプレイヤーは実行できないコマンドです");
                    return false;
                }
                    MVC_command.create(args[1]);

            }
            if (args[0].equalsIgnoreCase("getcoin")) {
                MVC_command.getCoin((Player)sender, Integer.parseInt(args[1]));
            }
        }

        config.saveConfig();
        return false;
    }
}
