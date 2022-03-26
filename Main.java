import java.util.*;
import config.*;

public class Main {
	public static void main(String[] args) {
		Player winner = null;
		Player player1;
		Player player2;
		Scanner scanner = new Scanner(System.in);
		boolean isGameStarted = true;
		/* Load data configuration */
		Configuration movePool = new Configuration("config/MovePool.csv");
		Configuration monsterPool = new Configuration("config/MonsterPool.csv");
		Configuration elementTypePool = new Configuration("config/ElementTypesEffectivity.csv");
		int input;
		while (isGameStarted) {
			Controller.showMenu();
			while (true) {
				try {
					input = scanner.nextInt();
					break;
				} catch (Exception e) {
					System.out.println("[Error] Wrong input, select an integer from the square bracket ([]).");
					Controller.showMenu();
					scanner.next();
					// TODO: handle exception
				}	
			}
			if (input == 0) {
				System.exit(0);
			} else if (input == 1) {
				/* -- Opening Game ------------------------ */
				System.out.print("\nMonster Saku starts in ");
				for (int i = 3; i > 0; i--) {
					System.out.print(i);
					Controller.loadingEffect();
				}
				System.out.println("");
				System.out.println("=======================");
				System.out.println("WELCOME TO MONSTER SAKU");
				System.out.println("=======================");
				System.out.print("Loading configuration");
				Controller.loadingEffect();
				System.out.print("\nGenerating monsters");
				Controller.loadingEffect();
				System.out.print("\nFetching data");
				Controller.loadingEffect();

				/* -- Variable Initializations ------------ */
				char command;
				List<Monster> monsters = Controller.loadMonster(Monster.class, monsterPool, movePool);
				List<List<Monster>> rm = Controller.shuffleMonster(monsters);
				System.out.print("\nDONE\n\n");

				/* -- Initialize player (team)'s name ------- */
				System.out.print("(PLAYER 1)'S Name: ");
				String p1Name = scanner.next();
				System.out.print("(PLAYER 2)'S Name: ");
				String p2Name = scanner.next();
				player1 = new Player(1, p1Name, rm.get(0));
				player2 = new Player(2, p2Name, rm.get(1));

				/* -- Initialize game's state --------------- */
				Player playing = player1;
				Player against = player2;
				Monster playingMonster = playing.getMonsters().get(0);
				Monster againstMonster = against.getMonsters().get(0);
				int playingAlive = playing.getMonsters().size();
				int againstAlive = against.getMonsters().size();

				/* -- Gameplay ------------------------------ */
				while (isGameStarted) {

					/* -- StatusCondition handling ---------- */
					if (playingMonster.getBaseStats().getBurnTime() > 0) {
						playingMonster.getBaseStats().burn(playingMonster);
						if (playingMonster.getBaseStats().getHealthPoint() == 0) {
							System.out.println(playingMonster.getName()+" is dead because of burned.");
							playingAlive--;
						}
					}
					if (playingMonster.getBaseStats().getPoisonTime() > 0) {
						playingMonster.getBaseStats().poison(playingMonster);
						if (playingMonster.getBaseStats().getHealthPoint() == 0) {
							System.out.println(playingMonster.getName()+" is dead because of poisoned.");
							playingAlive--;
						}
					}
					if (playingMonster.getBaseStats().getSleepTime() > 0) {
						playingMonster.getBaseStats().sleep(playingMonster);
						if (playingMonster.getBaseStats().getHealthPoint() == 0) {
							System.out.println(playingMonster.getName()+" is dead because of sleeping.");
							playingAlive--;
						}
					}
					if (playingMonster.getBaseStats().getParalyzeTime() > 0) {
						playingMonster.getBaseStats().paralyze(playingMonster);
						if (playingMonster.getBaseStats().getHealthPoint() == 0) {
							System.out.println(playingMonster.getName()+" is dead because of paralyzed.");
							playingAlive--;
						}
					}

					/* -- Main loop ------------------------- */
					System.out.print("\n<"+playing.getName()+"-["+playingMonster.getName()+"]> ");
					command = scanner.next().charAt(0);
					if (command == '-') {
						/* -- Exit -------------------------- */
						System.exit(0);

					} else if (command == '0') {
						/* -- Help -------------------------- */
						Controller.showHelp();

					} else if (command == '1') {
						/* -- Move -------------------------- */
						if (playingMonster.getBaseStats().getHealthPoint() == 0) {
							System.out.println("This monster is dead.");
							System.out.println("Please switch to another monster ([2] Switch).");
						} else {
							playingMonster.selectMove();
							System.out.print("---> ");
							String m = scanner.next();
							if (m.charAt(0) == '-') {
								// pass
							} else {
								try {
									Move move = playingMonster.getMoves().get(Integer.valueOf(m)-1);
									move.execute(playingMonster, againstMonster, elementTypePool.getConfig());
									Monster currentMonster = playingMonster;
									if (againstMonster.getBaseStats().getHealthPoint() == 0) {
										System.out.println("("+playingMonster.getName()+") Killing spree!");
										System.out.println(againstMonster.getName()+" has been eliminated by "+playingMonster.getName());
										againstAlive--;
										System.out.println("\n("+playing.getName()+")'s has "+String.valueOf(playingAlive)+" monsters left.");
										System.out.println("("+against.getName()+")'s has "+String.valueOf(againstAlive)+" monsters left.");
									}
									if (playing.getId() == player1.getId()) {
										playing = player2;
										against = player1;
									} else {
										playing = player1;
										against = player2;
									}
									playingMonster = againstMonster;
									againstMonster = currentMonster;
									int switcher = playingAlive;
									playingAlive = againstAlive;
									againstAlive = switcher;
								} catch (Exception e) {
									System.out.println("[Error] Wrong input, select an integer from the square bracket ([]).");
									// e.printStackTrace();
								}
							}
						}

					} else if (command == '2') {
						/* -- Switch monster ---------------- */
						playing.printAvailableMonsters();
						System.out.print("Choose your monster: ");
						try {
							String n = scanner.next();
							playingMonster = playing.getMonsters().get(Integer.valueOf(n)-1);
							System.out.println("You have switched your playing monster to "+playingMonster.getName()+".");
						} catch (Exception e) {
							System.out.println("[Error] Wrong input, select an integer from the square bracket ([]).");
							// e.printStackTrace();
						}

					} else if (command == '3') {
						/* -- View monster info ------------- */
						Controller.viewMonsterInfo(playingMonster);

					} else if (command == '4') {
						/* -- View game info ---------------- */
						Controller.viewGameInfo(playingMonster, againstMonster, playing.getMonsters(), against.getMonsters());

					} else {
						System.out.println("[Error] Wrong input, type [0] Help.");
					}

					/* -- End game -------------------------- */
					if (playingAlive == 0) {
						winner = against;
						isGameStarted = false;
					} else if (againstAlive == 0) {
						winner = playing;
						isGameStarted = false;
					}
				}

				/* ------------------------------------------ */
			} else if (input == 2) {
				System.out.println("\n(C) Monsaku Inc.");
			} else {
				System.out.println("[Error] Wrong input, select an integer from the square bracket ([]).");
			}
			if (!isGameStarted) {
				System.out.println("\n>-----[ CONGRATULATIONS ]-----<\n");
				System.out.println("Winner: "+winner.getName());
				System.out.println("Remaining monsters:");
				for (Monster m : winner.getMonsters()) {
					if (m.getBaseStats().getHealthPoint() != 0) {
						System.out.println(" - "+m.getName());
					}
				}
			}
		}
	}
}