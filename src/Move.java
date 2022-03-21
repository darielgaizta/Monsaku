import java.lang.Math;
import java.util.Random;

public abstract class Move {
	int accuracy;
	int priority;
	int basePower;
	int ammunition;

	String name;
	String moveType;

	ElementType elementType;

	// constructor
	public Move(String name, ElementType elementType, String moveType,
		int accuracy, int priority, int basePower, int ammunition) {
		this.name = name;
		this.moveType = moveType;
		this.accuracy = accuracy;
		this.priority = priority;
		this.ammunition = ammunition;
		this.elementType = elementType;
	}

	public double getDamage(double sourceAttack, double targetDefense, double effectivity, double burn) {
		double rint = (new Random().nextInt((int)1.15) + 1);
		return Math.floor((this.basePower * (sourceAttack/targetDefense) + 2) * rint * effectivity * burn);
	}

	public abstract void execute(Monster target);
}