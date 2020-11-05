package ethos.clip;

import ethos.model.npcs.NPCClipping;

public class PathChecker {

	public static boolean isProjectilePathClear(final int x0, final int y0,
			final int z, final int x1, final int y1) {
		int deltaX = x1 - x0;
		int deltaY = y1 - y0;

		double error = 0;
		final double deltaError = Math.abs(
				(deltaY) / (deltaX == 0
						? ((double) deltaY)
						: ((double) deltaX)));

		int x = x0;
		int y = y0;

		int pX = x;
		int pY = y;

		boolean incrX = x0 < x1;
		boolean incrY = y0 < y1;

		while (true) {
			if (x != x1) {
				x += (incrX ? 1 : -1);
			}

			if (y != y1) {
				error += deltaError;

				if (error >= 0.5) {
					y += (incrY ? 1 : -1);
					error -= 1;
				}
			}

			if (!shootable(x, y, z, pX, pY)) {
				return false;
			}

			if (incrX && incrY
					&& x >= x1 && y >= y1) {
				break;
			} else if (!incrX && !incrY
					&& x <= x1 && y <= y1) {
				break;
			} else if (!incrX && incrY
					&& x <= x1 && y >= y1) {
				break;
			} else if (incrX && !incrY
					&& x >= x1 && y <= y1) {
				break;
			}

			pX = x;
			pY = y;
		}

		return true;
	}

	public static boolean isMeleePathClear(final int x0, final int y0,
			final int z, final int x1, final int y1) {
		int deltaX = x1 - x0;
		int deltaY = y1 - y0;

		double error = 0;
		final double deltaError = Math.abs(
				(deltaY) / (deltaX == 0
						? ((double) deltaY)
						: ((double) deltaX)));

		int x = x0;
		int y = y0;

		int pX = x;
		int pY = y;

		boolean incrX = x0 < x1;
		boolean incrY = y0 < y1;

		while (true) {
			if (x != x1) {
				x += (incrX ? 1 : -1);
			}

			if (y != y1) {
				error += deltaError;

				if (error >= 0.5) {
					y += (incrY ? 1 : -1);
					error -= 1;
				}
			}

			if (!canAttackOver(x, y, z, pX, pY)) {
				return false;
			}

			if (incrX && incrY
					&& x >= x1 && y >= y1) {
				break;
			} else if (!incrX && !incrY
					&& x <= x1 && y <= y1) {
				break;
			} else if (!incrX && incrY
					&& x <= x1 && y >= y1) {
				break;
			} else if (incrX && !incrY
					&& x >= x1 && y <= y1) {
				break;
			}

			pX = x;
			pY = y;
		}

		return true;
	}

	private static boolean canAttackOver(int x, int y, int z, int pX, int pY) {
		if (x == pX && y == pY) {
			return true;
		}

		int dir = NPCClipping.getDirection(x, y, pX, pY);
		int dir2 = NPCClipping.getDirection(pX, pY, x, y);

		if (dir == -1 || dir2 == -1) {
			System.out.println("NEGATIVE DIRECTION MELEE CLIP CHECK ERROR");
			return false;
		}
		
		return Region.canMove(x, y, z, dir)
				&& Region.canMove(pX, pY, z, dir2);
	}

	private static boolean shootable(int x, int y, int z, int pX, int pY) {
		if (x == pX && y == pY) {
			return true;
		}

		int dir = NPCClipping.getDirection(x, y, pX, pY);
		int dir2 = NPCClipping.getDirection(pX, pY, x, y);

		if (dir == -1 || dir2 == -1) {
			System.out.println("NEGATIVE DIRECTION PROJECTILE ERROR");
			return false;
		}

		if (Region.canMove(x, y, z, dir)
				&& Region.canMove(pX, pY, z, dir2)) {
			return true;
		}
		
		return Region.canShoot(x, y, z, dir) && Region.canShoot(pX, pY, z, dir2);
	}
}
