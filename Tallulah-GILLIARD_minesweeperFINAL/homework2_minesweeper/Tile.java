package homework2_minesweeper;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class Tile extends JToggleButton {
	
	// VARIABLES =================================
	boolean bomb = false;
	int neighbourbomb = 0;
	boolean pressed = false;
	int flag = 0;
	int x;
	int y;

	public Tile(String text)
	{
		super.setText(text);
	}
	
	// CONSTRUCTOR =================================
	public Tile( int newX, int newY )
	{
		this.setPreferredSize(new Dimension(30,30));
		this.setMargin(new Insets( 1, 1, 1, 1 ) );
		this.setFocusPainted( false );
		x = newX;
		y = newY;
	}
	
	// METHODS =================================
	
	// P O S I T I O N S
	// X
	public int getXpos()
	{ 
		return x;
	}
	// Y
	public int getYpos()
	{
		return y;
	}
	
	
	// N E I G H B O U R S
	// return the number of neighbours that have bombs on them
	public int getNeighbours()
	{
		return neighbourbomb;
	}
	
	// set bombs in the field
	public void setNeighbours( int newNeighbours )
	{
		neighbourbomb = newNeighbours;
			
	}
	
	// check the ones arround
	public void addNeighboursInfo(int number)
	{
		neighbourbomb += number;
	}
	
	// B O M B S
	// return true or false if there is a bomb
	public boolean isBomb()
	{
		return bomb;
	}
	
	// place a bomb on a tile
	public void setBomb()
	{
		bomb = true;
	}
	
	// B U T T O N S
	// button is down
	public void setPressed()
	{
		this.setEnabled( false );
	}
	
	// check if button is down
	public boolean isPressed()
	{
		return pressed;
	}
	
	// C L I C K S
	
	// left click
	public void leftClick()
	{
		// set the color of the number of neighbors
		switch(neighbourbomb)
		{
			case 1:
				this.setForeground( Color.BLUE );
				break;
				
			case 2:
				this.setForeground( Color.GREEN );
				break;

			case 3:
				this.setForeground( Color.RED );
				break;

			case 4:
				this.setForeground( Color.YELLOW );
				break;
				
			case 5:
				this.setForeground( Color.ORANGE );
				break;

				
			case 6:
				this.setForeground( Color.CYAN );
				break;
				
			case 7:
				this.setForeground( Color.PINK );
				break;
				
			case 8:
				this.setForeground( Color.MAGENTA );
				break;
			default:
				break;
		}	

		if( pressed )
		{
			setSelected( true );
			return;
		}

		// if a flag is set you cannot click
		if( flag == 1)
			setSelected( false );
		else
		{
			setSelected( true );
			pressed = true;

			if( neighbourbomb > 0 )
				setText( "" + neighbourbomb );
		}
	}
	
	
	// right click
	public int rightClick()
	{
		if(!pressed )
		{
			switch(flag) // TODO
			{
				// flag a square with a 'flag'
				case 0:
					flag = 1;
					setEnabled( false );
					return -1;

				// mark a square with a '?'
				case 1:
					flag = 2;
					setEnabled( true );
					setIcon( null );
					setText( "?" );
					return 1;

				// clear '?'
				case 2:
					flag = 0;
					setText( "" );
					return 0;

				default:
					break;
			}
		}
		return 0;
	}
	
	
	
	// O T H E R S
	// reset a tile to its initial state
	public void reset()
	{
		setText("");
		flag = 0;
		bomb = false;
		neighbourbomb = 0;
		pressed = false;
		setEnabled(true);
		this.setSelected(false);
		setIcon(null);
	}
	
	// ex flag boolean --> TODO : different options
	public int getFlag()
	{
		return flag;
	}
	
	
}
