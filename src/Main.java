import java.util.Scanner;
import java.lang.Thread;

public class Main {
	public static void main(String[] args) {
		Configuration movePool = new Configuration("config/MovePool.csv");
		Configuration monsterPool = new Configuration("config/MonsterPool.csv");
		Configuration elementTypesPool = new Configuration("config/ElementTypesEffectivity.csv");

		String turn;	// Home's or Away's
		String home;	// Player 1
		String away;	// Player 2

		boolean playing = true;
		Scanner scanner = new Scanner(System.in);

		while (playing) {
			showMenu();
			int q = scanner.nextInt();
			if (q == 0) {
				playing = false;
			} else if (q == 1) {
				try {
					System.out.print("\nMonster Saku starts in ");
					for (int i = 3; i > 0; i--) {
						System.out.print(i);
						loadingEffect();
						Thread.sleep(800);
					}
					System.out.println("");
				} catch (Exception e) {
					System.out.println("Game has failed to run, pleas contact our customer support.");
					System.out.println(e);
					break;
				}

				System.out.println("=======================");
				System.out.println("WELCOME TO MONSTER SAKU");
				System.out.println("=======================");

				try {
					System.out.print("Loading configuration");
					loadingEffect();
					System.out.print("\nGenerating monsters");
					loadingEffect();
					System.out.print("\nFetching data");
					loadingEffect();
					System.out.print("\nDONE\n\n");
				} catch (Exception e) {
					System.out.println("Error occured:"+String.valueOf(e));
				}

				System.out.print("Home Team: ");
				home = scanner.next();
				System.out.print("Away Team: ");
				away = scanner.next();

				System.out.println("\n>----["+home+" vs "+away+"]----<\n");

				// pass
				System.out.println("Game is not ready yet :)\nDEBUG:");
				break;

			} else if (q == 2) {
				System.out.println("\"Caught up in Heaven but your Heaven ain't the same. And I've never been a saint, have I?. This evanescence, always fleeting like a flame. But I'm never one to change, am I?. Call it a lesson when I feel you slide away. And I'm missing out on half my life. Oh, you make me complete. You make me complete. You make me a complete mess. Oh, you make me complete. You make me complete. You make me a complete mess. Hang on to moments like they'll never drift away. 'Cause you'll never get to say goodbye. I ask no questions as your colors take their hold. As my darkness turns to gold inside. I learned my lesson when I felt you slip away. And I'm missing out on half my life. Oh, you make me complete. You make me complete. You make me a complete mess. Oh, you make me complete. You make me complete. You make me a complete mess. Caught up in Heaven but your Heaven ain't the same. But I've never been a saint, have I?. Oh, you make me complete. You make me complete (you make me complete). You make me a complete mess. Oh, you make me complete (you make me complete). You make me complete (make me complete). You make me a complete mess.\"");
			} else {
				System.out.println("Error: Wrong input.");
			}
		}
	}
	/*
	 * showMenu()
	 * Show menu of the game.
	 */
	public static void showMenu() {
		System.out.println("\n");
		System.out.println("[1] Start Game");
		System.out.println("[2] Help");
		System.out.println("[0] Exit");
		System.out.println("--------");
		System.out.print(">>> ");
	}
	/*
	 * viewMonsterInfo()
	 * Show monster's attribute.
	 */
	public static void viewMonsterInfo(Monster monster) {
		System.out.println("Name: "+monster.getNama());
		System.out.println("Stat:");
		monster.getStats().printStats();
		System.out.println("Elmt:");
		System.out.println(monster.getElementTypes());
		System.out.println("Move:");
		monster.printMoves();
	}
	/*
	 * viewGameInfo()
	 * Show game's information.
	 */
	public static void viewGameInfo(Monster home, Monster away,
		Monster[] homeSubsMonster, Monster[] awaySubsMonster) {
		System.out.println(">-----# GAME HAS STARTED #-----<");
		System.out.println("| "+home.getNama()+" vs "+away.getNama());
		System.out.println("| Substitutes from Home team:");
		for (Monster monster : homeSubsMonster) {
			System.out.println("| - "+monster.getNama());
		}
		System.out.println("| Substitutes from Away team:");
		for (Monster monster : awaySubsMonster) {
			System.out.println("| - "+monster.getNama());
		}
		System.out.println("| ===========================");
		System.out.println("| "+home.getNama()+"'s turn.");
	}
	/*
	 * loadingEffect()
	 * Show loading effect.
	 */
	public static void loadingEffect() {
		try {
			for(int j = 0; j < 2; j++) {
				System.out.print(".");
				Thread.sleep(400);
			}
			System.out.print(".");
		} catch (Exception e) {
			System.out.println("Error occured:"+String.valueOf(e));
		}
	}
}