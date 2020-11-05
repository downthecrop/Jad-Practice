package ethos.model.players.skills.hunter.trap.impl;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.items.GameItem;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCDumbPathFinder;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.model.players.skills.hunter.trap.Trap;
import ethos.world.objects.GlobalObject;

/**
 * The box trap implementation of the {@link Trap} class which represents a single box trap.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BoxTrap extends Trap {

	/**
	 * Constructs a new {@link BoxTrap}.
	 * @param player	{@link #getPlayer()}.
	 */
	public BoxTrap(Player player) {
		super(player, TrapType.BOX_TRAP);
	}
	
	/**
	 * The npc trapped inside this box.
	 */
	private Optional<NPC> trapped = Optional.empty();

	/**
	 * Determines if an animal is going to the trap.
	 */
	private Optional<CycleEvent> event = Optional.empty();
	
	/**
	 * The object identification for a dismantled failed box trap.
	 */
	private static final int FAILED_ID = 9385;
	
	/**
	 * The object identification for a caught box trap.
	 */
	private static final int CAUGHT_ID = 721;
	
	/**
	 * The distance the npc has to have from the box trap before it gets triggered.
	 */
	private static final int DISTANCE_PORT = 3;
	
	/**
	 * A collection of all the npcs that can be caught with a box trap.
	 */
	private static final ImmutableSet<Integer> NPC_IDS = ImmutableSet.of(BoxTrapData.FERRET.npcId, BoxTrapData.CHINCHOMPA.npcId,
			BoxTrapData.CARNIVOROUS_CHINCHOMPA.npcId, BoxTrapData.BLACK_CHINCHOMPA.npcId);
	
	/**
	 * Kills the specified {@code npc}.
	 * @param npc	the npc to kill.
	 */
	private void kill(NPC npc) {
		//NPCHandler.kill(npc.npcType, npc.heightLevel);
//		npc.absX = 0;
//		npc.absY = 0;
//		npc = null;
		npc.isDead = true;
		trapped = Optional.of(npc);
	}
	
	@Override
	public boolean canCatch(NPC npc) {
		Optional<BoxTrapData> data = BoxTrapData.getBoxTrapDataByNpcId(npc.npcType);
		
		if(!data.isPresent()) {
			throw new IllegalStateException("Invalid box trap id.");
		}
		if (System.currentTimeMillis() - player.lastPickup < 2500)
			return false;	
		
		if(player.playerLevel[21] < data.get().requirement) {//(player.playerLevel[player.playerHunter] < data.get().requirement)FIXME
			player.lastPickup = System.currentTimeMillis();
			player.sendMessage("You do not have the required level to catch these.");
			setState(TrapState.FALLEN);
			return false;
		}
		return true;
	}

	@Override
	public void onPickUp() {
		player.sendMessage("You pick up your box trap.");		
	}

	@Override
	public void onSetup() {
		player.sendMessage("You set-up your box trap.");
	}

	@Override
	public void onCatch(NPC npc) {
		if(event.isPresent()) {
			return;
		}
		if(!Server.getGlobalObjects().anyExists(getObject().getX(), getObject().getY(), getObject().getHeight())) {
			System.out.println("Hunter; No trap existed while attempting to catch");
			return;
		}
		
		event = Optional.of(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				NPCDumbPathFinder.walkTowards(npc, getObject().getX(), getObject().getY());
				if(isAbandoned()) {
					container.stop();
					return;
				}
				if(npc.absX == getObject().getX() && npc.absY == getObject().getY()) {
					container.stop();
					
					if(!Server.getGlobalObjects().anyExists(getObject().getX(), getObject().getY(), getObject().getHeight())) {
						System.out.println("Hunter; No trap existed while attempting to catch");
						return;
					}

					int count = random.inclusive(180);
					int formula = successFormula(npc);
					if(count > formula) {
						setState(TrapState.FALLEN);
						container.stop();
						return;
					}
					npc.randomWalk = false;
					kill(npc);
					Server.getGlobalObjects().remove(getObject());
					Server.getGlobalObjects().remove(getObject().getObjectId(), getObject().getX(), getObject().getY(), getObject().getHeight());
					setObject(CAUGHT_ID);
					Server.getGlobalObjects().add(getObject());
					setState(TrapState.CAUGHT);
				}
			}
			
			@Override
			public void stop() {
				event = Optional.empty();
			}
		});
		
		CycleEventHandler.getSingleton().addEvent(player, event.get(), 1);
	}

	@Override
	public void onSequence(CycleEventContainer container) {
		for(NPC npc : NPCHandler.npcs) {
			if(npc == null || npc.isDead) {
				continue;
			}
			if(!NPC_IDS.stream().anyMatch(id -> npc.npcType == id)) {
				continue;
			}
//			if(random.inclusive(100) < 50) {
//				return;
//			}
			if(this.getObject().getHeight() == npc.heightLevel && Math.abs(this.getObject().getX() - npc.absX) <= DISTANCE_PORT && Math.abs(this.getObject().getY() - npc.absY) <= DISTANCE_PORT) {
				if(random.inclusive(100) < 20) {
					return;
				}
				if(this.isAbandoned()) {
					return;
				}
				trap(npc);
			}
		}
	}

	@Override
	public GameItem[] reward() {
		if(!trapped.isPresent()) {
			throw new IllegalStateException("No npc is trapped.");
		}
		
		Optional<BoxTrapData> data = BoxTrapData.getBoxTrapDataByNpcId(trapped.get().npcType);
		
		if(!data.isPresent()) {
			throw new IllegalStateException("Invalid object id.");
		}
		
		return data.get().reward;
	}

	@Override
	public double experience() {
		if(!trapped.isPresent()) {
			throw new IllegalStateException("No npc is trapped.");
		}
		
		Optional<BoxTrapData> data = BoxTrapData.getBoxTrapDataByNpcId(trapped.get().npcType);
		
		if(!data.isPresent()) {
			throw new IllegalStateException("Invalid object id.");
		}
		
		return data.get().experience;
	}

	@Override
	public boolean canClaim(GlobalObject object) {
		if(!trapped.isPresent()) {
			return false;
		}
		
		Optional<BoxTrapData> data = BoxTrapData.getBoxTrapDataByNpcId(trapped.get().npcType);
		
		if(data == null) {
			return false;
		}
		
		return true;
	}

	@Override
	public void setState(TrapState state) {
		if(state.equals(TrapState.PENDING)) {
			throw new IllegalArgumentException("Cannot set trap state back to pending.");
		}
		if(state.equals(TrapState.FALLEN)) {
			Server.getGlobalObjects().remove(getObject());
			Server.getGlobalObjects().remove(getObject().getObjectId(), getObject().getX(), getObject().getY(), getObject().getHeight());
			this.setObject(FAILED_ID);
			Server.getGlobalObjects().add(this.getObject());
		}
		player.sendMessage("Your trap has been triggered by something...");
		super.setState(state);
	}
	
	/**
	 * The enumerated type whose elements represent a set of constants
	 * used for box trapping.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum BoxTrapData {
		FERRET(1505, 27, 115, new GameItem(10092)),
		CHINCHOMPA(2910, 53, 198.25, new GameItem(10033)),
		CARNIVOROUS_CHINCHOMPA(2911, 63, 265, new GameItem(10034)),
		BLACK_CHINCHOMPA(2912, 73, 335, new GameItem (11959));
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<BoxTrapData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(BoxTrapData.class));
		
		/**
		 * The npc id for this box trap.
		 */
		private final int npcId;

		/**
		 * The requirement for this box trap.
		 */
		private final int requirement;

		/**
		 * The experience gained for this box trap.
		 */
		private final double experience;

		/**
		 * The reward obtained for this box trap.
		 */
		private final GameItem[] reward;

		/**
		 * Constructs a new {@link BoxTrapData}.
		 * @param npcId			{@link #npcId}.
		 * @param requirement	{@link #requirement}.
		 * @param experience	{@link #experience}.
		 * @param reward		{@link #reward}.
		 */
		private BoxTrapData(int npcId, int requirement, double experience, GameItem... reward) {
			this.npcId = npcId;
			this.requirement = requirement;
			this.experience = experience;
			this.reward = reward;
		}
		/**
		 * Retrieves a {@link BoxTrapData} enumerator dependant on the specified {@code id}.
		 * @param id	the npc id to return an enumerator from.
		 * @return a {@link BoxTrapData} enumerator wrapped inside an optional, {@link Optional#empty()} otherwise.
		 */
		public static Optional<BoxTrapData> getBoxTrapDataByNpcId(int id) {
			return VALUES.stream().filter(box -> box.npcId == id).findAny();
		}
	}
}
