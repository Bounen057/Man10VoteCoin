package man10.red.man10votingcoin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
//import org.bukkit.Bukkit.spigot;


public class MVC_command {
    private Man10VotingCoin plugin;
    private CustomConfig yml;

    public MVC_command(Man10VotingCoin plugin, CustomConfig yml) {
        this.plugin = plugin;
        this.yml = yml;
    }


    //////////////////////////////////////////////////
    //  コマンド 使い方
    //////////////////////////////////////////////////
    public void help(Player p) {
        if(p.hasPermission("votecoin.staff.help")) {
            p.sendMessage("§e§l=" + plugin.PluginName + " §6§lhelp§e§l=======================");
            p.sendMessage("§7§l/man10votecoin voting <MCID> §3§l-投票した時のevent(コンソール用)");
            p.sendMessage("§7§l/man10votecoin getcoin <投票番号> §3§l-コインを受け取る(コンソール用)");
            p.sendMessage("§7§l/man10votecoin stats §3§l-統計を見ます");
            p.sendMessage("§7§l/man10votecoin register §3§l-コインを設定します");
            p.sendMessage("§7§l/man10votecoin off §3§l-このplをOFFにします");
            p.sendMessage("§7§l/man10votecoin on §3§l-このplをONにします");
            if (plugin.config.getConfig().getBoolean("votecoin.vote.mode")) {
                p.sendMessage("§c§l現在の状況 OFF 閉じています。");
            } else {
                p.sendMessage("§a§l現在の状況 ON 開いています。");
            }
            p.sendMessage("§e§l==========================================================");
        }else{
            p.sendMessage(plugin.PluginName+"§c§lあなたには権限がありません!");
        }
    }

    //////////////////////////////////////////////////
    //  現在の状態 ON OFF
    //////////////////////////////////////////////////
    public void on(Player p){
        if(p.hasPermission("votecoin.staff.on")) {
            plugin.config.getConfig().set("votecoin.vote.mode", false);
            p.sendMessage(plugin.PluginName + "§a§lONにしました!");
        }else{
            p.sendMessage(plugin.PluginName+"§c§lあなたには権限がありません!");
        }
    }
    public void off(Player p){
        if(p.hasPermission("votecoin.staff.off")) {
            plugin.config.getConfig().set("votecoin.vote.mode", true);
            p.sendMessage(plugin.PluginName + "§a§lOFFにしました!");
        }else{
            p.sendMessage(plugin.PluginName+"§c§lあなたには権限がありません!");
        }
    }

    //////////////////////////////////////////////////
    //  コイン発行数などの統計
    //////////////////////////////////////////////////
    public void stats(Player p){
        if(p.hasPermission("votecoin.staff.stats")) {
            p.sendMessage("§e§l=" + plugin.PluginName + " §6§lstats§e§l=======================");
            if(plugin.config.getConfig().get("votecoin.stats.coinamount")==null){
                p.sendMessage("§a§lCoin発行数 : §e§l0枚");
            }else {
                p.sendMessage("§a§lCoin発行数 : §e§l" + plugin.config.getConfig().get("votecoin.stats.coinamount") + "枚");
            }
        }else{
            p.sendMessage(plugin.PluginName+"§c§lあなたには権限がありません!");
        }
    }

    public void register(Player p){
        if(p.hasPermission("votecoin.staff.register")) {
            plugin.voting.getConfig().set("voting.item",p.getInventory().getItemInMainHand());
            p.sendMessage(plugin.PluginName+"§a§lコインを登録しました!");
            plugin.voting.saveConfig();
        }else{
            p.sendMessage(plugin.PluginName+"§c§lあなたには権限がありません!");
        }
    }
    //////////////////////////////////////////////////
    //  プレイヤーが投票した時の処理
    //////////////////////////////////////////////////
    public void create(String args) {
        int i= plugin.config.getConfig().getInt("votecoin.vote.number");//投票 5回目の時
        if(plugin.config.getConfig().getBoolean("votecoin.vote.mode")){
            return;
        }
        //////////////////////////////////////////////////////////
        // チャットをクリックした時のイベント
        if(i==5) {
            plugin.vote_voter.add(plugin.vote_number,args);
            //////////////////////////////////////////////////////////
            //  CHAT の処理
            //////////////////////////////////////////////////////////
            TextComponent tc = new TextComponent();
            Bukkit.broadcastMessage(plugin.PluginName+"§a§l" + plugin.vote_voter.get(plugin.vote_number) + "さんが投票しました!§3§l5回目の投票です!");
            tc.setText(plugin.PluginName+"§b§l§kaa§e§l§f §k§lXX§a§lH§2§la§e§lp§d§lp§b§ly§6§lCoin§f§k§lXX§f§lゲット->§6§l§n<ここをクリック>§f §b§l§kaa");
            tc.setColor(ChatColor.AQUA);
            tc.setBold(true);
            tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/man10votecoin getcoin " + plugin.vote_number));
            String hover_text = "§e§l§k|§f§l If you click here,you get one §e§lHappy§6§lCoin§f§l chance §e§l§k|";
            tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover_text).create()));
            Bukkit.spigot().broadcast(tc);

            //  時間 管理
            plugin.vote_time.add(new Date());

            //音を鳴らす
            for(Player player:Bukkit.getOnlinePlayers()){
                Location loc = player.getLocation();
                loc.getWorld().playSound(loc,Sound.RECORD_WAIT,0.1f,1f);
            }
            //投票番号
            plugin.vote_number++;
            i=1;
        }else {
            TextComponent tc = new TextComponent();
            tc.setText(plugin.PluginName+"§a§l"+args+"さんが投票しました!§6§lみんなも投票しよう!§a§l§n[/vote]");
            tc.setColor(ChatColor.AQUA);
            tc.setBold(true);
            tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote"));
            String hover_text = "§e§l投票のURLを開きます";
            tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover_text).create()));
            Bukkit.spigot().broadcast(tc);
            Bukkit.broadcastMessage(plugin.PluginName+"§f§k§lXX§a§lH§2§la§e§lp§d§lp§b§ly§6§lCoin§f§k§lXX§f§lゲットまで...§e§l§n"+(5-i)+"回§e§l");
            i++;

            //音を鳴らす
            for(Player player:Bukkit.getOnlinePlayers()){
                Location loc = player.getLocation();
                loc.getWorld().playSound(loc,Sound.RECORD_STRAD,0.1f,1f);
            }
        }
            plugin.config.getConfig().set("votecoin.vote.number",i);
    }
