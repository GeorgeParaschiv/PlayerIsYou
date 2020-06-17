package game;
//George Paraschiv & Arthur Xu
//PLayerIsYou
//ICS4U1

import java.awt.*;
// Needed for ActionListener
import java.awt.event.*;
import javax.swing.*;
//Allows File Access
import java.io.*;
import javax.imageio.ImageIO;
//Needed for Scanner
import java.util.Scanner;
import java.util.ArrayList;

// Main Class that contains all the other classes that implements Key and Action Listener and extends JFrame
class PlayerIsYou extends JFrame implements KeyListener, ActionListener {

	// Level
	Level level;

	// Flags that racks if you are playing a level, are on a select screen, are on
	// the start screen, or have just beat the level
	boolean playing = false;
	boolean startscreen = true;
	boolean selectscreen = false;
	boolean win = false;

	// Game drawArea
	DrawArea game = new DrawArea(800, 500);

	// JButtons
	JButton start = new JButton("Start");
	JButton levelselect = new JButton("Level Select");
	JButton instructions = new JButton("Instructions");
	JButton quit = new JButton("Quit");
	JButton level1 = new JButton("1");
	JButton level2 = new JButton("2");
	JButton level3 = new JButton("3");
	JButton level4 = new JButton("4");
	JButton level5 = new JButton("5");
	JButton levelF = new JButton("?");

	// Flags for level completion
	boolean clear1 = false;
	boolean clear2 = false;
	boolean clear3 = false;
	boolean clear4 = false;
	boolean clear5 = false;

	// Checks if the level has changed
	boolean change = false;

	// Stack that tracks every state of the game
	Stack history = new Stack();

	// Constructor for the GUI aspect of the game
	public PlayerIsYou() {

		// JButtons put into an array so that their attributes can be more easily
		// changed, saving space
		JButton[] x = new JButton[] { start, levelselect, instructions, quit, level1, level2, level3, level4, level5,
				levelF };

		// Changing attributes of JButtons
		for (int i = 0; i < x.length; i++) {
			x[i].addActionListener(this);
			x[i].setFont(new Font("Tahoma", Font.BOLD, 20));
			x[i].setBorderPainted(false);
			if (i < 4) {
				x[i].setBackground(Color.BLACK);
				x[i].setForeground(Color.WHITE);
				x[i].setFocusPainted(true);
			} else {
				if (i == 4)
					x[i].setForeground(Color.WHITE);
				else
					x[i].setForeground(Color.GRAY);
				x[i].setOpaque(false);
				x[i].setContentAreaFilled(false);
			}
		}

		// KeyListener
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		// Area for game to be displayed
		game.setLayout(null);
		add(game);

		// Setting the Window Attributes
		pack();
		setTitle("Player Is You");
		setSize(816, 539); // 40x25 game play screen if each object is 20x20 pixels
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	// Handles the key press events
	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		// Up, Down, Left, and Right Arrow keys only call level.move() if the user is
		// playing a level
		case KeyEvent.VK_UP:
			if (playing == true) {
				level.move(-1, 0);
				repaint();
			}
			break;
		case KeyEvent.VK_DOWN:
			if (playing == true) {
				level.move(1, 0);
				repaint();
			}
			break;
		case KeyEvent.VK_LEFT:
			if (playing == true) {
				level.move(0, -1);
				repaint();
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (playing == true) {
				level.move(0, 1);
				repaint();
			}
			break;
		// Escape navigates to the start screen, but prompts for confirmation if playing
		// a level
		case KeyEvent.VK_ESCAPE:
			if (playing == true) {
				int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit to the main menu?",
						"Exit Level?", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					game.removeAll();
					playing = false;
					startscreen = true;
					selectscreen = false;
					win = false;
					repaint();
				}
			} else if (selectscreen == true) {
				game.removeAll();
				playing = false;
				startscreen = true;
				selectscreen = false;
				win = false;
				repaint();
			}
			break;
		// If playing a level, R prompts the user to restart the level
		case KeyEvent.VK_R:
			if (playing == true) {
				int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to restart?", "Restart?",
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					level = new Level(level.getid());
					win = false;
					repaint();
				}
			}
			break;
		// If a level has been beaten Enter just cycles from the congratulations screen
		// to the level select screen
		case KeyEvent.VK_ENTER:
			if (playing == true && win == true) {
				playing = false;
				win = false;
				selectscreen = true;
				repaint();
			}
			break;
		// While playing a level, if z is pressed it will remove the top level of the
		// stack and set the level equal to the current one at the top of the stack
		case KeyEvent.VK_Z:
			if (playing == true) {
				history.pop();
				if (history.gettop() != null) {
					level.setLevel(history.gettop());
				}
				repaint();
			}
		}
	}

	// Action Listener
	public void actionPerformed(ActionEvent e) {
		// If start is pressed go straight into the first level
		if (e.getActionCommand().equals("Start")) {
			playing = true;
			startscreen = false;
			game.removeAll();
			win = false;
			level = new Level(1);
			repaint();
		}
		// If level select is pressed go to the level select screen
		else if (e.getActionCommand().equals("Level Select")) {
			startscreen = false;
			selectscreen = true;
			win = false;
			game.removeAll();
			repaint();
		}
		// If instructions is pressed display the instructions in a JOptionPane
		else if (e.getActionCommand().equals("Instructions")) {
			Object[] options = { "OK", };
			JOptionPane.showOptionDialog(null,
					"Your objective is to satisfy the win condition in any way. \n You control what the rules are. \nArrow Keys to move, R to restart. Z to undo. \nEsc to go back to the main menu screen.\nGood Luck!",
					"Instructions", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
					options[0]);
			repaint();
		}
		// If quit is pressed close the game
		else if (e.getActionCommand().equals("Quit")) {
			dispose();
			repaint();
		}
		// If the level buttons are pressed, display the corresponding level
		else if (e.getActionCommand().equals("1")) {
			win = false;
			selectscreen = false;
			playing = true;
			game.removeAll();
			level = new Level(1);
			repaint();
		} else if (e.getActionCommand().equals("2")) {
			if (clear1 == true) {
				win = false;
				selectscreen = false;
				playing = true;
				game.removeAll();
				level = new Level(2);
				repaint();
			}
		} else if (e.getActionCommand().equals("3")) {
			if (clear2 == true) {
				win = false;
				selectscreen = false;
				playing = true;
				game.removeAll();
				level = new Level(3);
				repaint();
			}
		} else if (e.getActionCommand().equals("4")) {
			if (clear3 == true) {
				win = false;
				selectscreen = false;
				playing = true;
				game.removeAll();
				level = new Level(4);
				repaint();
			}
		} else if (e.getActionCommand().equals("5")) {
			if (clear4 == true) {
				win = false;
				selectscreen = false;
				playing = true;
				game.removeAll();
				level = new Level(5);
				repaint();
			}
		} else if (e.getActionCommand().equals("?")) {
			if (clear5 == true) {
				win = false;
				selectscreen = false;
				playing = true;
				game.removeAll();
				level = new Level(6);
				repaint();
			}
		}
		repaint(); // refresh game display
	}

