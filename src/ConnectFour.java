/* Notes
figure out how to color R and B on terminal
*/

//program for 2 players to play Connect Four
public class ConnectFour {
	public static final int rows = 6;
	public static final int columns = 7;
	public static final int start = 1;
	public static final int full = 42;
	public static final char red = 'R';
	public static final char black = 'B';
	public static final int draw = 2;
	private char[][] board;
	private String[] names;
	private int numTurns;
	//player 2
	public ConnectFour(String player1, String player2) {
		board = createBoard();
		names = new String[2];
		names[0] = player1;
		names[1] = player2;
		numTurns = 0;
	}
	
	private boolean isYourTurn(boolean goesFirst) {
		//if you were first.
		if (numTurns %2 == 0 && goesFirst) {
			return true;
		} 
		if (numTurns % 2 == 1 && !goesFirst) {
			return true;
		}
		return false;
	}
	
	public String getServerName() {
		return names[0].equals("Server") ? names[0]:names[1];
	}

	public String turns(String input, boolean who) {
		// check if valid column
		//System.out.println(names[0] + " name 0,     name 1" + names[1]);
		//System.out.println("name" +  getServerName());
		if (!checkInput(input) || getServerName().length() == 0) {
			System.out.println(input + " is not a choice");
			System.out.println("check input" + !checkInput(input) );
			System.out.println("server name" + (getServerName().length() == 0));
			System.out.println(names[0] +  "   " + names[1]);
			return null;
		}
		
		if (!isYourTurn(who)) {
			System.out.println(input + " is not your turn");
			return null;
		}
		numTurns++;
		//player = !player; //change player side
		
		// figure out which piece this person is
		int choice = Character.getNumericValue(input.charAt(1));
		char piece = black;
		if (numTurns % 2 == 1) {
			piece = red;
		}
		// update board and check for a win
		boardArray(choice, piece);
		if (winGame(piece)) {
			if (numTurns % 2 == 1) {
				System.out.println("System: " + names[0] + " wins!!");
			} else {
				System.out.println("System: " + names[1] + " wins!!");
			}
			String b= printBoard("Final");
			System.out.println(b);
			names[0] = "";
			names[1] = "";
			return b;
		} else if (numTurns == 42) {
			System.out.println("System: The game is a draw.");
			String board = printBoard("Final");
			names[0] = "";
			names[1] = "";
			return board;
		} else {
			return printBoard("Current");
		}
	}

	private boolean checkInput(String input) {
		System.out.println(input);
		if (input.length() != 2 || input.charAt(0) != 'c' || !Character.isDigit(input.charAt(1))) {
			//System.out.println("herrre");
			return false;
		}
		int result = Character.getNumericValue(input.charAt(1));
		if (result < 1 || result > 7) {
			System.err.println("INVALID COLUMN");
			return false;
		}
		if (!openColumn(result)) {
			System.out.println("This column is full. Try again");
			return false;
		}
		return true;
	}

	// updates the board with player's piece
	public void boardArray(int columnChoice, char pieces) {
		int indexOfRow = rows;
		// start from bottom, and keep going up column if position is filled
		while (board[indexOfRow][columnChoice] != '_') {
			indexOfRow--;
		}
		board[indexOfRow][columnChoice] = pieces;
	}

	// creates initial array of game board positions with a buffer zone
	private char[][] createBoard() {
		int border = 2;
		char[][] board = new char[rows + border][columns + border];
		for (int r = start; r <= rows; r++) {
			for (int c = start; c <= columns; c++) {
				board[r][c] = '_';
			}
		}
		return board;
	}


	// checks if there's an open spot in column
	private boolean openColumn(int columnNum) {
		for (int r = rows; r >= 1; r--) {
            System.out.println(board[r][columnNum]);
			if (board[r][columnNum] == '_') {
				return true;
			}
		}
		return false;
	}

	// prints out the board with the pieces
	public String printBoard(String version) {
		StringBuilder sb = new StringBuilder();
		sb.append(version + " Board\n");
		sb.append("1       2       3       4       5       6       7  column numbers\n");
		//System.out.println(version + " Board");
		//System.out.println("1 2 3 4 5 6 7  column numbers");
		for (int r = start; r <= rows; r++) {
			for (int c = start; c <= columns; c++) {
				sb.append(board[r][c] + "       ");
				//System.out.print(board[r][c]);
				//System.out.print(" ");
			}
			sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	// general method to search for 4 in row in certain direction
	private boolean searchForConnection(int r, int c, char piece, int incrementRow, int incrementColumn) {
		int connection = 0;
		int increment = 1;
		int neededConnection = 3;
		while (connection != neededConnection
				&& board[r - increment * incrementRow][c + increment * incrementColumn] == piece) {
			connection++;
			increment++;
		}
		return connection == neededConnection;
	}

	// checks entire board for a win
	private boolean winGame(char piece) {
		for (int r = start; r <= rows; r++) {
			for (int c = start; c <= columns; c++) {
				// if that element is the player's piece, search in 4 directions for 3
				// connection
				if (board[r][c] == piece) {
					boolean connectVertical = searchForConnection(r, c, piece, 1, 0);
					boolean connectHorizontal = searchForConnection(r, c, piece, 0, 1);
					boolean connectLowerLeftDiagonal = searchForConnection(r, c, piece, 1, -1);
					boolean connectLowerRightDiagonal = searchForConnection(r, c, piece, 1, 1);
					boolean connection = connectVertical || connectHorizontal || connectLowerLeftDiagonal
							|| connectLowerRightDiagonal;
					if (connection) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
