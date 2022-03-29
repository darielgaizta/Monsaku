import java.util.*;
import config.*;

public class Main {
	public static void main(String[] args) {
		Player win = null;
		Player player1;
		Player player2;
		Scanner scanner = new Scanner(System.in);

		Configuration movePool = new Configuration("config/MovePool.csv");
		Configuration monsterPool = new Configuration("config/MonsterPool.csv");
		Configuration elementTypePool = new Configuration("config/ElementTypesEffectivity.csv");

		boolean isGameStarted = true;
		while (isGameStarted) {
			Controller.showMenu();
			try {
				char in = scanner.next().charAt(0);
				if (in == '1') {
					/* Start Game */
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

					/* Variables Initialization */
					char command;
					List<Monster> monsters = Controller.loadMonster(Monster.class, monsterPool, movePool);
					List<List<Monster>> rm = Controller.shuffleMonster(monsters);
					System.out.print("\nDONE\n\n");

					/* Initialize players ----- */
					System.out.print("(PLAYER 1)'S Name: ");
					String p1Name = scanner.next();
					System.out.print("(PLAYER 2)'S Name: ");
					String p2Name = scanner.next();
					player1 = new Player(1, p1Name, rm.get(0));
					player2 = new Player(2, p2Name, rm.get(1));

					/* Gameplay --------------- */
					while (isGameStarted) {

						/* Initialize state --- */
						Monster monster1 = null;
						Monster monster2 = null;
						List<Move> moves = new ArrayList<Move>();
						List<Monster> source = new ArrayList<Monster>();
						List<Monster> target = new ArrayList<Monster>();
						
						/* Initialize monster - */
						for (Monster m1 : player1.getMonsters()) {
							if (m1.getBaseStats().getHealthPoint() != 0) {
								monster1 = m1;
							}
						}
						for (Monster m2 : player2.getMonsters()) {
							if (m2.getBaseStats().getHealthPoint() != 0) {
								monster2 = m2;
							}
						}

						if (monster1 == null) {
							win = player2;
							isGameStarted = false;
							break;
						} else if (monster2 == null) {
							win = player1;
							isGameStarted = false;
							break;
						}

						/* Main loop ---------- */
						int counter = 0;
						while (counter < 2) {
							boolean hasTurn = false;
							Monster monster = null;
							Player player = null;

							/* Selecting turn - */
							if (counter == 0) {
								player = player1;
								monster = monster1;
							} else if (counter == 1) {
								player = player2;
								monster = monster2;
							}

							/* Player's turn -- */
							while (!hasTurn) {
								System.out.print("\n<"+player.getName()+"-["+monster.getName()+"]> ");
								command = scanner.next().charAt(0);
								if (command == '0') {
									System.exit(0);
								} else if (command == '1') {
									// pick a move
									monster.selectMove();
									System.out.print("---> ");
									String m = scanner.next();
									try {
										if (monster.getMoves().get(Integer.valueOf(m)-1).getAmmunition() == 0) {
											System.out.println("Ammunition is empty, choose another move.");
										} else {
											Move move = monster.getMoves().get(Integer.valueOf(m)-1);
											moves.add(move);
											if (monster == monster1) {
												source.add(monster1);
												target.add(monster2);
											} else if (monster == monster2) {
												source.add(monster2);
												target.add(monster1);
											}
											counter++;
											hasTurn = true;
										}
									} catch (Exception e) {
										System.out.println("[Error] Wrong input, select an integer from the square bracket.");
										e.printStackTrace();
									}
								} else if (command == '2') {
									// switch monster
									player.printAvailableMonsters();
									System.out.print("Choose your monster: ");
									try {
										String n = scanner.next();
										if (player.getMonsters().get(Integer.valueOf(n)-1).getBaseStats().getHealthPoint() == 0) {
											System.out.println("Can't switch to dead monster.");
										} else {
											if (monster == monster1) {
												monster1 = player.getMonsters().get(Integer.valueOf(n)-1);
												System.out.println("You have switched your playing monster to "+monster1.getName()+".");
											} else if (monster == monster2) {
												monster2 = player.getMonsters().get(Integer.valueOf(n)-1);
												System.out.println("You have switched your playing monster to "+monster2.getName()+".");
											}
											counter++;
											hasTurn = true;
										}
									} catch (Exception e) {
										System.out.println("[Error] Wrong input, select an integer from the square bracket.");
										e.printStackTrace();
									}
								} else if (command == '3') {
									// view monster info
									Controller.viewMonsterInfo(monster);
								} else if (command == '4') {
									// view game info
									Controller.viewGameInfo(monster1, monster2, player1.getMonsters(), player2.getMonsters());
								}
							}
						}

						/* Sort Moves --------- */
						List<Move> sortedMoves = new ArrayList<Move>();
						List<Monster> sortedSource = new ArrayList<Monster>();
						List<Monster> sortedTarget = new ArrayList<Monster>();
						if (moves.size() == 2) {
							if (moves.get(0).getPriority() > moves.get(1).getPriority()) {
								for (int i = 0; i < 2; i++) {
									sortedMoves.add(moves.get(i));
									sortedSource.add(source.get(i));
									sortedTarget.add(target.get(i));
								}
							} else if (moves.get(1).getPriority() > moves.get(0).getPriority()) {
								for (int i = 1; i > -1; i--) {
									sortedMoves.add(moves.get(i));
									sortedSource.add(source.get(i));
									sortedTarget.add(target.get(i));
								}
							} else {
								if (source.get(1).getBaseStats().getSpeed() > source.get(0).getBaseStats().getSpeed()) {
									for (int i = 1; i > -1; i--) {
										sortedMoves.add(moves.get(i));
										sortedSource.add(source.get(i));
										sortedTarget.add(target.get(i));
									}
								} else {
									for (int i = 0; i < 2; i++) {
										sortedMoves.add(moves.get(i));
										sortedSource.add(source.get(i));
										sortedTarget.add(target.get(i));
									}
								}
							}
						} else {
							sortedMoves.add(moves.get(0));
							sortedSource.add(source.get(0));
							sortedTarget.add(target.get(0));
						}

						/* Execute moves ------ */
						for (int i = 0; i < sortedMoves.size(); i++) {
							Move move = sortedMoves.get(i);
							Monster sourceMonster = sortedSource.get(i);
							Monster targetMonster = sortedTarget.get(i);
							// DEBUG
							Controller.loadingEffect();
							System.out.println("\n]==== FIGHT ====[>\n");
							System.out.println("HP "+sourceMonster.getName()+": "+sourceMonster.getBaseStats().getHealthPoint());
							System.out.println("HP "+targetMonster.getName()+": "+targetMonster.getBaseStats().getHealthPoint());
							Controller.handleStatusCondition(sourceMonster);
							// -----
							move.execute(sourceMonster, targetMonster, elementTypePool.getConfig());
							if (targetMonster.getBaseStats().getHealthPoint() == 0) {
								System.out.println("("+sourceMonster.getName()+") Killing spree!");
								System.out.println(targetMonster.getName()+" has been eliminated by "+sourceMonster.getName());
							}
						}
					}
					System.out.println("\n>=== CONGRATULATIONS ===<");
					System.out.println(win.getName());
					System.out.println("Remaining monsters:");
					for (Monster m : win.getMonsters()) {
						if (m.getBaseStats().getHealthPoint() != 0) {
							System.out.println(" - "+m.getName());
						}
					}
				} else if (in == '2') {
					/* About ---- */
					System.out.println("\n(C) Monsaku Inc.");
				} else if (in == '0') {
					/* Exit ----- */
					isGameStarted = false;
				} else {
					System.out.println("[Error] Wrong input, select an integer from the square bracket.");
				}
			} catch (Exception e) {
				System.out.println("[Error] Game has failed to start.");
				isGameStarted = false;
				e.printStackTrace();
			}
		}
	}
}