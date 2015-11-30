package NGrams;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class NGrams {

	public static void main(String[] args) {
		File trainingFile, testFile;
		int totalWords = 0, totalBigrams = 0;
		HashMap<String, Integer> unigrams = new HashMap<String, Integer>();
		HashMap<Bigram, Integer> bigrams = new HashMap<Bigram, Integer>();
		String pattern = "##.####";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		Scanner in;

		if (args.length < 2) {
			System.out.println("Error. Incorrect number of arguments.");
			return;
		}

		trainingFile = new File(args[0]);
		testFile = new File(args[1]);

		try {
			in = new Scanner(trainingFile);
			while(in.hasNextLine()){
				String[] temp = in.nextLine().split("\\s+");
				String firstWord = temp[0].toLowerCase();
				// Add the starting bigram
				Bigram b = new Bigram("START", firstWord);
				if(bigrams.containsKey(b))
					bigrams.put(b, bigrams.get(b)+1);
				else
					bigrams.put(b, 1);

				for(int i = 0; i < temp.length-1; i++){
					totalWords++;
					String first = temp[i].toLowerCase();
					String second = temp[i+1].toLowerCase();


					// Unigrams
					if(unigrams.containsKey(first))
						unigrams.put(first, unigrams.get(first)+1);
					else
						unigrams.put(first, 1);

					// Bigrams
					Bigram bigram = new Bigram(first, second);
					if(bigrams.containsKey(bigram))
						bigrams.put(bigram, bigrams.get(bigram)+1);
					else
						bigrams.put(bigram, 1);
				}

				// Take care of the last word for unigrams
				String last = temp[temp.length-1].toLowerCase();
				totalWords++;
				if(unigrams.containsKey(last))
					unigrams.put(last, unigrams.get(last)+1);
				else
					unigrams.put(last, 1);


			}
			in.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("Error: Training File could not be read");
			return;
		}

		try {
			in = new Scanner(testFile);

			while(in.hasNextLine()){
				String line = in.nextLine();
				String[] temp = line.split("\\s+");
				boolean undefined = false;
				double logProbUnigram = 0, logProbBigram = 0, logProbBigramSmooth = 0;

				String firstWord = temp[0].toLowerCase();

				if(!unigrams.containsKey(firstWord)){
					System.out.println("Error, " + firstWord + " does not exist in dictionary.");
					in.close();
					return;
				}

				// Calculate denominator for bigram probability
				totalBigrams = 0;
				for(Bigram b : bigrams.keySet()){
					if(b.getFirst().equals("START"))
						totalBigrams += bigrams.get(b);
				}

				// First bigram
				Bigram bigram = new Bigram("START", firstWord);

				if(!bigrams.containsKey(bigram)){
					undefined = true;
					// Add one to denominator to account for "START"
					logProbBigramSmooth += Math.log(1.0/(totalBigrams + unigrams.size()))/Math.log(2);
				}
				else{
					logProbBigram += Math.log((double)bigrams.get(bigram)/totalBigrams)/Math.log(2);
					logProbBigramSmooth += Math.log((double)(bigrams.get(bigram)+1)/(totalBigrams + unigrams.size()))/Math.log(2);
				}

				for(int i = 0; i < temp.length-1; i++){
					String first = temp[i].toLowerCase();
					String second = temp[i+1].toLowerCase();

					// Make sure the words are in our dictionary
					if(!unigrams.containsKey(second)){
						System.out.println("Error, " + second + " does not exist in dictionary.");
						in.close();
						return;
					}

					// Calculate denominator for bigram probability
					totalBigrams = 0;
					for(Bigram b : bigrams.keySet()){
						if(b.getFirst().equals(first))
							totalBigrams += bigrams.get(b);
					}

					logProbUnigram += Math.log((double)unigrams.get(first)/totalWords)/Math.log(2);

					// Bigrams
					bigram = new Bigram(first, second);
					// If we haven't recorded the bigram, assume frequency is 0 if unsmoothed, 1 if smoothed
					if(!bigrams.containsKey(bigram)){
						undefined = true;
						logProbBigramSmooth += Math.log(1.0/(totalBigrams + unigrams.size()))/Math.log(2);
					}
					else{
						logProbBigram += Math.log((double)bigrams.get(bigram)/totalBigrams)/Math.log(2);
						logProbBigramSmooth += Math.log((double)(bigrams.get(bigram)+1)/(totalBigrams + unigrams.size()))/Math.log(2);
					}

				}

				// Take care of the last word for unigrams
				String lastWord = temp[temp.length-1].toLowerCase();
				logProbUnigram += Math.log((double)unigrams.get(lastWord)/totalWords)/Math.log(2);

				// Print results
				System.out.println("S = " + line);
				System.out.println();
				System.out.println("Unigrams: logprob(S) = " + decimalFormat.format(logProbUnigram));
				if(undefined)
					System.out.println("Bigrams: logprob(S) = undefined");
				else
					System.out.println("Bigrams: logprob(S) = " + decimalFormat.format(logProbBigram));
				System.out.println("Smoothed Bigrams: logprob(S) = " + decimalFormat.format(logProbBigramSmooth));
				System.out.println();
			}

			in.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("Error: Testing File could not be read");
			return;
		}


	}

}
