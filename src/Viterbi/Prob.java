package hw2;

public class Prob {

	private String X, Y;
//	private boolean isBigram;
	
	public Prob(String X, String Y){
		this.X = X;
		this.Y = Y;
//		this.isBigram = isBigram;
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Prob))
			return false;
		Prob other = (Prob) o;
		return this.X.equals(other.X) && this.Y.equals(other.Y);
	}
	
	@Override
    public int hashCode() {
        return (X + Y).hashCode();
    }
}
