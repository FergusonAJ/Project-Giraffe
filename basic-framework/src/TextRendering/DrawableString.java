/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextRendering;

import framework.Program;
import java.util.ArrayList;

/**
 *
 * @author ajart
 */
public class DrawableString 
{
    ArrayList<DrawableChar> dCharList = new ArrayList();
    int pointerX, pointerY;
    public DrawableString(String s, int startX, int startY, int charHeight, Font font)
    {
        char[] chars = s.toCharArray();
        pointerX = startX;
        pointerY = startY + charHeight;
        for(int i = 0; i < chars.length; i++)
        {
            CharInfo info = font.getCharInfo((int)chars[i]);
            DrawableChar tempChar = new DrawableChar(chars[i], pointerX+info.mXOffset + info.mWidth/2, (int)(pointerY + info.mYOffset * (charHeight / 1080.0f)), charHeight / 1080.0f, font);
            dCharList.add(tempChar);
            pointerX += info.mXAdvance/1.25f * (charHeight / 20.0f) ;
        }
    }
    public DrawableString(String s, Font font)
    {
        char[] chars = s.toCharArray();
        for(int i = 0; i < chars.length; i++)
        {
            dCharList.add(new DrawableChar(chars[i], 100 * (i + 1),540, 0.05f, font));
        }
    }
    public void draw(Program prog)
    {
        for(int i = 0; i < dCharList.size(); i++)
        {
            dCharList.get(i).draw(prog);
        }
    }
}
