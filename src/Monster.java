import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class Monster {
	private String nama;								// monster's name
	private Stats baseStats;							// stats of the monster
	private ElementType<String> elementTypes;			// element types owned by the monster
	private List<Move> moves = new ArrayList<Move>();	// monster's available moves

	// constructor: Monster
	public Monster(String nama, double maxHealthPoint, double attack, double defense, double specialAttack,
		double specialDefense, double speed, Move[] moves, ElementType<String> elementTypes) {
		this.nama = nama;
		this.baseStats = new Stats(maxHealthPoint, attack, defense, specialAttack, specialDefense, speed);
		this.elementTypes = elementTypes;
		for (Move move : moves) {
			this.moves.add(move);
		}
	}

	public String getNama() {
		return this.nama;
	}

	public Stats getStats() {
		return this.baseStats;
	}

	public List<Move> getMoves() {
		return this.moves;
	}

	public ElementType<String> getElementTypes() {
		return this.elementTypes;
	}

	public void printMoves() {
		for (Move move : this.moves) {
			System.out.println("  => "+move.name);
		}
	}

	public void setStatsByFactors(int buffHp, int buffAttack, int buffDefense,
			int buffSpecialAttack, int buffSpecialDefense, int buffSpeed) {
		Stats stats = this.baseStats;
		stats.setFactors(buffHp, buffAttack, buffDefense,
			buffSpecialAttack, buffSpecialDefense, buffSpeed);
		stats.healthPoint *= stats.factors.get(0);
		stats.attack *= stats.factors.get(1);
		stats.defense *= stats.factors.get(2);
		stats.specialAttack *= stats.factors.get(3);
		stats.specialDefense *= stats.factors.get(4);
		stats.speed *= stats.factors.get(5);
	}

	/*
	 * static class Stats
	 * Performs a "has a" from Monster to Stats (Nested Class)
	 * Each Monster must be having their own Stats (baseStats)
	 */
	static class Stats implements StatsBuff, StatusCondition {
		double maxHealthPoint;
		double healthPoint;
		double speed;
		double attack;
		double defense;
		double specialAttack;
		double specialDefense;
		
		List<Double> factors = new ArrayList<Double>();

		boolean conditionActive = false;
		boolean stunned = false;

		int stuns = 0;

		// constructor: Stats
		public Stats(double maxHealthPoint, double attack, double defense, double specialAttack,
			double specialDefense, double speed) {
			this.maxHealthPoint = maxHealthPoint;
			this.healthPoint = maxHealthPoint;
			this.speed = speed;
			this.attack = attack;
			this.defense = defense;
			this.specialAttack = specialAttack;
			this.specialDefense = specialDefense;
		}

		public void printStats() {
			System.out.println("  => HP: "+String.valueOf(this.healthPoint)+"/"+String.valueOf(this.maxHealthPoint));
			System.out.println("  => Attack: "+String.valueOf(this.attack));
			System.out.println("  => Defense: "+String.valueOf(this.defense));
			System.out.println("  => Sp. Attack: "+String.valueOf(this.specialAttack));
			System.out.println("  => Sp. Defense: "+String.valueOf(this.specialDefense));
			System.out.println("  => Speed: "+String.valueOf(this.speed));
		}

		@Override
		public void setFactors(int buffHp, int buffAttack, int buffDefense,
			int buffSpecialAttack, int buffSpecialDefense, int buffSpeed) {
			int[] buffs = {
				buffHp,
				buffAttack,
				buffDefense,
				buffSpecialAttack,
				buffSpecialDefense,
				buffSpeed
			};

			for (int buff : buffs) {
				if (buff == -4) {
					this.factors.add(Math.floor(2/6));
				} else if (buff == -3) {
					this.factors.add(Math.floor(2/5));
				} else if (buff == -2) {
					this.factors.add(Math.floor(2/4));
				} else if (buff == -1) {
					this.factors.add(Math.floor(2/3));
				} else if (buff == 0) {
					this.factors.add((double) 1);
				} else if (buff == 1) {
					this.factors.add(Math.floor(3/2));
				} else if (buff == 2) {
					this.factors.add((double) 2);
				} else if (buff == 3) {
					this.factors.add(Math.floor(5/2));
				} else if (buff == 4) {
					this.factors.add((double) 3);
				}
			}
		}

		@Override
		public void burn() {
			this.healthPoint -= Math.floor(this.maxHealthPoint / 8);
		}

		@Override
		public void poison() {
			this.healthPoint -= Math.floor(this.maxHealthPoint / 16);
		}

		@Override
		public void sleep() {
			setStunned();
			this.stuns = (int) Math.floor(Math.random() * 7) + 1;
		}

		@Override
		public void paralyze() {
			this.speed /= 2;
			if (stun() == 1 && !isStunned()) {
				setStunned();
			}
		}

		@Override
		public boolean isConditionActive() {
			return this.conditionActive;
		}

		@Override
		public void setConditionActive() {
			if (isConditionActive()) {
				this.conditionActive = false;
			} else {
				this.conditionActive = true;
			}
		}

		@Override
		public boolean isStunned() {
			return this.stunned;
		}

		@Override
		public void setStunned() {
			if (isStunned()) {
				this.stunned = false;
			} else {
				this.stunned = true;
			}
		}

		/*
		 * stun()
		 * Returns integer for isStunned.
		 * Will be used in paralyze()
		 */
		static int stun() {
			int isStun[] = {0, 1};
			int freq[] = {25, 75};
			return randomize(isStun, freq, 2);
		}

		/*
		 * static findCeil()
		 * Returns index of the ceiling of x in arr[l..r]
		 */
		static int findCeil(int arr[], int x, int l, int r) {
			int mid;
			while (l < r) {
				mid = l + ((r - 1) >> 1);
				if (x > arr[mid]) {
					l = mid + 1;
				} else {
					r = mid;
				}
			}
			return (arr[l] >= x) ? l : -1;
		}

		/*
		 * static randomize()
		 * Returns the possibility from poss[]
		 * IF possibility = 1, THEN isStunned = true
		 */
		static int randomize(int poss[], int freq[], int n) {
			int prefix[] = new int[n];
			
			// Initialize prefix
			prefix[0] = poss[0];
			for (int i = 1; i < n; i++) {
				prefix[i] = prefix[i - 1] + freq[i];
			}

			// Generate random number from 1 to prefix[n-1]
			int x = ((int)(Math.random()*(323567)) % prefix[n - 1]) + 1;

			int i = findCeil(prefix, x, 0, n-1);
			return poss[i];
		}
	}
}