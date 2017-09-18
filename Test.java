import java.util.*;
public class Test {

	
	public static void main(String[] args)
	{
		String word = "Hello";
		ArrayList<String> words = new ArrayList<String>();
		words.add(word);
		word = "dog";
		words.add(word);
		String d = "hd3"; //-> 53
		String move = "kf7"; //-> 15
		String s = "" + (8 - Integer.parseInt(move.substring(2,3))) + (move.charAt(1) - 97);
		System.out.println(s);
		System.out.println(words.get(1));
	}
}
