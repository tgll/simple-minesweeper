package homework2_minesweeper;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;


public class Minesweeper_v3 extends JFrame implements MouseListener, ActionListener 
{
	
	// VARIABLES
	// private variables
	private int rows = 10;
	private int columns = 10;
	private Tile [][]buttons = new Tile[rows][columns];
	
	private int bombs = 10;
	private int bombsLeft = bombs;
	private int fieldsLeft = rows * columns - bombsLeft;
	
	private JButton btnStart;
	private JButton btnColA;
	private JButton btnColB;
	private JButton btnColC;
	
	private JLabel txtBombsLeft;
	private JLabel txtTime;
	
	private boolean started = false;
	private boolean finished = false;
	
	private int currentTime = 0;
	private javax.swing.Timer timer;
	
	private JPanel field;

	
	// CONSTRUCTOR ///////////////////////////////////////////////////////////////
	Minesweeper_v3()
	{
		Container contPane = getContentPane();
		getContentPane().setLayout( new BorderLayout() );
		this.setTitle("MINESWEEPER");

		
		// Panel that contain color mode settings & start/reset, nb bombs, timer
		JPanel MenuPane = new JPanel( new GridLayout( 2, 1, 1, 1 ) );
		
		// create a color option bar panel with buttons
		JPanel colorPane = new JPanel( new GridLayout( 1, 3, 5, 5 ) );
		colorPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
			// JButton "A"
			btnColA = new JButton( "forest" );
			btnColA.setPreferredSize( new Dimension( 15, 15 ) );
			btnColA.setBackground((new Color (46, 204, 113)));
			btnColA.addMouseListener( this );
			colorPane.add( btnColA );	
			
			// JButton "B"
			btnColB = new JButton( "sea" );
			btnColB.setPreferredSize( new Dimension( 15, 15 ) );
			btnColB.setBackground((new Color (93, 173, 226)));
			btnColB.addMouseListener( this );
			colorPane.add( btnColB );
			
			// JButton "C"
			btnColC = new JButton( "castle" );
			btnColC.setPreferredSize( new Dimension( 15, 15 ) );
			btnColC.setBackground((new Color (159, 138, 197)));
			btnColC.addMouseListener( this );
			colorPane.add( btnColC );
		
		// create a score bar panel with score and time
		JPanel scoresPane = new JPanel( new GridLayout( 2, 3, 10, 10 ) );

		
			// JLabel "Bombs Left"
			JLabel lblBombsLeft = new JLabel( "Bombs Left" );
			lblBombsLeft.setHorizontalAlignment( JLabel.CENTER );
			scoresPane.add( lblBombsLeft );
			
			// JButton " S T A R T "
			btnStart = new JButton( "S T A R T" );
			btnStart.setPreferredSize( new Dimension( 25, 25 ) );
			btnStart.addMouseListener( this );
			scoresPane.add( btnStart );		
			
			// JLabel "Time"
			JLabel lblTime = new JLabel( "Time" );
			lblTime.setHorizontalAlignment( JLabel.CENTER );
			scoresPane.add( lblTime, 2 );
			
			// JLabel "Bombs Left" NUMBER below
			txtBombsLeft = new JLabel( "" + bombsLeft );
			txtBombsLeft.setHorizontalAlignment( JLabel.CENTER );
			txtBombsLeft.setForeground( Color.blue );
			txtBombsLeft.setFont( new Font( "DialogInput", Font.BOLD, 18 ) );
			scoresPane.add( txtBombsLeft );
			
			// JButton " S T A R T " Space below
			scoresPane.add( new JLabel("") );
			
			// JLabel "Time" NUMBER below
			txtTime = new JLabel( "000" );
			txtTime.setHorizontalAlignment( JLabel.CENTER );
			txtTime.setForeground( Color.blue );
			txtTime.setFont( new Font( "DialogInput", Font.BOLD, 18 ) );
			scoresPane.add( txtTime );
		
		MenuPane.add( colorPane, BorderLayout.NORTH );
		MenuPane.add( scoresPane, BorderLayout.CENTER );
		contPane.add( MenuPane, BorderLayout.NORTH );
		CreateField();
		
		// create timer
		timer = new javax.swing.Timer( 1000, this );

	}
	
/////////////////////////////////////////////////////////////////////////////////
// INIT 
/////////////////////////////////////////////////////////////////////////////////
	
