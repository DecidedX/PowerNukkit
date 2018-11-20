package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;

/**
 * Created by PetteriM1
 */
public class PlaySoundCommand extends VanillaCommand {

    public PlaySoundCommand(String name) {
        super(name, "%nukkit.command.playsound.description", "%commands.playsound.usage", new String[]{"?"});
        this.setPermission("nukkit.command.playsound");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("sound", CommandParameter.ARG_TYPE_STRING, false),
                new CommandParameter("player", CommandParameter.ARG_TYPE_PLAYER, true)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }

        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(new TranslationContainer("commands.generic.ingame"));
                return true;
            }

            Player p = (Player) sender;

            p.getLevel().addSound(p, args[0]);
            p.sendMessage(new TranslationContainer("commands.playsound.success", args[0], p.getName()));
            
            return true;
        }

        if (args.length > 1) {
            Player p = Server.getInstance().getPlayer(args[1]);
            
            if (p == null) {
                sender.sendMessage(new TranslationContainer("commands.generic.player.notFound"));
                return true;
            }

            p.getLevel().addSound(p, args[0]);
            sender.sendMessage(new TranslationContainer("commands.playsound.success", args[0], p.getName()));
        }

        return true;
    }
}