////////////////////////////////////////////////////////////////////////////////
// コインをもらう処理
////////////////////////////////////////////////////////////////////////////////
    public void getCoin(Player p,int number){
        //////////////////////////////////////////
        //   ERROR CHECK
        //////////////////////////////////////////
        if(!(p.hasPermission("votecoin.use.getcoin"))) {
            p.sendMessage(plugin.PluginName+"§c§lあなたには権限がありません!");
            return;
        }
        if(plugin.config.getConfig().getBoolean("votecoin.vote.mode")){
            p.sendMessage(plugin.PluginName+"§c§l現在はCLOSE中です");
            return;
        }
        if(plugin.vote_voter.get(number) == null){
            p.sendMessage(plugin.PluginName+"§c§lそのような投票番号は存在しません!");
            return;
        }
        //時間差分で管理
        Date now_time = new Date();
        long dateVoting = plugin.vote_time.get(number).getTime();
        long dateTimenow = now_time.getTime();

        long dayDiff = (( dateTimenow - dateVoting)) / (1000 );
        if(dayDiff > 180){
            p.sendMessage(plugin.PluginName+"§c§l投票してから一定時間がたったので受け取れません!");
            return;
        }

        // 複数回のコインの受け取り
        if(plugin.vote_JoinedPlayer.get(number) == null) {
            plugin.vote_JoinedPlayer_list = new ArrayList<String>();
            plugin.vote_JoinedPlayer_list.add("-");
            plugin.vote_JoinedPlayer.put(number, plugin.vote_JoinedPlayer_list);
        }

        for(int i = 0;i < plugin.vote_JoinedPlayer.get(number).size(); i++){
            if(plugin.vote_JoinedPlayer.get(number).get(i).equalsIgnoreCase(p.getUniqueId().toString())){
                p.sendMessage(plugin.PluginName+"§c§lあなたは既に§f§k§lXX§a§lH§2§la§e§lp§d§lp§b§ly§6§lCoin§f§k§lXX§c§lを貰っています!");
                return;
            }
        }

        // 投票した本人は受け取れない設定
        /*if(plugin.vote_voter.get(number).equalsIgnoreCase(p.getName())){
            p.sendMessage(plugin.PluginName+"§c§l自分のCoinGetChanceには参加できません!");
            return;
        }*/

        /////////////////////////////////////////
        //  2どめの対策
        plugin.vote_JoinedPlayer_list = new ArrayList<String>();
        plugin.vote_JoinedPlayer_list = plugin.vote_JoinedPlayer.get(number);
        plugin.vote_JoinedPlayer_list.add(p.getUniqueId().toString());
        plugin.vote_JoinedPlayer.put(number,plugin.vote_JoinedPlayer_list);

        giveCoin(p);//アイテムを付与
    }

    public void giveCoin(Player p){
        plugin.config.getConfig().set("votecoin.stats.coinamount",(plugin.config.getConfig().getInt("votecoin.stats.coinamount"))+1);
        p.sendMessage(plugin.PluginName+"§f§k§lXX§a§lH§2§la§e§lp§d§lp§b§ly§6§lCoin§f§k§lXXを受け取りました!");
        p.getInventory().addItem(plugin.voting.getConfig().getItemStack("voting.item"));
        plugin.config.saveConfig();
    }
}
