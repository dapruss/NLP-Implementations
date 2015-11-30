package hw1;

public class Bigram {
	
	// Just a tuple of two words
	
	private String first, second;
	
	public Bigram(String first, String second){
		this.first = first;
		this.second = second;
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Bigram))
			return false;
		Bigram other = (Bigram) o;
		return this.first.equals(other.first) && this.second.equals(other.second);
	}
	
	@Override
	public int hashCode(){
		return first.hashCode()*second.hashCode();
	}
	
	@Override
	public String toString(){
		return "[" + first + " " + second + "]";
	}
	
	public String getFirst(){
		return first;
	}
	
	public String getSecond(){
		return second;
	}

}
