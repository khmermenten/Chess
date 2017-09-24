import java.util.*;
import java.io.*;
public class Chess{
	public static int [][] board;
	public static ChessPiece[] playerOne;
	public static ChessPiece[] playerTwo;
	public static boolean whiteTurn;
	public static int gameNumber;
	public Chess()
	{
		playerOne = new ChessPiece[16];
		playerTwo = new ChessPiece[16];
		board = new int[8][8];
		gameNumber = 1;
	}

	public static void initializePieces()
	{
		for (int i = 0; i < 16; i++)
		{
			playerOne[i] = new ChessPiece("rook", true, 666);
			playerTwo[i] = new ChessPiece("rook", true, 666);
		}
		//		for (int i = 0; i < 8; i++)
		//		{
		//			playerOne[i] = new ChessPiece("pawn", true, 60 + i);
		//			playerTwo[i] = new ChessPiece("pawn", false, 10 + i);
		//		}
		//		playerOne[8] = new ChessPiece("rook", true, 70);
		//		playerOne[9] = new ChessPiece("rook", true, 77);
		//		playerOne[10] = new ChessPiece("night", true, 71);
		//		playerOne[11] = new ChessPiece("night", true, 76);
		//		playerOne[12] = new ChessPiece("bishop", true, 72);
		//		playerOne[13] = new ChessPiece("bishop", true, 75);
		playerOne[14] = new ChessPiece("pawn", true, 43);
		playerOne[11] = new ChessPiece("pawn", true, 61);
		//		playerOne[15] = new ChessPiece("king", true, 73);
		//
		//		playerTwo[8] = new ChessPiece("rook", false, 0);
		//		playerTwo[9] = new ChessPiece("rook", false, 7);
		//		playerTwo[10] = new ChessPiece("night", false, 1);
		//		playerTwo[11] = new ChessPiece("night", false, 6);
		//		playerTwo[12] = new ChessPiece("bishop", false, 2);
		//		playerTwo[13] = new ChessPiece("bishop", false, 5);
		//		playerTwo[14] = new ChessPiece("qrookbishop", false, 4);
		playerTwo[15] = new ChessPiece("pawn", false, 12);
		playerTwo[12] = new ChessPiece("pawn", false, 42);
	}

	public static void initializeBoard() throws IOException
	{
		//		for (ChessPiece piece: playerOne)
		//			board[piece.position/10][piece.position%10] = piece.number();
		//		for (ChessPiece piece: playerTwo)
		//			board[piece.position/10][piece.position%10] = piece.number();
		for (int i = 0; i < 8; i++)
		{
			for (int k = 0; k < 8; k++)
				board[i][k] = 0;
		}
		board[6][1] = 1;
		board[4][3] = 1;
		board[1][2] = -1;
		board[4][2] = -1;
	}

	public static void main(String[] args) throws IOException
	{
		gameNumber = 1;
		File file = new File("chess" + gameNumber + ".txt");
		while (!file.createNewFile())
		{
			gameNumber++;
			file = new File("chess" + gameNumber + ".txt");
		}
		whiteTurn = true;
		playerOne = new ChessPiece[16];
		playerTwo = new ChessPiece[16];
		initializePieces();
		board = new int[8][8];
		initializeBoard();
		playGame();
	}

	public static void gameState()
	{
		for (ChessPiece piece: playerOne)
			System.out.println(piece.toString());
		for (ChessPiece piece: playerTwo)
			System.out.println(piece.toString());
	}

	public static void playGame() throws IOException
	{
		ArrayList<String> log = new ArrayList<String>();
		String player = "";
		Scanner keyIn = new Scanner(System.in);
		String response = "";
		while(!staleMate())
		{
			if (whiteTurn)
				player = "White";
			else
				player = "Black";
			if (check())
			{
				if (!checkMate())
					System.out.println("Check!");
				else
				{
					if (whiteTurn)
						player = "Black";
					else
						player = "White";
					System.out.println("Checkmate! " + player + " wins!");
					break;
				}
			}
			printBoard();
			System.out.println(player + "'s Turn! Make your move: ");
			response = keyIn.nextLine();
			while (invalidMove(response) || response.equalsIgnoreCase("save") || response.equalsIgnoreCase("load"))
			{
				if (response.equalsIgnoreCase("load"))
				{
					load();
					printBoard();
					System.out.println("Game loaded! Make your move: ");
				}
				else if (response.equalsIgnoreCase("save"))
				{
					save();
					System.out.println("Game saved! \nMake your move: ");
				}
				else 
					System.out.println("Invalid Move! Please make a valid move: ");
				response = keyIn.nextLine();
			}
			makeMove(response);
			log.add(response);
			whiteTurn = !whiteTurn;
		}
		printBoard();
		if (staleMate())
			System.out.println("Stalemate! " + player + " wins!");
		System.out.println("GG! Type \"log\" to save a log of your game! Otherwise, the program will end.");
		response = keyIn.nextLine();
		if (response.equalsIgnoreCase("log"))
			record(log);
		keyIn.close();
	}

