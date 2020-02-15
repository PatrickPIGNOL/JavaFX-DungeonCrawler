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
		Rectangle vRectangle = new Rectangle(0.0, 0.0, 640.0, 480.0);
		Dimension2D vUnit = new Dimension2D(vRectangle.getWidth() / 20.0, vRectangle.getHeight() / 20.0);
		
		PerspectiveTransform vFirstPlanLeftWall = new PerspectiveTransform();
		vFirstPlanLeftWall.setUlx(-vUnit.getWidth() * 3);		// Upper left
		vFirstPlanLeftWall.setUly(-vUnit.getHeight() * 3);
		vFirstPlanLeftWall.setUrx(vUnit.getWidth());		// Upper right
		vFirstPlanLeftWall.setUry(vUnit.getHeight());
		vFirstPlanLeftWall.setLlx(-vUnit.getWidth() * 3);		// Lower left
		vFirstPlanLeftWall.setLly(vRectangle.getHeight() + vUnit.getHeight() * 3);
		vFirstPlanLeftWall.setLrx(vUnit.getWidth());		// Lower right
		vFirstPlanLeftWall.setLry(vRectangle.getHeight() - vUnit.getHeight());
		PerspectiveTransform vFirstPlanRightWall = new PerspectiveTransform();
		vFirstPlanRightWall.setUlx(vRectangle.getWidth() - vUnit.getWidth());		// Upper left
		vFirstPlanRightWall.setUly(vUnit.getHeight());
		vFirstPlanRightWall.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 3);		// Upper right
		vFirstPlanRightWall.setUry(-vUnit.getHeight() * 3);
		vFirstPlanRightWall.setLlx(vRectangle.getWidth() - vUnit.getWidth());		// Lower left
		vFirstPlanRightWall.setLly(vRectangle.getHeight() - vUnit.getHeight());
		vFirstPlanRightWall.setLrx(vRectangle.getWidth() + vUnit.getWidth() * 3);		// Lower right
		vFirstPlanRightWall.setLry(vRectangle.getHeight() + vUnit.getHeight() * 3);
		PerspectiveTransform vSecondPlanLeftWall = new PerspectiveTransform();
		vSecondPlanLeftWall.setUlx(vUnit.getWidth());	// Upper left
		vSecondPlanLeftWall.setUly(vUnit.getHeight());
		vSecondPlanLeftWall.setUrx(vUnit.getWidth() * 4);		// Upper right
		vSecondPlanLeftWall.setUry(vUnit.getHeight() * 4);
		vSecondPlanLeftWall.setLlx(vUnit.getWidth());	// Lower left
		vSecondPlanLeftWall.setLly(vRectangle.getHeight() - vUnit.getHeight());
		vSecondPlanLeftWall.setLrx(vUnit.getWidth()*4);		// Lower right
		vSecondPlanLeftWall.setLry(vRectangle.getHeight() - vUnit.getHeight()*4);
		PerspectiveTransform vSecondPlanRightWall = new PerspectiveTransform();
		vSecondPlanRightWall.setUlx(vRectangle.getWidth() - vUnit.getWidth() * 4);	// Upper left
		vSecondPlanRightWall.setUly(vUnit.getHeight() * 4);
		vSecondPlanRightWall.setUrx(vRectangle.getWidth() - vUnit.getWidth());		// Upper right
		vSecondPlanRightWall.setUry(vUnit.getHeight());
		vSecondPlanRightWall.setLlx(vRectangle.getWidth() - vUnit.getWidth() * 4);	// Lower left
		vSecondPlanRightWall.setLly(vRectangle.getHeight() - vUnit.getHeight() * 4);
		vSecondPlanRightWall.setLrx(vRectangle.getWidth()-vUnit.getWidth());		// Lower right
		vSecondPlanRightWall.setLry(vRectangle.getHeight()-vUnit.getHeight());
		PerspectiveTransform vSecondPlanRightFaceWall = new PerspectiveTransform();
		vSecondPlanRightFaceWall.setUlx(vRectangle.getWidth() - vUnit.getWidth());	// Upper left
		vSecondPlanRightFaceWall.setUly(vUnit.getHeight());
		vSecondPlanRightFaceWall.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 17);		// Upper right
		vSecondPlanRightFaceWall.setUry(vUnit.getHeight());
		vSecondPlanRightFaceWall.setLlx(vRectangle.getWidth() - vUnit.getWidth());	// Lower left
		vSecondPlanRightFaceWall.setLly(vRectangle.getHeight() - vUnit.getHeight());
		vSecondPlanRightFaceWall.setLrx(vRectangle.getWidth() + vUnit.getWidth() * 17);		// Lower right
		vSecondPlanRightFaceWall.setLry(vRectangle.getHeight() - vUnit.getHeight());
		PerspectiveTransform vSecondPlanLeftFaceWall = new PerspectiveTransform();
		vSecondPlanLeftFaceWall.setUlx(-vUnit.getWidth() * 17);	// Upper left
		vSecondPlanLeftFaceWall.setUly(vUnit.getHeight());
		vSecondPlanLeftFaceWall.setUrx(vUnit.getWidth());		// Upper right
		vSecondPlanLeftFaceWall.setUry(vUnit.getHeight());
		vSecondPlanLeftFaceWall.setLlx(-vUnit.getWidth() * 17);	// Lower left
		vSecondPlanLeftFaceWall.setLly(vRectangle.getHeight() - vUnit.getHeight());
		vSecondPlanLeftFaceWall.setLrx(vUnit.getWidth());		// Lower right
		vSecondPlanLeftFaceWall.setLry(vRectangle.getHeight() - vUnit.getHeight());
		PerspectiveTransform vSecondPlanFaceWall = new PerspectiveTransform();
		vSecondPlanFaceWall.setUlx(vUnit.getWidth());	// Upper left
		vSecondPlanFaceWall.setUly(vUnit.getHeight());
		vSecondPlanFaceWall.setUrx(vRectangle.getWidth() - vUnit.getWidth());		// Upper right
		vSecondPlanFaceWall.setUry(vUnit.getHeight());
		vSecondPlanFaceWall.setLlx(vUnit.getWidth());	// Lower left
		vSecondPlanFaceWall.setLly(vRectangle.getHeight() - vUnit.getHeight());
		vSecondPlanFaceWall.setLrx(vRectangle.getWidth() - vUnit.getWidth());		// Lower right
		vSecondPlanFaceWall.setLry(vRectangle.getHeight() - vUnit.getHeight());
		PerspectiveTransform vThirdPlanLeftFaceWall = new PerspectiveTransform();
		vThirdPlanLeftFaceWall.setUlx(-vUnit.getWidth() * 8);	// Upper left
		vThirdPlanLeftFaceWall.setUly(vUnit.getHeight() * 4);
		vThirdPlanLeftFaceWall.setUrx(vUnit.getWidth() * 4);		// Upper right
		vThirdPlanLeftFaceWall.setUry(vUnit.getHeight() * 4);
		vThirdPlanLeftFaceWall.setLlx(-vUnit.getWidth() * 8);	// Lower left
		vThirdPlanLeftFaceWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 4);
		vThirdPlanLeftFaceWall.setLrx(vUnit.getWidth() * 4);		// Lower right
		vThirdPlanLeftFaceWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 4);
		PerspectiveTransform vThirdPlanRightFaceWall = new PerspectiveTransform();
		vThirdPlanRightFaceWall.setUlx(vRectangle.getWidth() - vUnit.getWidth() * 4);	// Upper left
		vThirdPlanRightFaceWall.setUly(vUnit.getHeight() * 4);
		vThirdPlanRightFaceWall.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 8);		// Upper right
		vThirdPlanRightFaceWall.setUry(vUnit.getHeight() * 4);
		vThirdPlanRightFaceWall.setLlx(vRectangle.getWidth() - vUnit.getWidth() * 4);	// Lower left
		vThirdPlanRightFaceWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 4);
		vThirdPlanRightFaceWall.setLrx(vRectangle.getWidth() + vUnit.getWidth() * 8);		// Lower right
		vThirdPlanRightFaceWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 4);
		PerspectiveTransform vThirdPlanFaceWall = new PerspectiveTransform();
		vThirdPlanFaceWall.setUlx(vUnit.getWidth() * 4);	// Upper left
		vThirdPlanFaceWall.setUly(vUnit.getHeight() * 4);
		vThirdPlanFaceWall.setUrx(vRectangle.getWidth() - vUnit.getWidth() * 4);		// Upper right
		vThirdPlanFaceWall.setUry(vUnit.getHeight() * 4);
		vThirdPlanFaceWall.setLlx(vUnit.getWidth() * 4);	// Lower left
		vThirdPlanFaceWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 4);
		vThirdPlanFaceWall.setLrx(vRectangle.getWidth() - vUnit.getWidth() * 4);		// Lower right
		vThirdPlanFaceWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 4);
		PerspectiveTransform vThirdPlanRightWall = new PerspectiveTransform();
		vThirdPlanRightWall.setUlx(vRectangle.getWidth() - vUnit.getWidth() * 6);	// Upper left
		vThirdPlanRightWall.setUly(vUnit.getHeight() * 6);
		vThirdPlanRightWall.setUrx(vRectangle.getWidth() - vUnit.getWidth() * 4);		// Upper right
		vThirdPlanRightWall.setUry(vUnit.getHeight() * 4);
		vThirdPlanRightWall.setLlx(vRectangle.getWidth() - vUnit.getWidth() * 6);	// Lower left
		vThirdPlanRightWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 6);
		vThirdPlanRightWall.setLrx(vRectangle.getWidth() - vUnit.getWidth() * 4);		// Lower right
		vThirdPlanRightWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 4);
		PerspectiveTransform vThirdPlanLeftWall = new PerspectiveTransform();
		vThirdPlanLeftWall.setUlx(vUnit.getWidth() * 4);	// Upper left
		vThirdPlanLeftWall.setUly(vUnit.getHeight() * 4);
		vThirdPlanLeftWall.setUrx(vUnit.getWidth() * 6);		// Upper right
		vThirdPlanLeftWall.setUry(vUnit.getHeight() * 6);
		vThirdPlanLeftWall.setLlx(vUnit.getWidth() * 4);	// Lower left
		vThirdPlanLeftWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 4);
		vThirdPlanLeftWall.setLrx(vUnit.getWidth() * 6);		// Lower right
		vThirdPlanLeftWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanFaceWall = new PerspectiveTransform();
		vFourthPlanFaceWall.setUlx(vUnit.getWidth() * 6);	// Upper left
		vFourthPlanFaceWall.setUly(vUnit.getHeight() * 6);
		vFourthPlanFaceWall.setUrx(vRectangle.getWidth() - vUnit.getWidth() * 6);		// Upper right
		vFourthPlanFaceWall.setUry(vUnit.getHeight() * 6);
		vFourthPlanFaceWall.setLlx(vUnit.getWidth() * 6);	// Lower left
		vFourthPlanFaceWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanFaceWall.setLrx(vRectangle.getWidth() - vUnit.getWidth() * 6);		// Lower right
		vFourthPlanFaceWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanLeftFaceWall = new PerspectiveTransform();
		vFourthPlanLeftFaceWall.setUlx(-vUnit.getWidth() * 2);	// Upper left
		vFourthPlanLeftFaceWall.setUly(vUnit.getHeight() * 6);
		vFourthPlanLeftFaceWall.setUrx(vUnit.getWidth() * 6);		// Upper right
		vFourthPlanLeftFaceWall.setUry(vUnit.getHeight() * 6);
		vFourthPlanLeftFaceWall.setLlx(-vUnit.getWidth() * 2);	// Lower left
		vFourthPlanLeftFaceWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanLeftFaceWall.setLrx(vUnit.getWidth() * 6);		// Lower right
		vFourthPlanLeftFaceWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanRightFaceWall = new PerspectiveTransform();
		vFourthPlanRightFaceWall.setUlx(vRectangle.getWidth()-vUnit.getWidth() * 6);	// Upper left
		vFourthPlanRightFaceWall.setUly(vUnit.getHeight() * 6);
		vFourthPlanRightFaceWall.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 2);		// Upper right
		vFourthPlanRightFaceWall.setUry(vUnit.getHeight() * 6);
		vFourthPlanRightFaceWall.setLlx(vRectangle.getWidth() - vUnit.getWidth() * 6);	// Lower left
		vFourthPlanRightFaceWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanRightFaceWall.setLrx(vRectangle.getWidth() + vUnit.getWidth() * 2);		// Lower right
		vFourthPlanRightFaceWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanRightWall = new PerspectiveTransform();
		vFourthPlanRightWall.setUlx(vRectangle.getWidth() - vUnit.getWidth() * 7);	// Upper left
		vFourthPlanRightWall.setUly(vUnit.getHeight() * 7);
		vFourthPlanRightWall.setUrx(vRectangle.getWidth() - vUnit.getWidth() * 6);		// Upper right
		vFourthPlanRightWall.setUry(vUnit.getHeight() * 6);
		vFourthPlanRightWall.setLlx(vRectangle.getWidth() - vUnit.getWidth() * 7);	// Lower left
		vFourthPlanRightWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 7);
		vFourthPlanRightWall.setLrx(vRectangle.getWidth() - vUnit.getWidth() * 6);		// Lower right
		vFourthPlanRightWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanLeftWall = new PerspectiveTransform();
		vFourthPlanLeftWall.setUlx(vUnit.getWidth() * 6);	// Upper left
		vFourthPlanLeftWall.setUly(vUnit.getHeight() * 6);
		vFourthPlanLeftWall.setUrx(vUnit.getWidth() * 7);		// Upper right
		vFourthPlanLeftWall.setUry(vUnit.getHeight() * 7);
		vFourthPlanLeftWall.setLlx(vUnit.getWidth() * 6);	// Lower left
		vFourthPlanLeftWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanLeftWall.setLrx(vUnit.getWidth() * 7);		// Lower right
		vFourthPlanLeftWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 7);
		PerspectiveTransform vFifthPlanFaceWall = new PerspectiveTransform();
		vFifthPlanFaceWall.setUlx(vUnit.getWidth() * 7);	// Upper left
		vFifthPlanFaceWall.setUly(vUnit.getHeight() * 7);
		vFifthPlanFaceWall.setUrx(vRectangle.getWidth() - vUnit.getWidth() * 7);		// Upper right
		vFifthPlanFaceWall.setUry(vUnit.getHeight() * 7);
		vFifthPlanFaceWall.setLlx(vUnit.getWidth() * 7);	// Lower left
		vFifthPlanFaceWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 7);
		vFifthPlanFaceWall.setLrx(vRectangle.getWidth() - vUnit.getWidth() * 7);		// Lower right
		vFifthPlanFaceWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 7);
		PerspectiveTransform vFifthPlanRight1FaceWall = new PerspectiveTransform();
		vFifthPlanRight1FaceWall.setUlx(vRectangle.getWidth() - vUnit.getWidth() * 7);	// Upper left
		vFifthPlanRight1FaceWall.setUly(vUnit.getHeight() * 7);
		vFifthPlanRight1FaceWall.setUrx(vRectangle.getWidth() - vUnit.getWidth() * 1);		// Upper right
		vFifthPlanRight1FaceWall.setUry(vUnit.getHeight() * 7);
		vFifthPlanRight1FaceWall.setLlx(vRectangle.getWidth() - vUnit.getWidth() * 7);	// Lower left
		vFifthPlanRight1FaceWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 7);
		vFifthPlanRight1FaceWall.setLrx(vRectangle.getWidth() - vUnit.getWidth() * 1);		// Lower right
		vFifthPlanRight1FaceWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 7);
		PerspectiveTransform vFifthPlanRight2FaceWall = new PerspectiveTransform();
		vFifthPlanRight2FaceWall.setUlx(vRectangle.getWidth() - vUnit.getWidth() * 1);	// Upper left
		vFifthPlanRight2FaceWall.setUly(vUnit.getHeight() * 7);
		vFifthPlanRight2FaceWall.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 5);		// Upper right
		vFifthPlanRight2FaceWall.setUry(vUnit.getHeight() * 7);
		vFifthPlanRight2FaceWall.setLlx(vRectangle.getWidth() - vUnit.getWidth() * 1);	// Lower left
		vFifthPlanRight2FaceWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 7);
		vFifthPlanRight2FaceWall.setLrx(vRectangle.getWidth() + vUnit.getWidth() * 5);		// Lower right
		vFifthPlanRight2FaceWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 7);
		PerspectiveTransform vFifthPlanLeft1FaceWall = new PerspectiveTransform();
		vFifthPlanLeft1FaceWall.setUlx(vUnit.getWidth() * 1);	// Upper left
		vFifthPlanLeft1FaceWall.setUly(vUnit.getHeight() * 7);
		vFifthPlanLeft1FaceWall.setUrx(vUnit.getWidth() * 7);		// Upper right
		vFifthPlanLeft1FaceWall.setUry(vUnit.getHeight() * 7);
		vFifthPlanLeft1FaceWall.setLlx(vUnit.getWidth() * 1);	// Lower left
		vFifthPlanLeft1FaceWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 7);
		vFifthPlanLeft1FaceWall.setLrx(vUnit.getWidth() * 7);		// Lower right
		vFifthPlanLeft1FaceWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 7);
		PerspectiveTransform vFifthPlanLeft2FaceWall = new PerspectiveTransform();
		vFifthPlanLeft2FaceWall.setUlx(-vUnit.getWidth() * 5);	// Upper left
		vFifthPlanLeft2FaceWall.setUly(vUnit.getHeight() * 7);
		vFifthPlanLeft2FaceWall.setUrx(vUnit.getWidth() * 1);		// Upper right
		vFifthPlanLeft2FaceWall.setUry(vUnit.getHeight() * 7);
		vFifthPlanLeft2FaceWall.setLlx(-vUnit.getWidth() * 5);	// Lower left
		vFifthPlanLeft2FaceWall.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 7);
		vFifthPlanLeft2FaceWall.setLrx(vUnit.getWidth() * 1);		// Lower right
		vFifthPlanLeft2FaceWall.setLry(vRectangle.getHeight() - vUnit.getHeight() * 7);

		PerspectiveTransform vFourthPlanLeft2Ground = new PerspectiveTransform();
		vFourthPlanLeft2Ground.setUlx(-vUnit.getWidth() * 5);	// Upper left
		vFourthPlanLeft2Ground.setUly(vRectangle.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanLeft2Ground.setUrx(vUnit.getWidth() * 1);		// Upper right
		vFourthPlanLeft2Ground.setUry(vRectangle.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanLeft2Ground.setLlx(-vUnit.getWidth() * 10);	// Lower left
		vFourthPlanLeft2Ground.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanLeft2Ground.setLrx(-vUnit.getWidth() * 2);		// Lower right
		vFourthPlanLeft2Ground.setLry(vRectangle.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanRight2Ground = new PerspectiveTransform();
		vFourthPlanRight2Ground.setUlx(vRectangle.getWidth() - vUnit.getWidth() * 1);	// Upper left
		vFourthPlanRight2Ground.setUly(vRectangle.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanRight2Ground.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 5);		// Upper right
		vFourthPlanRight2Ground.setUry(vRectangle.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanRight2Ground.setLlx(vRectangle.getWidth() + vUnit.getWidth() * 2);	// Lower left
		vFourthPlanRight2Ground.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanRight2Ground.setLrx(vRectangle.getWidth() + vUnit.getWidth() * 10);		// Lower right
		vFourthPlanRight2Ground.setLry(vRectangle.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanRight1Ground = new PerspectiveTransform();
		vFourthPlanRight1Ground.setUlx(vRectangle.getWidth() - vUnit.getWidth() * 7);	// Upper left
		vFourthPlanRight1Ground.setUly(vRectangle.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanRight1Ground.setUrx(vRectangle.getWidth() - vUnit.getWidth() * 1);		// Upper right
		vFourthPlanRight1Ground.setUry(vRectangle.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanRight1Ground.setLlx(vRectangle.getWidth() - vUnit.getWidth() * 6);	// Lower left
		vFourthPlanRight1Ground.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanRight1Ground.setLrx(vRectangle.getWidth() + vUnit.getWidth() * 2);		// Lower right
		vFourthPlanRight1Ground.setLry(vRectangle.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanLeft1Ground = new PerspectiveTransform();
		vFourthPlanLeft1Ground.setUlx(vUnit.getWidth() * 1);	// Upper left
		vFourthPlanLeft1Ground.setUly(vRectangle.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanLeft1Ground.setUrx(vUnit.getWidth() * 7);		// Upper right
		vFourthPlanLeft1Ground.setUry(vRectangle.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanLeft1Ground.setLlx(-vUnit.getWidth() * 2);	// Lower left
		vFourthPlanLeft1Ground.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanLeft1Ground.setLrx(vUnit.getWidth() * 6);		// Lower right
		vFourthPlanLeft1Ground.setLry(vRectangle.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanCenterGround = new PerspectiveTransform();
		vFourthPlanCenterGround.setUlx(vUnit.getWidth() * 7);	// Upper left
		vFourthPlanCenterGround.setUly(vRectangle.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanCenterGround.setUrx(vRectangle.getWidth() - vUnit.getWidth() * 7);		// Upper right
		vFourthPlanCenterGround.setUry(vRectangle.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanCenterGround.setLlx(vUnit.getWidth() * 6);	// Lower left
		vFourthPlanCenterGround.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanCenterGround.setLrx(vRectangle.getWidth() - vUnit.getWidth() * 6);		// Lower right
		vFourthPlanCenterGround.setLry(vRectangle.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vThirdPlanLeftGround = new PerspectiveTransform();
		vThirdPlanLeftGround.setUlx(-vUnit.getWidth() * 2);	// Upper left
		vThirdPlanLeftGround.setUly(vRectangle.getHeight() - vUnit.getHeight() * 6);
		vThirdPlanLeftGround.setUrx(vUnit.getWidth() * 6);		// Upper right
		vThirdPlanLeftGround.setUry(vRectangle.getHeight() - vUnit.getHeight() * 6);
		vThirdPlanLeftGround.setLlx(-vUnit.getWidth() * 8);	// Lower left
		vThirdPlanLeftGround.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 4);
		vThirdPlanLeftGround.setLrx(vUnit.getWidth() * 4);		// Lower right
		vThirdPlanLeftGround.setLry(vRectangle.getHeight() - vUnit.getHeight() * 4);
		PerspectiveTransform vThirdPlanRightGround = new PerspectiveTransform();
		vThirdPlanRightGround.setUlx(vRectangle.getWidth() - vUnit.getWidth() * 6);	// Upper left
		vThirdPlanRightGround.setUly(vRectangle.getHeight() - vUnit.getHeight() * 6);
		vThirdPlanRightGround.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 2);		// Upper right
		vThirdPlanRightGround.setUry(vRectangle.getHeight() - vUnit.getHeight() * 6);
		vThirdPlanRightGround.setLlx(vRectangle.getWidth() - vUnit.getWidth() * 4);	// Lower left
		vThirdPlanRightGround.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 4);
		vThirdPlanRightGround.setLrx(vRectangle.getWidth() + vUnit.getWidth() * 8);		// Lower right
		vThirdPlanRightGround.setLry(vRectangle.getHeight() - vUnit.getHeight() * 4);
		PerspectiveTransform vThirdPlanCenterGround = new PerspectiveTransform();
		vThirdPlanCenterGround.setUlx(vUnit.getWidth() * 6);	// Upper left
		vThirdPlanCenterGround.setUly(vRectangle.getHeight() - vUnit.getHeight() * 6);
		vThirdPlanCenterGround.setUrx(vRectangle.getWidth() - vUnit.getWidth() * 6);		// Upper right
		vThirdPlanCenterGround.setUry(vRectangle.getHeight() - vUnit.getHeight() * 6);
		vThirdPlanCenterGround.setLlx(vUnit.getWidth() * 4);	// Lower left
		vThirdPlanCenterGround.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 4);
		vThirdPlanCenterGround.setLrx(vRectangle.getWidth() - vUnit.getWidth() * 4);		// Lower right
		vThirdPlanCenterGround.setLry(vRectangle.getHeight() - vUnit.getHeight() * 4);
		PerspectiveTransform vSecondPlanRightGround = new PerspectiveTransform();
		vSecondPlanRightGround.setUlx(vRectangle.getWidth() - vUnit.getWidth() * 4);	// Upper left
		vSecondPlanRightGround.setUly(vRectangle.getHeight() - vUnit.getHeight() * 4);
		vSecondPlanRightGround.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 8);		// Upper right
		vSecondPlanRightGround.setUry(vRectangle.getHeight() - vUnit.getHeight() * 4);
		vSecondPlanRightGround.setLlx(vRectangle.getWidth() - vUnit.getWidth() * 1);	// Lower left
		vSecondPlanRightGround.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 1);
		vSecondPlanRightGround.setLrx(vRectangle.getWidth() + vUnit.getWidth() * 18);		// Lower right
		vSecondPlanRightGround.setLry(vRectangle.getHeight() - vUnit.getHeight() * 1);
		PerspectiveTransform vSecondPlanLeftGround = new PerspectiveTransform();
		vSecondPlanLeftGround.setUlx(-vUnit.getWidth() * 8);	// Upper left
		vSecondPlanLeftGround.setUly(vRectangle.getHeight() - vUnit.getHeight() * 4);
		vSecondPlanLeftGround.setUrx(vUnit.getWidth() * 4);		// Upper right
		vSecondPlanLeftGround.setUry(vRectangle.getHeight() - vUnit.getHeight() * 4);
		vSecondPlanLeftGround.setLlx(-vUnit.getWidth() * 17);	// Lower left
		vSecondPlanLeftGround.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 1);
		vSecondPlanLeftGround.setLrx(vUnit.getWidth() * 1);		// Lower right
		vSecondPlanLeftGround.setLry(vRectangle.getHeight() - vUnit.getHeight() * 1);
		PerspectiveTransform vSecondPlanCenterGround = new PerspectiveTransform();
		vSecondPlanCenterGround.setUlx(vUnit.getWidth() * 4);	// Upper left
		vSecondPlanCenterGround.setUly(vRectangle.getHeight() - vUnit.getHeight() * 4);
		vSecondPlanCenterGround.setUrx(vRectangle.getWidth() - vUnit.getWidth() * 4);		// Upper right
		vSecondPlanCenterGround.setUry(vRectangle.getHeight() - vUnit.getHeight() * 4);
		vSecondPlanCenterGround.setLlx(vUnit.getWidth() * 1);	// Lower left
		vSecondPlanCenterGround.setLly(vRectangle.getHeight() - vUnit.getHeight()  * 1);
		vSecondPlanCenterGround.setLrx(vRectangle.getWidth() - vUnit.getWidth() * 1);		// Lower right
		vSecondPlanCenterGround.setLry(vRectangle.getHeight() - vUnit.getHeight() * 1);
		PerspectiveTransform vFirstPlanLeftGround = new PerspectiveTransform();
		vFirstPlanLeftGround.setUlx(-vUnit.getWidth() * 17);	// Upper left
		vFirstPlanLeftGround.setUly(vRectangle.getHeight() - vUnit.getHeight() * 1);
		vFirstPlanLeftGround.setUrx(vUnit.getWidth() * 1);		// Upper right
		vFirstPlanLeftGround.setUry(vRectangle.getHeight() - vUnit.getHeight() * 1);
		vFirstPlanLeftGround.setLlx(-vUnit.getWidth() * 28);	// Lower left
		vFirstPlanLeftGround.setLly(vRectangle.getHeight() + vUnit.getHeight()  * 3);
		vFirstPlanLeftGround.setLrx(-vUnit.getWidth() * 3);		// Lower right
		vFirstPlanLeftGround.setLry(vRectangle.getHeight() + vUnit.getHeight() * 3);
		PerspectiveTransform vFirstPlanRightGround = new PerspectiveTransform();
		vFirstPlanRightGround.setUlx(vRectangle.getWidth() - vUnit.getWidth() * 1);	// Upper left
		vFirstPlanRightGround.setUly(vRectangle.getHeight() - vUnit.getHeight() * 1);
		vFirstPlanRightGround.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 18);		// Upper right
		vFirstPlanRightGround.setUry(vRectangle.getHeight() - vUnit.getHeight() * 1);
		vFirstPlanRightGround.setLlx(vRectangle.getWidth() + vUnit.getWidth() * 3);	// Lower left
		vFirstPlanRightGround.setLly(vRectangle.getHeight() + vUnit.getHeight() * 3);
		vFirstPlanRightGround.setLrx(vRectangle.getWidth() + vUnit.getWidth() * 30);		// Lower right
		vFirstPlanRightGround.setLry(vRectangle.getHeight() + vUnit.getHeight() * 3);
		PerspectiveTransform vFirstPlanCenterGround = new PerspectiveTransform();
		vFirstPlanCenterGround.setUlx(vUnit.getWidth() * 1);	// Upper left
		vFirstPlanCenterGround.setUly(vRectangle.getHeight() - vUnit.getHeight() * 1);
		vFirstPlanCenterGround.setUrx(vRectangle.getWidth() - vUnit.getWidth() * 1);		// Upper right
		vFirstPlanCenterGround.setUry(vRectangle.getHeight() - vUnit.getHeight() * 1);
		vFirstPlanCenterGround.setLlx(-vUnit.getWidth() * 3);	// Lower left
		vFirstPlanCenterGround.setLly(vRectangle.getHeight() + vUnit.getHeight()  * 3);
		vFirstPlanCenterGround.setLrx(vRectangle.getWidth() + vUnit.getWidth() * 3);		// Lower right
		vFirstPlanCenterGround.setLry(vRectangle.getHeight() + vUnit.getHeight() * 3);
		
		
		
		PerspectiveTransform vFirstPlanCenterRoof = new PerspectiveTransform();
		vFirstPlanCenterRoof.setUlx(-vUnit.getWidth() * 3);	// Upper left
		vFirstPlanCenterRoof.setUly(-vUnit.getHeight() * 3);
		vFirstPlanCenterRoof.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 3);		// Upper right
		vFirstPlanCenterRoof.setUry(-vUnit.getHeight() * 3);
		vFirstPlanCenterRoof.setLlx(vUnit.getWidth() * 1);	// Lower left
		vFirstPlanCenterRoof.setLly(vUnit.getHeight()  * 1);
		vFirstPlanCenterRoof.setLrx(vRectangle.getWidth() - vUnit.getWidth() * 1);		// Lower right
		vFirstPlanCenterRoof.setLry(vUnit.getHeight() * 1);
		PerspectiveTransform vFirstPlanLeftRoof = new PerspectiveTransform();
		vFirstPlanLeftRoof.setUlx(-vUnit.getWidth() * 28);	// Upper left                                
		vFirstPlanLeftRoof.setUly(-vUnit.getHeight() * 3);                                             
		vFirstPlanLeftRoof.setUrx(-vUnit.getWidth() * 3);		// Upper right   
		vFirstPlanLeftRoof.setUry(-vUnit.getHeight() * 3);                                             
		vFirstPlanLeftRoof.setLlx(-vUnit.getWidth() * 17);	// Lower left                                
		vFirstPlanLeftRoof.setLly(vUnit.getHeight()  * 1);                                             
		vFirstPlanLeftRoof.setLrx(vUnit.getWidth() * 1);		// Lower right   
		vFirstPlanLeftRoof.setLry(vUnit.getHeight() * 1);                                              
		PerspectiveTransform vFirstPlanRightRoof = new PerspectiveTransform();
		vFirstPlanRightRoof.setUlx(vRectangle.getWidth() + vUnit.getWidth() * 3);	// Upper left
		vFirstPlanRightRoof.setUly(-vUnit.getHeight() * 3);
		vFirstPlanRightRoof.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 28);		// Upper right
		vFirstPlanRightRoof.setUry(-vUnit.getHeight() * 3);
		vFirstPlanRightRoof.setLlx(vRectangle.getWidth() - vUnit.getWidth() * 1);	// Lower left
		vFirstPlanRightRoof.setLly(vUnit.getHeight() * 1);
		vFirstPlanRightRoof.setLrx(vRectangle.getWidth() + vUnit.getWidth() * 17);		// Lower right
		vFirstPlanRightRoof.setLry(vUnit.getHeight() * 1);
		PerspectiveTransform vSecondPlanCenterRoof = new PerspectiveTransform();
		vSecondPlanCenterRoof.setUlx(vUnit.getWidth() * 1);	// Upper left
		vSecondPlanCenterRoof.setUly(vUnit.getHeight() * 1);
		vSecondPlanCenterRoof.setUrx(vRectangle.getWidth() - vUnit.getWidth() * 1);		// Upper right
		vSecondPlanCenterRoof.setUry(vUnit.getHeight() * 1);
		vSecondPlanCenterRoof.setLlx(vUnit.getWidth() * 4);	// Lower left
		vSecondPlanCenterRoof.setLly(vUnit.getHeight() * 4);
		vSecondPlanCenterRoof.setLrx(vRectangle.getWidth() - vUnit.getWidth() * 4);		// Lower right
		vSecondPlanCenterRoof.setLry(vUnit.getHeight() * 4);
		PerspectiveTransform vSecondPlanRightRoof = new PerspectiveTransform();
		vSecondPlanRightRoof.setUlx(vRectangle.getWidth() - vUnit.getWidth() * 1);	// Upper left
		vSecondPlanRightRoof.setUly(vUnit.getHeight() * 1);
		vSecondPlanRightRoof.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 17);		// Upper right
		vSecondPlanRightRoof.setUry(vUnit.getHeight() * 1);
		vSecondPlanRightRoof.setLlx(vRectangle.getWidth() - vUnit.getWidth() * 4);	// Lower left
		vSecondPlanRightRoof.setLly(vUnit.getHeight() * 4);
		vSecondPlanRightRoof.setLrx(vRectangle.getWidth() + vUnit.getWidth() * 8);		// Lower right
		vSecondPlanRightRoof.setLry(vUnit.getHeight() * 4);
		PerspectiveTransform vSecondPlanLeftRoof = new PerspectiveTransform();
		vSecondPlanLeftRoof.setUlx(-vUnit.getWidth() * 17);	// Upper left
		vSecondPlanLeftRoof.setUly(vUnit.getHeight() * 1);
		vSecondPlanLeftRoof.setUrx(vUnit.getWidth() * 1);		// Upper right
		vSecondPlanLeftRoof.setUry(vUnit.getHeight() * 1);
		vSecondPlanLeftRoof.setLlx(-vUnit.getWidth() * 8);	// Lower left
		vSecondPlanLeftRoof.setLly(vUnit.getHeight() * 4);
		vSecondPlanLeftRoof.setLrx(vUnit.getWidth() * 4);		// Lower right
		vSecondPlanLeftRoof.setLry(vUnit.getHeight() * 4);
		PerspectiveTransform vThirdPlanRightRoof = new PerspectiveTransform();
		vThirdPlanRightRoof.setUlx(vRectangle.getWidth() - vUnit.getWidth() * 4);	// Upper left
		vThirdPlanRightRoof.setUly(vUnit.getHeight() * 4);
		vThirdPlanRightRoof.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 8);		// Upper right
		vThirdPlanRightRoof.setUry(vUnit.getHeight() * 4);
		vThirdPlanRightRoof.setLlx(vRectangle.getWidth()-vUnit.getWidth() * 6);	// Lower left
		vThirdPlanRightRoof.setLly(vUnit.getHeight() * 6);
		vThirdPlanRightRoof.setLrx(vRectangle.getWidth()+vUnit.getWidth() * 2);		// Lower right
		vThirdPlanRightRoof.setLry(vUnit.getHeight() * 6);
		PerspectiveTransform vThirdPlanLeftRoof = new PerspectiveTransform();
		vThirdPlanLeftRoof.setUlx(-vUnit.getWidth() * 8);	// Upper left
		vThirdPlanLeftRoof.setUly(vUnit.getHeight() * 4);
		vThirdPlanLeftRoof.setUrx(vUnit.getWidth() * 4);		// Upper right
		vThirdPlanLeftRoof.setUry(vUnit.getHeight() * 4);
		vThirdPlanLeftRoof.setLlx(-vUnit.getWidth() * 2);	// Lower left
		vThirdPlanLeftRoof.setLly(vUnit.getHeight() * 6);
		vThirdPlanLeftRoof.setLrx(vUnit.getWidth() * 6);		// Lower right
		vThirdPlanLeftRoof.setLry(vUnit.getHeight() * 6);
		PerspectiveTransform vThirdPlanCenterRoof = new PerspectiveTransform();
		vThirdPlanCenterRoof.setUlx(vUnit.getWidth() * 4);	// Upper left
		vThirdPlanCenterRoof.setUly(vUnit.getHeight() * 4);
		vThirdPlanCenterRoof.setUrx(vRectangle.getWidth() - vUnit.getWidth() * 4);		// Upper right
		vThirdPlanCenterRoof.setUry(vUnit.getHeight() * 4);
		vThirdPlanCenterRoof.setLlx(vUnit.getWidth() * 6);	// Lower left
		vThirdPlanCenterRoof.setLly(vUnit.getHeight() * 6);
		vThirdPlanCenterRoof.setLrx(vRectangle.getWidth() - vUnit.getWidth() * 6);		// Lower right
		vThirdPlanCenterRoof.setLry(vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanCenterRoof = new PerspectiveTransform();
		vFourthPlanCenterRoof.setUlx(vUnit.getWidth() * 6);	// Upper left
		vFourthPlanCenterRoof.setUly(vUnit.getHeight() * 6);
		vFourthPlanCenterRoof.setUrx(vRectangle.getWidth() - vUnit.getWidth() * 6);		// Upper right
		vFourthPlanCenterRoof.setUry(vUnit.getHeight() * 6);
		vFourthPlanCenterRoof.setLlx(vUnit.getWidth() * 7);	// Lower left
		vFourthPlanCenterRoof.setLly(vUnit.getHeight() * 7);
		vFourthPlanCenterRoof.setLrx(vRectangle.getWidth() - vUnit.getWidth() * 7);		// Lower right
		vFourthPlanCenterRoof.setLry(vUnit.getHeight() * 7);
		PerspectiveTransform vFourthPlanRight1Roof = new PerspectiveTransform();
		vFourthPlanRight1Roof.setUlx(vRectangle.getWidth() - vUnit.getWidth() * 6);	// Upper left
		vFourthPlanRight1Roof.setUly(vUnit.getHeight() * 6);
		vFourthPlanRight1Roof.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 2);		// Upper right
		vFourthPlanRight1Roof.setUry(vUnit.getHeight() * 6);
		vFourthPlanRight1Roof.setLlx(vRectangle.getWidth() - vUnit.getWidth() * 7);	// Lower left
		vFourthPlanRight1Roof.setLly(vUnit.getHeight() * 7);
		vFourthPlanRight1Roof.setLrx(vRectangle.getWidth() - vUnit.getWidth() * 1);		// Lower right
		vFourthPlanRight1Roof.setLry(vUnit.getHeight() * 7);
		PerspectiveTransform vFourthPlanRight2Roof = new PerspectiveTransform();
		vFourthPlanRight2Roof.setUlx(vRectangle.getWidth() + vUnit.getWidth() * 2);	// Upper left
		vFourthPlanRight2Roof.setUly(vUnit.getHeight() * 6);
		vFourthPlanRight2Roof.setUrx(vRectangle.getWidth() + vUnit.getWidth() * 10);		// Upper right
		vFourthPlanRight2Roof.setUry(vUnit.getHeight() * 6);
		vFourthPlanRight2Roof.setLlx(vRectangle.getWidth() - vUnit.getWidth() * 1);	// Lower left
		vFourthPlanRight2Roof.setLly(vUnit.getHeight() * 7);
		vFourthPlanRight2Roof.setLrx(vRectangle.getWidth() + vUnit.getWidth() * 5);		// Lower right
		vFourthPlanRight2Roof.setLry(vUnit.getHeight() * 7);
		PerspectiveTransform vFourthPlanLeft1Roof = new PerspectiveTransform();
		vFourthPlanLeft1Roof.setUlx(-vUnit.getWidth() * 2);	// Upper left
		vFourthPlanLeft1Roof.setUly(vUnit.getHeight() * 6);
		vFourthPlanLeft1Roof.setUrx(vUnit.getWidth() * 6);		// Upper right
		vFourthPlanLeft1Roof.setUry(vUnit.getHeight() * 6);
		vFourthPlanLeft1Roof.setLlx(vUnit.getWidth() * 1);	// Lower left
		vFourthPlanLeft1Roof.setLly(vUnit.getHeight() * 7);
		vFourthPlanLeft1Roof.setLrx(vUnit.getWidth() * 7);		// Lower right
		vFourthPlanLeft1Roof.setLry(vUnit.getHeight() * 7);
		PerspectiveTransform vFourthPlanLeft2Roof = new PerspectiveTransform();
		vFourthPlanLeft2Roof.setUlx(-vUnit.getWidth() * 10);	// Upper left
		vFourthPlanLeft2Roof.setUly(vUnit.getHeight() * 6);
		vFourthPlanLeft2Roof.setUrx(-vUnit.getWidth() * 2);		// Upper right
		vFourthPlanLeft2Roof.setUry(vUnit.getHeight() * 6);
		vFourthPlanLeft2Roof.setLlx(-vUnit.getWidth() * 5);	// Lower left
		vFourthPlanLeft2Roof.setLly(vUnit.getHeight() * 7);
		vFourthPlanLeft2Roof.setLrx(vUnit.getWidth() * 1);		// Lower right
		vFourthPlanLeft2Roof.setLry(vUnit.getHeight() * 7);

		this.aDungeon.mDrawView(pGraphicsContext, new Rectangle(50,50,640,480), new Point2D(this.aX, this.aY), this.aDirrection);
		this.aDungeon.mDrawMiniMap(pGraphicsContext, new Rectangle(700,50,300,300), new Point2D(this.aX, this.aY), this.aDirrection);
		pGraphicsContext.setEffect(null);
		
		pGraphicsContext.setFill(Color.BLACK);
		
		Point2D vOrigin = new Point2D(700, 0);		
	}	
}
