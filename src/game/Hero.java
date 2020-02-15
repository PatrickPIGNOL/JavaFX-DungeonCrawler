package game;

import java.awt.Dimension;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;

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

	public void mLoad()
	{
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
						if(this.aDungeon.mMap().get(this.aY - 1).get(this.aX) == EDungeonMapTerrain.Path)
						{
							this.aY = aY - 1;
						}
					}break;
					case Right:
					{
						if(this.aDungeon.mMap().get(this.aY).get(this.aX + 1) == EDungeonMapTerrain.Path)
						{
							this.aX = aX + 1;
						}
					}break;
					case Down:
					{
						if(this.aDungeon.mMap().get(this.aY + 1).get(this.aX) == EDungeonMapTerrain.Path)
						{
							this.aY = aY + 1;
						}
					}break;
					case Left:
					{
						if(this.aDungeon.mMap().get(this.aY).get(this.aX - 1) == EDungeonMapTerrain.Path)
						{
							this.aX = aX - 1;
						}
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
					case Up:
					{
						this.aDirrection = EDungeonDirrection.Left;
					}break;
					case Right:
					{
						this.aDirrection = EDungeonDirrection.Up;
					}break;
					case Down:
					{
						this.aDirrection = EDungeonDirrection.Right;
					}break;
					case Left:
					{
						this.aDirrection = EDungeonDirrection.Down;
					}break;
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
		Canvas vCanvas = pGraphicsContext.getCanvas();
		
		int vCellSize = this.aDungeon.mCellSize();
		Rectangle vRectangle = new Rectangle(0.0, 0.0, 1024.0, 768.0);
		this.aDungeon.mDrawView(pGraphicsContext, vRectangle, new Point2D(this.aX, this.aY), this.aDirrection);
		this.aDungeon.mDrawMiniMap(pGraphicsContext, new Rectangle(824,0,200,200), new Point2D(this.aX, this.aY), this.aDirrection);
		pGraphicsContext.setEffect(null);
		
		pGraphicsContext.setFill(Color.BLACK);
		
		Point2D vOrigin = new Point2D(700, 0);		
	}	
}
