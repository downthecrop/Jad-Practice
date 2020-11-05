package ethos.model.npcs;

public class NPCDef {

	public int npcId;
	public String npcName;
	public int npcCombat;
	public int npcHealth;
	public int maxHit;
	public int meleeAtk;
	public int meleeDef;
	public int rangeAtk;
	public int rangeDef;
	public int mageAtk;
	public int mageDef;
	public int attackEmote;
	public int deathEmote;
	public int blockEmote;
	public int size;

	public NPCDef(int _npcId) {
		this.npcId = _npcId;
	}

	public void loadDefaultValues() {
		this.maxHit = this.npcCombat / 10;
		this.meleeAtk = (int) (this.npcCombat * 1.5D);
		this.meleeDef = (int) (this.npcCombat * 1.1D);
		this.rangeAtk = (int) (this.npcCombat * 2.5D);
		this.rangeDef = (int) (this.npcCombat * 1.2D);
		this.mageAtk = this.npcCombat * 2;
		this.mageDef = (int) (this.npcCombat * 1.5D);
	}

	public int getDeathEmote() {
		return deathEmote;
	}

	public void setDeathEmote(int deathEmote) {
		this.deathEmote = deathEmote;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getMaxHit() {
		return maxHit;
	}

	public void setMaxHit(int maxHit) {
		this.maxHit = maxHit;
	}

}
