import java.util.*;
import java.io.*;
import java.nio.file.Files;
public class Chess {
	public static int [][] board;
	public static ChessPiece[] playerOne;
	public static ChessPiece[] playerTwo;
	public static boolean whiteTurn;
	public Chess()
	{
		playerOne = new ChessPiece[16];
		playerTwo = new ChessPiece[16];
		board = new int[8][8];
	}

	public static void initializePieces()
	{
		for (int i = 0; i < 8; i++)
		{
			playerOne[i] = new ChessPiece("pawn", true, 60 + i);
			playerTwo[i] = new ChessPiece("pawn", false, 10 + i);
		}
		playerOne[8] = new ChessPiece("rook", true, 70);
		playerOne[9] = new ChessPiece("rook", true, 77);
		playerOne[10] = new ChessPiece("night", true, 71);
		playerOne[11] = new ChessPiece("night", true, 76);
		playerOne[12] = new ChessPiece("bishop", true, 72);
		playerOne[13] = new ChessPiece("bishop", true, 75);
		playerOne[14] = new ChessPiece("qrookbishop", true, 74);
		playerOne[15] = new ChessPiece("king", true, 73);

		playerTwo[8] = new ChessPiece("rook", false, 0);
		playerTwo[9] = new ChessPiece("rook", false, 7);
		playerTwo[10] = new ChessPiece("night", false, 1);
		playerTwo[11] = new ChessPiece("night", false, 6);
		playerTwo[12] = new ChessPiece("bishop", false, 2);
		playerTwo[13] = new ChessPiece("bishop", false, 5);
		playerTwo[14] = new ChessPiece("qrookbishop", false, 4);
		playerTwo[15] = new ChessPiece("king", false, 3);
	}

	public static void initializeBoard()
	{
		for (ChessPiece piece: playerOne)
			board[piece.position/10][piece.position%10] = piece.number();
		for (ChessPiece piece: playerTwo)
			board[piece.position/10][piece.position%10] = piece.number();
		for (int i = 2; i < 6; i++)
		{
			for (int k = 0; k < 8; k++)
				board[i][k] = 0;
		}
	}

	public static void main(String[] args)
	{
		whiteTurn = true;
		playerOne = new ChessPiece[16];
		playerTwo = new ChessPiece[16];
		initializePieces();
		board = new int[8][8];
		initializeBoard();
		playGame();
	}

	public static void playGame()
	{
		String player = "white";
		Scanner keyIn = new Scanner(System.in);
		String response = "";
		while(gameIsNotOver(whiteTurn))
		{
			printBoard();
			System.out.println();
			if (whiteTurn)
				player = "white";
			else
				player = "black";
			if (check())
			{
				if (!checkMate())
					System.out.println("Check!");
				else
				{
					System.out.println("Checkmate! " + player + " wins!");
					break;
				}
			}
			if (staleMate())
			{
				System.out.println("Stalemate!");
				break;
			}
			System.out.println(player + "'s Turn! Make your move: ");
			response = keyIn.nextLine();
			while (invalidMove(response.toLowerCase()))
			{
				System.out.println("Invalid Move! Please make a valid move: ");
				response = keyIn.nextLine();
			}
			makeMove(response.toLowerCase());
			recordMove(response);
			whiteTurn = !whiteTurn;
		}
		System.out.println("GG! Type \"log\" to see a log of your game! Otherwise, the program will end.");
		response = keyIn.nextLine();
		if (response.equalsIgnoreCase("log"))
			openFile();
		keyIn.close();
	}

