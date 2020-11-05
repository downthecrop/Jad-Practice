package ethos.model.players.skills.crafting;

import java.util.HashMap;
import java.util.Map;

import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;

public class Enchantment {

	private static final Enchantment SINGLETON = new Enchantment();

	private Enchantment() {
	}

	public enum Enchant {
		SAPPHIRE_RING(1637, 2550, 7, 18, 719, 114, 1), 
		SAPPHIRE_AMULET(1694, 1727, 7, 18, 719, 114, 1), 
		SAPPHIRE_NECKLACE(1656, 3853, 7, 18, 719, 114, 1),

		EMERALD_RING(1639, 2552, 27, 37, 719, 114, 2), 
		EMERALD_AMULET(1696, 1729, 27, 37, 719, 114, 2), 
		EMERALD_NECKLACE(1658, 5521, 27, 37, 719, 114, 2),

		RUBY_RING(1641, 2568, 47, 59, 720, 115, 3), 
		RUBY_AMULET(1698, 1725, 47, 59, 720, 115, 3), 
		RUBY_NECKLACE(1660, 11194, 47, 59, 720, 115, 3),

		DIAMOND_RING(1643, 2570, 57, 67, 720, 115, 4), 
		DIAMOND_AMULET(1700, 1731, 57, 67, 720, 115, 4), 
		DIAMOND_NECKLACE(1662, 11090, 57, 67, 720, 115, 4),

		DRAGONSTONE_RING(1645, 2572, 68, 78, 721, 116, 5), 
		DRAGONSTONE_AMULET(1702, 1712, 68, 78, 721, 116, 5), 
		DRAGONSTONE_NECKLACE(1664, 11105, 68, 78, 721, 116, 5), 
		DRAGONSTONE_BRACELET(11115, 11126, 68, 78, 721, 116, 5),

		ONYX_RING(6575, 6583, 87, 97, 721, 452, 6), 
		ONYX_AMULET(6581, 6585, 87, 97, 721, 452, 6), 
		ONYX_NECKLACE(6577, 11128, 87, 97, 721, 452, 6), 
		ONYX_BRACELET(11130, 11133, 87, 97, 721, 116, 6),
		
		ZENYTE_RING(19538, 19550, 87, 97, 721, 452, 6),
		ZENYTE_NECKLACE(19535, 19547, 87, 97, 721, 452, 6),
		ZENYTE_AMULET(19541, 19553, 87, 97, 721, 452, 6),
		ZENYTE_BRACELET(19532, 19544, 93, 110, 721, 452, 6);
		
		int unenchanted, enchanted, levelReq, xpGiven, anim, gfx, reqEnchantmentLevel;

		private Enchant(int unenchanted, int enchanted, int levelReq, int xpGiven, int anim, int gfx, int reqEnchantmentLevel) {
			this.unenchanted = unenchanted;
			this.enchanted = enchanted;
			this.levelReq = levelReq;
			this.xpGiven = xpGiven;
			this.anim = anim;
			this.gfx = gfx;
			this.reqEnchantmentLevel = reqEnchantmentLevel;
		}

		public int getUnenchanted() {
			return unenchanted;
		}

		public int getEnchanted() {
			return enchanted;
		}

		public int getLevelReq() {
			return levelReq;
		}

		public int getXp() {
			return xpGiven;
		}

		public int getAnim() {
			return anim;
		}

		public int getGFX() {
			return gfx;
		}

		public int getELevel() {
			return reqEnchantmentLevel;
		}

		private static final Map<Integer, Enchant> enc = new HashMap<Integer, Enchant>();

		public static Enchant forId(int itemID) {
			return enc.get(itemID);
		}

		static {
			for (Enchant en : Enchant.values()) {
				enc.put(en.getUnenchanted(), en);
			}
		}
	}

	private enum EnchantSpell {

		SAPPHIRE(1155), 
		EMERALD(1165), 
		RUBY(1176), 
		DIAMOND(1180), 
		DRAGONSTONE(1187), 
		ONYX(6003);

		int spell;

		private EnchantSpell(int spell) {
			this.spell = spell;
		}

		public int getSpell() {
			return spell;
		}

		public static final Map<Integer, EnchantSpell> ens = new HashMap<Integer, EnchantSpell>();

		public static EnchantSpell forId(int id) {
			return ens.get(id);
		}

		static {
			for (EnchantSpell en : EnchantSpell.values()) {
				ens.put(en.getSpell(), en);
			}
		}

	}

	private boolean hasRunes(Player player, int spellID) {
		switch (spellID) {
		case 1155:
			if (!player.getCombat().checkMagicReqs(56)) {
				return false;
			}
			break;
		case 1165:
			if (!player.getCombat().checkMagicReqs(57)) {
				return false;
			}
			break;
		case 1176:
			if (!player.getCombat().checkMagicReqs(58)) {
				return false;
			}
			break;
		case 1180:
			if (!player.getCombat().checkMagicReqs(59)) {
				return false;
			}
			break;
		case 1187:
			if (!player.getCombat().checkMagicReqs(60)) {
				return false;
			}
			break;
		case 6003:
			if (!player.getCombat().checkMagicReqs(61)) {
				return false;
			}
			break;
		}
		return true;
	}

	private int getEnchantmentLevel(int spellID) {
		switch (spellID) {
		case 1155: // Lvl-1 enchant sapphire
			return 1;
		case 1165: // Lvl-2 enchant emerald
			return 2;
		case 1176: // Lvl-3 enchant ruby
			return 3;
		case 1180: // Lvl-4 enchant diamond
			return 4;
		case 1187: // Lvl-5 enchant dragonstone
			return 5;
		case 6003: // Lvl-6 enchant onyx
			return 6;
		}
		return 0;
	}

	public void enchantItem(Player player, int itemID, int spellID) {
		Enchant enc = Enchant.forId(itemID);
		EnchantSpell ens = EnchantSpell.forId(spellID);
		if (enc == null || ens == null) {
			return;
		}
		/*if (enc.equals(Enchant.DRAGONSTONE_RING)) {
			player.sendMessage("This item cannot be enchanted into a 'ring of wealth' at this time. However, the ring");
			player.sendMessage("of wealth can be bought through the voting, donation and achievement store.");
			return;
		}*/
		if (player.playerLevel[player.playerMagic] >= enc.getLevelReq()) {
			if (player.getItems().playerHasItem(enc.getUnenchanted(), 1)) {
				if (hasRunes(player, spellID)) {
					if (getEnchantmentLevel(spellID) == enc.getELevel()) {
						player.getItems().deleteItem(enc.getUnenchanted(), 1);
						player.getItems().addItem(enc.getEnchanted(), 1);
						player.getPA().addSkillXP(enc.getXp() * (player.getMode().getType().equals(ModeType.OSRS) ? 1 : 10), player.playerMagic, true);
						player.startAnimation(enc.getAnim());
						player.gfx100(enc.getGFX());
						player.getPA().sendFrame106(6);
					} else {
						player.sendMessage("You can only enchant this jewelry using a level-" + enc.getELevel()
								+ " enchantment spell!");
					}
				} else {

				}
			}
		} else {
			player.sendMessage("You need a magic level of at least " + enc.getLevelReq() + " to cast this spell.");
		}
	}

	public static Enchantment getSingleton() {
		return SINGLETON;
	}

}
