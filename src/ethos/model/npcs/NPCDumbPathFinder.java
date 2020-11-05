package ethos.model.npcs;

import ethos.clip.Region;
import ethos.clip.Tile;
import ethos.clip.TileControl;
import ethos.model.entity.Entity;
import ethos.model.players.Player;
import ethos.util.Misc;

public class NPCDumbPathFinder {

private static final int NORTH = 0, EAST = 1,  SOUTH = 2, WEST = 3;
	
	public static void follow(NPC npc, Entity following) {

		Tile[] npcTiles = TileControl.getTiles(npc);
		
		int[] npcLocation = TileControl.currentLocation(npc);
		int[] followingLocation = TileControl.currentLocation(following);
	
		/** test 4 movements **/
		boolean[] moves = new boolean[4];
		
		int dir = -1;
		
		int distance = TileControl.calculateDistance(npc, following);
		
		if (distance > 16) {
			return;
		}
		
		npc.facePlayer(((Player) following).getIndex());
		
		if (npc.freezeTimer > 0) {
			return;
		}
		
		if (distance > 1) {
			
			for (int i = 0; i < moves.length; i++) {
				moves[i] = true;
			}
			
			/** remove false moves **/
			if (npcLocation[0] < followingLocation[0]) {
				moves[EAST] = true;	
				moves[WEST] = false;
			} else if (npcLocation[0] > followingLocation[0]) {
				moves[WEST] = true;	
				moves[EAST] = false;	
			} else {
				moves[EAST] = false;	
				moves[WEST] = false;
			}
			if (npcLocation[1] > followingLocation[1]) {
				moves[SOUTH] = true;
				moves[NORTH] = false;
			} else if (npcLocation[1] < followingLocation[1]) {
				moves[NORTH] = true;	
				moves[SOUTH] = false;
			} else {
				moves[NORTH] = false;	
				moves[SOUTH] = false;
			}	
			for (Tile tiles : npcTiles) {
				if (tiles.getTile()[0] == following.getX()) { //same x line
					moves[EAST] = false;
					moves[WEST] = false;
				} else if (tiles.getTile()[1] == following.getY()) { //same y line
					moves[NORTH] = false;
					moves[SOUTH] = false;
				}
			}
			boolean[] blocked = new boolean[3];
			
			if (moves[NORTH] && moves[EAST]) {
				for (Tile tiles : npcTiles) {
					if (Region.blockedNorth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						blocked[0] = true;
					}
					if (Region.blockedEast(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						blocked[1] = true;
					}
					if (Region.blockedNorthEast(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						blocked[2] = true;
					}
				}
				if (!blocked[2] && !blocked[0] && !blocked[1]) {  //northeast
					dir = 2;
				} else if (!blocked[0]) { //north
					dir = 0;
				} else if (!blocked[1]) { //east
					dir = 4;
				}	
				
			} else if (moves[NORTH] && moves[WEST]) {
				for (Tile tiles : npcTiles) {
					if (Region.blockedNorth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						blocked[0] = true;
					}
					if (Region.blockedWest(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						blocked[1] = true;
					}
					if (Region.blockedNorthWest(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						blocked[2] = true;
					}
				}
				if (!blocked[2] && !blocked[0] && !blocked[1]) { //north-west
					dir = 14;
				} else if (!blocked[0]) { //north
					dir = 0;
				} else if (!blocked[1]) { //west
					dir = 12;
				}	
			} else if (moves[SOUTH] && moves[EAST]) {
				for (Tile tiles : npcTiles) {
					if (Region.blockedSouth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						blocked[0] = true;
					}
					if (Region.blockedEast(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						blocked[1] = true;
					}
					if (Region.blockedSouthEast(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						blocked[2] = true;
					}
				}
				if (!blocked[2] && !blocked[0] && !blocked[1]) { //south-east
					dir = 6; 
				} else if (!blocked[0]) { //south
					dir = 8;
				} else if (!blocked[1]) { //east
					dir = 4;
				}	
			} else if (moves[SOUTH] && moves[WEST]) {
				for (Tile tiles : npcTiles) {
					if (Region.blockedSouth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						blocked[0] = true;
					}
					if (Region.blockedWest(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						blocked[1] = true;
					}
					if (Region.blockedSouthWest(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						blocked[2] = true;
					}
				}
				if (!blocked[2] && !blocked[0] && !blocked[1]) { //south-west
					dir = 10; 
				} else if (!blocked[0]) { //south
					dir = 8;
				} else if (!blocked[1]) { //west
					dir = 12;
				}	
				
			} else if (moves[NORTH]) {
				dir = 0;
				for (Tile tiles : npcTiles) {
					if (Region.blockedNorth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						dir = -1;
					}
				}
			} else if (moves[EAST]) {
				dir = 4;
				for (Tile tiles : npcTiles) {
					if (Region.blockedEast(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						dir = -1;
					}
				}
			} else if (moves[SOUTH]) {
				dir = 8;
				for (Tile tiles : npcTiles) {
					if (Region.blockedSouth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						dir = -1;
					}
				}
			} else if (moves[WEST]) {
				dir = 12;
				for (Tile tiles : npcTiles) {
					if (Region.blockedWest(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						dir = -1;
					}
				}
			}
		} else if (distance == 0) {
			for (int i = 0; i < moves.length; i++) {
				moves[i] = true;
			}
			for (Tile tiles : npcTiles) {
				
				if (Region.blockedNorth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
					moves[NORTH] = false;
				}
				if (Region.blockedEast(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
					moves[EAST] = false;
				}
				if (Region.blockedSouth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
					moves[SOUTH] = false;
				}
				if (Region.blockedWest(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
					moves[WEST] = false;
				}
			}
			int randomSelection = Misc.random(3);
			
			if (moves[randomSelection]) {
				dir = randomSelection * 4;
			} else if (moves[NORTH]) {
				dir = 0;
			} else if (moves[EAST]) {
				dir = 4;
			} else if (moves[SOUTH])	{
				dir = 8;
			} else if (moves[WEST]) {	
				dir = 12;
			}
		}
		
		if (dir == -1) {
			return;
		}
		
		dir >>= 1;	
			
		if (dir < 0) {
			return;
		}
		
		npc.moveX = Misc.directionDeltaX[dir];
		npc.moveY = Misc.directionDeltaY[dir];
		
		npc.getNextNPCMovement();
		npc.updateRequired = true;
	}

	public static void walkTowards(NPC npc, int waypointx, int waypointy) {
		int x = npc.getX();
		int y = npc.getY();
		
		if (waypointx == x && waypointy == y) {
			return;
		}
		
		int direction = -1;
		final int xDifference = waypointx - x;
		final int yDifference = waypointy - y;

		int toX = 0;
		int toY = 0;

		if (xDifference > 0) {
			toX = 1;
		} else if (xDifference < 0) {
			toX = -1;
		}

		if (yDifference > 0) {
			toY = 1;
		} else if (yDifference < 0) {
			toY = -1;
		}

		int toDir = NPCClipping.getDirection(x, y, x + toX, y + toY);
			if (canMoveTo(npc, toDir)) {
				direction = toDir;
			} else {
				if (toDir == 0) {
					if (canMoveTo(npc, 3)) {
						direction = 3;
					} else if (canMoveTo(npc, 1)) {
						direction = 1;
					}
				} else if (toDir == 2) {
					if (canMoveTo(npc, 1)) {
						direction = 1;
					} else if (canMoveTo(npc, 4)) {
						direction = 4;
					}
				} else if (toDir == 5) {
					if (canMoveTo(npc, 3)) {
						direction = 3;
					} else if (canMoveTo(npc, 6)) {
						direction = 6;
					}
				} else if (toDir == 7) {
					if (canMoveTo(npc, 4)) {
						direction = 4;
					} else if (canMoveTo(npc, 6)) {
						direction = 6;
					}
				}
			}

		if (direction == -1) {
			return;
		}
		
		if (direction == -1) {
			return;
		}
		/*direction >>= 1;	
			
		if (direction < 0) {
			return;
		}*/
		//if (npc.moveX != 0 || npc.moveY != 0)
			//throw new IllegalStateException("walking multiple times.");
		/*try {
			throw new Exception();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		npc.moveX = NPCClipping.DIR[direction][0];
		npc.moveY = NPCClipping.DIR[direction][1];
		//npc.moveY = Server.npcHandler.GetMove(npc.getY(), npcLocation[1] + movey);
		npc.getNextNPCMovement();
		npc.updateRequired = true;
	}
	
	@SuppressWarnings("static-access")
	public static boolean canMoveTo(final NPC mob, final int direction) {
		if (direction == -1) {
			return false;
		}
		
		final int x = mob.getX();
		final int y = mob.getY();
		final int z = mob.heightLevel > 3 ? mob.heightLevel % 4 : mob.heightLevel;

		final int x5 = mob.getX() + NPCClipping.DIR[direction][0];
		final int y5 = mob.getY() + NPCClipping.DIR[direction][1];

		final int size = mob.getSize();

		for (int i = 1; i < size + 1; i++) {
			for (int k = 0; k < NPCClipping.SIZES[i].length; k++) {
				int x3 = x + NPCClipping.SIZES[i][k][0];
				int y3 = y + NPCClipping.SIZES[i][k][1];

				int x2 = x5 + NPCClipping.SIZES[i][k][0];
				int y2 = y5 + NPCClipping.SIZES[i][k][1];

				if (NPCClipping.withinBlock(x, y, size, x2, y2)) {
					continue;
				}

				Region region = Region.getRegion(x3, y3);
				if (region == null)
					return false;
				
				if (!Region.getRegion(x3, y3).canMove(x3, y3, z, direction)) {
					return false;
				}

				/*if (!virtual) {
					if (Region.getRegion(x2, y2).isNpcOnTile(x2, y2, z))
						return false;
				} else {
					if (region.isMobOnTile(x2, y2, z))
						return false;
				}*/

				for (int j = 0; j < 8; j++) {
					int x6 = x3 + NPCClipping.DIR[j][0];
					int y6 = y3 + NPCClipping.DIR[j][1];

					if (NPCClipping.withinBlock(x5, y5, size, x6, y6)) {
						if (!Region.getRegion(x3, y3).canMove(x3, y3, z, j)) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	

}