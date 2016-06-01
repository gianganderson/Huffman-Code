import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.io.*;
import java.util.*;
import java.util.Scanner;
public class HuffmanCoding {
	private String text;
	private int[] freqArray = new int[256];
	private HashMap<String, String> table = new HashMap<String,String>();
	
	public static void main(String[] args) {
		HuffmanCoding hc = new HuffmanCoding();
		System.out.println("Project 1: Huffman Code");
		hc.start();
	}
	/**
	 * Reads the text from the file.
	 */
	public void readFile() {
		System.out.println("Reading the file: input.txt");
		Scanner input;
		try {
			input = new Scanner(new File("input.txt"));
		}
		catch (FileNotFoundException e) {
			System.out.println("Could not find input.txt. \n Please make sure it is in the correct directory!");
			return;
		}
		
		while (input.hasNextLine()) {
			text = input.nextLine();
		}
		input.close();
	}
	
	/**
	 * 
	 * @param String text
	 * Increments the number of times the letter appears inside of the text file.
	 * Type casting each character and putting there frequency into there respective indexes.
	 */
	public  void getFrequencies(String text) {
		for (int i = 0; i < text.length(); i++) {
			freqArray[(int) text.charAt(i)]++;
		}
	}
	
	/**
	 * Prints all the letter counts.
	 * Example: c 3/32
	 * 			e 1/32
	 */
	public void printLetterCounts() {
		System.out.println("Letter Counts");
		for (int i = 0; i < freqArray.length; i++) {
			if (freqArray[i] != 0) {
				System.out.println((char) i + " " + freqArray[i] + "/" + text.length());
			}
		}

	}
	
	/**
	 * Iterating over the text and retrieving the value (code) from the respect key (letter).
	 * Need to use StringBuilder to reverse the code because I concatted 0 and 1
	 */
	public void printFileInHuffman() {
		for (int i = 0; i < text.length(); i++) {
			String value = table.get(String.valueOf(text.charAt(i)));
			StringBuilder sb = new StringBuilder(value).reverse();
			System.out.print(sb);
		}
	}
	/**
	 * Printing out the letter and the code.
	 * Example: a 00
	 * 			b 011
	 */
	public void printCode() {
		//iterate over entrySet() if you want both values and keys().
		for (Map.Entry<String, String> entry :table.entrySet()) {
			StringBuilder sb = new StringBuilder(entry.getValue()).reverse();
			System.out.println(entry.getKey() + " " + sb );
		}
	}
	
	/**
	 * 
	 * @param Node minNode
	 * @param Node secondMinNode
	 * The secondMinNode (larger freq) goes to the right of the subtree (concat 0) and the minNode goes to the left of the subtree (concat 1)
	 * Used a HashMap to perform this method.
	 */
	public void concatCode(Node minNode, Node secondMinNode) {
		for (int i = 0; i < minNode.getText().length(); i++) {
			String value = table.get(String.valueOf(minNode.getText().charAt(i)));
			table.put(String.valueOf(minNode.getText().charAt(i)), value.concat("1"));	
		}
		
		for (int i = 0; i < secondMinNode.getText().length(); i++) {
			String value = table.get(String.valueOf(secondMinNode.getText().charAt(i)));
			table.put(String.valueOf(secondMinNode.getText().charAt(i)), value.concat("0"));	
		}
	}
	
	/**
	 * 
	 * @param Node minNode
	 * @param Node secondMinNode
	 * When the frequencies are both equal the group with more nodes goes to the right subtree (i know because of the length of the text for each node)
	 * And the group with less nodes goes to the left subtree.
	 */
	public void concatCodeWhenEqual(Node minNode, Node secondMinNode) {
		for (int i = 0; i < secondMinNode.getText().length(); i++) {
			String value = table.get(String.valueOf(secondMinNode.getText().charAt(i)));
			table.put(String.valueOf(secondMinNode.getText().charAt(i)), value.concat("1"));
		}
		
		for (int i = 0; i < minNode.getText().length(); i++) {
			String value = table.get(String.valueOf(minNode.getText().charAt(i)));
			table.put(String.valueOf(minNode.getText().charAt(i)), value.concat("0"));
		}		
	}
	
	
	/**
	 * Runs the application
	 */
	public void start() {
		readFile();
		getFrequencies(text);
		//Min PQ by frequency.
		PriorityQueue<Node> pq = new PriorityQueue<Node>(10,
				new Comparator<Node>() {
					public int compare(Node a, Node b) {
						return a.getFreq() - b.getFreq();
					}
		});

		for (int i = 0; i < freqArray.length; i++) {
			if (freqArray[i] != 0) {
				Node newNode = new Node(String.valueOf((char) i), freqArray[i]);
				table.put(String.valueOf((char) i), "");
				pq.add(newNode);
			}
		}
		
		while (!pq.isEmpty()) {
			Node minNode = pq.remove();
			Node secondMinNode = pq.remove();
			Node parent = new Node(minNode.getText().concat(secondMinNode.getText()), minNode.getFreq() + secondMinNode.getFreq());
			
			//at least one group
			if (minNode.getText().length() > 1 || secondMinNode.getText().length() > 1) {
				//breaking ties
				if (minNode.getFreq() == secondMinNode.getFreq()) {
					//if they are = freq, take the larger string and put it on the RHS
					if (minNode.getText().length() >= secondMinNode.getText().length()) {
						concatCode(minNode, secondMinNode);
					} 
					//if they are = freq, take the larger/equal string and put it on the RHS
					else if (secondMinNode.getText().length() > minNode.getText().length()) {
						concatCodeWhenEqual(minNode, secondMinNode);
					}
				}
				
				else {
					concatCode(minNode, secondMinNode);
				}
			}
			//singles
			else {
				//retrieves the value of the key
				String value = table.get(minNode.getText());
				//updates the value of the respective key
				table.put(minNode.getText(), value.concat("1"));
				value = table.get(secondMinNode.getText());
				table.put(secondMinNode.getText(),  value.concat("0"));
			}

			
			if (!pq.isEmpty()) {
				pq.add(parent);
			}
				
		}
		//print everything
		printLetterCounts();
		System.out.println();
		System.out.println("Code");
		printCode();
		System.out.println();
		System.out.println("File in Huffman Code");
		printFileInHuffman();	
		
	}
}


