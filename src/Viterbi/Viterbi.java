package hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Viterbi {
	
	public static void main(String[] args) {
		ArrayList<String> tags = new ArrayList<String>(Arrays.asList("noun", "verb", "inf", "prep"));
		HashMap<Prob, Double> probabilities = new HashMap<Prob, Double>();
		File probabilitiesFile, sentencesFile;
		String pattern = "##.0000000000";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		Scanner in;

		if (args.length < 2) {
			System.out.println("Error. Incorrect number of arguments.");
			return;
		}

		probabilitiesFile = new File(args[0]);
		sentencesFile = new File(args[1]);

		try {
			in = new Scanner(probabilitiesFile);
			while(in.hasNextLine()){
				String[] temp = in.nextLine().toLowerCase().split("\\s+");
				String firstWord = temp[0];
				String secondWord = temp[1];
				double probability = Double.parseDouble(temp[2]);
				Prob p = new Prob(firstWord, secondWord);
				// Add to our map of probability values 
				probabilities.put(p, probability);

			}
			in.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("Error: Probabilities could not be read");
			return;
		}
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			return;
		}
		
		try {
			in = new Scanner(sentencesFile);
			while(in.hasNextLine()){
				String line = in.nextLine().toLowerCase();
				System.out.println("PROCESSING SENTENCE: " + line + "\n");
				String[] temp = line.split("\\s+");
				int[][] backPtr = new int[tags.size()][temp.length];
				double[][] score = new double[tags.size()][temp.length];
				
				for(int i = 0; i < temp.length; i++){
					if(i == 0){
						int t = 0;
						for(String tag : tags){
							double prob = 0, prob1 = 0, prob2 = 0;
							
							if(probabilities.containsKey(new Prob(temp[0], tag)))
								prob1 = probabilities.get(new Prob(temp[0], tag));
							else
								prob1 = 0.0001;
							
							if(probabilities.containsKey(new Prob(tag, "phi")))
								prob2 = probabilities.get(new Prob(tag, "phi"));
							else
								prob2 = 0.0001;
							
							prob = prob1 * prob2; 
							
							score[t][0] = prob;
							backPtr[t][0] = 0;
							t++;
						}
					}
					else{
						int t = 0;
						for(String tag : tags){
							
							double prob = 0, prob1 = 0, prob2 = 0;
							
							if(probabilities.containsKey(new Prob(temp[i], tag)))
								prob1 = probabilities.get(new Prob(temp[i], tag));
							else
								prob1 = 0.0001;
							
							// Find max of previous probabilities
							
							int bestTagIndex = 0;
							for(int j = 0; j < tags.size(); j++){
								double tempP = 0.0001;
								if(probabilities.containsKey(new Prob(tag, tags.get(j))))
									tempP = probabilities.get(new Prob(tag, tags.get(j)));
								if(score[j][i-1]*tempP > prob2){
									prob2 = score[j][i-1]*tempP;
									bestTagIndex = j;
								}
							}
							
							prob = prob1 * prob2; 
							
							score[t][i] = prob;
							backPtr[t][i] = bestTagIndex;
							t++;
						}
						
					}
				}
				
				System.out.println("FINAL VITERBI NETWORK");
				for(int i = 0; i < temp.length; i++){
					for(int t = 0; t < tags.size(); t++){
						System.out.println("P(" + temp[i] + "=" + tags.get(t) + ") = " + decimalFormat.format(score[t][i]));
					}
				}
				System.out.println();
				System.out.println("FINAL BACKPTR NETWORK");
				

				
				for(int i = 1; i < temp.length; i++){
					for(int t = 0; t < tags.size(); t++){
						System.out.println("Backptr(" + temp[i] + "=" + tags.get(t) + ") = " + tags.get(backPtr[t][i]));
					}
				}
				System.out.println();
				
				double maxProb = 0;
				for(int t = 0; t < tags.size(); t++){
					if(score[t][temp.length-1] > maxProb){
						maxProb = score[t][temp.length-1];
					}
				}
				
				System.out.println("BEST TAG SEQUENCE HAS PROBABILITY = " + decimalFormat.format(maxProb));
				for(int i = temp.length-1; i >= 0; i--){
					System.out.print(temp[i] + " -> ");
					double maxValue = 0;
					int maxIndex = 0;
					for(int t = 0; t < tags.size(); t++){
						if(score[t][i] > maxValue){
							maxValue = score[t][i];
							maxIndex = t;
						}
					}
					System.out.println(tags.get(maxIndex));
				}

				System.out.println();
				System.out.println();

			}
			in.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("Error: Sentences could not be read");
			return;
		}
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			return;
		}

	}

}

