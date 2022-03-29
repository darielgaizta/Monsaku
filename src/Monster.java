import java.util.*;

public class Monster {
	private int id;
	private String name;
	private Stats baseStats;
	private List<ElementType> elementTypes = new ArrayList<ElementType>();
	private List<Move> moves = new ArrayList<Move>();

	/* Constructor: Monster */
	public Monster(int id, String name, double healthPoint, double attack, double defense, double spAttack, double spDefense, double speed, List<ElementType> elementTypes, List<Move> moves) {
		this.id = id;
		this.name = name;
		this.baseStats = this.baseStats = new Stats(healthPoint, attack, defense, spAttack, spDefense, speed);
		this.elementTypes = elementTypes;
		this.moves = moves;
	}

	/* Getter: id */
	public int getId() {
		return this.id;
	}

	/* Getter: name */
	public String getName() {
		return this.name;
	}

	/* Getter: baseStats */
	public Stats getBaseStats() {
		return this.baseStats;
	}

	/* Getter: elementTypes */
	public List<ElementType> getElementTypes() {
		return this.elementTypes;
	}

	/* Getter: moves */
	public List<Move> getMoves() {
		return this.moves;
	}


	/* Method: printElementTypes(), print all element types */
	public void printElementTypes() {
		for (ElementType e : elementTypes) {
			System.out.println("| => "+String.valueOf(e));
		}
	}


	/* Class: Stats */
	static class Stats implements StatusCondition {
		private double maxHealthPoint;
		private double healthPoint;
		private double attack;
		private double defense;
		private double spAttack;
		private double spDefense;
		private double speed;
		private int burnTime;
		private int poisonTime;
		private int sleepTime;
		private int paralyzeTime;

		/* Constructor: Stats */
		public Stats(double healthPoint, double attack, double defense, double spAttack, double spDefense, double speed) {
			this.maxHealthPoint = healthPoint;
			this.healthPoint = healthPoint;
			this.attack = attack;
			this.defense = defense;
			this.spAttack = spAttack;
			this.spDefense = spDefense;
			this.speed = speed;
		}

		/* Getter: maxHealthPoint */
		public double getMaxHealthPoint() {
			return this.maxHealthPoint;
		}

		/* Getter: healthPoint */
		public double getHealthPoint() {
			return this.healthPoint;
		}

		/* Getter: attack */
		public double getAttack() {
			return this.attack;
		}

		/* Getter: defense */
		public double getDefense() {
			return this.defense;
		}

		/* Getter: spAttack */
		public double getSpAttack() {
			return this.spAttack;
		}

		/* Getter: spDefense */
		public double getSpDefense() {
			return this.spDefense;
		}

		/* Getter: speed */
		public double getSpeed() {
			return this.speed;
		}

		/* Getter: burnTime */
		public int getBurnTime() {
			return this.burnTime;
		}

		/* Getter: poisonTime */
		public int getPoisonTime() {
			return this.poisonTime;
		}

		/* Getter: sleepTime */
		public int getSleepTime() {
			return this.sleepTime;
		}

		/* Getter: paralyzeTime */
		public int getParalyzeTime() {
			return this.paralyzeTime;
		}

		/* Setter: healthPoint */
		public void setHealthPoint(double healthPoint) {
			this.healthPoint = healthPoint;
			if (this.healthPoint < 0) {
				this.healthPoint = 0;
			} else if (this.healthPoint > this.maxHealthPoint) {
				this.healthPoint = this.maxHealthPoint;
			}
		}

		/* Setter: attack */
		public void setAttack(double attack) {
			this.attack = attack;
		}

		/* Setter: defense */
		public void setDefense(double defense) {
			this.defense = defense;
		}

		/* Setter: spAttack */
		public void setSpAttack(double spAttack) {
			this.spAttack = spAttack;
		}

		/* Setter: spDefense */
		public void setSpDefense(double spDefense) {
			this.spDefense = spDefense;
		}

		/* Setter: speed */
		public void setSpeed(double speed) {
			this.speed = speed;
		}

		/* Setter: burnTime */
		public void setBurnTime(int burnTime) {
			this.burnTime = burnTime;
		}

		/* Setter: poisonTime */
		public void setPoisonTime(int poisonTime) {
			this.poisonTime = poisonTime;
		}

		/* Setter: sleepTime */
		public void setSleepTime(int sleepTime) {
			this.sleepTime = sleepTime;
		}

		/* Setter: paralyzeTime */
		public void setParalyzeTime(int paralyzeTime) {
			this.paralyzeTime = paralyzeTime;
		}

		@Override
		public void burn(Monster target) {
			double healthPoint = target.getBaseStats().getHealthPoint();
			target.getBaseStats().setHealthPoint(Math.floor(healthPoint - (healthPoint / 8)));
			target.getBaseStats().setBurnTime(target.getBaseStats().getBurnTime() - 1);
			System.out.println(target.getName()+" is burned, remaining burned time: "+String.valueOf(target.getBaseStats().getBurnTime()));
		}

		@Override
		public void poison(Monster target) {
			double healthPoint = target.getBaseStats().getHealthPoint();
			target.getBaseStats().setHealthPoint(Math.floor(healthPoint - (healthPoint / 16)));
			target.getBaseStats().setPoisonTime(target.getBaseStats().getPoisonTime() - 1);
			System.out.println(target.getName()+" is poisoned, remaining poisoned time: "+String.valueOf(target.getBaseStats().getPoisonTime()));
		}

		@Override
		public void sleep(Monster target) {
			target.getBaseStats().setSleepTime(target.getBaseStats().getSleepTime() - 1);
			System.out.println(target.getName()+" is sleeping, remaining sleeping time: "+String.valueOf(target.getBaseStats().getSleepTime()));
		}

		@Override
		public void paralyze(Monster target) {
			target.getBaseStats().setParalyzeTime(target.getBaseStats().getParalyzeTime() - 1);
			if (target.getBaseStats().getParalyzeTime() == 0) {
				target.getBaseStats().setSpeed(target.getBaseStats().getSpeed() * 2);
			}
			System.out.println(target.getName()+" is paralyzed, remaining paralyzed time: "+String.valueOf(target.getBaseStats().getParalyzeTime()));
		}
	}
}