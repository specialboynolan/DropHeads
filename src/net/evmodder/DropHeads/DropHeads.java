/*
 * DropHeds - a Bukkit plugin for naturally dropping mob heads
 *
 * Copyright (C) 2017 - 2019 Nathan / EvModder
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.evmodder.DropHeads;

import net.evmodder.DropHeads.commands.*;
import net.evmodder.DropHeads.listeners.*;
import net.evmodder.EvLib.EvPlugin;
import net.evmodder.EvLib.Updater;

public final class DropHeads extends EvPlugin{
	private static DropHeads instance; public static DropHeads getPlugin(){return instance;}
	private HeadAPI api;
	public HeadAPI getAPI(){return api;}

	@Override public void onEvEnable(){
		if(config.getBoolean("update-plugin", true)){
			new Updater(this, 274151, getFile(), Updater.UpdateType.DEFAULT, false);
		}
		instance = this;
		api = new HeadAPI();
		getServer().getPluginManager().registerEvents(new EntitySpawnListener(), this);
		getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
		if(config.getBoolean("drop-for-indirect-kills", false)){
			getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
		}
		if(config.getBoolean("refresh-textures", false)){
			getServer().getPluginManager().registerEvents(new ItemDropListener(), this);
			getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
		}

		new CommandSpawnHead(this);
		new Commanddebug_all_heads(this);
	}
}