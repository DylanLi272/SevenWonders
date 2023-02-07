import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Board {
	private int age, round, playing;
	private boolean won; 
	private Player[] players;
	private Deck deck;
	private TreeMap<Integer, Integer> scores;
//	private int[] scores;

	public Board() throws IOException {
		age = round = 1;
		playing = 0;
		won = false;
		players = new Player[3];
		scores = new TreeMap<>();
//		scores = new int[3];
		deck = new Deck();
		for(int x = 0; x < 3; x++)
			players[x] = new Player(deck.revRanWonder());
		deck.deal(age);
	}
	
	public int nextPlayer() {
		playing++;
		if (playing == 3) {
			nextRound();
			deck.rotate(age);
		}
		playing %= 3;
		return playing;
	}
	public void rotate() {
		deck.rotate(age);
	}
	public void nextRound() {
		round++;
		if (round == 7) {
			for (int i = 0; i < 3; i++)
				deck.discard(deck.getChoices(i).remove(0));
			war();
			nextAge();
			round = 1;
		}
	}
	public void nextAge() {
		age++;
		if (age == 4) {
			won = true;
			calcWinner();
			return;
		}
		deck.deal(age);
		for (int i = 0; i < 3; i++)
			if (players[i].getWonder().getName().equals("olympia"))
				players[i].getWonder().resetUse();
	}
	public void discard(int p, Card c) {
		players[p].addCoins(3);
		deck.discard(c);
	}
	public void setWon(boolean w) {
		won = w;
	}
	public int getAge() {
		return age;
	}
	public int getPlaying() {
		return playing;
	}
	public Player getPlayer(int i) {
		return players[i%3];
	}
	public ArrayList<Card> getChoices() {
		return deck.getChoices(playing);
	}
	public ArrayList<Card> getDiscard() {
		return deck.getDiscard();
	}
	public boolean getWon() {
		return won;
	}
	public TreeMap<Integer, Integer> getScores() {
		return scores;
	}
	public void war() {
		int one = players[0].getShield();
		int two = players[1].getShield();
		int three = players[2].getShield();
		if (one - two < 0) {
			players[0].addLoss();
			players[1].addWins(age*2-1);
		}
		else if (one - two > 0) {
			players[0].addWins(age*2-1);
			players[1].addLoss();
		}
		if (one - three < 0) {
			players[0].addLoss();
			players[2].addWins(age*2-1);
		}
		else if (one - three > 0) {
			players[0].addWins(age*2-1);
			players[2].addLoss();
		}
		if (two - three < 0) {
			players[1].addLoss();
			players[2].addWins(age*2-1);
		}
		else if (two - three > 0) {
			players[1].addWins(age*2-1);
			players[2].addLoss();
		}
	}
	public boolean build(int p, Card c) {
		Wonder w = players[p].getWonder();
		int next = w.nextBuild();
		if (next == 3)
			return false;
		ArrayList<Resource> cost = w.hasResource(players[p].getResource(), next);
		if (!cost.isEmpty())
			if (!trade(p, cost))
				return false;
		
		if (w.nextBuild() == 2) {
			String wn = w.getName();
			if (wn.equals("alexandria")) {
				Resource r = new Resource();
				char[] arr = w.getEffect(1).toCharArray();
				for (int i = 0; i < arr.length; i += 2)
					r.add(arr[i]);
				players[p].getResource().add(r);
			}
			else if (wn.equals("ephesos")) {
				players[p].addCoins(9);
			}
			else if (wn.equals("rhodos")) {
				players[p].addShield(2);
			}
		}
			
		w.build(c, next);
		return true;
	}
	public boolean playCard(int p, Card c) {
		Player player = players[p];
		ArrayList<Card> hand = player.getHand();
		boolean bo = false;
		String[] a = c.getFree().split("/");
		for (int i = 0; i < a.length; i++)
			if (hand.contains(new Card(a[i])))
				bo = true;
		if (!bo) {
			if (c.getCoinCost() > player.getCoins())
				return false;
			player.addCoins(-1*c.getCoinCost());
			ArrayList<Resource> cost = player.hasResource(c);
			if (!cost.isEmpty())
				if (!trade(p, cost))
					return false;
		}
		
		//everything below is for when the card can be played
		String cn = c.getName();
		if (cn.equals("vineyard")) {
			int total = 0;
			for (int i = 0; i < 3; i++) {
				hand = players[(p+i)%3].getHand();
				for (int j = 0; j < hand.size(); j++)
					if (hand.get(j).getColor() == 0)
						total++;
			}
			player.addCoins(total);
		}
		else if (cn.equals("arena")) {
			player.addCoins(3*player.getWonder().nextBuild());
		}
		else if (cn.equals("haven")) {
			for (int i = 0; i < hand.size(); i++)
				if (hand.get(i).getColor() == 0)
					player.addCoins(1);
		}
		else if (cn.equals("lighthouse")) {
			for (int i = 0; i < hand.size(); i++)
				if (hand.get(i).getColor() == 5)
					player.addCoins(1);
		}
		player.playCard(c);
		
		return true;
	}
	public void wonderThing(int p, Card c) {
		Player player = players[p];
		ArrayList<Card> hand = player.getHand();
		String cn = c.getName();
		if (cn.equals("vineyard")) {
			int total = 0;
			for (int i = 0; i < 3; i++) {
				hand = players[(p+i)%3].getHand();
				for (int j = 0; j < hand.size(); j++)
					if (hand.get(j).getColor() == 0)
						total++;
			}
			player.addCoins(total);
		}
		else if (cn.equals("arena")) {
			player.addCoins(3*player.getWonder().nextBuild());
		}
		else if (cn.equals("haven")) {
			for (int i = 0; i < hand.size(); i++)
				if (hand.get(i).getColor() == 0)
					player.addCoins(1);
		}
		else if (cn.equals("lighthouse")) {
			for (int i = 0; i < hand.size(); i++)
				if (hand.get(i).getColor() == 5)
					player.addCoins(1);
		}
		player.playCard(c);
	}
	public boolean trade(int p, ArrayList<Resource> cost) {
		boolean mart = players[p].getHand().contains(new Card("marketplace"));
		boolean east = players[p].getHand().contains(new Card("easttradingpost"));
		boolean west = players[p].getHand().contains(new Card("westtradingpost"));
		int left = 0, right = 0;
		if (east && !west) {
			//right first
			ArrayList<Resource> resource = players[(p+2)%3].getResource();
			ArrayList<Resource> res = new ArrayList<>();
			for (Resource item : resource)
				res.add(item);
			for (int i = cost.size()-1; i >= 0; i--) {
				for (int j = 0; j < res.size(); j++) {
					if (res.get(j).getTradable()) {
						if (res.get(j).contains(cost.get(i))) {
							Resource r = cost.remove(i);
							right += r.toString().matches("[CSOW]") ? 1 : mart ? 1 : 2;
							res.remove(j);
							break;
						}
					}
				}
			}
			resource = players[(p+1)%3].getResource();
			res.clear();
			for (Resource item : resource)
				res.add(item);
			for (int i = cost.size()-1; i >= 0; i--) {
				for (int j = 0; j < res.size(); j++) {
					if (res.get(j).getTradable()) {
						if (res.get(j).contains(cost.get(i))) {
							Resource r = cost.remove(i);
							left += r.toString().matches("[CSOW]") ? 2 : mart ? 1 : 2;
							res.remove(j);
							break;
						}
					}
				}
			}
		}
		else {
			ArrayList<Resource> resource = players[(p+1)%3].getResource();
			ArrayList<Resource> res = new ArrayList<>();
			for (Resource item : resource)
				res.add(item);
			for (int i = cost.size()-1; i >= 0; i--) {
				for (int j = 0; j < res.size(); j++) {
					if (res.get(j).getTradable()) {
						if (res.get(j).contains(cost.get(i))) {
							Resource r = cost.remove(i);
							left += r.toString().matches("[CSOW]") ? west ? 1 : 2 : mart ? 1 : 2;
							res.remove(j);
							break;
						}
					}
				}
			}
			resource = players[(p+2)%3].getResource();
			res.clear();
			for (Resource item : resource)
				res.add(item);
			for (int i = cost.size()-1; i >= 0; i--) {
				for (int j = 0; j < res.size(); j++) {
					if (res.get(j).getTradable()) {
						if (res.get(j).contains(cost.get(i))) {
							Resource r = cost.remove(i);
							right += r.toString().matches("[CSOW]") ? east ? 1 : 2 : mart ? 1 : 2;
							res.remove(j);
							break;
						}
					}
				}
			}
		}
		
		if (cost.isEmpty()) {
			if (players[p].getCoins() < left + right)
				return false;
			players[p].addCoins(-1*(left + right));
			players[(p+1)%3].addCoins(left);
			players[(p+2)%3].addCoins(right);
			return true;
		}
		return false;
	}
	public void calcWinner() {
		for (int i = 0; i < 3; i++) {
			int score = 0;
			//military
			int military = 0;
			military += players[i].getWins() - players[i].getLosses();
			//coins
			int coins = 0;
			coins += players[i].getCoins() / 3;
			//wonder points
			int wonder = 0;
			Wonder w = players[i].getWonder();
			for (int j = 0; j < w.nextBuild(); j++)
				if (w.getEffect(j).contains("{"))
					wonder += j*2 + 3;
			//blue cards
			int blue = 0;
			ArrayList<Card> hand = players[i].getHand();
			for (int j = 0; j < hand.size(); j++)
				if (hand.get(j).getColor() == 2)
					blue += hand.get(j).getEffect().charAt(1) - 48;
			//science
			int science = 0;
			int max = 0;
			if (w.getName().equals("babylon") && hand.contains(new Card("scientists")))
				for (int j = 0; j < 3; j++)
					for (int k = 0; k < 3; k++)
						max = Math.max(max, getScience(j, k, i));
			else if (w.getName().equals("babylon") || hand.contains(new Card("scientists")))
				for (int j = 0; j < 3; j++)
					max = Math.max(max, getScience(j, 3, i));
			science += Math.max(max, getScience(3, 3, i));
			//yellow cards
			int yellow = 0;
			for (int j = 0; j < hand.size(); j++) {
				String name = hand.get(j).getName();
				if (name.equals("arena"))
					yellow += players[i].getWonder().nextBuild();
				else if (name.equals("haven")) {
					for (int k = 0; k < hand.size(); k++)
						if (hand.get(k).getColor() == 0)
							yellow++;
				}
				else if (name.equals("lighthouse")) {
					for (int k = 0; k < hand.size(); k++)
						if (hand.get(k).getColor() == 5)
							yellow++;
				}
			}
			//guild
			int guild = 0;
			for (int j = 0; j < hand.size(); j++)
				if (hand.get(j).getColor() == 6)
					guild += getGuild(hand.get(j), i);
			score += military + coins + wonder + blue + science + yellow + guild;
			scores.put(score, i+1);
//			scores[i] = score;
			System.out.printf("Player %d : %d\n", i+1, score);
			System.out.printf("Military: %d\nCoins: %d\nWonder: %d\nBlue: %d\nScience: %d\nYellow: %d\nGuild: %d\n", military, coins, wonder, blue, science, yellow, guild);
			System.out.println();
		}
//		for (int i = 0; i < 3; i++)
//			if (scores[i] > scores[winner])
//				winner = i;
	}
	public int getScience(int s, int s2, int x) {
		//&-0, #-1, @-2
		int total = 0;
		int[] science = new int[3];
		if (s < 3)
			science[s]++;
		if (s2 < 3)
			science[s2]++;
		ArrayList<Card> hand = players[x].getHand();
		for (int i = 0; i < hand.size(); i++)
			if (hand.get(i).getColor() == 3) {
				char ch = hand.get(i).getEffect().charAt(0);
				science[ch == '&' ? 0 : ch == '#' ? 1 : 2]++;
			}
		int set = 0;
		for (int i = 0; i < 3; i++) {
			total += science[i] * science[i];
			set = Math.min(set, science[i]);
		}
		total += set*7;
		return total;
	}
	public int getGuild(Card c, int x) {
		int total = 0;
		String name = c.getName();
		if (name.equals("builders"))
			for (int i = 0; i < 3; i++)
				total += players[(x+i)%3].getWonder().nextBuild();
		else if (name.equals("stategists"))
			for (int i = 1; i < 3; i++)
				total += players[(x+i)%3].getLosses();
		else if (name.equals("shipowners"))
			for (int j = 1; j < 3; j++) {
				ArrayList<Card> hand = players[(x+j)%3].getHand();
				for (int i = 0; i < hand.size(); i++) {
					int color = hand.get(i).getColor();
					if (color == 0 || color == 1 || color == 6)
						total++;
				}
			}
		else if (!name.equals("scientists"))
			for (int j = 1; j < 3; j++) {
				String effect = c.getEffect();
				ArrayList<Card> hand = players[(x+j)%3].getHand();
				for (int i = 0; i < hand.size(); i++)
					if (hand.get(i).getColor() == effect.charAt(1)-48)
						total += effect.charAt(4)-48;
			}
		
		return total;
	}
}
