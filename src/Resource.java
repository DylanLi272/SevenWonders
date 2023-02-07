import java.util.ArrayList;

public class Resource implements Comparable<Resource>
{
	private ArrayList<Character> resource;
	private boolean tradable;
	
	public Resource() {
		resource = new ArrayList<Character>();
		tradable = false;
	}
	public Resource(char r) {
		this();
		resource.add(r);
	}
	public Resource(char r, boolean t) {
		this(r);
		tradable = t;
	}
	
	public void add(char r) {
		resource.add(r);
	}
	public void tradable() {
		tradable = true;
	}
	public ArrayList<Character> getResource() {
		return resource; 
	}
	public int len() {
		return resource.size();
	}
	public boolean contains(char r) {
		return resource.contains(r);
	}
	public boolean contains(Resource r) {
		for (int i = 0; i < r.resource.size(); i++)
			if (!resource.contains(r.resource.get(i)))
				return false;
		return true;
	}
	public boolean isEmpty() {
		return resource.contains('-');
	}
	public boolean getTradable() {
		return tradable;
	}
	
	public int compareTo(Resource r) {
		return resource.size() - r.resource.size();
	}
	public boolean equals(Object o) {
		Resource r = (Resource) o;
		if (resource.size() != r.resource.size())
			return false;
		return resource.containsAll(r.resource);
	}
	public String toString() {
		return ""+resource.get(0);
	}
}
