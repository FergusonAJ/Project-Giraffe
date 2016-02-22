/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextRendering;

/**
 *
 * @author ajart
 */
public class CharInfo 
{
    int mID, mX, mY, mWidth, mHeight, mXOffset, mYOffset, mXAdvance;
    public CharInfo()
    {
    }
    void setID(int id)
    {
        mID = id;
    }
    void setX(int x)
    {
        mX = x;
    }
    void setY(int y)
    {
        mY = y;
    }
    void setWidth(int width)
    {
        mWidth = width;
    }
    void setHeight(int height)
    {
        mHeight = height;
    }
    void setXOffset(int xOffset)
    {
        mXOffset = xOffset;
    }
    void setYOffset(int yOffset)
    {
        mYOffset = yOffset;
    }
    void setXAdvance(int xAdvance)
    {
        mXAdvance = xAdvance;
    }
    @Override
    public String toString()
    {
        String builder = "Char ID# : " + mID;
        builder += "\n\t X : " + mX;
        builder += "\n\t Y : " + mY;
        builder += "\n\t Width : " + mWidth;
        builder += "\n\t Height : " + mHeight;
        builder += "\n\t X Offset : " + mXOffset;
        builder += "\n\t Y Offset : " + mYOffset;
        builder += "\n\t X Advance : " + mXAdvance;
        return builder;
    }
}