	public static void save() throws IOException
	{
		File saveData = new File("Save.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(saveData));
		writer.flush();
		writer.write(gameNumber);
		writer.newLine();
		writer.write(whiteTurn + "");
		writer.newLine();
		for (int i = 0; i < 8; i ++)
		{
			for (int k = 0; k < 8; k++)
			{
				writer.write(board[i][k] + "");
				writer.newLine();
			}
		}
		for (ChessPiece piece: playerOne)
		{
			writer.write(piece.toString());
			writer.newLine();
		}
		for (ChessPiece piece: playerTwo)
		{
			writer.write(piece.toString());
			writer.newLine();
		}
		writer.close();
	}

	public static void load()
	{
		Scanner scan = new Scanner("Save.txt");
		String line = scan.nextLine();
		gameNumber = Integer.parseInt(line);
		line = scan.nextLine();
		whiteTurn = Boolean.parseBoolean(line);
		line = scan.nextLine();
		for (int i = 0; i < 8; i++)
		{
			for (int k = 0; k < 8; k++)
			{
				board[i][k] = Integer.parseInt(line);
				line = scan.nextLine();
			}
		}
		for (ChessPiece piece: playerOne)
		{
			String[] temp = line.split(" ");
			piece = new ChessPiece(temp[0], Boolean.parseBoolean(temp[1]), Integer.parseInt(temp[2]));
			piece.moved = Boolean.parseBoolean(temp[3]);
			line = scan.nextLine();
		}
		for (ChessPiece piece: playerTwo)
		{
			String[] temp = line.split(" ");
			piece = new ChessPiece(temp[0], Boolean.parseBoolean(temp[1]), Integer.parseInt(temp[2]));
			piece.moved = Boolean.parseBoolean(temp[3]);
			line = scan.nextLine();
		}
		scan.close();
	}

	public static void record(ArrayList<String> log) throws IOException
	{
		File record = new File("chess" + gameNumber + ".txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(record));
		for (String move: log)
		{
			System.out.println(move);
			writer.write(move);
			writer.newLine();
		}
		writer.close();
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

	public static Set<String> allPossibleMoves()
	{
		ChessPiece[] player = playerOne;
		if (!whiteTurn)
			player = playerTwo;
		Set<String> moves = new TreeSet<String>();
		for (ChessPiece piece: player)
		{
			String build = "";
			if (piece.position != 666)
			{
				int save = piece.position;
				for (int i: getPossibleMoves(piece))
				{
					System.out.println(piece.toString());
					System.out.println(i);
					build = piece.pieceName.substring(0, 1);
					if (whiteTurn)
						build = build.toUpperCase();
					build += (char)(save%10 + 97);
					build += 8 - save/10;
					build += (char)(i%10 + 97);
					build += 8 - i/10;
					System.out.println(build);
					piece.position = i;
					if (!check())
					{
						moves.add(build);
						moves.add(build.charAt(0) + build.substring(3,  5));
					}
					piece.position = save;
				}
			}
		}
		return moves;
	}

	public static boolean invalidMove(String move)
	{
		ChessPiece[] player = playerOne;
		if (!whiteTurn)
			player = playerTwo;
		if	(move.equalsIgnoreCase("0-0"))
			return clearPath(false) && player[15].moved && player[8].moved;
		if (move.equalsIgnoreCase("0-0-0"))
			return clearPath(true) && player[15].moved && player[9].moved;
		return !allPossibleMoves().contains(move);
	}

	public static Set<Integer> getPossibleMoves(ChessPiece piece)
	{
		int row = piece.position/10;
		int col = piece.position%10;
		Set<Integer> moves = new TreeSet<Integer>();
		Set<Integer> possibleMoves = new TreeSet<Integer>();
		int team = 1;
		if (!piece.color)
			team = -1;
		if (piece.pieceName.equals("pawn"))
		{
			ChessPiece [] player = playerOne;
			if (whiteTurn)
				player = playerTwo;
			for (ChessPiece enemy: player)
			{
				if (enemy.pieceName.equals("pawn") && enemy.position == piece.position - 1 && enemy.enPassant)
					possibleMoves.add(piece.position - (10 * team) - 1);
				if (enemy.pieceName.equals("pawn") && enemy.position == piece.position + 1 && enemy.enPassant)
					possibleMoves.add(piece.position - (10 * team) + 1);
			}
			moves.add(piece.position - (10 * team));
			if (!outOfBounds(row - team, col - 1) && board[row - team][col - 1] * team < 0)
				possibleMoves.add(piece.position - (10 * team) - 1);
			if (!outOfBounds(row - team, col + 1) && board[row - team][col + 1] * team < 0)
				possibleMoves.add(piece.position - (10 * team) + 1);
			if (!piece.moved)
				possibleMoves.add(piece.position - (20 * team));
		}
		if (piece.pieceName.contains("rook"))
		{
			int i = col - 1;
			//explore left
			while (!(outOfBounds(row, i)) && board[row][i] * team <= 0)
			{
				possibleMoves.add((row * 10) + i);
				if (board[row][i] != 0) //enemy team
					break;
				i--;
			}
			i = col + 1;
			//explore right
			while (!(outOfBounds(row, i)) && board[row][i] * team <= 0)
			{
				possibleMoves.add((row * 10) + i);
				if (board[row][i] != 0) //enemy team
					break;
				i++;
			}
			i = row - 1;
			//explore up
			while (!(outOfBounds(i, col)) && board[i][col] * team <= 0)
			{
				possibleMoves.add((i * 10) + col);
				if (board[i][col] != 0) //enemy team
					break;
				i--;
			}
			i = row + 1;
			//explore down
			while (!(outOfBounds(i, col)) && board[i][col] * team <= 0)
			{
				possibleMoves.add((i * 10) + col);
				if (board[i][col] != 0) //enemy team
					break;
				i++;
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
			int i = piece.position - 11;
			//explore top left
			while (!(outOfBounds(i/10, i%10)) && board[i/10][i%10] * team <= 0)
			{
				possibleMoves.add(i);
				if (board[i/10][i%10] != 0) //enemy team
					break;
				i -= 11;
			}
			//explore top right
			i = piece.position - 9;
			while (!(outOfBounds(i/10, i%10)) && board[i/10][i%10] * team <= 0)
			{
				possibleMoves.add(i);
				if (board[i/10][i%10] != 0) //enemy team
					break;
				i -= 9;
			}
			//explore bottom left
			i = piece.position + 9;
			while (!(outOfBounds(i/10, i%10)) && board[i/10][i%10] * team <= 0)
			{
				possibleMoves.add(i);
				if (board[i/10][i%10] != 0) //enemy team
					break;
				i += 9;
			}
			//explore bottom right
			i = piece.position + 11;
			while (!(outOfBounds(i/10, i%10)) && board[i/10][i%10] * team <= 0)
			{
				possibleMoves.add(i);
				if (board[i/10][i%10] != 0) //enemy team
					break;
				i += 11;
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
		for (int i: moves)
		{
			if (!outOfBounds(i/10, i%10) && board[i/10][i%10] * team <= 0)
				possibleMoves.add(i);
		}
		return possibleMoves;
	}

	public static boolean outOfBounds(int row, int col)
	{
		return row < 0 || row > 7 || col < 0 || col > 7;
	}

	public static ChessPiece findPiece(String move)
	{
		ChessPiece [] player = playerOne;
		if (!whiteTurn)
			player = playerTwo;
		for (ChessPiece piece: player)
		{
			if (piece.pieceName.charAt(0) == move.toLowerCase().charAt(0))
			{
				if (move.length() == 3)
				{
					int i = 10 * (8 - Integer.parseInt("" + move.charAt(2)));
					i +=((int)(move.charAt(1) - 97));
					if (getPossibleMoves(piece).contains(i))
						return piece;
				}
				else
				{
					String s = "" + (8 - Integer.parseInt(move.substring(2,3))) + (move.charAt(1) - 97);
					if (move.length() == 5 && Integer.parseInt(s) == piece.position)
						return piece;
				}
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
			ChessPiece[] enemy = playerOne;
			if (whiteTurn)
				enemy = playerTwo;
			for (ChessPiece capture: enemy)
			{
				if ((piece.pieceName.equals("pawn")&& Math.abs(piece.position - capture.position) == 1 && capture.enPassant) ||
					(piece.position == capture.position))
				{
					board[capture.position/10][capture.position%10] = 0;
					capture.position = 666;
					break;
				}
			}
			if (piece.pieceName.equals("pawn") && Math.abs(row - (piece.position/10)) == 2)
				piece.enPassant = true;
			else
				piece.enPassant = false;
			board[row][col] = piece.number();
			piece.position = (row * 10) + col;
			piece.moved = true;
			if (piece.pieceName.equals("pawn"))
			{
				if (piece.color && piece.position/10 == 0)
					piece.transform();
				if (piece.color && piece.position/10 == 7)
					piece.transform();
			}

		}
	}
	
	public static boolean staleMate()
	{
		return !check() && allPossibleMoves().isEmpty();
	}

	public static boolean checkMate()
	{
		return allPossibleMoves().isEmpty();
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
		System.out.flush();
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
		System.out.println();
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
			board[row][1] = 6 * color; 
			board[row][2] = 2 * color;
			player[15].position = (row * 10) + 2;
			player[8].position = (row * 10) + 3;
			player[8].moved = true;
		}
		else
		{
			board[row][7] = 0;
			board[row][5] = 6 * color;
			board[row][4] = 2 * color;
			player[15].position = (row * 10) + 6;
			player[9].position = (row * 10) + 5;
			player[9].moved = true;
		}
	}
}
