import java.util.ArrayList;

public class Player
{
	private ArrayList<Card> hand;
	private ArrayList<Resource> resources;
	private Wonder wonder;
	private int coins, wins, losses, shield;
	
	public Player(Wonder w) {
		hand = new ArrayList<Card>();
		resources = new ArrayList<Resource>();
		coins = 3;
		wins = 0;
		losses = 0;
		shield = 0;
		wonder = w;
		resources.add(wonder.getResource());
	}
	
	public void addCoins(int amountToAdd) {
		coins += amountToAdd;
	}
	public void addWins(int amountToAddToAdd) {
		wins += amountToAddToAdd;
	}
	public void addLoss() {
		losses++;
	}
	public void addShield(int s) {
		shield += s;
	}
	public int getCoins() {
		return coins;
	}
	public int getWins() {
		return wins;
	}
	public int getLosses() {
		return losses;
	}
	public int getShield() {
		return shield;
	}
	public ArrayList<Card> getHand() {
		return hand;
	}
	public ArrayList<Resource> getResource() {
		return resources;
	}
	public Wonder getWonder() {
		return wonder;
	}
	public void build(Card c, int next) {
		wonder.build(c, next);
	}
	public ArrayList<Resource> hasResource(Card c) {
		ArrayList<Resource> qwer = c.getResourceCost();
		ArrayList<Resource> cost = new ArrayList<>();
		for (int i = 0; i < qwer.size(); i++)
			cost.add(qwer.get(i));
		if (cost.get(0).isEmpty()) {
			cost.clear();
			return cost;
		}
		ArrayList<Resource> temp = new ArrayList<>();
		for (int i = 0; i < resources.size(); i++)
			temp.add(resources.get(i));
		for (int i = cost.size()-1; i >= 0; i--) {
			Resource r = cost.get(i);
			for (int j = 0; j < temp.size(); j++) {
				if (temp.get(j).len() > 1)
					continue;
				else if (temp.get(j).contains(r)) {
					temp.remove(j);
					cost.remove(i);
					break;
				}
			}
		}
		for (int i = cost.size()-1; i >= 0; i--) {
			Resource r = cost.get(i);
			for (int j = 0; j < temp.size(); j++) {
				if (temp.get(j).contains(r)) {
					temp.remove(j);
					cost.remove(i);
					break;
				}
			}
		}
		
		return cost;
	}
	public boolean playCard(Card c) {
		hand.add(c);
		c.setOrder(hand.size());
		String effect = c.getEffect();
		if (effect.matches("[CSOWLGP]+.*")) {
			if (effect.contains("/")) {
				char[] arr = effect.toCharArray();
				Resource r = new Resource();
				for (int i = 0; i < arr.length; i += 2)
					r.add(arr[i]);
				if (c.getColor() != 5)
					r.tradable();
				resources.add(r);
			}
			else
				for (int i = 0; i < effect.length(); i++)
					resources.add(new Resource(effect.charAt(i), c.getColor() != 5));
		}
		else if (effect.matches("X+")) {
			shield += effect.length();
		}
		return true;
	}
}
