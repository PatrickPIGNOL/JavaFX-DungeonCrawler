package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dungeon 
{
	private Random aRandom;
	private List<List<EDungeonMapTerrain>> aDungeonMap;
	private Dimension aMapSize;
		
	public Dungeon(Dimension pMapSize)
	{
		this.aRandom = new Random(1);//System.nanoTime());
		Dimension vDimension = new Dimension();
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
	
	public void mDraw(GraphicsContext pGraphicsContext)
	{
		int vCellSize = 5;
		for(int vYIndex = 0; vYIndex < this.aDungeonMap.size(); vYIndex++)
		{
			List<EDungeonMapTerrain> vLine = this.aDungeonMap.get(vYIndex);
			for(int vXIndex = 0; vXIndex < vLine.size(); vXIndex++)
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
				pGraphicsContext.fillRect(vXIndex * vCellSize, vYIndex * vCellSize, vCellSize, vCellSize);
			}
		}
	}
}
