package Viterbi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Network {
	private Network parent;
	private HashMap<Prob, Double> probabilities;
	private HashSet<String> tags;
	private ArrayList<Network> children;
	private String previousWord, X, Y;
	private double prob;
	
	public Network(Network parent, String previousWord, String X, String Y, double prob, HashMap<Prob, Double> probabilities){
		this.parent = parent;
		this.previousWord = previousWord;
		this.X = X;
		this.Y = Y;
		this.prob = prob;
		children = new ArrayList<Network>();
		this.probabilities = probabilities;
		tags = new HashSet<String>(Arrays.asList("noun", "verb", "inf", "prep", "phi"));
	}
	
	public void addChild(String childX, String childY, double probability){
		children.add(new Network(this, X, childX, childY, probability, probabilities));
	}
	
	public double getProbability(){
		return prob;
	}
	
	public double[] getMaxProbOfChildren(){
		double[] maxProb = new double[2];
		for(int i = 0; i < children.size(); i++){
			if(children.get(i).getProbability() > maxProb[0]){
				maxProb[0] = children.get(i).getProbability();
				maxProb[1] = i;
			}
		}
		return maxProb;
	}
	
	public Network getChild(int index){
		return children.get(index);
	}
	
	public Network getParent(){
		return parent;
	}
	
}
