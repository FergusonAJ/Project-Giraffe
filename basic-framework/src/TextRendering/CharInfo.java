package TextRendering;

/**
 * Stores all necessary information for a character in our font
 * @author Austin Ferguson
 */
public class CharInfo 
{
    int mID, mX, mY, mWidth, mHeight, mXOffset, mYOffset, mXAdvance;
    //mID is the ASCII value of the character
    //mX and mY are the pixel coordinates of the character in the spritesheet
    //mWidth and mHeight are the pixel dimensions of the character in the spritesheet
    //mXOffset and mYOffset are the pixel offsets to use when grabbing the sprite from the sheet
    //mXAdvance is the amount to move the cursor (In pixels in the spritesheet) after placing the character
    public CharInfo()
    {
    }
    
    /**
     * Sets the ID of the character
     */
    void setID(int id)
    {
        mID = id;
    }
    
    /**
     * Sets the X coordinate of the character
     * @param x 
     */
    void setX(int x)
    {
        mX = x;
    }
    
    /**
     * Sets the Y coordinate of the character 
     */
    void setY(int y)
    {
        mY = y;
    }
    
    /**
     * Sets the width of the character
     */
    void setWidth(int width)
    {
        mWidth = width;
    }
    
    /**
     * Sets the height of the character
     */
    void setHeight(int height)
    {
        mHeight = height;
    }
    
    /**
     * Sets the X Offset of the character
     */
    void setXOffset(int xOffset)
    {
        mXOffset = xOffset;
    }
    
    /** 
     * Sets the Y Offset of the character
     */
    void setYOffset(int yOffset)
    {
        mYOffset = yOffset;
    }
    
    /**
     * Sets the X Advance of the character
     */
    void setXAdvance(int xAdvance)
    {
        mXAdvance = xAdvance;
    }
    
    /**
     * Returns a nice, formatted string with all the information of the character
     * @return 
     */
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