	// Create a field grid made of buttons
	private void CreateField()
	{
		// tile dimentions
		super.setSize( (int)(1 * columns), (int)(1 * rows)); // 

		field = new JPanel( new GridLayout( rows, columns, 5, 5 ) );
		field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		int i, j;
		
		// double loop that create the grid
		for( i = 0 ; i < rows ; i++ )
			for( j = 0 ; j < columns ; j++)
			{
				buttons[i][j] = new Tile( i, j );
				buttons[i][j].addMouseListener( this );
				field.add( buttons[i][j] );
			}		

		getContentPane().add( field, BorderLayout.CENTER );
	}
	
	// Reset the field (start new game)
	private void resetField()
	{
		for( int i = 0 ; i < rows ; i++ )
			for( int j = 0 ; j < columns ; j++)
			{
				buttons[i][j].reset();
			}		
		
		// reset all the flags and the timer
		started = false;
		finished = false;
		currentTime = 0;
		bombsLeft = bombs;
		fieldsLeft = rows * columns - bombsLeft;
		txtBombsLeft.setText( "" + bombsLeft );
		txtTime.setText( "00" + currentTime );
		btnStart.setText( "S T A R T" );
		btnStart.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
		int i, j;
		// double loop that goes through the grid and change color
		for( i = 0 ; i < rows ; i++ )
		{
			for( j = 0 ; j < columns ; j++)
			{
				buttons[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
				field.add( buttons[i][j] );
			}
		}

	}
	
	// 
	private void GenerateBombs()
	{
		Random rand = new Random();
		int randCol, randRow;
		int i, j;

		// place bombs in random order
		for( i = 0 ; i < bombs ; i++ )
		{
			randCol = rand.nextInt( rows );
			randRow = rand.nextInt( columns );
			// don't put more than one bomb on one square
			if( !buttons[ randCol ][ randRow ].isBomb() && !buttons[ randCol ][ randRow ].isSelected() )
				buttons[ randCol ][ randRow ].setBomb();
			else
				i--;
		}
		
		// define the matrix of neighbours and add the colored info number
		for( i = 0 ; i < rows ; i++ )
		{
			for( j = 0 ; j < columns ; j++)
			{
				if( buttons[i][j].isBomb() )
				{
					if( i - 1 >= 0 && j - 1 >= 0 )	// upper left 
						buttons[i - 1][j - 1].addNeighboursInfo( 1 );
					if( i - 1 >= 0 && j >= 0 )	// upper middle 
						buttons[i - 1][j].addNeighboursInfo( 1 );
					if( i - 1 >= 0 && j + 1 < columns )	// upper right 
						buttons[i - 1][j + 1].addNeighboursInfo( 1 );
					if( i >= 0 && j - 1 >= 0 )	// middle left 
						buttons[i][j - 1].addNeighboursInfo( 1 );
					if( i >= 0 && j + 1 < columns )	// middle right 
						buttons[i][j + 1].addNeighboursInfo( 1 );
					if( i + 1 < rows && j - 1 >= 0 )	// lower left 
						buttons[i + 1][j - 1].addNeighboursInfo( 1 );
					if( i + 1 < rows && j >= 0 )	// lower middle 
						buttons[i + 1][j].addNeighboursInfo( 1 );
					if( i + 1 < rows && j + 1 < columns )	// lower left 
						buttons[i + 1][j + 1].addNeighboursInfo( 1 );
				}
			}
		}
	}
	

	// endgame
	private void endGame( boolean lost )
	{
		finished = true;
		timer.stop();
		
		// start/reset button
		if( lost ) 
		{
			btnStart.setText( "D E A T H" );
			btnStart.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.red));
		}
		
