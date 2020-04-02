package TDEntities;

public class RegularTower extends Tower {

	public static final int		cost = 2;
	public static final char	graphic = 't';

	public RegularTower(int xCoord, int yCoord) {
		super(xCoord, yCoord);
		dmgPoints = 50;
		healthPoints = 100;
		rangeOfAttack = 3;
	}
}
