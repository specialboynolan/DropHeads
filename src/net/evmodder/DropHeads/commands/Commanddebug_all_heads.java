package net.evmodder.DropHeads.commands;

import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import net.evmodder.DropHeads.DropHeads;
import net.evmodder.DropHeads.HeadAPI;
import net.evmodder.DropHeads.HeadUtils;
import net.evmodder.EvLib.EvCommand;
import net.evmodder.EvLib.EvPlugin;

public class Commanddebug_all_heads extends EvCommand{
//	private final EvPlugin pl;

	public Commanddebug_all_heads(EvPlugin plugin) {
		super(plugin);
//		pl = plugin;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){return null;}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String args[]){
		if(sender instanceof Player == false){
			sender.sendMessage(ChatColor.RED+"This command can only be run by in-game players!");
			return true;
		}
		final HeadAPI api = DropHeads.getPlugin().getAPI();
		boolean noGrumm = true;

		final Location loc = ((Player)sender).getLocation();
		int numHeads = api.getTextures().size();
		if(noGrumm) numHeads /= 2;
		int dimX, dimY, dimZ;
		dimX = dimY = dimZ = (int)Math.floor(Math.cbrt(numHeads));
		if(dimX*dimY*dimZ < numHeads) if((++dimX)*dimY*dimZ < numHeads) if(dimX*dimY*(++dimZ) < numHeads) ++dimY;
		final int dX = dimX, dY = dimY, dZ = dimZ;

		for(int x=0; x<dX; ++x) for(int z=0; z<dZ; ++z) for(int y=0; y<dY; ++y){
			if(!loc.clone().add(((dX/2)-x)*2, ((dY/2)-y)*2, ((dZ/2)-z)*2).getBlock().isEmpty()){
				sender.sendMessage(ChatColor.RED+"Error: Not enough space around the player to place the heads!");
				return true;
			}
		}
		sender.sendMessage("Placing "+numHeads+" heads...");
		sender.sendMessage("Dimensions: "+dX+","+dY+","+dZ);
		Iterator<String> it = api.getTextures().keySet().iterator();
		//pl.getServer().getScheduler()(pl, new Runnable(){public void run(){
			for(int z=dZ; z>0 && it.hasNext(); --z)
			for(int y=dY; y>0 && it.hasNext(); --y)
			for(int x=dX; x>0 && it.hasNext(); --x){
				Location hLoc = loc.clone().add(((dX/2)-x)*2, ((dY/2)-y)*2, ((dZ/2)-z)*2);
				String key = it.next();
				if(noGrumm){
					while(key.endsWith("|GRUMM") && it.hasNext()) key = it.next();
					if(key.endsWith("|GRUMM")) break;
				}
				ItemStack skullItem = api.makeTextureSkull(key);
				hLoc.getBlock().setType(Material.PLAYER_HEAD);
				Skull blockState = (Skull) hLoc.getBlock().getState();
				blockState.setType(Material.PLAYER_HEAD);
				//blockState.setData(skull.getData());
				HeadUtils.setGameProfile(blockState, HeadUtils.getGameProfile((SkullMeta)skullItem.getItemMeta()));
				blockState.update();
			}
		//}});
		sender.sendMessage("Finished placing "+numHeads+" heads");

		return true;
	}
}