package TDEntities;

public class StrongTower extends Tower{

	private int					healthPoints = 50;
	private final int			dmgPoints = 100;
	private static final int	id = 4;
	public static final char	graphic = 'T';

	public StrongTower(int xCoord, int yCoord) {
		super(xCoord, yCoord);
	}
}
