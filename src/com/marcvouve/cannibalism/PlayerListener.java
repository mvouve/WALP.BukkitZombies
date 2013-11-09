package com.marcvouve.cannibalism;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


/**
 * Player listener for the Cannibalism plugin. When players have have the 
 * "HUNGER" potion effect, and are not holding an item, player eats the
 * other player dealing *2 the normal damage. However, this amplifies the
 * hunger by +100% (up to 400%) and doubles the hungers duration.
 * (max ~10minutes)
 * 
 * Hunger is reset on death.
 * 
 * @author Marc Vouve
 *
 */
public class PlayerListener implements Listener {
	
	/**
	 * The maximum amount of time the hunger can last
	 */
	private static final int MAX_DURATION = 6000;
	
	/**
	 * The maximum amplification of the hunger
	 */
	private static final int MAX_AMP = 4;
	
	/**
	 * The maximum food points a player can have, this is already predefined.
	 */
	private static final int MAX_FOOD = 10;
	
	/**
	 * The amount of food hitting another player heals.
	 */
	private static final int FOOD_HEALED = 3;
	
	
	/**
	 * When hit by someone with hunger on and empty hand, victim takes double damage,
	 * attacker gains food, but hunger grows in amplitude.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onHitByCannibal(EntityDamageByEntityEvent event){
		
		//Checks if both entities are Players
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player){
			
			//Player victim = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();
			
			//Makes sure the player has Hunger effect and nothing in their hand
			if(damager.hasPotionEffect(PotionEffectType.HUNGER) &&
					damager.getItemInHand().getType() == Material.AIR){
				
				event.setDamage(event.getDamage() * 2);
				
				if(damager.getFoodLevel() < MAX_FOOD){
					
					damager.setFoodLevel(damager.getFoodLevel() + FOOD_HEALED);
					
				}
				//Finds hunger in list of status effects.
				for(PotionEffect pots: damager.getActivePotionEffects()){
					Bukkit.getConsoleSender().sendMessage("" + pots.getType());
					
					if(pots.getType().equals(PotionEffectType.HUNGER)){
						
						//Gets current amplifier and duration
						int potDur = pots.getDuration();
						int potAmp = pots.getAmplifier();
						
						//If duration is less then MAX_DURATION, sets duration to duration * 2
						if (potDur < MAX_DURATION){
							
							potDur *= 2;
							
						}
						
						//Checks if Hunger is lower then MAX_AMP, and adds 1x amplification to hunger.
						if (potAmp < MAX_AMP){
							
							damager.sendMessage("The hunger has grown stronger");
							++potAmp;
							
						}
						
						//Removes hunger and replies with the new modifier
						damager.removePotionEffect(PotionEffectType.HUNGER);
						damager.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, potDur, potAmp));
						
					}
				}
			}
		}
	}
	
	/**
	 * On death removes hunger, otherwise it stays in effect, even if it's set
	 * for a ridiculous amount of time.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onDeath(PlayerDeathEvent event){

		if(event.getEntity().hasPotionEffect(PotionEffectType.HUNGER)){
			event.getEntity().removePotionEffect(PotionEffectType.HUNGER);
		}
	}
}
