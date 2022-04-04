import java.lang.reflect.Constructor;
import java.lang.*;
import java.util.*;
import config.*;

public abstract class Controller {

	/* Method: showMenu(), show the game menu. */
	public static void showMenu() {
		System.out.println("\n");
		System.out.println("[1] Start Game");
		System.out.println("[2] About");
		System.out.println("[0] Exit");
		System.out.println("--------");
		System.out.print(">>> ");
	}

	/* Method: showHelp(), show the command help. */
	public static void showHelp() {
		System.out.println("");
		System.out.println("-[0] Help");
		System.out.println("-[1] Move");
		System.out.println("-[2] Switch Monster");
		System.out.println("-[3] View Monster Info");
		System.out.println("-[4] View Game Info");
		System.out.println("-[-] Exit");
	}

	/* Method: viewMonsterInfo(), view information of the monster. */
	public static void viewMonsterInfo(Monster monster) {
		System.out.println("----------------------------");
		System.out.println("| Name: "+monster.getName());
		System.out.println("| Stat:");
		monster.printBaseStats();
		System.out.println("| Elmt:");
		monster.printElementTypes();
		System.out.println("| Move:");
		monster.printMoves();
		System.out.println("----------------------------");
	}

	/* Method: viewGameInfo(), view information of the game. */
	public static void viewGameInfo(Monster monst1, Monster monst2, List<Monster> subsMonst1, List<Monster> subsMonst2) {
		System.out.println("");
		System.out.println(">-----------# GAME HAS STARTED #-----------<");
		System.out.println("| (Playing) "+monst1.getName()+" vs "+monst2.getName()+" (Against)");
		System.out.println("| Monsters from Player 1:");
		for (Monster monster : subsMonst1) {
			if (monster.getBaseStats().getHealthPoint() > 0) {
				System.out.print("| - (Live) ");
			} else {
				System.out.print("| - (Dead) ");
			}
			System.out.println(monster.getName());
		}
		System.out.println("| ==========================================");
		System.out.println("| Monsters from Player 2:");
		for (Monster monster : subsMonst2) {
			if (monster.getBaseStats().getHealthPoint() > 0) {
				System.out.print("| - (Live) ");
			} else {
				System.out.print("| - (Dead) ");
			}
			System.out.println(monster.getName());
		}
		System.out.println("| ==========================================");
		System.out.println("| "+monst1.getName()+"'s turn.");
	}

