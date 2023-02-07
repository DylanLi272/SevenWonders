import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Deck
{
	private HashMap<Integer, ArrayList<Card>> ageMap;
	private ArrayList<Wonder> wonders;
	private ArrayList<Card> discard;
	private ArrayList<ArrayList<Card>> current;

	public Deck() throws IOException {
		current = new ArrayList<ArrayList<Card>>();
		wonders = new ArrayList<>(); 
		ageMap = new HashMap<Integer, ArrayList<Card>>();
		discard = new ArrayList<Card>();
		
		for (int x = 1; x <= 3; x++)
			current.add(new ArrayList<Card>());
		
		for (int x = 1; x <= 3; x++)
			ageMap.put(x, readList("Age"+x+"Cards.txt", x));
		
		ArrayList<Card> temp = readList("GuildCards.txt", 3);
		for (int i = 0; i < 5; i++)
			ageMap.get(3).add(temp.remove((int) Math.random()*temp.size()));
		
		/*
		 * wonder input order
		 * name
		 * resource
		 * cost1
		 * cost2
		 * cost3
		 * effect1|effect2|effect3|
		 * 
		 */
		Scanner sc = new Scanner(new File("WonderCards.txt"));
		for (int i = 0; i < 7; i++)
		{
			String name = sc.nextLine();
			Resource resource = new Resource(sc.nextLine().charAt(0));
			@SuppressWarnings("unchecked")
			ArrayList<Resource>[] cost = new ArrayList[3];
			for (int j = 0; j < 3; j++) {
				ArrayList<Resource> a = new ArrayList<>();
				char[] c = sc.nextLine().toCharArray();
				for (int x = 0; x < c.length; x++)
					a.add(new Resource(c[x]));
				cost[j] = a;
			}
			String[] effect = new String[3];
			String[] arr = sc.nextLine().split("\\|");
			for (int j = 0; j < 3; j++)
				effect[j] = arr[j];
			wonders.add(new Wonder(name, cost, effect, resource));
		}
		sc.close();
	}
	
	public ArrayList<Card> readList(String name, int age) throws IOException {
		/*
		 * card input order
		 * age
		 * color
		 * name|effect|resourceCost|coinCost|free|chain1|chain2|
		 * 
		 */
		ArrayList<Card> list = new ArrayList<>();
		Scanner sc = new Scanner(new File(name));
		while (sc.hasNext()) {
			StringTokenizer line = new StringTokenizer(sc.nextLine());
			int color = Integer.parseInt(line.nextToken());
			int n = Integer.parseInt(line.nextToken());
			for (int i = 0; i < n; i++) {
				String[] c = sc.nextLine().split("\\|");
				list.add(new Card(age, color, c[0], c[1], c[2], Integer.parseInt(c[3]), c[4], c[5], c[6], i));
			}
		}
		sc.close();
		return list;
	}
	public Wonder revRanWonder() {
		return wonders.remove((int) (Math.random() * wonders.size()));
	}
	public void rotate(int age) {
		if (age == 2)
			current.add(current.remove(0)); //counterclockwise
		else
			current.add(0, current.remove(2)); //clockwise
	}
	public void discard(Card c) {
		discard.add(c);
	}
	public ArrayList<Card> getChoices(int i) {
		return current.get(i);
	}
	public ArrayList<Card> getDiscard() {
		return discard;
	}
	public void deal(int age) {
		ArrayList<Card> c = ageMap.get(age);
		Collections.shuffle(c);
		for (int x = 0; x < 3; x++) {
			ArrayList<Card> list = new ArrayList<>();
			for (int y = 0; y < 7; y++)
				list.add(c.get(7*x+y));
			current.set(x, list);
		}
	}
}
