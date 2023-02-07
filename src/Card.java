import java.util.ArrayList;

public class Card implements Comparable<Card>
{
	private int age, color, coinCost;
	private String name, effect, free, chain1, chain2;
	private ArrayList<Resource> resourceCost;
	private int order;
	
//	&-math, #-literature, @-engineer
//	integers correspond w/ colors:
//	[0-brown, 1-gray, 2-blue, 3-green, 4-red, 5-yellow, 6-purple]
	
	public Card(int age, int color, String name, String effect, String resourceCost, 
				int coinCost, String free, String chain1, String chain2, int order)
	{
		this.age = age;
		this.color = color;
		this.name = name;
		this.coinCost = coinCost;
		this.free = free;
		this.chain1 = chain1;
		this.chain2 = chain2;
		this.resourceCost = new ArrayList<>();
		if (resourceCost.contains("/")) {
			char[] arr = resourceCost.toCharArray();
			Resource r = new Resource();
			for (int i = 0; i < arr.length; i += 2)
				r.add(arr[i]);
			this.resourceCost.add(r);
		}
		else
			for (int i = 0; i < resourceCost.length(); i++)
				this.resourceCost.add(new Resource(resourceCost.charAt(i)));
		this.effect = effect;
		order = 0;
	}
	public Card(String n) {
		name = n;
	}
	
	public int getAge() {
		return age;
	}
	public int getColor() {
		return color;
	}
	public String getName() {
		return name;
	}
	public String getEffect() {
		return effect;
	}
	public ArrayList<Resource> getResourceCost() {
		return resourceCost;
	}
	public int getCoinCost() {
		return coinCost;
	}
	public String getFree() {
		return free;
	}
	public String getChain1() {
		return chain1;
	}
	public String getChain2() {
		return chain2;
	}
	public void setOrder(int o) {
		order = o;
	}
	public boolean equals(Object o) {
		Card c = (Card) o;
		return name.equals(c.name);
	}
	public int compareTo(Card c) {
		if (color == c.color || (color == 0 && c.color == 1) || (color == 1 && c.color == 0))
			return order - c.order;
		return color - c.color;
	}
	public String toString() {
		return "age: " + age 
				+ "| color: " + color 
				+ "| name: " + name 
				+ "| effect: " + effect
				+ "| resource cost: " + resourceCost
				+ "| coin cost: " + coinCost
				+ "| free: " + free 
				+ "| chain1: " + chain1 
				+ "| chain2: " + chain2
				+ "| order: " + order;  
	}
}
