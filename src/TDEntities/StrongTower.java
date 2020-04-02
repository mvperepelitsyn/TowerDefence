package TDEntities;

public class StrongTower extends Tower{

	public static final int		cost = 6;
	public static final char	graphic = 'T';

	public StrongTower(int xCoord, int yCoord) {
		super(xCoord, yCoord);
		dmgPoints = 70;
		healthPoints = 150;
		rangeOfAttack = 5;
	}
}
