import java.util.ArrayList;

public class Wonder
{
	private String name;
	private Resource resource;
	private ArrayList<Resource>[] cost;
	private String[] effect;
	private Card[] built;
	private boolean used;

	public Wonder(String name, ArrayList<Resource>[] cost, String[] effect, Resource resource) {
		this.name = name;
		this.effect = effect;
		this.cost = cost;
		built = new Card[3];
		this.resource = resource;
	}
	
	public int nextBuild() {
		int i = 0;
		while (i < 3 && built[i] != null)
			i++;
		return i;
	}
	public ArrayList<Resource> hasResource(ArrayList<Resource> resource, int x) {
		ArrayList<Resource> temp = new ArrayList<>();
		for (Resource item : cost[x])
			temp.add(item);
		ArrayList<Resource> re = new ArrayList<>();
		for (Resource item : resource)
			re.add(item);
		for (int i = temp.size()-1; i >= 0; i--) {
			for (int j = 0; j < re.size(); j++) {
				if (re.get(j).contains(temp.get(i))) {
					temp.remove(i);
					re.remove(j);
					break;
				}
			}
		}
		return temp;
	}
	public void build(Card c, int i) {
		built[i] = c;
	}
	public String getName() {
		return name;
	}
	public String getEffect(int i) {
		return effect[i];
	}
	public Resource getResource() {
		return resource;
	}
	public Card[] getBuilt() {
		return built;
	}
	public ArrayList<Resource> getCost(int x) {
		return cost[x];
	}
	public void use() {
		used = true;
	}
	public void resetUse() {
		used = false;
	}
	public boolean used() {
		return used;
	}
}