	/* Method: loadingEffect(), show loading effect ala-ala. */
	public static void loadingEffect() {
		try {
			for (int j = 0; j < 2; j++) {
				System.out.print(".");
				Thread.sleep(300);
			}
			System.out.print(".");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/* Method: findCeil(), find index of the ceiling of x in arr[l..r] */
	public static int findCeil(int[] arr, int x, int l, int r) {
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

	/* Method: randomizeSleepPossibility(), generate possibility to sleep */
	public static int randomizeSleepPossibility(int[] poss, int[] freq, int n) {
		int[] prefix = new int[n];

		prefix[0] = poss[0];
		for (int i = 1; i < n; i++) {
			prefix[i] = prefix[i-1] + freq[1];
		}
		int x = ((int)(Math.random()*(323567)) % prefix[n - 1]) + 1;
		int i = findCeil(prefix, x, 0, n-1);
		return poss[i];
	}

	/* Method: getSleepPossibility(), get sleep possibility. */
	public static int getSleepPossibility() {
		int[] possibilities = {1, 0};
		int[] frequencies = {25, 75};
		return randomizeSleepPossibility(possibilities, frequencies, 2);
	}

	/* Method: loadMove(), load moves from data configuration (move pool). */
	public static <T> List<T> loadMove(Class<T> moveClass, Configuration movePool) {
		if (moveClass == null) {
			System.out.println("[Error] Class not found.");
			return null;
		}

		T move;
		List<T> moves = new ArrayList<T>();
		Object[] stat = new Object[15];

		try {
			for (int i = 0; i < movePool.getConfig().size(); i++) {
				Class c = Class.forName("Move");
				Constructor[] cons = c.getConstructors();
				Constructor useCon = cons[0];
				if (movePool.getConfig().get(i)[1].equals("STATUS")) {
					useCon = cons[1];
				}
				Object[] arr = new Object[9];
				arr[0] = Integer.valueOf(movePool.getConfig().get(i)[0]);		// id
				arr[1] = movePool.getConfig().get(i)[1];						// moveType
				arr[2] = movePool.getConfig().get(i)[2];						// name
				arr[3] = ElementType.valueOf(movePool.getConfig().get(i)[3]);	// elementType
				arr[4] = Integer.valueOf(movePool.getConfig().get(i)[4]);		// accuracy
				arr[5] = Integer.valueOf(movePool.getConfig().get(i)[5]);		// priority
				arr[6] = Integer.valueOf(movePool.getConfig().get(i)[6]);		// ammunition
				arr[7] = movePool.getConfig().get(i)[7];						// target
				if (!arr[1].equals("STATUS")) {
					arr[8] = Integer.valueOf(movePool.getConfig().get(i)[8]);	// basePower
				}
				Object prototype;
				if (arr[1].equals("STATUS")) {
					for (int j = 0; j < 8; j++) {
						stat[j] = arr[j];
					}
					String[] buffStats = movePool.getConfig().get(i)[9].split(",");
					stat[8] = movePool.getConfig().get(i)[8];	// status condition
					stat[9] = Integer.valueOf(buffStats[0]);	// healthPointEffect
					stat[10] = Integer.valueOf(buffStats[1]);	// attackEffect
					stat[11] = Integer.valueOf(buffStats[2]);	// defenseEffect
					stat[12] = Integer.valueOf(buffStats[3]);	// spAttackEffect
					stat[13] = Integer.valueOf(buffStats[4]);	// spDefenseEffect
					stat[14] = Integer.valueOf(buffStats[5]);	// speedEffect
					prototype = useCon.newInstance(stat);
				} else {
					prototype = useCon.newInstance(arr);
				}
				move = (T) prototype;
				moves.add(move);
			}
		} catch (Exception e) {
			System.out.println("Failed to load move.");
			e.printStackTrace();
		}
		return moves;
	}

	/* Method: loadMonster(), load all mosnters from data configuration (monster pool). */
	public static <T> List<T> loadMonster(Class<T> monsterClass, Configuration monsterPool, Configuration movePool) {
		if (monsterClass == null) {
			System.out.println("[Error] Class not found.");
			return null;
		}

		String[] monstData;
		String[] baseStats;
		String[] elementTypes;
		String[] moves;
		Move[] moveSet;
		
		T monster;
		List<T> monstersList = new ArrayList<T>();
		List<Move> movesList = loadMove(Move.class, movePool);

		for (int i = 0; i < monsterPool.getConfig().size(); i++) {
			try {
				Class c = Class.forName("Monster");
				Constructor useCon = c.getConstructors()[0];
				monstData = monsterPool.getConfig().get(i);
				elementTypes = monstData[2].split(",");
				baseStats = monstData[3].split(",");
				moves = monstData[4].split(",");
				moveSet = new Move[moves.length];
				int counter = 0;
				for (Move move : movesList) {
					if (move.getId() == Integer.valueOf(moves[counter])) {
						moveSet[counter] = move;
						counter++;
					}
					if (counter == moves.length) {
						break;
					}
				}
				List<Move> m = new ArrayList<Move>();
				m.add(new DefaultMove());
				for (Move move : moveSet) {
					m.add(move);
				}
				List<ElementType> e = new ArrayList<ElementType>();
				for (String s : elementTypes) {
					e.add(ElementType.valueOf(s));
				}
				Object[] arr = new Object[10];
				arr[0] = Integer.valueOf(monstData[0]);	// id
				arr[1] = monstData[1];					// nama
				arr[2] = Double.valueOf(baseStats[0]);	// healthPoint
				arr[3] = Double.valueOf(baseStats[1]);	// attack
				arr[4] = Double.valueOf(baseStats[2]);	// defense
				arr[5] = Double.valueOf(baseStats[3]);	// specialAttack
				arr[6] = Double.valueOf(baseStats[4]);	// specialDefense
				arr[7] = Double.valueOf(baseStats[5]);	// speed
				arr[8] = e;								// List<ElementType>
				arr[9] = m;								// List<Move>
				Object prototype = useCon.newInstance(arr);
				monster = (T) prototype;
				monstersList.add(monster);
			} catch (Exception e) {
				System.out.println("Failed to create monster.");
				e.printStackTrace();

			}
		}
		return monstersList;
	}

	/* Method: shuffleMonster(), divide a list of monsters into two. */
	public static List<List<Monster>> shuffleMonster(List<Monster> monsters) {
		int len = monsters.size();
		int tmp = (int) Math.floor((len+1)/2);
		int idx = 0;
		List<List<Monster>> retval = new ArrayList<List<Monster>>();
		List<Monster> m1 = new ArrayList<Monster>();
		List<Monster> m2 = new ArrayList<Monster>();

		for (int i = 0; i < len; i++) {
			if (idx < tmp) {
				m1.add(monsters.get(i));
			} else {
				m2.add(monsters.get(i));
			}
			idx++;
		}

		retval.add(m1);
		retval.add(m2);

		return retval;
	}

	/* Method: handleStatusCondition(), get effect from status condition. */
	public static void handleStatusCondition(Monster playingMonster) {
		if (playingMonster.getBaseStats().getBurnTime() > 0) {
			playingMonster.getBaseStats().burn(playingMonster);
			if (playingMonster.getBaseStats().getHealthPoint() == 0) {
				System.out.println(playingMonster.getName()+" is dead because of burned.");
			}
		}
		if (playingMonster.getBaseStats().getPoisonTime() > 0) {
			playingMonster.getBaseStats().poison(playingMonster);
			if (playingMonster.getBaseStats().getHealthPoint() == 0) {
				System.out.println(playingMonster.getName()+" is dead because of poisoned.");
			}
		}
		if (playingMonster.getBaseStats().getSleepTime() > 0) {
			playingMonster.getBaseStats().sleep(playingMonster);
		}
	}

	/* Method: generateMonster(), get available monster. */
	public Monster generateMonster(Player player) {
		for (Monster m : player.getMonsters()) {
			if (m.getBaseStats().getHealthPoint() != 0) {
				return m;
			}
		}
		return null;
	}
}