//DrawArea------------------------------------------------------------------------------------------------------
	
	// Redraws the game
	class DrawArea extends JPanel {

		// DrawArea Constructor
		public DrawArea(int width, int height) {
			this.setPreferredSize(new Dimension(width, height)); // size
		}

		// method that runs when repaint is called
		public void paintComponent(Graphics g) {

			// Getting the width and height of the game for scaling
			int width = game.getWidth();
			int height = game.getHeight();

			// Setting the bounds of the buttons so that they scale accordingly
			start.setBounds(width / 16 * 7, height / 20 * 11, width / 8, height / 50 * 3);
			levelselect.setBounds(width / 8 * 3, height / 20 * 13, width / 4, height / 50 * 3);
			instructions.setBounds(width / 8 * 3, height / 20 * 15, width / 4, height / 50 * 3);
			quit.setBounds(width / 20 * 9, height / 20 * 17, width / 10, height / 50 * 3);
			level1.setBounds(width / 16 * 5, height / 10 * 7, width / 16, height / 10);
			level2.setBounds(width / 16 * 9, height / 20 * 13, width / 16, height / 10);
			level3.setBounds(width / 8 * 5, height / 20 * 7, width / 16, height / 10);
			level4.setBounds(width / 8 * 3, height / 5, width / 16, height / 10);
			level5.setBounds(width / 32 * 5, height / 20 * 5, width / 16, height / 10);
			levelF.setBounds(width / 2, height / 10 * 4 + height / 50, width / 16, height / 10);

			// If playing but not won yet repaint the level
			if (playing == true && win == false)
				level.print(g);
			else if (playing == true && win == true && level.getid() == 6) {
				try {
					Image winscreen = ImageIO.read(PlayerIsYou.class.getResource("/backgrounds/finalscreen.png"));
					g.drawImage(winscreen, 0, 0, width, height, null);
				} catch (IOException e) {
					System.out.println("The winscreen image could not be found.");
				}
			}
			// If playing and won display congratulations screen until enter is pressed
			else if (playing == true && win == true && level.getid() != 6) {
				try {
					Image image1 = ImageIO.read(PlayerIsYou.class.getResource("/backgrounds/congratulations.png"));
					Image image2 = ImageIO.read(PlayerIsYou.class.getResource("/backgrounds/pressenter.png"));
					// Setting the boolean clear flags based on what level got beat
					if (clear1 == false && level.getid() == 1)
						clear1 = true;
					else if (clear2 == false && level.getid() == 2)
						clear2 = true;
					else if (clear3 == false && level.getid() == 3)
						clear3 = true;
					else if (clear4 == false && level.getid() == 4)
						clear4 = true;
					else if (clear5 == false && level.getid() == 5)
						clear5 = true;
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, width, height);
					g.drawImage(image1, width / 16 * 3, height / 250 * 81, width / 8 * 5, height / 250 * 86, null);
					g.drawImage(image2, width / 16 * 3, height / 5 * 4, width / 8 * 5, height / 250 * 38, null);
				} catch (IOException e) {
					System.out.println("The congratulations or pressenter image could not be found.");
				}
			}
			// If on the start screen add the 4 buttons start, level select, instructions,
			// and quit
			if (playing == false && startscreen == true) {
				try {
					Image image = ImageIO.read(PlayerIsYou.class.getResource("/backgrounds/startscreen.png"));
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, width, height);
					g.drawImage(image, width / 8, height / 10, width / 4 * 3, height / 250 * 82, null);
					game.add(start);
					game.add(levelselect);
					game.add(instructions);
					game.add(quit);
				} catch (IOException e) {
					System.out.println("The start screen image could not be found.");
				}
			}

			// If on the level select screen display the 5 level buttons
			// The secret level is only displayed if the other 5 are beaten
			// Each level is grayed out until the one before it has been beaten
			if (playing == false && selectscreen == true) {
				try {
					Image image = ImageIO.read(PlayerIsYou.class.getResource("/backgrounds/level.png"));
					game.removeAll();
					g.drawImage(image, 0, 0, width, height, null);
					game.add(level1);
					game.add(level2);
					game.add(level3);
					game.add(level4);
					game.add(level5);
					if (clear1) {
						level2.setForeground(Color.WHITE);
						if (clear2) {
							level3.setForeground(Color.WHITE);
							if (clear3) {
								level4.setForeground(Color.WHITE);
								if (clear4) {
									level5.setForeground(Color.WHITE);
									if (clear5) {
										game.add(levelF);
										levelF.setForeground(Color.WHITE);
									}
								}
							}
						}
					}
				} catch (IOException e) {
					System.out.println("The level select background image could not be found.");
				}
			}
		}
	}

	// Main Method
	public static void main(String[] args) {
		// Making the window visible
		PlayerIsYou window = new PlayerIsYou();
		window.setVisible(true);
	}

