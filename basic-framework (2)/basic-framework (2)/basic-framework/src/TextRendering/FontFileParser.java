/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextRendering;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author ajart
 */
public class FontFileParser 
{
    HashMap<Integer, CharInfo> mCharacters = new HashMap();
    private int mLineHeight, mBase, mWidth, mHeight;
    public FontFileParser(String filename) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        Stream<String> lines = br.lines();
        Iterator I = lines.iterator();
        while(I.hasNext())
        {
            String[] parts = I.next().toString().split("\\s");
            if(parts[0].contains("char"))
            {
                CharInfo newChar = new CharInfo();
                for(int i = 0; i < parts.length; i++)
                {
                    int equalsIndex = parts[i].indexOf('=');
                    if(equalsIndex > 0)
                    {
                        String id = parts[i].substring(0, equalsIndex);
                        String value = parts[i].substring(equalsIndex + 1);
                        switch(id)
                        {
                            case "id":
                                newChar.setID(Integer.parseInt(value));
                                break;
                            case "x":
                                newChar.setX(Integer.parseInt(value));
                                break;
                            case "y":
                                newChar.setY(Integer.parseInt(value));
                                break;
                            case "width":
                                newChar.setWidth(Integer.parseInt(value));
                                break;
                            case "height":
                                newChar.setHeight(Integer.parseInt(value));
                                break;
                            case "xoffset":
                                newChar.setXOffset(Integer.parseInt(value));
                                break;
                            case "yoffset":
                                newChar.setYOffset(Integer.parseInt(value));
                                break;
                            case "xadvance":
                                newChar.setXAdvance(Integer.parseInt(value));
                                break;
                        }
                    }
                }
                mCharacters.put(newChar.mID, newChar);
            }
            else if(parts[0].contains("common"))
            {
                 for(int i = 0; i < parts.length; i++)
                {
                    int equalsIndex = parts[i].indexOf('=');
                    if(equalsIndex > 0)
                    {
                        String id = parts[i].substring(0, equalsIndex);
                        String value = parts[i].substring(equalsIndex + 1);
                        switch(id)
                        {
                            case "lineHeight":
                                mLineHeight = Integer.parseInt(value);
                                break;
                            case "base":
                                mBase = Integer.parseInt(value);
                                break;
                            case "scaleW":
                                mWidth = Integer.parseInt(value);
                                break;
                            case "scaleH":
                                mHeight = Integer.parseInt(value);
                                break;
                        }
                    }
                }
            }
        }
    }
    public HashMap<Integer, CharInfo> getCharacters()
    {
        return mCharacters;
    }
    public int getWidth()
    {
        return mWidth;
    }
    public int getHeight()
    {
        return mHeight;
    }
    public int getBase()
    {
        return mBase;
    }
    public int getLineHeight()
    {
        return mLineHeight;
    }
}
