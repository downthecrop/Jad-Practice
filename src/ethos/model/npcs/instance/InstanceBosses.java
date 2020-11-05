package ethos.model.npcs.instance;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * 6/16/17
 * @author Grant_ @ rune-server.ee/grant_
 *
 */
public enum InstanceBosses {

	KING_BLACK_DRAGON(239,240,25,400,300),
	Chaos_Elemental(239,250,25,400,270),
	Scorpia(239,240,25,400,300);
	
	private int id;
	private int hp;
	private int maxHit;
	private int attack;
	private int defence;
	
	//NPC ID, NPC HP, NPC MAXHIT, NPC ATTACK, NPC DEFENCE
	private InstanceBosses(int id, int hp, int maxHit, int attack, int defence){
		this.id = id;
		this.hp = hp;
		this.maxHit = maxHit;
		this.attack = attack;
		this.defence = defence;
	}
	
	public int getID(){
		return this.id;
	}
	
	public int getHP(){
		return this.hp;
	}
	
	public int getMaxHit(){
		return this.maxHit;
	}
	
	public int getAttack(){
		return this.attack;
	}
	
	public int getDefence(){
		return this.defence;
	}

	public static final Set<InstanceBosses> VALUES = Collections.unmodifiableSet(EnumSet.allOf(InstanceBosses.class));
	
	/**
	 * Finds the boss based on the ID given
	 * @param npcId
	 * @return
	 */
	public static InstanceBosses getBossForID(int npcId) {
		for(InstanceBosses isb : VALUES){
			if(isb.getID() == npcId){
				return isb;
			}
		}
		return null;
	}
	
}
