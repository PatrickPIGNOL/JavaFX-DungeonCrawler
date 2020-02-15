package game;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dungeon 
{
	private Random aRandom;
	private List<List<EDungeonMapTerrain>> aDungeonMap;
	private Dimension aMapSize;
	private int aCellSize;
	private Image aWall;
	private Image aGround;
	private Image aRoof;

	public Dungeon(Dimension pMapSize)
	{
		this.aRandom = new Random(System.nanoTime());
		Dimension vDimension = new Dimension();
		this.aCellSize = 5;
		if(pMapSize.height % 2 == 0)
		{
			vDimension.height = pMapSize.height - 1;
		}
		else
		{
			vDimension.height = pMapSize.height;
		}
		if(pMapSize.width % 2 == 0)
		{
			vDimension.width = pMapSize.width - 1;
		}
		else
		{
			vDimension.width = pMapSize.width;
		}
		this.aMapSize = vDimension;
	}

	public void mLoad()
	{
		this.aRoof = new Image("Roof.jpg");
		this.aGround = new Image("Ground.jpg");
		this.aWall = new Image("Wall.png");
		this.mBuildDungeon();
		
	}
	
	private void mBuildDungeon()
	{
		if(this.aDungeonMap != null)
		{
			for(List<EDungeonMapTerrain> vLine : this.aDungeonMap)
			{
				vLine.clear();
			}
			this.aDungeonMap.clear();
			this.aDungeonMap = null;
		}
		this.aDungeonMap = new ArrayList<List<EDungeonMapTerrain>>();
		for(int vYIndex = 0; vYIndex < this.aMapSize.getHeight(); vYIndex++)
		{
			List<EDungeonMapTerrain> vLine = new ArrayList<EDungeonMapTerrain>();
			for(int vXIndex = 0; vXIndex < this.aMapSize.getWidth(); vXIndex++)
			{
				vLine.add(EDungeonMapTerrain.Wall);
			}
			this.aDungeonMap.add(vLine);
		}		
		this.mExplore(1, 1);
	}
	
	private List<EDungeonDirrection> mDirrections(int pX, int pY)
	{
		List<EDungeonDirrection> vResult = new ArrayList<EDungeonDirrection>();
		if
		(
			(pY > 2)
			&& 
			(this.aDungeonMap.get(pY - 2).get(pX) == EDungeonMapTerrain.Wall)
		)
		{
			vResult.add(EDungeonDirrection.Up);
		}
		if
		(
			(pX < this.aMapSize.getWidth() - 2) 
			&& 
			(this.aDungeonMap.get(pY).get(pX + 2) == EDungeonMapTerrain.Wall)
		)
		{
			vResult.add(EDungeonDirrection.Right);
		}
		if
		(
			(pY < this.aMapSize.getHeight() - 2) 
			&& 
			(this.aDungeonMap.get(pY + 2).get(pX) == EDungeonMapTerrain.Wall)
		)
		{
			vResult.add(EDungeonDirrection.Down);
		}
		if
		(
			(pX > 2) 
			&& 
			(this.aDungeonMap.get(pY).get(pX - 2) == EDungeonMapTerrain.Wall)
		)
		{
			vResult.add(EDungeonDirrection.Left);
		}
		return vResult;		
	}
	
	private void mExplore(int pX, int pY)
	{		
		this.mDigWall(pX, pY);
		while(this.mDirrections(pX, pY).size() > 0)
		{
			int vRandom = this.aRandom.nextInt(this.mDirrections(pX, pY).size());
			switch(this.mDirrections(pX, pY).get(vRandom))
			{
				case Up:
				{
					this.mDigWall(pX, pY-1);
					this.mExplore(pX, pY-2);
				}break;
				case Right:
				{
					this.mDigWall(pX + 1, pY);
					this.mExplore(pX + 2, pY);
				}break;
				case Down:
				{
					this.mDigWall(pX, pY + 1);
					this.mExplore(pX, pY + 2);
				}break;
				case Left:
				{
					this.mDigWall(pX - 1, pY);
					this.mExplore(pX - 2, pY);
				}break;
				default:
				{
					
				}break;
			}
		}
	}
	
	private void mDigWall(int pX, int pY)
	{
		this.aDungeonMap.get(pY).set(pX, EDungeonMapTerrain.Path);
	}
	
	public List<List<EDungeonMapTerrain>> mMap()
	{
		return this.aDungeonMap;
	}
	
	public Dimension mDimensions()
	{
		return this.aMapSize;
	}
	
	public int mCellSize()
	{
		return this.aCellSize;
	}
	
	public void mDrawView(GraphicsContext pGraphicsContext, Rectangle pView, Point2D pViewerLocation, EDungeonDirrection pViewerDirrection)
	{
		Canvas vCanvas = pGraphicsContext.getCanvas();
		
		Dimension2D vUnit = new Dimension2D(pView.getWidth() / 20.0, pView.getHeight() / 20.0);
		
		PerspectiveTransform vFirstPlanLeftWall = new PerspectiveTransform();
		vFirstPlanLeftWall.setUlx(pView.getX()+-vUnit.getWidth() * 3);		// Upper left
		vFirstPlanLeftWall.setUly(pView.getY()+-vUnit.getHeight() * 3);
		vFirstPlanLeftWall.setUrx(pView.getX()+vUnit.getWidth());		// Upper right
		vFirstPlanLeftWall.setUry(pView.getY()+vUnit.getHeight());
		vFirstPlanLeftWall.setLlx(pView.getX()+-vUnit.getWidth() * 3);		// Lower left
		vFirstPlanLeftWall.setLly(pView.getY()+pView.getHeight() + vUnit.getHeight() * 3);
		vFirstPlanLeftWall.setLrx(pView.getX()+vUnit.getWidth());		// Lower right
		vFirstPlanLeftWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight());
		PerspectiveTransform vFirstPlanRightWall = new PerspectiveTransform();
		vFirstPlanRightWall.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth());		// Upper left
		vFirstPlanRightWall.setUly(pView.getY()+vUnit.getHeight());
		vFirstPlanRightWall.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 3);		// Upper right
		vFirstPlanRightWall.setUry(pView.getY()+-vUnit.getHeight() * 3);
		vFirstPlanRightWall.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth());		// Lower left
		vFirstPlanRightWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight());
		vFirstPlanRightWall.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 3);		// Lower right
		vFirstPlanRightWall.setLry(pView.getY()+pView.getHeight() + vUnit.getHeight() * 3);
		PerspectiveTransform vSecondPlanLeftWall = new PerspectiveTransform();
		vSecondPlanLeftWall.setUlx(pView.getX()+vUnit.getWidth());	// Upper left
		vSecondPlanLeftWall.setUly(pView.getY()+vUnit.getHeight());
		vSecondPlanLeftWall.setUrx(pView.getX()+vUnit.getWidth() * 4);		// Upper right
		vSecondPlanLeftWall.setUry(pView.getY()+vUnit.getHeight() * 4);
		vSecondPlanLeftWall.setLlx(pView.getX()+vUnit.getWidth());	// Lower left
		vSecondPlanLeftWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight());
		vSecondPlanLeftWall.setLrx(pView.getX()+vUnit.getWidth()*4);		// Lower right
		vSecondPlanLeftWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight()*4);
		PerspectiveTransform vSecondPlanRightWall = new PerspectiveTransform();
		vSecondPlanRightWall.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);	// Upper left
		vSecondPlanRightWall.setUly(pView.getY()+vUnit.getHeight() * 4);
		vSecondPlanRightWall.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth());		// Upper right
		vSecondPlanRightWall.setUry(pView.getY()+vUnit.getHeight());
		vSecondPlanRightWall.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);	// Lower left
		vSecondPlanRightWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 4);
		vSecondPlanRightWall.setLrx(pView.getX()+pView.getWidth()-vUnit.getWidth());		// Lower right
		vSecondPlanRightWall.setLry(pView.getY()+pView.getHeight()-vUnit.getHeight());
		PerspectiveTransform vSecondPlanRightFaceWall = new PerspectiveTransform();
		vSecondPlanRightFaceWall.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth());	// Upper left
		vSecondPlanRightFaceWall.setUly(pView.getY()+vUnit.getHeight());
		vSecondPlanRightFaceWall.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 17);		// Upper right
		vSecondPlanRightFaceWall.setUry(pView.getY()+vUnit.getHeight());
		vSecondPlanRightFaceWall.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth());	// Lower left
		vSecondPlanRightFaceWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight());
		vSecondPlanRightFaceWall.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 17);		// Lower right
		vSecondPlanRightFaceWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight());
		PerspectiveTransform vSecondPlanLeftFaceWall = new PerspectiveTransform();
		vSecondPlanLeftFaceWall.setUlx(pView.getX()+-vUnit.getWidth() * 17);	// Upper left
		vSecondPlanLeftFaceWall.setUly(pView.getY()+vUnit.getHeight());
		vSecondPlanLeftFaceWall.setUrx(pView.getX()+vUnit.getWidth());		// Upper right
		vSecondPlanLeftFaceWall.setUry(pView.getY()+vUnit.getHeight());
		vSecondPlanLeftFaceWall.setLlx(pView.getX()+-vUnit.getWidth() * 17);	// Lower left
		vSecondPlanLeftFaceWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight());
		vSecondPlanLeftFaceWall.setLrx(pView.getX()+vUnit.getWidth());		// Lower right
		vSecondPlanLeftFaceWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight());
		PerspectiveTransform vSecondPlanFaceWall = new PerspectiveTransform();
		vSecondPlanFaceWall.setUlx(pView.getX()+vUnit.getWidth());	// Upper left
		vSecondPlanFaceWall.setUly(pView.getY()+vUnit.getHeight());
		vSecondPlanFaceWall.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth());		// Upper right
		vSecondPlanFaceWall.setUry(pView.getY()+vUnit.getHeight());
		vSecondPlanFaceWall.setLlx(pView.getX()+vUnit.getWidth());	// Lower left
		vSecondPlanFaceWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight());
		vSecondPlanFaceWall.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth());		// Lower right
		vSecondPlanFaceWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight());
		PerspectiveTransform vThirdPlanLeftFaceWall = new PerspectiveTransform();
		vThirdPlanLeftFaceWall.setUlx(pView.getX()+-vUnit.getWidth() * 8);	// Upper left
		vThirdPlanLeftFaceWall.setUly(pView.getY()+vUnit.getHeight() * 4);
		vThirdPlanLeftFaceWall.setUrx(pView.getX()+vUnit.getWidth() * 4);		// Upper right
		vThirdPlanLeftFaceWall.setUry(pView.getY()+vUnit.getHeight() * 4);
		vThirdPlanLeftFaceWall.setLlx(pView.getX()+-vUnit.getWidth() * 8);	// Lower left
		vThirdPlanLeftFaceWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 4);
		vThirdPlanLeftFaceWall.setLrx(pView.getX()+vUnit.getWidth() * 4);		// Lower right
		vThirdPlanLeftFaceWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 4);
		PerspectiveTransform vThirdPlanRightFaceWall = new PerspectiveTransform();
		vThirdPlanRightFaceWall.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);	// Upper left
		vThirdPlanRightFaceWall.setUly(pView.getY()+vUnit.getHeight() * 4);
		vThirdPlanRightFaceWall.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 8);		// Upper right
		vThirdPlanRightFaceWall.setUry(pView.getY()+vUnit.getHeight() * 4);
		vThirdPlanRightFaceWall.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);	// Lower left
		vThirdPlanRightFaceWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 4);
		vThirdPlanRightFaceWall.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 8);		// Lower right
		vThirdPlanRightFaceWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 4);
		PerspectiveTransform vThirdPlanFaceWall = new PerspectiveTransform();
		vThirdPlanFaceWall.setUlx(pView.getX()+vUnit.getWidth() * 4);	// Upper left
		vThirdPlanFaceWall.setUly(pView.getY()+vUnit.getHeight() * 4);
		vThirdPlanFaceWall.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);		// Upper right
		vThirdPlanFaceWall.setUry(pView.getY()+vUnit.getHeight() * 4);
		vThirdPlanFaceWall.setLlx(pView.getX()+vUnit.getWidth() * 4);	// Lower left
		vThirdPlanFaceWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 4);
		vThirdPlanFaceWall.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);		// Lower right
		vThirdPlanFaceWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 4);
		PerspectiveTransform vThirdPlanRightWall = new PerspectiveTransform();
		vThirdPlanRightWall.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 6);	// Upper left
		vThirdPlanRightWall.setUly(pView.getY()+vUnit.getHeight() * 6);
		vThirdPlanRightWall.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);		// Upper right
		vThirdPlanRightWall.setUry(pView.getY()+vUnit.getHeight() * 4);
		vThirdPlanRightWall.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 6);	// Lower left
		vThirdPlanRightWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 6);
		vThirdPlanRightWall.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);		// Lower right
		vThirdPlanRightWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 4);
		PerspectiveTransform vThirdPlanLeftWall = new PerspectiveTransform();
		vThirdPlanLeftWall.setUlx(pView.getX()+vUnit.getWidth() * 4);	// Upper left
		vThirdPlanLeftWall.setUly(pView.getY()+vUnit.getHeight() * 4);
		vThirdPlanLeftWall.setUrx(pView.getX()+vUnit.getWidth() * 6);		// Upper right
		vThirdPlanLeftWall.setUry(pView.getY()+vUnit.getHeight() * 6);
		vThirdPlanLeftWall.setLlx(pView.getX()+vUnit.getWidth() * 4);	// Lower left
		vThirdPlanLeftWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 4);
		vThirdPlanLeftWall.setLrx(pView.getX()+vUnit.getWidth() * 6);		// Lower right
		vThirdPlanLeftWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanFaceWall = new PerspectiveTransform();
		vFourthPlanFaceWall.setUlx(pView.getX()+vUnit.getWidth() * 6);	// Upper left
		vFourthPlanFaceWall.setUly(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanFaceWall.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 6);		// Upper right
		vFourthPlanFaceWall.setUry(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanFaceWall.setLlx(pView.getX()+vUnit.getWidth() * 6);	// Lower left
		vFourthPlanFaceWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanFaceWall.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 6);		// Lower right
		vFourthPlanFaceWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanLeftFaceWall = new PerspectiveTransform();
		vFourthPlanLeftFaceWall.setUlx(pView.getX()+-vUnit.getWidth() * 2);	// Upper left
		vFourthPlanLeftFaceWall.setUly(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanLeftFaceWall.setUrx(pView.getX()+vUnit.getWidth() * 6);		// Upper right
		vFourthPlanLeftFaceWall.setUry(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanLeftFaceWall.setLlx(pView.getX()+-vUnit.getWidth() * 2);	// Lower left
		vFourthPlanLeftFaceWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanLeftFaceWall.setLrx(pView.getX()+vUnit.getWidth() * 6);		// Lower right
		vFourthPlanLeftFaceWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanRightFaceWall = new PerspectiveTransform();
		vFourthPlanRightFaceWall.setUlx(pView.getX()+pView.getWidth()-vUnit.getWidth() * 6);	// Upper left
		vFourthPlanRightFaceWall.setUly(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanRightFaceWall.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 2);		// Upper right
		vFourthPlanRightFaceWall.setUry(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanRightFaceWall.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 6);	// Lower left
		vFourthPlanRightFaceWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanRightFaceWall.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 2);		// Lower right
		vFourthPlanRightFaceWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanRight1Wall = new PerspectiveTransform();
		vFourthPlanRight1Wall.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 7);	// Upper left
		vFourthPlanRight1Wall.setUly(pView.getY()+vUnit.getHeight() * 7);
		vFourthPlanRight1Wall.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 6);		// Upper right
		vFourthPlanRight1Wall.setUry(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanRight1Wall.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 7);	// Lower left
		vFourthPlanRight1Wall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 7);
		vFourthPlanRight1Wall.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 6);		// Lower right
		vFourthPlanRight1Wall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanRight2Wall = new PerspectiveTransform();
		vFourthPlanRight2Wall.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);	// Upper left
		vFourthPlanRight2Wall.setUly(pView.getY()+vUnit.getHeight() * 7);
		vFourthPlanRight2Wall.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 2);		// Upper right
		vFourthPlanRight2Wall.setUry(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanRight2Wall.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);	// Lower left
		vFourthPlanRight2Wall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 7);
		vFourthPlanRight2Wall.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 2);		// Lower right
		vFourthPlanRight2Wall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanLeft1Wall = new PerspectiveTransform();
		vFourthPlanLeft1Wall.setUlx(pView.getX()+vUnit.getWidth() * 6);	// Upper left
		vFourthPlanLeft1Wall.setUly(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanLeft1Wall.setUrx(pView.getX()+vUnit.getWidth() * 7);		// Upper right
		vFourthPlanLeft1Wall.setUry(pView.getY()+vUnit.getHeight() * 7);
		vFourthPlanLeft1Wall.setLlx(pView.getX()+vUnit.getWidth() * 6);	// Lower left
		vFourthPlanLeft1Wall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanLeft1Wall.setLrx(pView.getX()+vUnit.getWidth() * 7);		// Lower right
		vFourthPlanLeft1Wall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		PerspectiveTransform vFourthPlanLeft2Wall = new PerspectiveTransform();
		vFourthPlanLeft2Wall.setUlx(pView.getX()+-vUnit.getWidth() * 2);	// Upper left
		vFourthPlanLeft2Wall.setUly(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanLeft2Wall.setUrx(pView.getX()+vUnit.getWidth() * 1);		// Upper right
		vFourthPlanLeft2Wall.setUry(pView.getY()+vUnit.getHeight() * 7);
		vFourthPlanLeft2Wall.setLlx(pView.getX()+-vUnit.getWidth() * 2);	// Lower left
		vFourthPlanLeft2Wall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanLeft2Wall.setLrx(pView.getX()+vUnit.getWidth() * 1);		// Lower right
		vFourthPlanLeft2Wall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		PerspectiveTransform vFifthPlanFaceWall = new PerspectiveTransform();
		vFifthPlanFaceWall.setUlx(pView.getX()+vUnit.getWidth() * 7);	// Upper left
		vFifthPlanFaceWall.setUly(pView.getY()+vUnit.getHeight() * 7);
		vFifthPlanFaceWall.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 7);		// Upper right
		vFifthPlanFaceWall.setUry(pView.getY()+vUnit.getHeight() * 7);
		vFifthPlanFaceWall.setLlx(pView.getX()+vUnit.getWidth() * 7);	// Lower left
		vFifthPlanFaceWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 7);
		vFifthPlanFaceWall.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 7);		// Lower right
		vFifthPlanFaceWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		PerspectiveTransform vFifthPlanRight1FaceWall = new PerspectiveTransform();
		vFifthPlanRight1FaceWall.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 7);	// Upper left
		vFifthPlanRight1FaceWall.setUly(pView.getY()+vUnit.getHeight() * 7);
		vFifthPlanRight1FaceWall.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);		// Upper right
		vFifthPlanRight1FaceWall.setUry(pView.getY()+vUnit.getHeight() * 7);
		vFifthPlanRight1FaceWall.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 7);	// Lower left
		vFifthPlanRight1FaceWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 7);
		vFifthPlanRight1FaceWall.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);		// Lower right
		vFifthPlanRight1FaceWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		PerspectiveTransform vFifthPlanRight2FaceWall = new PerspectiveTransform();
		vFifthPlanRight2FaceWall.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);	// Upper left
		vFifthPlanRight2FaceWall.setUly(pView.getY()+vUnit.getHeight() * 7);
		vFifthPlanRight2FaceWall.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 5);		// Upper right
		vFifthPlanRight2FaceWall.setUry(pView.getY()+vUnit.getHeight() * 7);
		vFifthPlanRight2FaceWall.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);	// Lower left
		vFifthPlanRight2FaceWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 7);
		vFifthPlanRight2FaceWall.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 5);		// Lower right
		vFifthPlanRight2FaceWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		PerspectiveTransform vFifthPlanLeft1FaceWall = new PerspectiveTransform();
		vFifthPlanLeft1FaceWall.setUlx(pView.getX()+vUnit.getWidth() * 1);	// Upper left
		vFifthPlanLeft1FaceWall.setUly(pView.getY()+vUnit.getHeight() * 7);
		vFifthPlanLeft1FaceWall.setUrx(pView.getX()+vUnit.getWidth() * 7);		// Upper right
		vFifthPlanLeft1FaceWall.setUry(pView.getY()+vUnit.getHeight() * 7);
		vFifthPlanLeft1FaceWall.setLlx(pView.getX()+vUnit.getWidth() * 1);	// Lower left
		vFifthPlanLeft1FaceWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 7);
		vFifthPlanLeft1FaceWall.setLrx(pView.getX()+vUnit.getWidth() * 7);		// Lower right
		vFifthPlanLeft1FaceWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		PerspectiveTransform vFifthPlanLeft2FaceWall = new PerspectiveTransform();
		vFifthPlanLeft2FaceWall.setUlx(pView.getX()+-vUnit.getWidth() * 5);	// Upper left
		vFifthPlanLeft2FaceWall.setUly(pView.getY()+vUnit.getHeight() * 7);
		vFifthPlanLeft2FaceWall.setUrx(pView.getX()+vUnit.getWidth() * 1);		// Upper right
		vFifthPlanLeft2FaceWall.setUry(pView.getY()+vUnit.getHeight() * 7);
		vFifthPlanLeft2FaceWall.setLlx(pView.getX()+-vUnit.getWidth() * 5);	// Lower left
		vFifthPlanLeft2FaceWall.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 7);
		vFifthPlanLeft2FaceWall.setLrx(pView.getX()+vUnit.getWidth() * 1);		// Lower right
		vFifthPlanLeft2FaceWall.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);

		PerspectiveTransform vFourthPlanLeft2Ground = new PerspectiveTransform();
		vFourthPlanLeft2Ground.setUlx(pView.getX()+-vUnit.getWidth() * 5);	// Upper left
		vFourthPlanLeft2Ground.setUly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanLeft2Ground.setUrx(pView.getX()+vUnit.getWidth() * 1);		// Upper right
		vFourthPlanLeft2Ground.setUry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanLeft2Ground.setLlx(pView.getX()+-vUnit.getWidth() * 10);	// Lower left
		vFourthPlanLeft2Ground.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanLeft2Ground.setLrx(pView.getX()+-vUnit.getWidth() * 2);		// Lower right
		vFourthPlanLeft2Ground.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanRight2Ground = new PerspectiveTransform();
		vFourthPlanRight2Ground.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);	// Upper left
		vFourthPlanRight2Ground.setUly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanRight2Ground.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 5);		// Upper right
		vFourthPlanRight2Ground.setUry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanRight2Ground.setLlx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 2);	// Lower left
		vFourthPlanRight2Ground.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanRight2Ground.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 10);		// Lower right
		vFourthPlanRight2Ground.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanRight1Ground = new PerspectiveTransform();
		vFourthPlanRight1Ground.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 7);	// Upper left
		vFourthPlanRight1Ground.setUly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanRight1Ground.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);		// Upper right
		vFourthPlanRight1Ground.setUry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanRight1Ground.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 6);	// Lower left
		vFourthPlanRight1Ground.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanRight1Ground.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 2);		// Lower right
		vFourthPlanRight1Ground.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanLeft1Ground = new PerspectiveTransform();
		vFourthPlanLeft1Ground.setUlx(pView.getX()+vUnit.getWidth() * 1);	// Upper left
		vFourthPlanLeft1Ground.setUly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanLeft1Ground.setUrx(pView.getX()+vUnit.getWidth() * 7);		// Upper right
		vFourthPlanLeft1Ground.setUry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanLeft1Ground.setLlx(pView.getX()+-vUnit.getWidth() * 2);	// Lower left
		vFourthPlanLeft1Ground.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanLeft1Ground.setLrx(pView.getX()+vUnit.getWidth() * 6);		// Lower right
		vFourthPlanLeft1Ground.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanCenterGround = new PerspectiveTransform();
		vFourthPlanCenterGround.setUlx(pView.getX()+vUnit.getWidth() * 7);	// Upper left
		vFourthPlanCenterGround.setUly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanCenterGround.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 7);		// Upper right
		vFourthPlanCenterGround.setUry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 7);
		vFourthPlanCenterGround.setLlx(pView.getX()+vUnit.getWidth() * 6);	// Lower left
		vFourthPlanCenterGround.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 6);
		vFourthPlanCenterGround.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 6);		// Lower right
		vFourthPlanCenterGround.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		PerspectiveTransform vThirdPlanLeftGround = new PerspectiveTransform();
		vThirdPlanLeftGround.setUlx(pView.getX()+-vUnit.getWidth() * 2);	// Upper left
		vThirdPlanLeftGround.setUly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		vThirdPlanLeftGround.setUrx(pView.getX()+vUnit.getWidth() * 6);		// Upper right
		vThirdPlanLeftGround.setUry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		vThirdPlanLeftGround.setLlx(pView.getX()+-vUnit.getWidth() * 8);	// Lower left
		vThirdPlanLeftGround.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 4);
		vThirdPlanLeftGround.setLrx(pView.getX()+vUnit.getWidth() * 4);		// Lower right
		vThirdPlanLeftGround.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 4);
		PerspectiveTransform vThirdPlanRightGround = new PerspectiveTransform();
		vThirdPlanRightGround.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 6);	// Upper left
		vThirdPlanRightGround.setUly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		vThirdPlanRightGround.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 2);		// Upper right
		vThirdPlanRightGround.setUry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		vThirdPlanRightGround.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);	// Lower left
		vThirdPlanRightGround.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 4);
		vThirdPlanRightGround.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 8);		// Lower right
		vThirdPlanRightGround.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 4);
		PerspectiveTransform vThirdPlanCenterGround = new PerspectiveTransform();
		vThirdPlanCenterGround.setUlx(pView.getX()+vUnit.getWidth() * 6);	// Upper left
		vThirdPlanCenterGround.setUly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		vThirdPlanCenterGround.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 6);		// Upper right
		vThirdPlanCenterGround.setUry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 6);
		vThirdPlanCenterGround.setLlx(pView.getX()+vUnit.getWidth() * 4);	// Lower left
		vThirdPlanCenterGround.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 4);
		vThirdPlanCenterGround.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);		// Lower right
		vThirdPlanCenterGround.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 4);
		PerspectiveTransform vSecondPlanRightGround = new PerspectiveTransform();
		vSecondPlanRightGround.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);	// Upper left
		vSecondPlanRightGround.setUly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 4);
		vSecondPlanRightGround.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 8);		// Upper right
		vSecondPlanRightGround.setUry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 4);
		vSecondPlanRightGround.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);	// Lower left
		vSecondPlanRightGround.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 1);
		vSecondPlanRightGround.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 18);		// Lower right
		vSecondPlanRightGround.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 1);
		PerspectiveTransform vSecondPlanLeftGround = new PerspectiveTransform();
		vSecondPlanLeftGround.setUlx(pView.getX()+-vUnit.getWidth() * 8);	// Upper left
		vSecondPlanLeftGround.setUly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 4);
		vSecondPlanLeftGround.setUrx(pView.getX()+vUnit.getWidth() * 4);		// Upper right
		vSecondPlanLeftGround.setUry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 4);
		vSecondPlanLeftGround.setLlx(pView.getX()+-vUnit.getWidth() * 17);	// Lower left
		vSecondPlanLeftGround.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 1);
		vSecondPlanLeftGround.setLrx(pView.getX()+vUnit.getWidth() * 1);		// Lower right
		vSecondPlanLeftGround.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 1);
		PerspectiveTransform vSecondPlanCenterGround = new PerspectiveTransform();
		vSecondPlanCenterGround.setUlx(pView.getX()+vUnit.getWidth() * 4);	// Upper left
		vSecondPlanCenterGround.setUly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 4);
		vSecondPlanCenterGround.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);		// Upper right
		vSecondPlanCenterGround.setUry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 4);
		vSecondPlanCenterGround.setLlx(pView.getX()+vUnit.getWidth() * 1);	// Lower left
		vSecondPlanCenterGround.setLly(pView.getY()+pView.getHeight() - vUnit.getHeight()  * 1);
		vSecondPlanCenterGround.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);		// Lower right
		vSecondPlanCenterGround.setLry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 1);
		PerspectiveTransform vFirstPlanLeftGround = new PerspectiveTransform();
		vFirstPlanLeftGround.setUlx(pView.getX()+-vUnit.getWidth() * 17);	// Upper left
		vFirstPlanLeftGround.setUly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 1);
		vFirstPlanLeftGround.setUrx(pView.getX()+vUnit.getWidth() * 1);		// Upper right
		vFirstPlanLeftGround.setUry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 1);
		vFirstPlanLeftGround.setLlx(pView.getX()+-vUnit.getWidth() * 28);	// Lower left
		vFirstPlanLeftGround.setLly(pView.getY()+pView.getHeight() + vUnit.getHeight()  * 3);
		vFirstPlanLeftGround.setLrx(pView.getX()+-vUnit.getWidth() * 3);		// Lower right
		vFirstPlanLeftGround.setLry(pView.getY()+pView.getHeight() + vUnit.getHeight() * 3);
		PerspectiveTransform vFirstPlanRightGround = new PerspectiveTransform();
		vFirstPlanRightGround.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);	// Upper left
		vFirstPlanRightGround.setUly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 1);
		vFirstPlanRightGround.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 18);		// Upper right
		vFirstPlanRightGround.setUry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 1);
		vFirstPlanRightGround.setLlx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 3);	// Lower left
		vFirstPlanRightGround.setLly(pView.getY()+pView.getHeight() + vUnit.getHeight() * 3);
		vFirstPlanRightGround.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 30);		// Lower right
		vFirstPlanRightGround.setLry(pView.getY()+pView.getHeight() + vUnit.getHeight() * 3);
		PerspectiveTransform vFirstPlanCenterGround = new PerspectiveTransform();
		vFirstPlanCenterGround.setUlx(pView.getX()+vUnit.getWidth() * 1);	// Upper left
		vFirstPlanCenterGround.setUly(pView.getY()+pView.getHeight() - vUnit.getHeight() * 1);
		vFirstPlanCenterGround.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);		// Upper right
		vFirstPlanCenterGround.setUry(pView.getY()+pView.getHeight() - vUnit.getHeight() * 1);
		vFirstPlanCenterGround.setLlx(pView.getX()+-vUnit.getWidth() * 3);	// Lower left
		vFirstPlanCenterGround.setLly(pView.getY()+pView.getHeight() + vUnit.getHeight()  * 3);
		vFirstPlanCenterGround.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 3);		// Lower right
		vFirstPlanCenterGround.setLry(pView.getY()+pView.getHeight() + vUnit.getHeight() * 3);
		
		
		
		PerspectiveTransform vFirstPlanCenterRoof = new PerspectiveTransform();
		vFirstPlanCenterRoof.setUlx(pView.getX()+-vUnit.getWidth() * 3);	// Upper left
		vFirstPlanCenterRoof.setUly(pView.getY()+-vUnit.getHeight() * 3);
		vFirstPlanCenterRoof.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 3);		// Upper right
		vFirstPlanCenterRoof.setUry(pView.getY()+-vUnit.getHeight() * 3);
		vFirstPlanCenterRoof.setLlx(pView.getX()+vUnit.getWidth() * 1);	// Lower left
		vFirstPlanCenterRoof.setLly(pView.getY()+vUnit.getHeight()  * 1);
		vFirstPlanCenterRoof.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);		// Lower right
		vFirstPlanCenterRoof.setLry(pView.getY()+vUnit.getHeight() * 1);
		PerspectiveTransform vFirstPlanLeftRoof = new PerspectiveTransform();
		vFirstPlanLeftRoof.setUlx(pView.getX()+-vUnit.getWidth() * 28);	// Upper left                                
		vFirstPlanLeftRoof.setUly(pView.getY()+-vUnit.getHeight() * 3);                                             
		vFirstPlanLeftRoof.setUrx(pView.getX()+-vUnit.getWidth() * 3);		// Upper right   
		vFirstPlanLeftRoof.setUry(pView.getY()+-vUnit.getHeight() * 3);                                             
		vFirstPlanLeftRoof.setLlx(pView.getX()+-vUnit.getWidth() * 17);	// Lower left                                
		vFirstPlanLeftRoof.setLly(pView.getY()+vUnit.getHeight()  * 1);                                             
		vFirstPlanLeftRoof.setLrx(pView.getX()+vUnit.getWidth() * 1);		// Lower right   
		vFirstPlanLeftRoof.setLry(pView.getY()+vUnit.getHeight() * 1);                                              
		PerspectiveTransform vFirstPlanRightRoof = new PerspectiveTransform();
		vFirstPlanRightRoof.setUlx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 3);	// Upper left
		vFirstPlanRightRoof.setUly(pView.getY()+-vUnit.getHeight() * 3);
		vFirstPlanRightRoof.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 28);		// Upper right
		vFirstPlanRightRoof.setUry(pView.getY()+-vUnit.getHeight() * 3);
		vFirstPlanRightRoof.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);	// Lower left
		vFirstPlanRightRoof.setLly(pView.getY()+vUnit.getHeight() * 1);
		vFirstPlanRightRoof.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 17);		// Lower right
		vFirstPlanRightRoof.setLry(pView.getY()+vUnit.getHeight() * 1);
		PerspectiveTransform vSecondPlanCenterRoof = new PerspectiveTransform();
		vSecondPlanCenterRoof.setUlx(pView.getX()+vUnit.getWidth() * 1);	// Upper left
		vSecondPlanCenterRoof.setUly(pView.getY()+vUnit.getHeight() * 1);
		vSecondPlanCenterRoof.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);		// Upper right
		vSecondPlanCenterRoof.setUry(pView.getY()+vUnit.getHeight() * 1);
		vSecondPlanCenterRoof.setLlx(pView.getX()+vUnit.getWidth() * 4);	// Lower left
		vSecondPlanCenterRoof.setLly(pView.getY()+vUnit.getHeight() * 4);
		vSecondPlanCenterRoof.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);		// Lower right
		vSecondPlanCenterRoof.setLry(pView.getY()+vUnit.getHeight() * 4);
		PerspectiveTransform vSecondPlanRightRoof = new PerspectiveTransform();
		vSecondPlanRightRoof.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);	// Upper left
		vSecondPlanRightRoof.setUly(pView.getY()+vUnit.getHeight() * 1);
		vSecondPlanRightRoof.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 17);		// Upper right
		vSecondPlanRightRoof.setUry(pView.getY()+vUnit.getHeight() * 1);
		vSecondPlanRightRoof.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);	// Lower left
		vSecondPlanRightRoof.setLly(pView.getY()+vUnit.getHeight() * 4);
		vSecondPlanRightRoof.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 8);		// Lower right
		vSecondPlanRightRoof.setLry(pView.getY()+vUnit.getHeight() * 4);
		PerspectiveTransform vSecondPlanLeftRoof = new PerspectiveTransform();
		vSecondPlanLeftRoof.setUlx(pView.getX()+-vUnit.getWidth() * 17);	// Upper left
		vSecondPlanLeftRoof.setUly(pView.getY()+vUnit.getHeight() * 1);
		vSecondPlanLeftRoof.setUrx(pView.getX()+vUnit.getWidth() * 1);		// Upper right
		vSecondPlanLeftRoof.setUry(pView.getY()+vUnit.getHeight() * 1);
		vSecondPlanLeftRoof.setLlx(pView.getX()+-vUnit.getWidth() * 8);	// Lower left
		vSecondPlanLeftRoof.setLly(pView.getY()+vUnit.getHeight() * 4);
		vSecondPlanLeftRoof.setLrx(pView.getX()+vUnit.getWidth() * 4);		// Lower right
		vSecondPlanLeftRoof.setLry(pView.getY()+vUnit.getHeight() * 4);
		PerspectiveTransform vThirdPlanRightRoof = new PerspectiveTransform();
		vThirdPlanRightRoof.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);	// Upper left
		vThirdPlanRightRoof.setUly(pView.getY()+vUnit.getHeight() * 4);
		vThirdPlanRightRoof.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 8);		// Upper right
		vThirdPlanRightRoof.setUry(pView.getY()+vUnit.getHeight() * 4);
		vThirdPlanRightRoof.setLlx(pView.getX()+pView.getWidth()-vUnit.getWidth() * 6);	// Lower left
		vThirdPlanRightRoof.setLly(pView.getY()+vUnit.getHeight() * 6);
		vThirdPlanRightRoof.setLrx(pView.getX()+pView.getWidth()+vUnit.getWidth() * 2);		// Lower right
		vThirdPlanRightRoof.setLry(pView.getY()+vUnit.getHeight() * 6);
		PerspectiveTransform vThirdPlanLeftRoof = new PerspectiveTransform();
		vThirdPlanLeftRoof.setUlx(pView.getX()+-vUnit.getWidth() * 8);	// Upper left
		vThirdPlanLeftRoof.setUly(pView.getY()+vUnit.getHeight() * 4);
		vThirdPlanLeftRoof.setUrx(pView.getX()+vUnit.getWidth() * 4);		// Upper right
		vThirdPlanLeftRoof.setUry(pView.getY()+vUnit.getHeight() * 4);
		vThirdPlanLeftRoof.setLlx(pView.getX()+-vUnit.getWidth() * 2);	// Lower left
		vThirdPlanLeftRoof.setLly(pView.getY()+vUnit.getHeight() * 6);
		vThirdPlanLeftRoof.setLrx(pView.getX()+vUnit.getWidth() * 6);		// Lower right
		vThirdPlanLeftRoof.setLry(pView.getY()+vUnit.getHeight() * 6);
		PerspectiveTransform vThirdPlanCenterRoof = new PerspectiveTransform();
		vThirdPlanCenterRoof.setUlx(pView.getX()+vUnit.getWidth() * 4);	// Upper left
		vThirdPlanCenterRoof.setUly(pView.getY()+vUnit.getHeight() * 4);
		vThirdPlanCenterRoof.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 4);		// Upper right
		vThirdPlanCenterRoof.setUry(pView.getY()+vUnit.getHeight() * 4);
		vThirdPlanCenterRoof.setLlx(pView.getX()+vUnit.getWidth() * 6);	// Lower left
		vThirdPlanCenterRoof.setLly(pView.getY()+vUnit.getHeight() * 6);
		vThirdPlanCenterRoof.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 6);		// Lower right
		vThirdPlanCenterRoof.setLry(pView.getY()+vUnit.getHeight() * 6);
		PerspectiveTransform vFourthPlanCenterRoof = new PerspectiveTransform();
		vFourthPlanCenterRoof.setUlx(pView.getX()+vUnit.getWidth() * 6);	// Upper left
		vFourthPlanCenterRoof.setUly(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanCenterRoof.setUrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 6);		// Upper right
		vFourthPlanCenterRoof.setUry(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanCenterRoof.setLlx(pView.getX()+vUnit.getWidth() * 7);	// Lower left
		vFourthPlanCenterRoof.setLly(pView.getY()+vUnit.getHeight() * 7);
		vFourthPlanCenterRoof.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 7);		// Lower right
		vFourthPlanCenterRoof.setLry(pView.getY()+vUnit.getHeight() * 7);
		PerspectiveTransform vFourthPlanRight1Roof = new PerspectiveTransform();
		vFourthPlanRight1Roof.setUlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 6);	// Upper left
		vFourthPlanRight1Roof.setUly(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanRight1Roof.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 2);		// Upper right
		vFourthPlanRight1Roof.setUry(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanRight1Roof.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 7);	// Lower left
		vFourthPlanRight1Roof.setLly(pView.getY()+vUnit.getHeight() * 7);
		vFourthPlanRight1Roof.setLrx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);		// Lower right
		vFourthPlanRight1Roof.setLry(pView.getY()+vUnit.getHeight() * 7);
		PerspectiveTransform vFourthPlanRight2Roof = new PerspectiveTransform();
		vFourthPlanRight2Roof.setUlx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 2);	// Upper left
		vFourthPlanRight2Roof.setUly(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanRight2Roof.setUrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 10);		// Upper right
		vFourthPlanRight2Roof.setUry(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanRight2Roof.setLlx(pView.getX()+pView.getWidth() - vUnit.getWidth() * 1);	// Lower left
		vFourthPlanRight2Roof.setLly(pView.getY()+vUnit.getHeight() * 7);
		vFourthPlanRight2Roof.setLrx(pView.getX()+pView.getWidth() + vUnit.getWidth() * 5);		// Lower right
		vFourthPlanRight2Roof.setLry(pView.getY()+vUnit.getHeight() * 7);
		PerspectiveTransform vFourthPlanLeft1Roof = new PerspectiveTransform();
		vFourthPlanLeft1Roof.setUlx(pView.getX()+-vUnit.getWidth() * 2);	// Upper left
		vFourthPlanLeft1Roof.setUly(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanLeft1Roof.setUrx(pView.getX()+vUnit.getWidth() * 6);		// Upper right
		vFourthPlanLeft1Roof.setUry(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanLeft1Roof.setLlx(pView.getX()+vUnit.getWidth() * 1);	// Lower left
		vFourthPlanLeft1Roof.setLly(pView.getY()+vUnit.getHeight() * 7);
		vFourthPlanLeft1Roof.setLrx(pView.getX()+vUnit.getWidth() * 7);		// Lower right
		vFourthPlanLeft1Roof.setLry(pView.getY()+vUnit.getHeight() * 7);
		PerspectiveTransform vFourthPlanLeft2Roof = new PerspectiveTransform();
		vFourthPlanLeft2Roof.setUlx(pView.getX()+-vUnit.getWidth() * 10);	// Upper left
		vFourthPlanLeft2Roof.setUly(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanLeft2Roof.setUrx(pView.getX()+-vUnit.getWidth() * 2);		// Upper right
		vFourthPlanLeft2Roof.setUry(pView.getY()+vUnit.getHeight() * 6);
		vFourthPlanLeft2Roof.setLlx(pView.getX()+-vUnit.getWidth() * 5);	// Lower left
		vFourthPlanLeft2Roof.setLly(pView.getY()+vUnit.getHeight() * 7);
		vFourthPlanLeft2Roof.setLrx(pView.getX()+vUnit.getWidth() * 1);		// Lower right
		vFourthPlanLeft2Roof.setLry(pView.getY()+vUnit.getHeight() * 7);
		
		EDungeonMapTerrain vFirstLeft    = null;
		EDungeonMapTerrain vFirstRight   = null;
		EDungeonMapTerrain vSecondLeft   = null;
		EDungeonMapTerrain vSecondCenter = null;
		EDungeonMapTerrain vSecondRight  = null;
		EDungeonMapTerrain vThirdLeft    = null;
		EDungeonMapTerrain vThirdCenter  = null;
		EDungeonMapTerrain vThirdRight   = null;
		EDungeonMapTerrain vFourthLeft1  = null;
		EDungeonMapTerrain vFourthLeft2  = null;
		EDungeonMapTerrain vFourthCenter = null;
		EDungeonMapTerrain vFourthRight1 = null;
		EDungeonMapTerrain vFourthRight2 = null;
		EDungeonMapTerrain vFifthLeft1   = null;
		EDungeonMapTerrain vFifthLeft2   = null;
		EDungeonMapTerrain vFifthCenter  = null;
		EDungeonMapTerrain vFifthRight1  = null;
		EDungeonMapTerrain vFifthRight2  = null;
		
		double vX = pViewerLocation.getX();
		double vY = pViewerLocation.getY();
		double vMaxX = this.aMapSize.getWidth();
		double vMaxY = this.aMapSize.getHeight();
		switch(pViewerDirrection)
		{
			case Up:
			{
				if(vX >= 0 && vX < vMaxX)
				{	
					if(vX >= 1)
					{
						vFirstLeft = this.aDungeonMap.get((int) vY).get((int) vX - 1);
					}
					else
					{
						vFirstLeft = EDungeonMapTerrain.Wall;
					}
					if(vX < vMaxX - 1)
					{
						vFirstRight = this.aDungeonMap.get((int) vY).get((int) vX + 1);
					}					
					else
					{
						vFirstRight = EDungeonMapTerrain.Wall;
					}
					if(vY >= 1)
					{
						vSecondCenter = this.aDungeonMap.get((int) (vY - 1)).get((int) vX);
						if(vX >= 1)
						{
							vSecondLeft = this.aDungeonMap.get((int) vY - 1).get((int) vX - 1);
						}
						else
						{
							vSecondLeft = EDungeonMapTerrain.Wall;
						}
						if(vX < vMaxX - 1)
						{
							vSecondRight = this.aDungeonMap.get((int) vY - 1).get((int) vX + 1);
						}
						else
						{
							vSecondRight = EDungeonMapTerrain.Wall;
						}
						if(vY >= 2)
						{
							vThirdCenter = this.aDungeonMap.get((int) (vY - 2)).get((int) vX);
							if(vX >= 1)
							{
								vThirdLeft = this.aDungeonMap.get((int) vY - 2).get((int) vX - 1);
							}
							else
							{
								vThirdLeft = EDungeonMapTerrain.Wall;
							}
							if(vX < vMaxX - 1)
							{
								vThirdRight = this.aDungeonMap.get((int) vY - 2).get((int) vX + 1);
							}
							else
							{
								vThirdRight = EDungeonMapTerrain.Wall;
							}
							if(vY >= 3)
							{
								vFourthCenter = this.aDungeonMap.get((int) (vY - 3)).get((int) vX);
								if(vX >= 1)
								{
									vFourthLeft1 = this.aDungeonMap.get((int) vY - 3).get((int) vX - 1);
								}
								else
								{
									vFourthLeft1 = EDungeonMapTerrain.Wall;
								}
								if(vX >= 2)
								{
									vFourthLeft2 = this.aDungeonMap.get((int) vY - 3).get((int) vX - 2);
								}
								else
								{
									vFourthLeft2 = EDungeonMapTerrain.Wall;
								}
								if(vX < vMaxX - 1)
								{
									vFourthRight1 = this.aDungeonMap.get((int) vY - 3).get((int) vX + 1);
								}
								else
								{
									vFourthRight1 = EDungeonMapTerrain.Wall;
								}
								if(vX < vMaxX - 2)
								{
									vFourthRight2 = this.aDungeonMap.get((int) vY - 3).get((int) vX + 2);
								}
								else
								{
									vFourthRight2 = EDungeonMapTerrain.Wall;
								}
							}
							else
							{
								vFourthCenter = EDungeonMapTerrain.Wall;
							}
							if(vY >= 4)
							{
								vFifthCenter = this.aDungeonMap.get((int) (vY - 4)).get((int) vX);
								if(vX >= 1)
								{
									vFifthLeft1 = this.aDungeonMap.get((int) vY - 4).get((int) vX - 1);
								}
								else
								{
									vFifthLeft1 = EDungeonMapTerrain.Wall;
								}
								if(vX >= 2)
								{
									vFifthLeft2 = this.aDungeonMap.get((int) vY - 4).get((int) vX - 2);
								}
								else
								{
									vFifthLeft2 = EDungeonMapTerrain.Wall;
								}
								if(vX < vMaxX - 1)
								{
									vFifthRight1 = this.aDungeonMap.get((int) vY - 4).get((int) vX + 1);
								}
								else
								{
									vFifthRight1 = EDungeonMapTerrain.Wall;
								}
								if(vX < vMaxX - 2)
								{
									vFifthRight2 = this.aDungeonMap.get((int) vY - 4).get((int) vX + 2);
								}
								else
								{
									vFifthRight2 = EDungeonMapTerrain.Wall;
								}
							}
						}
					}
				}
			}break;
			case Down:
			{
				if(vX >= 0 && vX < vMaxX)
				{	
					if(vX < vMaxX - 1)
					{
						vFirstLeft = this.aDungeonMap.get((int) vY).get((int) vX + 1);
					}
					else
					{
						vFirstLeft = EDungeonMapTerrain.Wall;
					}
					if(vX > 1)
					{
						vFirstRight = this.aDungeonMap.get((int) vY).get((int) vX - 1);
					}					
					else
					{
						vFirstRight = EDungeonMapTerrain.Wall;
					}
					if(vY < vMaxY - 1)
					{
						vSecondCenter = this.aDungeonMap.get((int) (vY + 1)).get((int) vX);
						if(vX < vMaxX - 1)
						{
							vSecondLeft = this.aDungeonMap.get((int) vY + 1).get((int) vX + 1);
						}
						else
						{
							vSecondLeft = EDungeonMapTerrain.Wall;
						}
						if(vX > 1)
						{
							vSecondRight = this.aDungeonMap.get((int) vY + 1).get((int) vX - 1);
						}
						else
						{
							vSecondRight = EDungeonMapTerrain.Wall;
						}
						if(vY < vMaxY - 2)
						{
							vThirdCenter = this.aDungeonMap.get((int) (vY + 2)).get((int) vX);
							if(vX < vMaxX - 1)
							{
								vThirdLeft = this.aDungeonMap.get((int) vY + 2).get((int) vX + 1);
							}
							else
							{
								vThirdLeft = EDungeonMapTerrain.Wall;
							}
							if(vX > 1)
							{
								vThirdRight = this.aDungeonMap.get((int) vY + 2).get((int) vX - 1);
							}
							else
							{
								vThirdRight = EDungeonMapTerrain.Wall;
							}
							if(vY < vMaxY - 3)
							{
								vFourthCenter = this.aDungeonMap.get((int) (vY + 3)).get((int) vX);
								if(vX < vMaxX - 1)
								{
									vFourthLeft1 = this.aDungeonMap.get((int) vY + 3).get((int) vX + 1);
								}
								else
								{
									vFourthLeft1 = EDungeonMapTerrain.Wall;
								}
								if(vX < vMaxX - 2)
								{
									vFourthLeft2 = this.aDungeonMap.get((int) vY + 3).get((int) vX + 2);
								}
								else
								{
									vFourthLeft2 = EDungeonMapTerrain.Wall;
								}
								if(vX > 1)
								{
									vFourthRight1 = this.aDungeonMap.get((int) vY + 3).get((int) vX - 1);
								}
								else
								{
									vFourthRight1 = EDungeonMapTerrain.Wall;
								}
								if(vX > 2)
								{
									vFourthRight2 = this.aDungeonMap.get((int) vY + 3).get((int) vX - 2);
								}
								else
								{
									vFourthRight2 = EDungeonMapTerrain.Wall;
								}
							}
							else
							{
								vFourthCenter = EDungeonMapTerrain.Wall;
							}
							if(vY < vMaxY - 4)
							{
								vFifthCenter = this.aDungeonMap.get((int) (vY + 4)).get((int) vX);
								if(vX < vMaxX - 1)
								{
									vFifthLeft1 = this.aDungeonMap.get((int) vY + 4).get((int) vX + 1);
								}
								else
								{
									vFifthLeft1 = EDungeonMapTerrain.Wall;
								}
								if(vX < vMaxX - 2)
								{
									vFifthLeft2 = this.aDungeonMap.get((int) vY + 4).get((int) vX + 2);
								}
								else
								{
									vFifthLeft2 = EDungeonMapTerrain.Wall;
								}
								if(vX > 1)
								{
									vFifthRight1 = this.aDungeonMap.get((int) vY + 4).get((int) vX - 1);
								}
								else
								{
									vFifthRight1 = EDungeonMapTerrain.Wall;
								}
								if(vX > 2)
								{
									vFifthRight2 = this.aDungeonMap.get((int) vY + 4).get((int) vX - 2);
								}
								else
								{
									vFifthRight2 = EDungeonMapTerrain.Wall;
								}
							}
							else
							{
								vFifthCenter = EDungeonMapTerrain.Wall;
							}
						}
					}
				}
			}break;
			case Left:
			{
				if(vY >= 0 && vY < vMaxY)
				{
					if(vY < vMaxY - 1)
					{
						vFirstLeft = this.aDungeonMap.get((int) vY + 1).get((int) vX);
					}
					if(vY >= 1)
					{
						vFirstRight = this.aDungeonMap.get((int) vY - 1).get((int) vX);
					}
					if(vX >= 1)
					{
						vSecondCenter = this.aDungeonMap.get((int) vY).get((int) vX - 1);
						if(vY < vMaxY - 1)
						{
							vSecondLeft = this.aDungeonMap.get((int) vY + 1).get((int) vX - 1);
						}
						if(vY >= 1)
						{
							vSecondRight = this.aDungeonMap.get((int) vY - 1).get((int) vX - 1);
						}
						if(vX >= 2)
						{
							vThirdCenter = this.aDungeonMap.get((int) vY).get((int) vX - 2);
							if(vY < vMaxY - 1)
							{
								vThirdLeft = this.aDungeonMap.get((int) vY + 1).get((int) vX - 2);
							}
							if(vY >= 1)
							{
								vThirdRight = this.aDungeonMap.get((int) vY - 1).get((int) vX - 2);
							}
							if(vX >= 3)
							{
								vFourthCenter = this.aDungeonMap.get((int) (vY)).get((int) vX - 3);
								if(vY < vMaxY - 1)
								{
									vFourthLeft1 = this.aDungeonMap.get((int) vY + 1).get((int) vX - 3);
								}
								else
								{
									vFourthLeft1 = EDungeonMapTerrain.Wall;
								}
								if(vY < vMaxY - 2)
								{
									vFourthLeft2 = this.aDungeonMap.get((int) vY + 2).get((int) vX - 3);
								}
								else
								{
									vFourthLeft2 = EDungeonMapTerrain.Wall;
								}
								if(vY >= 1)
								{
									vFourthRight1 = this.aDungeonMap.get((int) vY - 1).get((int) vX - 3);
								}
								else
								{
									vFourthRight1 = EDungeonMapTerrain.Wall;
								}
								if(vY >= 2)
								{
									vFourthRight2 = this.aDungeonMap.get((int) vY - 2).get((int) vX - 3);
								}
								else
								{
									vFourthRight2 = EDungeonMapTerrain.Wall;
								}
								if(vX >= 4)
								{
									vFifthCenter = this.aDungeonMap.get((int) vY).get((int) vX - 4);
									if(vY >= 1)
									{
										vFifthLeft1 = this.aDungeonMap.get((int) vY - 1).get((int) vX - 4);
									}
									if(vY >= 2)
									{
										vFifthLeft2 = this.aDungeonMap.get((int) vY - 2).get((int) vX - 4);
									}
									if(vY < vMaxY - 1)
									{
										vFifthRight1 = this.aDungeonMap.get((int) vY + 1).get((int) vX - 4);
									}
									if(vY < vMaxY - 2)
									{
										vFifthRight2 = this.aDungeonMap.get((int) vY + 2).get((int) vX - 4);
									}
								}
							}
						}
					}
				}
			}break;
			case Right:
			{
				if(vX >= 0 && vX < vMaxX && vY >= 0 && vY < vMaxY)
				{
					if(vY >= 1)
					{
						vFirstLeft = this.aDungeonMap.get((int) vY - 1).get((int) vX);
					}
					if(vY < vMaxY - 1)
					{
						vFirstRight = this.aDungeonMap.get((int) vY + 1).get((int) vX);
					}
					if(vX < vMaxX - 1)
					{
						vSecondCenter = this.aDungeonMap.get((int) vY).get((int) vX + 1);
						if(vY >= 1)
						{
							vSecondLeft = this.aDungeonMap.get((int) vY - 1).get((int) vX + 1);
						}
						if(vY < vMaxY - 1)
						{
							vSecondRight = this.aDungeonMap.get((int) vY + 1).get((int) vX + 1);
						}
						if(vX < vMaxX - 2)
						{
							vThirdCenter = this.aDungeonMap.get((int) vY).get((int) vX + 2);
							if(vY < vMaxY - 1)
							{
								vThirdLeft = this.aDungeonMap.get((int) vY - 1).get((int) vX + 2);
							}
							if(vY >= 1)
							{
								vThirdRight = this.aDungeonMap.get((int) vY + 1).get((int) vX + 2);
							}
							if(vX < vMaxX - 3)
							{
								vFourthCenter = this.aDungeonMap.get((int) (vY)).get((int) vX + 3);
								if(vY >= 1)
								{
									vFourthLeft1 = this.aDungeonMap.get((int) vY - 1).get((int) vX + 3);
								}
								else
								{
									vFourthLeft1 = EDungeonMapTerrain.Wall;
								}
								if(vY >= 2)
								{
									vFourthLeft2 = this.aDungeonMap.get((int) vY - 2).get((int) vX + 3);
								}
								else
								{
									vFourthLeft2 = EDungeonMapTerrain.Wall;
								}
								if(vY < vMaxY - 1)
								{
									vFourthRight1 = this.aDungeonMap.get((int) vY + 1).get((int) vX + 3);
								}
								else
								{
									vFourthRight1 = EDungeonMapTerrain.Wall;
								}
								if(vY < vMaxY - 2)
								{
									vFourthRight2 = this.aDungeonMap.get((int) vY + 2).get((int) vX + 3);
								}
								else
								{
									vFourthRight2 = EDungeonMapTerrain.Wall;
								}
								if(vX < vMaxX - 4)
								{
									vFifthCenter = this.aDungeonMap.get((int) vY).get((int) vX + 4);
									if(vY >= 1)
									{
										vFifthLeft1 = this.aDungeonMap.get((int) vY - 1).get((int) vX + 4);
									}
									else
									{
										vFifthLeft1 = EDungeonMapTerrain.Wall;
									}
									if(vY >= 2)
									{
										vFifthLeft2 = this.aDungeonMap.get((int) vY - 2).get((int) vX + 4);
									}
									else
									{
										vFifthLeft2 = EDungeonMapTerrain.Wall;
									}
									if(vY < vMaxY - 1)
									{
										vFifthRight1 = this.aDungeonMap.get((int) vY + 1).get((int) vX + 4);
									}
									else
									{
										vFifthRight1 = EDungeonMapTerrain.Wall;
									}
									if(vY < vMaxY - 2)
									{
										vFifthRight2 = this.aDungeonMap.get((int) vY + 2).get((int) vX + 4);
									}
									else
									{
										vFifthRight2 = EDungeonMapTerrain.Wall;
									}
								}
							}
						}
					}
				}
			}break;
			case None:
			default:
			{
				
			}break;
		}
				
		pGraphicsContext.setEffect(vFirstPlanCenterRoof);
		pGraphicsContext.drawImage(this.aRoof, 0, 0);
		pGraphicsContext.setEffect(vFirstPlanCenterGround);
		pGraphicsContext.drawImage(this.aGround, 0, 0);

		if(vFifthRight2 != null)
		{
			switch(vFifthRight2)
			{
				case Wall:
				{
					pGraphicsContext.setEffect(vFifthPlanRight2FaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				case Path:
				default:
				{					
				}break;
				
			}
		}		
		if(vFifthRight1 != null)
		{
			switch(vFifthRight1)
			{
				case Wall:
				{
					pGraphicsContext.setEffect(vFifthPlanRight1FaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				case Path:
				default:
				{					
				}break;
				
			}
		}
		if(vFifthLeft2 != null)
		{
			switch(vFifthLeft2)
			{
				case Wall:
				{
					pGraphicsContext.setEffect(vFifthPlanLeft2FaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				case Path:
				default:
				{					
				}break;
				
			}
		}
		if(vFifthLeft1 != null)
		{
			switch(vFifthLeft1)
			{
				case Wall:
				{
					pGraphicsContext.setEffect(vFifthPlanLeft1FaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				case Path:
				default:
				{					
				}break;
				
			}
		}
		if(vFifthCenter != null)
		{
			switch(vFifthCenter)
			{
				case Wall:
				{
					pGraphicsContext.setEffect(vFifthPlanFaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				case Path:
				default:
				{					
				}break;
				
			}
		}
		if(vFourthRight2 != null)
		{
			pGraphicsContext.setEffect(vFourthPlanRight2Roof);
			pGraphicsContext.drawImage(this.aRoof, 0, 0);
			pGraphicsContext.setEffect(vFourthPlanRight2Ground);
			pGraphicsContext.drawImage(this.aGround, 0, 0);
			switch(vFourthRight2)
			{
				case Path:
				{
				}break;
				case Wall:
				{
					pGraphicsContext.setEffect(vFourthPlanRightFaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
					pGraphicsContext.setEffect(vFourthPlanRight2Wall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				default:
				{
				}break;	
			}
		}
		
		if(vFourthRight1 != null)
		{
			pGraphicsContext.setEffect(vFourthPlanRight1Roof);
			pGraphicsContext.drawImage(this.aRoof, 0, 0);
			pGraphicsContext.setEffect(vFourthPlanRight1Ground);
			pGraphicsContext.drawImage(this.aGround, 0, 0);
			switch(vFourthRight1)
			{
				case Wall:
				{
					pGraphicsContext.setEffect(vFourthPlanRightFaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
					pGraphicsContext.setEffect(vFourthPlanRight1Wall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				case Path:
				default:
				{
				}break;	
			}
		}
		
		if(vFourthLeft2 != null)
		{
			pGraphicsContext.setEffect(vFourthPlanLeft2Roof);
			pGraphicsContext.drawImage(this.aRoof, 0, 0);
			pGraphicsContext.setEffect(vFourthPlanLeft2Ground);
			pGraphicsContext.drawImage(this.aGround, 0, 0);
			switch(vFourthLeft2)
			{
				case Wall:
				{
					pGraphicsContext.setEffect(vFourthPlanLeftFaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
					pGraphicsContext.setEffect(vFourthPlanLeft2Wall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				case Path:
				default:
				{
				}break;	
			}
		}
		if(vFourthLeft1 != null)
		{
			pGraphicsContext.setEffect(vFourthPlanLeft1Roof);
			pGraphicsContext.drawImage(this.aRoof, 0, 0);
			pGraphicsContext.setEffect(vFourthPlanLeft1Ground);
			pGraphicsContext.drawImage(this.aGround, 0, 0);
			switch(vFourthLeft1)
			{
				case Wall:
				{
					pGraphicsContext.setEffect(vFourthPlanLeftFaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
					pGraphicsContext.setEffect(vFourthPlanLeft1Wall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				case Path:
				default:
				{
				}break;	
			}
		}
		if(vFourthCenter != null)
		{
			switch(vFourthCenter)
			{
				case Path:
				{
					pGraphicsContext.setEffect(vFourthPlanCenterRoof);
					pGraphicsContext.drawImage(this.aRoof, 0, 0);
					pGraphicsContext.setEffect(vFourthPlanCenterGround);
					pGraphicsContext.drawImage(this.aGround, 0, 0);
				}break;
				case Wall:
				{
					pGraphicsContext.setEffect(vFourthPlanFaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				default:
				{
				}break;	
			}
		}
		if(vThirdRight != null)
		{
			switch(vThirdRight)
			{
				case Path:
				{
					pGraphicsContext.setEffect(vThirdPlanRightRoof);
					pGraphicsContext.drawImage(this.aRoof, 0, 0);
					pGraphicsContext.setEffect(vThirdPlanRightGround);
					pGraphicsContext.drawImage(this.aGround, 0, 0);
				}break;
				case Wall:
				{
					pGraphicsContext.setEffect(vThirdPlanRightFaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
					pGraphicsContext.setEffect(vThirdPlanRightWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				default:
				{
				}break;				
			}
		}
		if(vThirdLeft != null)
		{
			switch(vThirdLeft)
			{
				case Path:
				{
					pGraphicsContext.setEffect(vThirdPlanLeftRoof);
					pGraphicsContext.drawImage(this.aRoof, 0, 0);
					pGraphicsContext.setEffect(vThirdPlanLeftGround);
					pGraphicsContext.drawImage(this.aGround, 0, 0);
				}break;
				case Wall:
				{
					pGraphicsContext.setEffect(vThirdPlanLeftFaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
					pGraphicsContext.setEffect(vThirdPlanLeftWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				default:
				{
				}break;				
			}
		}
		if(vThirdCenter != null)
		{
			switch(vThirdCenter)
			{
				case Path:
				{
					pGraphicsContext.setEffect(vThirdPlanCenterRoof);
					pGraphicsContext.drawImage(this.aRoof, 0, 0);		
					pGraphicsContext.setEffect(vThirdPlanCenterGround);
					pGraphicsContext.drawImage(this.aGround, 0, 0);
				}break;
				case Wall:
				{
					pGraphicsContext.setEffect(vThirdPlanFaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				default:
				{
				}break;
			}
		}
		if(vSecondRight != null)
		{
			switch(vSecondRight)
			{
				case Path:
				{
					pGraphicsContext.setEffect(vSecondPlanRightRoof);
					pGraphicsContext.drawImage(this.aRoof, 0, 0);
					pGraphicsContext.setEffect(vSecondPlanRightGround);
					pGraphicsContext.drawImage(this.aGround, 0, 0);
				}break;
				case Wall:
				{
					pGraphicsContext.setEffect(vSecondPlanRightWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
					pGraphicsContext.setEffect(vSecondPlanRightFaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				default:
				{
					
				}break;				
			}
		}
		if(vSecondLeft != null) 
		{
			switch(vSecondLeft)
			{
				case Path:
				{
					pGraphicsContext.setEffect(vSecondPlanLeftRoof);
					pGraphicsContext.drawImage(this.aRoof, 0, 0);
					pGraphicsContext.setEffect(vSecondPlanLeftGround);
					pGraphicsContext.drawImage(this.aGround, 0, 0);
				}break;
				case Wall:
				{
					pGraphicsContext.setEffect(vSecondPlanLeftWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
					pGraphicsContext.setEffect(vSecondPlanLeftFaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;		
				default:
				{					
				}break;	
			}
		}
		if(vSecondCenter != null) 
		{
			switch(vSecondCenter)
			{
				case Path:
				{
					pGraphicsContext.setEffect(vSecondPlanCenterRoof);
					pGraphicsContext.drawImage(this.aRoof, 0, 0);
					pGraphicsContext.setEffect(vSecondPlanCenterGround);
					pGraphicsContext.drawImage(this.aGround, 0, 0);
				}break;
				case Wall:
				{
					pGraphicsContext.setEffect(vSecondPlanFaceWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				default:
				{					
				}break;				
			}
		}
		if(vFirstLeft != null)
		{
			switch(vFirstLeft)
			{
				case Path:
				{
					pGraphicsContext.setEffect(vFirstPlanLeftRoof);
					pGraphicsContext.drawImage(this.aRoof, 0, 0);
					pGraphicsContext.setEffect(vFirstPlanLeftGround);
					pGraphicsContext.drawImage(this.aGround, 0, 0);
				}break;
				case Wall:
				{
					pGraphicsContext.setEffect(vFirstPlanLeftWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);	
				}break;
				default:
				{					
				}break;
				
			}
		}
		if(vFirstRight != null)
		{
			switch(vFirstRight)
			{
				case Path:
				{
					pGraphicsContext.setEffect(vFirstPlanRightRoof);
					pGraphicsContext.drawImage(this.aRoof, 0, 0);
					pGraphicsContext.setEffect(vFirstPlanRightGround);
					pGraphicsContext.drawImage(this.aGround, 0, 0);
				}break;
				case Wall:
				{
					pGraphicsContext.setEffect(vFirstPlanRightWall);
					pGraphicsContext.drawImage(this.aWall, 0, 0);
				}break;
				default:
				{
				}break;				
			}
		}
		
		pGraphicsContext.setEffect(null);

		//masquage des contours du dessin de la vue...
		pGraphicsContext.setFill(Color.BLACK);
		//*
		pGraphicsContext.fillRect(0, 0, vCanvas.getWidth(), vCanvas.getHeight() - (vCanvas.getHeight() - pView.getY()));
		pGraphicsContext.fillRect(0, 0, vCanvas.getWidth() -(vCanvas.getWidth() - pView.getX()), vCanvas.getHeight());		
		pGraphicsContext.fillRect(pView.getX() + pView.getWidth(), 0, vCanvas.getWidth() - (pView.getX() + pView.getWidth()), vCanvas.getHeight());		
		pGraphicsContext.fillRect(0, pView.getY() + pView.getHeight(), vCanvas.getWidth(), vCanvas.getHeight() - (pView.getY() + pView.getHeight()));
		//*/
	}
	
	public void mDrawMiniMap(GraphicsContext pGraphicsContext, Rectangle pView, Point2D pViewerLocation, EDungeonDirrection pViewerDirrection)
	{
		double vCellWidth = pView.getWidth() / this.aMapSize.getWidth();
		double vCellHeight = pView.getHeight() / this.aMapSize.getHeight();
		for(int vYIndex = 0; vYIndex < this.aMapSize.getHeight(); vYIndex++)
		{
			List<EDungeonMapTerrain> vLine = this.aDungeonMap.get(vYIndex);
			for(int vXIndex = 0; vXIndex < this.aMapSize.getWidth(); vXIndex++)
			{
				switch(vLine.get(vXIndex))
				{
					case Wall :
					{
						pGraphicsContext.setFill(Color.GRAY);
					}break;
					case Path :
					{
						pGraphicsContext.setFill(Color.WHITE);
					}break;
				}
				pGraphicsContext.fillRect(pView.getX() + vXIndex * vCellWidth, pView.getY() + vYIndex * vCellHeight, vCellWidth, vCellHeight);
			}
		}
		
		pGraphicsContext.setFill(Color.RED);
		pGraphicsContext.setStroke(Color.RED);
		pGraphicsContext.fillOval(pView.getX() + pViewerLocation.getX() * vCellWidth + vCellWidth / 4.0, pView.getY() + pViewerLocation.getY() * vCellHeight + vCellHeight / 4.0, vCellWidth / 2.0, vCellHeight / 2.0);
		switch(pViewerDirrection)
		{
			case Up :
			{
				pGraphicsContext.strokeLine(pView.getX() + pViewerLocation.getX() * vCellWidth + vCellWidth / 2.0, pView.getY() + pViewerLocation.getY() * vCellHeight + vCellHeight / 2.0, pView.getX() + pViewerLocation.getX() * vCellWidth + vCellWidth / 2.0, pView.getY() + pViewerLocation.getY() * vCellHeight);
			}break;
			case Right :
			{
				pGraphicsContext.strokeLine(pView.getX() + pViewerLocation.getX() * vCellWidth + vCellWidth / 2.0, pView.getY() + pViewerLocation.getY() * vCellHeight + vCellHeight / 2.0, pView.getX() + pViewerLocation.getX() * vCellWidth + vCellWidth, pView.getY() + pViewerLocation.getY() * vCellHeight + vCellHeight / 2.0);
			}break;
			case Down :
			{
				pGraphicsContext.strokeLine(pView.getX() + pViewerLocation.getX() * vCellWidth + vCellWidth / 2.0, pView.getY() + pViewerLocation.getY() * vCellHeight + vCellHeight / 2.0, pView.getX() + pViewerLocation.getX() * vCellWidth + vCellWidth / 2, pView.getY() + pViewerLocation.getY() * vCellHeight + vCellHeight);		
			}break;
			case Left :
			{
				pGraphicsContext.strokeLine(pView.getX() + pViewerLocation.getX() * vCellWidth + vCellWidth / 2.0, pView.getY() + pViewerLocation.getY() * vCellHeight + vCellHeight / 2.0, pView.getX() + pViewerLocation.getX() * vCellWidth, pView.getY() + pViewerLocation.getY() * vCellHeight + vCellHeight / 2.0);
			}break;
			case None:
			default:
			{
			}break;
		}
		
	}
}
