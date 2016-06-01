public class Node {
	private String text;
	private int frequency;
	
	public Node(String text, int frequency) {
		this.text = text;
		this.frequency = frequency;
	}
	
	public int getFreq() {
		return frequency;
	}
	
	public String getText() {
		return text;
	}
}
