package TDEntities;

public class SkeletonArcher extends Enemy {

	private static final char 	graphic = 's';

	public SkeletonArcher(int healthPoints, int dmgPoints, int speed, int xCoord, int yCoord, int rangeOfAttack) {
		super(healthPoints, dmgPoints, speed, xCoord, yCoord, rangeOfAttack);
	}

	public char getGraphic() {
		return graphic;
	}
}
