package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;

public class Hero
{
	private int aX;
	private int aY;
	private EDungeonDirrection aDirrection;
	private Dungeon aDungeon;
	
	public Hero(Dungeon pDungeon, int pX, int pY)
	{
		this.aX = pX;
		this.aY = pY;
		this.aDirrection = EDungeonDirrection.Down;
		this.aDungeon = pDungeon;
	}

	public void mKeyPress(KeyEvent e)
	{
		switch(e.getCode())
		{
			case UP: // go forward
			{
				switch(this.aDirrection)
				{
					case Up:
					{
						
					}break;
				}
			}break;
			case RIGHT: // turn right
			{
				switch(this.aDirrection)
				{
					case Up:
					{
						this.aDirrection = EDungeonDirrection.Right;
					}break;
					case Right:
					{
						this.aDirrection = EDungeonDirrection.Down;
					}break;
					case Down:
					{
						this.aDirrection = EDungeonDirrection.Left;
					}break;
					case Left:
					{
						this.aDirrection = EDungeonDirrection.Up;
					}break;
				}
			}break;
			case LEFT:
			{
				switch(this.aDirrection)
				{
					
				}
			}break;
			case DOWN: // no way
			default:
			{
				
			}break;
		}
	}

	public void mDraw(GraphicsContext pGraphicsContext)
	{
		
		this.aDungeon.mDraw(pGraphicsContext);
	}	
}
