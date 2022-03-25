import java.util.*;

public class Player {
	private int id;
	private String name;
	private List<Monster> monsters = new ArrayList<Monster>();

	/* Constructor: Player */
	public Player(int id, String name, List<Monster> monsters) {
		this.id = id;
		this.name = name;
		this.monsters = monsters;
	}

	/* Getter: id */
	public int getId() {
		return this.id;
	}

	/* Getter: name */
	public String getName() {
		return this.name;
	}

	/* Getter: monsters */
	public List<Monster> getMonsters() {
		return this.monsters;
	}

	/* Method: printAvailableMonsters(), print owned monsters. */
	public void printAvailableMonsters() {
		int id = 0;
		System.out.println("###########################") ;
		System.out.println("###  Available Monsters ###") ;
		System.out.println("---------------------------") ;
		for (Monster monster : monsters) {
			System.out.println("-["+String.valueOf(id+1)+"] "+monster.getName());
			id++;
		}
		System.out.println("---------------------------") ;
		System.out.println("###########################") ;
	}
}