	public static void openFile()
	{
		System.out.print("\f");
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader("record"));
			String line;
			line = reader.readLine();
			while (line != null)
			{
				System.out.println(line);
				line = reader.readLine();
			}
			reader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void recordMove(String move)
	{
		if (File.createNewFile()
		BufferedWriter writer = null;
		try
		{
			File record = new File("record");
			writer = new BufferedWriter(new FileWriter(record));
			writer.write(move + "\n");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public static boolean clearPath (boolean kingSide)
	{
		if (whiteTurn)
		{
			if (kingSide)
				return board[7][1] == 0 && board[7][2] == 0;
			return board[7][4] == 0 && board[7][5] == 0 && board[7][6] == 0;
		}
		if (kingSide)
			return board[0][1] == 0 && board[0][2] == 0;
		return board[0][4] == 0 && board[0][5] == 0 && board[0][6] == 0;
	}

	public static boolean invalidMove(String move)
	{
		ArrayList<String> moves = new ArrayList<String>();
		String build = "";
		ChessPiece[] player = playerOne;
		if (whiteTurn)
			player = playerTwo;
		if	(move.equals("0-0"))
			return clearPath(true) && !player[15].moved && !player[8].moved;
		if (move.equals("0-0-0"))
			return clearPath(false) && !player[15].moved && !player[9].moved;
		for (ChessPiece piece: player)
		{
			if (piece.position != 666)
			{
				int save = piece.position;
				build = piece.pieceName.substring(0, 1);
				if (whiteTurn)
					build = build.toUpperCase();
				for (int i: getPossibleMoves(piece))
				{
					piece.position = i;
					if (!check())
						moves.add(build + ((char)(i%10 + 97)) + (8 - (i/10)));
					piece.position = save;
				}
			}
		}
		return moves.contains(move);
	}

	public static boolean gameIsNotOver(boolean whiteTurn)
	{
		return !staleMate() && !checkMate();
	}

	public static boolean outOfBounds(int row, int col)
	{
		return row < 0 || row > 7 || col < 0 || col > 7;
	}

	public static Set<Integer> getPossibleMoves(ChessPiece piece)
	{
		int row = piece.position/10;
		int col = piece.position%10;
		Set<Integer> moves = new TreeSet<Integer>();
		int team = 1;
		if (piece.color)
			team = -1;
		if (piece.pieceName.equals("pawn"))
		{
			if (piece.color)
			{
				moves.add(piece.position - 10);
				if (!outOfBounds(row - 1, col - 1) && board[row - 1][col - 1] < 0)
					moves.add(piece.position - 11);
				if (!outOfBounds(row - 1, col + 1) && board[row - 1][col + 1] < 0)
					moves.add(piece.position - 9);
				if (!piece.moved)
					moves.add(piece.position - 20);
			}
			else
			{
				moves.add(piece.position + 10);
				if (!outOfBounds(row + 1, col - 1) && board[row + 1][col - 1] < 0)
					moves.add(piece.position + 11);
				if (!outOfBounds(row + 1, col + 1) && board[row + 1][col + 1] < 0)
					moves.add(piece.position + 9);
				if (!piece.moved)
					moves.add(piece.position + 20);
			}
		}
		if (piece.pieceName.contains("rook"))
		{
			for (int i = 0; i < 80; i+=10)
			{
				if (!(outOfBounds(row + i, col)) && board[row + i][col] < team)
				{
					moves.add(piece.position + i);
					if (board[row + i][col] != 0)
					{
						moves.clear();
					}
				}
				if (!outOfBounds(row, col + (i/10)) && board[row][col + (i/10)] < team)
				{
					moves.add(piece.position + (i/10));
					if (board[row][col + (i/10)] != 0)
						break;
				}
			}
			for (int i = 0; i > 0; i-=10)
			{
				if (!(outOfBounds(row + i, col)) && board[row + i][col] < team)
				{
					moves.add(piece.position + i);
					if (board[row + i][col] != 0)
						break;
				}
				if (!outOfBounds(row, col + (i/10)) && board[row][col + (i/10)] < team)
				{
					moves.add(piece.position + (i/10));
					if (board[row][col + (i/10)] != 0)
						break;
				}
			}
		}
		if (piece.pieceName.equals("night"))
		{
			moves.add(piece.position + 19);
			moves.add(piece.position + 21);
			moves.add(piece.position + 8);
			moves.add(piece.position + 12);
			moves.add(piece.position - 19);
			moves.add(piece.position - 21);
			moves.add(piece.position - 8);
			moves.add(piece.position - 12);
		}
		if (piece.pieceName.contains("bishop"))
		{
			for(int i = 0; i < 8; i++)
			{
				moves.add(piece.position + 9);
				moves.add(piece.position + 11);
				moves.add(piece.position - 9);
				moves.add(piece.position - 11);
			}
		}
		if (piece.pieceName.equals("king"))
		{
			moves.add(piece.position + 9);
			moves.add(piece.position + 10);
			moves.add(piece.position + 11);
			moves.add(piece.position + 1);
			moves.add(piece.position - 1);
			moves.add(piece.position - 11);
			moves.add(piece.position - 10);
			moves.add(piece.position - 9);
		}
		Set<Integer> possibleMoves = new TreeSet<Integer>();
		for (int i: moves)
		{
			ChessPiece[] player = playerOne;
			piece.position = i;
			if (!piece.color)
				player = playerTwo;
			if (!outOfBounds(i/10, i%10))
			{
				for (ChessPiece ally: player)
				{
					if (!(i == ally.position))
						possibleMoves.add(i);
				}
			}
		}
		return possibleMoves;
	}

	public static ChessPiece findPiece(String move)
	{
		ChessPiece [] pieces = playerOne;
		if (!whiteTurn)
			pieces = playerTwo;
		for (ChessPiece piece: pieces)
		{
			if (move.charAt(0) == piece.pieceName.charAt(0))
			{
				if (move.length() == 3)
					return piece;
				String s = "" + (8 - Integer.parseInt(move.substring(2,3))) + (move.charAt(1) - 97);
				if (Integer.parseInt(s) == piece.position)
					return piece;
			}
		}
		System.out.println("Could not find piece!");
		return null;
	}

	public static void makeMove(String move)
	{
		if (move.equals("0-0"))
				castle(true);
		else if (move.equals("0-0-0"))
				castle(false);
		else
		{
			ChessPiece piece = findPiece(move);
			board[piece.position/10][piece.position % 10] = 0;
			int col = move.charAt(move.length() - 2) - 97;
			int row = 8 - Integer.parseInt(move.substring(move.length()-1, move.length()));
			board[row][col] = piece.number();
			piece.position = (row * 10) + col;
			piece.moved = true;
			ChessPiece[] enemy = playerOne;
			if (whiteTurn)
				enemy = playerTwo;
			for (ChessPiece capture: enemy)
			{
				if (piece.position == capture.position)
				{
					capture.position = 666;
					break;
				}
			}
			if (piece.pieceName.equals("pawn") && piece.position/10 == 0)
				piece.transform();
		}
	}

	public static boolean staleMate()
	{
		ChessPiece[] player = playerOne;
		if (!whiteTurn)
			player = playerTwo;
		int save = player[15].position;
		for (int i: getPossibleMoves(player[15]))
		{
			player[15].position = i;
			if (!check())
				return false;
			player[15].position = save;
		}
		return !check();
	}

	public static boolean checkMate()
	{
		if (whiteTurn)
			return getPossibleMoves(playerOne[15]).isEmpty();
		return getPossibleMoves(playerTwo[15]).isEmpty();
	}

	public static boolean check()
	{
		ChessPiece[] player = playerOne;
		ChessPiece[] other = playerTwo;
		if (!whiteTurn)
		{
			player = playerTwo;
			other = playerOne;
		}
		for (ChessPiece piece: other)
		{
			Set<Integer> moves = getPossibleMoves(piece);
			for (int i: moves)
			{
				if (player[15].position == i)
					return true;
			}
		}
		return false;
	}

	public static void printBoard()
	{
		boolean flip = false;
		for (int i = 0; i < 8; i++)
			System.out.print("   " + (char)(i + 97));
		System.out.println();
		for (int i = 0; i < 17; i++)
		{
			if (flip)
				System.out.print(8 - (i/2));
			else
				System.out.print(" ");
			for (int k = 0; k < 8; k++)
			{
				if (flip)
				{
					if (board[i/2][k] == 0)
						System.out.print("|   ");
					else if (board[i/2][k] == 1)
						System.out.print("| P ");
					else if (board[i/2][k] == 2)
						System.out.print("| R ");
					else if (board[i/2][k] == 3)
						System.out.print("| N ");
					else if (board[i/2][k] == 4)
						System.out.print("| B ");
					else if (board[i/2][k] == 5)
						System.out.print("| Q ");
					else if (board[i/2][k] == 6)
						System.out.print("| K ");
					else if (board[i/2][k] == -1)
						System.out.print("| p ");
					else if (board[i/2][k] == -2)
						System.out.print("| r ");
					else if (board[i/2][k] == -3)
						System.out.print("| n ");
					else if (board[i/2][k] == -4)
						System.out.print("| b ");
					else if (board[i/2][k] == -5)
						System.out.print("| q ");
					else
						System.out.print("| k ");
				}
				else
					System.out.print("+ - ");
			}
			if (flip)
				System.out.println("|");
			else
				System.out.println("+");
			flip = !flip;
		}
		for (int i = 0; i < 8; i++)
			System.out.print("   " + (char)(i + 97));

	}

	public static void castle(boolean shortSide)
	{
		ChessPiece[] player = playerOne;
		int row = 7;
		int color = 1;
		if (!whiteTurn)
		{
			player = playerTwo;
			row = 0;
			color = -1;
		}
		board[row][3] = 0;
		player[15].moved = true;
		if (shortSide)
		{
			board[row][0] = 0;
			board[row][2] = 6 * color; 
			board[row][3] = 2 * color;
			player[15].position = (row * 10)+ 2;
			player[8].position = (row * 10) + 3;
			player[8].moved = true;
		}
		else
		{
			board[row][7] = 0;
			board[row][6] = 6 * color;
			board[row][5] = 2 * color;
			player[15].position = (row * 10) + 6;
			player[9].position = (row * 10) + 5;
			player[9].moved = true;
		}
	}
}
