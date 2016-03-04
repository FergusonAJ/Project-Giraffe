package TextRendering;

import framework.Program;
import framework.StateManager;
import java.util.ArrayList;

/**
 * Creates vaos for a string of characters, can draw them at once
 * @author Austin Ferguson
 */
public class DrawableString 
{
    ArrayList<DrawableChar> dCharList = new ArrayList();
    int pointerX, pointerY;
    
    /**
     * Generates all the vaos for the individual characters
     * @param s The desired text
     * @param startX X component of the starting position on the screen
     * @param startY Y component of the starting position on the screen
     * @param charHeight How tall the characters will be, in pixels
     * @param font  The desired font
     */
    public DrawableString(String s, int startX, int startY, int charHeight, Font font)
    {
        char[] chars = s.toCharArray();
        pointerX = startX;
        pointerY = startY + charHeight;
        for(int i = 0; i < chars.length; i++)
        {
            CharInfo info = font.getCharInfo((int)chars[i]);
            DrawableChar tempChar = new DrawableChar(chars[i], pointerX+info.mXOffset + info.mWidth/2, (int)(pointerY + info.mYOffset * (charHeight / StateManager.resolution.y)), charHeight / StateManager.resolution.y, font);
            dCharList.add(tempChar);
            pointerX += info.mXAdvance/1.25f * (charHeight / 20.0f) ;
        }
    }
    
    /**
     * Generates all the vaos for the individual characters, starting at a default position
     * @param s The desired string
     * @param font The desired font
     */
    public DrawableString(String s, Font font)
    {
        char[] chars = s.toCharArray();
        for(int i = 0; i < chars.length; i++)
        {
            dCharList.add(new DrawableChar(chars[i], 100 * (i + 1),540, 0.05f, font));
        }
    }
    
    /**
     * Render the string to the screen
     * @param prog The program to use for rendering
     */
    public void draw(Program prog)
    {
        for(int i = 0; i < dCharList.size(); i++)
        {
            dCharList.get(i).draw(prog);
        }
    }
}
