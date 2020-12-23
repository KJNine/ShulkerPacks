package me.darkolythe.shulkerpacks;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class ShulkerPackOpenEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;
	private ItemStack shulker;
	
	public ShulkerPackOpenEvent(Player player, ShulkerPacks main, ItemStack shulker) {
		super(player);
		this.isCancelled = main.openonlyinclaim; // If true, It'll be uncancelled if the user can't build/access containers (WG and GP listeners).
		this.shulker = shulker;
	}
	
	public ItemStack getShulkerBox() {
		return shulker;
	}
	
	public void setShulkerBox(ItemStack shulker) {
		this.shulker = shulker;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
