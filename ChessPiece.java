import java.util.*;
public class ChessPiece {
	public String pieceName;
	public boolean color;
	public int position;
	public boolean moved;
	public boolean enPassant;
	
	public ChessPiece(String pieceName, boolean color, int position)
	{
		this.pieceName = pieceName;
		this.color = color;
		this.position = position;
		moved = false;
		enPassant = false;
	}
	
	public String toString()
	{
		return pieceName + " " + color + " " + position + " " + moved;
	}
	
	public int number()
	{
		int num = 0;
		if (pieceName.equals("pawn"))
			num = 1;
		if (pieceName.equals("rook"))
			num = 2;
		if (pieceName.equals("night"))
			num = 3;
		if (pieceName.equals("bishop"))
			num = 4;
		if (pieceName.equals("qrookbishop"))
			num = 5;
		if (pieceName.equals("king"))
			num = 6;
		if (!color)
			num = num * -1;
		return num;
	}

	public void transform()
	{
		System.out.println("Promotion! What do you want to upgrade your pawn into?");
		Scanner keyIn = new Scanner(System.in);
		String name = keyIn.nextLine().toLowerCase();
		pieceName = "dog";
		while (pieceName.equals("dog"))
		{
			if (name.equals("queen"))
				pieceName = "qrookbishop";
			if (name.equals("castle") || name.equals("rook"))
				pieceName = "rook";
			if (name.equals("horse") || name.equals("knight"))
				pieceName = "night";
			if (name.equals("bishop"))
				pieceName = "bishop";
			else
			{
				pieceName = "dog";
				System.out.println("Invalid promotion!");
				System.out.println("What do you want to upgrade your pawn into?");
				name = keyIn.nextLine().toLowerCase();
			}
		}
		keyIn.close();
	}
	
}