//Level---------------------------------------------------------------------------------------------------------

// Level Class
	class Level {

		// Level 2D array and its level id
		public Component[][] level;
		private int id;

		// Flags that keep track of what used to be controllable, moveable, winnable,
		// openable, and able to be shut
		char isup = 'S', isleft = 'S';
		char winup = 'S', winleft = 'S';
		char moveup = 'S', moveleft = 'S';
		char openup = 'S', openleft = 'S';
		char shutup = 'S', shutleft = 'S';

		// Constructor for the level, based off of the level id
		public Level(int levelid) {
			// Calling the import level method which will draw the level based on the id
			// number
			level = importlevel(levelid);
			// Finding out what the rules are at the beginning of the level
			decider();
			// Clearing the stack and adding the first state of the level to the stack
			history.clear();
			history.push(level);

			id = levelid;
		}

		// Method that sets the state of the level to a certain 2D array
		public void setLevel(Component[][] x) {
			for (int row = 0; row < level.length; row++) {
				for (int col = 0; col < level[0].length; col++) {
					level[row][col] = x[row][col];
				}
			}
		}

		// Getter method for the level id
		public int getid() {
			return id;
		}

		// Method that takes in a text file and converts it into a 2D level array
		private Component[][] importlevel(int levelid) {
			// Creating the basic 25x40 2D array for the level
			Component[][] level = new Component[25][40];

			// Scanner reads the text file row by row
			Scanner scan = new Scanner(PlayerIsYou.class.getResourceAsStream("/levels/" + levelid + ".txt"));
			for (int i = 0; i < 25; i++) {
				// Char array made up of each line split into characters
				char[] temp = scan.nextLine().toCharArray();
				for (int j = 0; j < 40; j++) {
					// 2D array created from the type method which differentiates the object from
					// its corresponding character
					level[i][j] = type(temp[j]);
				}
			}
			scan.close();
			return level;
		}

		// Simple Method to determine what each character in the text file represents
		public Component type(char c) {

			if (c == 'P')
				return new Player('P');
			else if (c == 'W')
				return new Wall('W');
			else if (c == 'S')
				return new Space('S');
			else if (c == 'F')
				return new Flag('F');
			else if (c == 'R')
				return new Rock('R');
			else if (c == 'K')
				return new Key('K');
			else if (c == 'G')
				return new Ghost('G');
			else if (c == 'D')
				return new Door('D');
			else if (c == 'B')
				return new Box('B');
			else if (c == 'i')
				return new Text('i', "/sprites/istext.png");
			else if (c == 'v')
				return new Text('v', "/sprites/wintext.png");
			else if (c == 'y')
				return new Text('y', "/sprites/youtext.png");
			else if (c == 'p')
				return new Text('p', "/sprites/playertext.png");
			else if (c == 'b')
				return new Text('b', "/sprites/boxtext.png");
			else if (c == 'f')
				return new Text('f', "/sprites/flagtext.png");
			else if (c == 'k')
				return new Text('k', "/sprites/keytext.png");
			else if (c == 'r')
				return new Text('r', "/sprites/rocktext.png");
			else if (c == 'g')
				return new Text('g', "/sprites/ghosttext.png");
			else if (c == 'd')
				return new Text('d', "/sprites/doortext.png");
			else if (c == 'm')
				return new Text('m', "/sprites/pushtext.png");
			else if (c == 's')
				return new Text('s', "/sprites/shuttext.png");
			else if (c == 'o')
				return new Text('o', "/sprites/opentext.png");
			else
				return new Space('S');
		}

		// Print method to display the actual game
		public void print(Graphics g) {

			// Getting the game width and height for scaling of the game
			int width = game.getWidth();
			int height = game.getHeight();

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			for (int row = 0; row < level.length; row++) {
				for (int col = 0; col < level[0].length; col++) {
					if (level[row][col].gettype() != 'S')
						g.drawImage(level[row][col].getimage(), col * width / 40, row * height / 25, width / 40,
								height / 25, null);
				}
			}
		}

		// Movement method for player called whenever an arrow key is pressed
		public void move(int x, int y) {

			// Temp array that makes the actual changes before transferring these changes
			// into level
			Component[][] temp = new Component[level.length][level[0].length];
			for (int row = 0; row < level.length; row++) {
				for (int col = 0; col < level[0].length; col++) {
					temp[row][col] = level[row][col];
				}
			}

			// Checks if the top of the stack is null, like if the player pressed undo all
			// the way to the beginning
			// If it is null it adds the current state onto the stack
			if (history.gettop() == null)
				history.push(level);

			// Try Catch for player movement in case the player tries to move off screen
			try {
				// If player moves up or left then check the array from top left to bottom right
				// for movement
				if (x == -1 || y == -1) {
					for (int row = 0; row < level.length; row++) {
						for (int col = 0; col < level[0].length; col++) {
							// Checks if that index is controllable
							if (level[row][col].getcontrollable() == true) {
								// Checks if you move down
								if (x == -1) {
									// Checks if you move into a win condition and set win to true
									if (level[row - 1][col].getwin() == true)
										win = true;
									// Checks if you push an openable object into one that is shut
									if (level[row - 1][col].getopenable() == true
											&& level[row - 2][col].getshut() == true
											&& level[row - 1][col].getmoveable() == true) {
										level[row - 2][col] = type('S');
										repaint();
									}
									// Checks if you are an openable object and you move into a shut object
									else if (level[row][col].getcontrollable() && level[row - 1][col].getshut()
											&& level[row][col].getopenable()) {
										level[row - 1][col] = type('S');
										repaint();
									}

									// Checks every object to the left until you reach an empty space or an
									// unmoveable object
									for (int z = row - 1; z >= 0; z--) {
										if (level[z][col].gettype() == 'S' || level[z][col].getmoveable() == false) {
											// Checks if it is an empty space and copying to the temp array accordingly
											if (level[z][col].gettype() == 'S') {
												for (int k = z; k < row; k++) {
													temp[k][col] = level[k + 1][col];
												}
												// Try catch that checks if the object behind you is also a player
												// Try catch is used in case you are on the edge and it checks an index
												// not in the array
												try {
													// If the object behind is also moveable move that into your place
													// Otherwise replace that with a space
													if (level[row + 1][col].getcontrollable() == true)
														temp[row][col] = type(level[row + 1][col].gettype());
													else
														temp[row][col] = new Space('S');
												} catch (ArrayIndexOutOfBoundsException e) {
													temp[row][col] = new Space('S');
												}
											}
											// Manually setting the condition for the loop to not run if such a
											// condition is met
											z = -1;
										}
									}
								}
								// Checks if you move left
								else if (y == -1) {
									// Checks if you move into a win condition and set win to true
									if (level[row][col - 1].getwin() == true)
										win = true;
									// Checks if you push an openable object into one that is shut
									if (level[row][col - 1].getopenable() == true
											&& level[row][col - 2].getshut() == true
											&& level[row][col - 1].getmoveable() == true) {
										level[row][col - 2] = type('S');
										repaint();
									}
									// Checks if you are an openable object and you move into a shut object
									else if (level[row][col].getcontrollable() && level[row][col - 1].getshut()
											&& level[row][col].getopenable()) {
										level[row][col - 1] = type('S');
										repaint();
									}
									// Checks every object above you until you reach an empty space or an
									// unmoveable object
									for (int z = col - 1; z >= 0; z--) {
										if (level[row][z].gettype() == 'S' || level[row][z].getmoveable() == false) {
											// Checks if it is an empty space and copying to the temp array accordingly
											if (level[row][z].gettype() == 'S') {
												for (int k = z; k < col; k++) {
													temp[row][k] = level[row][k + 1];
												}
												// Try catch that checks if the object behind you is also a player
												// Try catch is used in case you are on the edge and it checks an index
												// not in the array
												try {
													if (level[row][col + 1].getcontrollable() == true)
														temp[row][col] = type(level[row][col + 1].gettype());
													else
														temp[row][col] = new Space('S');
												} catch (ArrayIndexOutOfBoundsException e) {
													temp[row][col] = new Space('S');
												}
											}
											// Manually setting the condition for the loop to not run if such a
											// condition is met
											z = -1;
										}
									}
								}
							}
						}
					}
				}
				// If player moves down or right then check the array from bottom right to top
				// left
				// for movement
				else if (x == 1 || y == 1) {
					for (int row = level.length - 1; row >= 0; row--) {
						for (int col = level[0].length - 1; col >= 0; col--) {
							if (level[row][col].getcontrollable() == true) {
								// Checks if you move down
								if (x == 1) {
									// Checks if you move into a win condition and set win to true
									if (level[row + 1][col].getwin() == true)
										win = true;
									// Checks if you push an openable object into one that is shut
									if (level[row + 1][col].getopenable() == true
											&& level[row + 2][col].getshut() == true
											&& level[row + 1][col].getmoveable() == true) {
										level[row + 2][col] = type('S');
										repaint();
									}
									// Checks if you are an openable object and you move into a shut object
									else if (level[row][col].getcontrollable() && level[row + 1][col].getshut()
											&& level[row][col].getopenable()) {
										level[row + 1][col] = type('S');
										repaint();
									}
									// Checks every object below until you reach an empty space or an
									// unmoveable object
									for (int z = row + 1; z < level.length; z++) {
										if (level[z][col].gettype() == 'S' || level[z][col].getmoveable() == false) {
											// Checks if it is an empty space and copying to the temp array accordingly
											if (level[z][col].gettype() == 'S') {
												for (int k = z; k > row; k--) {
													temp[k][col] = level[k - 1][col];
												}
												// Try catch that checks if the object behind you is also a player
												// Try catch is used in case you are on the edge and it checks an index
												// not in the array
												try {
													if (level[row - 1][col].getcontrollable() == true)
														temp[row][col] = type(level[row - 1][col].gettype());
													else
														temp[row][col] = new Space('S');
												} catch (ArrayIndexOutOfBoundsException e) {
													temp[row][col] = new Space('S');
												}
											}
											// Manually setting the condition for the loop to not run if such a
											// condition is met
											z = level.length;
										}
									}
								}
								// Checks if you move right
								else if (y == 1) {
									// Checks if you move into a win condition and set win to true
									if (level[row][col + 1].getwin() == true)
										win = true;
									// Checks if you push an openable object into one that is shut
									if (level[row][col + 1].getopenable() == true
											&& level[row][col + 2].getshut() == true
											&& level[row][col + 1].getmoveable() == true) {
										level[row][col + 2] = type('S');
										repaint();
									}
									// Checks if you are an openable object and you move into a shut object
									else if (level[row][col].getcontrollable() && level[row][col + 1].getshut()
											&& level[row][col].getopenable()) {
										level[row][col + 1] = type('S');
										repaint();
									}
									// Checks every object to the right until you reach an empty space or an
									// unmoveable object
									for (int z = col + 1; z < level[0].length; z++) {
										if (level[row][z].gettype() == 'S' || level[row][z].getmoveable() == false) {
											// Checks if it is an empty space and copying to the temp array accordingly
											if (level[row][z].gettype() == 'S') {
												for (int k = z; k > col; k--) {
													temp[row][k] = level[row][k - 1];
												}
												// Try catch that checks if the object behind you is also a player
												// Try catch is used in case you are on the edge and it checks an index
												// not in the array
												try {
													if (level[row][col - 1].getcontrollable() == true)
														temp[row][col] = type(level[row][col - 1].gettype());
													else
														temp[row][col] = new Space('S');
												} catch (ArrayIndexOutOfBoundsException e) {
													temp[row][col] = new Space('S');
												}
											}
											// Manually setting the condition for the loop to not run if such a
											// condition is met
											z = level[0].length;
										}
									}
								}
							}
						}
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}
			// Setting level equal to the temp array
			level = temp;

			// Deciding the rules for the next turn, and also if any objects transform into
			// other objects
			decidetransform();
			decider();

			// Running through the area to see if anything has changed, for example it
			// wouldn't push the state to the stack if you kept walking into a wall over and
			// over again
			for (int a = 0; a < level.length; a++) {
				for (int b = 0; b < level[0].length; b++) {
					if (history.gettop() != null) {
						if (level[a][b] != history.gettop()[a][b])
							change = true;
					} else
						change = true;
				}
			}

			// If there is change or the top of the stack is null, then push the current
			// state onto the stack
			if (change == true || history.gettop() == null)
				history.push(level);

			// Setting change to be false
			change = false;

			// Checks if the thing that is controllable is also a win condition at the same
			// time and sets win to true
			for (int j = 0; j < level.length; j++) {
				for (int k = 0; k < level[0].length; k++) {
					if (level[j][k].getcontrollable() && level[j][k].getwin())
						win = true;
				}
			}
		}

		// Helper method telling which components are acceptable to be used.
		private boolean acceptable(char k) {
			if (k == 'p' || k == 'r' || k == 'k' || k == 'd' || k == 'f' || k == 'b' || k == 'g')
				return true;
			else
				return false;
		}

		// Helper method to detect if an object is present in the list
		private boolean charisin(char k, ArrayList<Character> x) {
			for (int i = 0; i < x.size(); i++) {
				if (k == x.get(i))
					return true;
			}
			return false;
		}

		// Helper method that adds all lowercase characters so that text is always
		// moveable
		private void addtext(ArrayList<Character> x) {
			for (char ch = 'a'; ch <= 'z'; ch++) {
				x.add(ch);
			}
		}

		// Method that decides the rules of the level and it runs after every movement
		public void decider() {

			// ArrayLists that store the 5 rules and they get set and reset after every
			// movement
			ArrayList<Character> controls = new ArrayList<Character>();
			ArrayList<Character> wins = new ArrayList<Character>();
			ArrayList<Character> moves = new ArrayList<Character>();
			ArrayList<Character> opens = new ArrayList<Character>();
			ArrayList<Character> shuts = new ArrayList<Character>();

			// Adds all the text to the moves list as all text in the game can be pushed
			addtext(moves);

			// Runs through every index of the level and checks for one of the five rule
			// setters.
			// It then checks if a rule exists to the left or above the important block, and
			// stores that value in the corresponding array list.
			for (int row = 0; row < level.length; row++) {
				for (int col = 0; col < level[0].length; col++) {
					if (level[row][col].gettype() == 'v' || level[row][col].gettype() == 'y'
							|| level[row][col].gettype() == 'm' || level[row][col].gettype() == 'o'
							|| level[row][col].gettype() == 's') {
						if (level[row - 1][col].gettype() == 'i' && acceptable(level[row - 2][col].gettype())) {
							if (level[row][col].gettype() == 'y') {
								controls.add(Character.toUpperCase(level[row - 2][col].gettype()));
								moves.add(Character.toUpperCase(level[row - 2][col].gettype()));
							} else if (level[row][col].gettype() == 'v')
								wins.add(Character.toUpperCase(level[row - 2][col].gettype()));
							else if (level[row][col].gettype() == 'm')
								moves.add(Character.toUpperCase(level[row - 2][col].gettype()));
							else if (level[row][col].gettype() == 'o')
								opens.add(Character.toUpperCase(level[row - 2][col].gettype()));
							else if (level[row][col].gettype() == 's')
								shuts.add(Character.toUpperCase(level[row - 2][col].gettype()));
						}

						if (level[row][col - 1].gettype() == 'i' && acceptable(level[row][col - 2].gettype())) {
							if (level[row][col].gettype() == 'y') {
								controls.add(Character.toUpperCase(level[row][col - 2].gettype()));
								moves.add(Character.toUpperCase(level[row][col - 2].gettype()));
							} else if (level[row][col].gettype() == 'v')
								wins.add(Character.toUpperCase(level[row][col - 2].gettype()));
							else if (level[row][col].gettype() == 'm')
								moves.add(Character.toUpperCase(level[row][col - 2].gettype()));
							else if (level[row][col].gettype() == 'o')
								opens.add(Character.toUpperCase(level[row][col - 2].gettype()));
							else if (level[row][col].gettype() == 's')
								shuts.add(Character.toUpperCase(level[row][col - 2].gettype()));
						}
					}
				}
			}

			// Double for loop runs through every index of the array list again, to set the
			// rules.
			for (int x = 0; x < level.length; x++) {
				for (int y = 0; y < level[0].length; y++) {
					if (charisin(level[x][y].gettype(), controls)) {
						level[x][y].setcontrollable(true);
						level[x][y].setmoveable(true);
					} else
						level[x][y].setcontrollable(false);
					if (charisin(level[x][y].gettype(), wins))
						level[x][y].setwin(true);
					else
						level[x][y].setwin(false);
					if (charisin(level[x][y].gettype(), moves))
						level[x][y].setmoveable(true);
					else
						level[x][y].setmoveable(false);
					if (charisin(level[x][y].gettype(), opens))
						level[x][y].setopenable(true);
					else
						level[x][y].setopenable(false);
					if (charisin(level[x][y].gettype(), shuts))
						level[x][y].setshut(true);
					else
						level[x][y].setshut(false);
				}
			}
		}

		// Method that decides if objects need to turn into other objects
		public void decidetransform() {
			// Checking every index of the 2D array
			for (int row = 0; row < level.length; row++) {
				for (int col = 0; col < level[0].length; col++) {
					// Checks if an is text block is found and below and above it are all acceptable
					// text blocks. It then transforms the objects that match the top text block
					// into objects that match the bottom text block.
					if (level[row][col].gettype() == 'i' && acceptable(level[row - 1][col].gettype())
							&& acceptable(level[row + 1][col].gettype())) {
						for (int j = 0; j < level.length; j++) {
							for (int k = 0; k < level[0].length; k++) {
								if (level[j][k].gettype() == Character.toUpperCase(level[row - 1][col].gettype())) {
									level[j][k] = type(Character.toUpperCase(level[row + 1][col].gettype()));
								}
							}
						}
					}
					// Checks if an is text block is found and to the left and right of it are all
					// acceptable text blocks. It then transforms the objects that match the left
					// text block into objects that match the right text block.
					if (level[row][col].gettype() == 'i' && acceptable(level[row][col - 1].gettype())
							&& acceptable(level[row][col + 1].gettype())) {
						for (int j = 0; j < level.length; j++) {
							for (int k = 0; k < level[0].length; k++) {
								if (level[j][k].gettype() == Character.toUpperCase(level[row][col - 1].gettype())) {
									level[j][k] = type(Character.toUpperCase(level[row][col + 1].gettype()));
								}
							}
						}
					}
				}
			}
		}
	}

//Component-----------------------------------------------------------------------------------------------------

// Abstract Component Class for every object in the game
	abstract class Component {

		// Important variables for every object in the game, that decide the rules
		protected boolean moveable, controllable, win, openable, shut;
		protected char type;
		protected Image image;

		// Getters
		public Image getimage() {
			return image;
		}

		public char gettype() {
			return type;
		}

		public boolean getmoveable() {
			return moveable;
		}

		public boolean getcontrollable() {
			return controllable;
		}

		public boolean getopenable() {
			return openable;
		}

		public boolean getshut() {
			return shut;
		}

		public boolean getwin() {
			return win;
		}

		// Setters
		public void setmoveable(boolean c) {
			moveable = c;
		}

		public void setcontrollable(boolean c) {
			controllable = c;
		}

		public void setopenable(boolean c) {
			openable = c;
		}

		public void setshut(boolean c) {
			shut = c;
		}

		public void setwin(boolean c) {
			win = c;
		}
	}

	/*
	 * Multiple classes for each object such as the player, rock, key, door, box,
	 * wall, flag, space, and the necessary text objects. Each object contains has
	 * its own variables that can be changed and compared using the getter and
	 * setters in Component.
	 */

	class Player extends Component {
		public Player(char t) {
			controllable = false;
			moveable = false;
			openable = false;
			shut = false;
			win = false;
			type = t;
			try {
				image = ImageIO.read(PlayerIsYou.class.getResource("/sprites/player.png"));
			} catch (IOException e) {
				System.out.println("The player image could not be found.");
			}
		}
	}

	class Wall extends Component {
		public Wall(char t) {
			controllable = false;
			moveable = false;
			openable = false;
			shut = false;
			win = false;
			type = t;
			try {
				image = ImageIO.read(PlayerIsYou.class.getResource("/sprites/wall.png"));
			} catch (IOException e) {
				System.out.println("The wall image could not be found.");
			}
		}
	}

	class Space extends Component {
		public Space(char t) {
			moveable = false;
			controllable = false;
			openable = false;
			shut = false;
			win = false;
			type = t;
			image = null;
		}
	}

	class Flag extends Component {
		public Flag(char t) {
			moveable = false;
			controllable = false;
			openable = false;
			shut = false;
			win = false;
			type = t;
			try {
				image = ImageIO.read(PlayerIsYou.class.getResource("/sprites/flag.png"));
			} catch (IOException e) {
				System.out.println("The flag image could not be found.");
			}
		}
	}

	class Rock extends Component {
		public Rock(char t) {
			moveable = false;
			controllable = false;
			openable = false;
			shut = false;
			win = false;
			type = t;
			try {
				image = ImageIO.read(PlayerIsYou.class.getResource("/sprites/rock.png"));
			} catch (IOException e) {
				System.out.println("The rock image could not be found.");
			}
		}
	}

	class Key extends Component {
		public Key(char t) {
			moveable = false;
			controllable = false;
			openable = false;
			shut = false;
			win = false;
			type = t;
			try {
				image = ImageIO.read(PlayerIsYou.class.getResource("/sprites/key.png"));
			} catch (IOException e) {
				System.out.println("The key image could not be found.");
			}
		}
	}

	class Door extends Component {
		public Door(char t) {
			moveable = false;
			controllable = false;
			openable = false;
			shut = false;
			win = false;
			type = t;
			try {
				image = ImageIO.read(PlayerIsYou.class.getResource("/sprites/door.png"));
			} catch (IOException e) {
				System.out.println("The door image could not be found.");
			}
		}
	}

	class Ghost extends Component {
		public Ghost(char t) {
			moveable = false;
			controllable = false;
			openable = false;
			shut = false;
			win = false;
			type = t;
			try {
				image = ImageIO.read(PlayerIsYou.class.getResource("/sprites/ghost.png"));
			} catch (IOException e) {
				System.out.println("The door image could not be found.");
			}
		}
	}

	class Box extends Component {
		public Box(char t) {
			moveable = false;
			controllable = false;
			openable = false;
			shut = false;
			win = false;
			type = t;
			try {
				image = ImageIO.read(PlayerIsYou.class.getResource("/sprites/box.png"));
			} catch (IOException e) {
				System.out.println("The box image could not be found.");
			}
		}
	}

	class Text extends Component {
		public Text(char t, String filename) {
			moveable = true;
			controllable = false;
			openable = false;
			shut = false;
			type = t;
			try {
				image = ImageIO.read(PlayerIsYou.class.getResource(filename));
			} catch (IOException e) {
				System.out.println("The text image could not be found.");
			}
		}
	}

//Stack---------------------------------------------------------------------------------------------------------
	
	// Stack Class for undo feature
	class Stack {
		// Node in each maze
		private class Node {
			Component[][] state;
			Node next;
		}

		Node top;

		// Stack Constructor
		Stack() {
			this.top = null;
		}

		// Returns the top value of the stack
		public Component[][] gettop() {
			if (top != null)
				return top.state;
			else
				return null;
		}

		// Adds a new value to the stack
		public void push(Component[][] x) {
			Node temp = new Node();
			temp.state = x;
			temp.next = top;
			top = temp;
		}

		// Removes a value from the stack
		public void pop() {
			if (top == null) {
				return;
			}
			top = top.next;
		}

		// Clears the stack
		public void clear() {
			while (top != null)
				pop();
		}
	}
}