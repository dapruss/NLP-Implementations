package hw2;

public class Node {
	
	private int t, w;
	private Node parent;
	private double value;
	
	public Node(int t, int w, Node parent, double value){
		this.t = t;
		this.w = w;
		this.parent = parent;
		this.value = value;
	}
	
	public double getValue(){
		return value;
	}
	
	

}
