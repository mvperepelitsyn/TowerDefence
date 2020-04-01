package TDEntities;

public class Enemy extends GameEntity {

	static int id = 1;

	@Override
	public int distance(GameEntity tower) {
		if (getyCoord() == tower.getyCoord()) {
			return Math.abs(getxCoord() - tower.getxCoord());

		} else {
			return -1;
		}
	}
}
