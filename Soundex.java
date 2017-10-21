//program accepts a single String, checks spelling, then gives possible suggestions based on matching Soundex code

import java.util.Scanner;
import java.io.*;

public class Soundex {

	private static String[] arrayWords = new String[58116];		//create array to contain words from text file

	private static char[] mapping = {
			'0','1','2','3','0','1','2','0','0','2','2','4','5','5','0','1','2','6','2','3','0','1','0','2','0','2'};
	private static int codeLength = 4;

	public static void main(String[] args) throws IOException {
		fillArray();
		for (;;) {
			String string = getString();
			System.out.print(checkSpelling(string));
			displayCode(string.toUpperCase());
			suggestions(string.toUpperCase());
		}
	}

	public static void displayCode(String string) {
		System.out.print("Soundex code: ");
		System.out.println(findCode(string));
		System.out.println();
	}

	public static void suggestions(String string) {
		char[] wordsCode = new char[4];		//creates array of char to hold temporary Soundex codes
		char[] finalCode = findCode(string);	//Soundex code for string input
		boolean availableSuggestions = false;

		System.out.print("Suggestions: ");
		for (int x = 0; x < arrayWords.length; x++) {
			wordsCode = findCode(arrayWords[x].toUpperCase());
			if (finalCode[0] == wordsCode[0] && finalCode[1] == wordsCode[1] &&		//checks to see if Soundex code mathces, and if word is not the same as string input
				finalCode[2] == wordsCode[2] && finalCode[3] == wordsCode[3] &&
				string.compareTo(arrayWords[x].toUpperCase()) != 0) {

				System.out.print("\n" + arrayWords[x]);
				availableSuggestions = true;
			}
		}
		if (availableSuggestions == false) {
			System.out.print("NONE");
		}
		System.out.println("\n");
	}

	public static String checkSpelling(String string) {		//searches array for string input using binary search
		int low = 0;
		int high = arrayWords.length - 1;
		while (low <= high) {
			int mid = (low + high) / 2;
			if (arrayWords[mid].compareTo(string) > 0) {
				high = mid - 1;
			} else if (arrayWords[mid].compareTo(string) < 0) {
				low = mid + 1;
			} else
				return "The word is spelled correctly.\n";
		}
		return "";
	}

	public static String getString() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter a String: ");
		String string = sc.next();
		return string;
	}

	public static void fillArray() throws IOException {
		File myFile = new File("Words.txt");
		Scanner inputFile = new Scanner(myFile);
		int x = 0;

		while (inputFile.hasNext()) {		//loop fills array with words
			arrayWords[x] = inputFile.next();
			x++;
		}
		inputFile.close();
	}

	public static char[] findCode(String string) {
		int codeIndex = 0;
		char[] code = new char[codeLength];						//this will be the array to store the final values in
		code[codeIndex] = string.charAt(0);						//put first letter of the string at beginning of array
		codeIndex++;											//update because this index is no longer needed
		char prevChar = string.charAt(0);						//create a variable to compare to make sure it does not match next in line

		for (int i = 1; i < string.length() && codeIndex < codeLength; i++) {
			char c = toNumber(string.charAt(i));				//takes character in string and stores in c, then runs through method to get integer value
			if (c != '0' && c != prevChar) {					//if c is not a vowel AND not equal to the previous c, then place in the final array
				code[codeIndex++] = c;
			}
			prevChar = c;
		}
		for (int i = codeIndex; i < codeLength; i++) { 			//in case all 4 spots are not filled, this will fill the rest with 0's
			code[i] = '0';
		}
		return code;
	}
	public static char toNumber(char c) {
		return mapping[c - 'A'];
	}
}