		// grid
		for( int i = 0 ; i < rows ; i++ )
			for( int j = 0 ; j < columns ; j++ )
			{
				if( lost )
				{
					// show all bombs
					if( buttons[i][j].isBomb() )
					{
						buttons[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
						buttons[i][j].setEnabled( false );
					}
					else
						buttons[i][j].setEnabled( false );
				}
			}
	}
	
	
/////////////////////////////////////////////////////////////////////////////////
// EVENTS
/////////////////////////////////////////////////////////////////////////////////

	public void mousePressed(MouseEvent e)
	{
	}
	
	public void mouseReleased(MouseEvent e)
	{
		//btnStart.setIcon( new ImageIcon("images/lach003.gif") );
		btnStart.setText( "R E S E T" );
	}
	
	public void mouseEntered(MouseEvent e)
	{}

	public void mouseExited(MouseEvent e)
	{}
	
	// LEFT AND RIGHT CLICK
	public void mouseClicked(MouseEvent e)
	{
		int button = e.getButton();

		// start button clicked = stop the timer and reset the field
		if( e.getSource() == (JButton)btnStart )
		{
			timer.stop();
			rows = 10;
			columns = 10;
			resetField();
			return;
		}
		
		// color buttons clicked = change color // FOREST
		if( e.getSource() == (JButton)btnColA )
		{
			int i, j;
			// double loop that goes through the grid and change color
			for( i = 0 ; i < rows ; i++ )
			{
				for( j = 0 ; j < columns ; j++)
				{
					buttons[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.green));
					field.add( buttons[i][j] );
				}
			}
			return;
		}
		// color buttons clicked = change color // SEA
		if( e.getSource() == (JButton)btnColB )
		{
			int i, j;
			// double loop that goes through the grid and change color
			for( i = 0 ; i < rows ; i++ )
			{
				for( j = 0 ; j < columns ; j++)
				{
					buttons[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.blue));
					field.add( buttons[i][j] );
				}
			}
			return;
		}		
		// color buttons clicked = change color // FOREST
		if( e.getSource() == (JButton)btnColC )
		{
			int i, j;
			// double loop that goes through the grid and change color
			for( i = 0 ; i < rows ; i++ )
			{
				for( j = 0 ; j < columns ; j++)
				{
					buttons[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
					field.add( buttons[i][j] );
				}
			}
			return;
		}
		Tile tile = (Tile)e.getSource();

		// if left mouse button was clicked
		if( button == 1 && !finished )
		{
			// if in the clicked field is bomb and it is not flagged end game
			if( tile.isBomb() && tile.getFlag() != 1 )
			{
				tile.setSelected( false );
				endGame( true );				
				return;
			}

			// if we click the first tile after starting the game
			if( !started )
			{
				GenerateBombs();
				started = true;
				timer.start();
			}

			// not clicked yet and not flagged square is clicked
			if( !tile.isPressed() && tile.getFlag() != 1 )
			{			
					fieldsLeft--;
			}
			// notify a square that it was clicked
			tile.leftClick();
			
		}
		// if right mouse button was clicked
		else if( button == 3 )
		{
			bombsLeft += tile.rightClick();
		}

		// when there is no more empty squares to click the game is won
		if( fieldsLeft == 0 )
		{
			finished = true;
			timer.stop();
			endGame( false );
		}

		txtBombsLeft.setText( "" + bombsLeft );
	}
	
	public void actionPerformed( ActionEvent e )
	{
		Object o = e.getSource();
		
		// color
		
		// time
		if( o == timer )
		{
			currentTime++;
			if( currentTime < 10 )
				txtTime.setText( "00" + currentTime );
			else if( currentTime < 100 )
				txtTime.setText( "0" + currentTime );
			else
				txtTime.setText( "" + currentTime );
		}
	}
	
/////////////////////////////////////////////////////////////////////////////////
// MAIN
/////////////////////////////////////////////////////////////////////////////////
	
	public static void main( String args[] )
	{
		Minesweeper_v3 msw = new Minesweeper_v3();
		msw.setSize(400, 520);
		msw.setMinimumSize(new Dimension(300,420));
		msw.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		msw.setVisible( true );
		msw.setResizable( true );
	}
	
}
