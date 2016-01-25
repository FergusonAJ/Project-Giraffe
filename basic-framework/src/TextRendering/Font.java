/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextRendering;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 *
 * @author ajart
 */
public class Font 
{
    String mFilename;
    HashMap<Integer, CharInfo> mCharacters;
    float mLineHeight, mWidth, mHeight, mBase;
    
    public Font(String filename) throws IOException
    {
        mFilename = filename;
        FontFileParser parser = new FontFileParser(filename);
        mCharacters = parser.getCharacters();
        mLineHeight = parser.getLineHeight();
        mWidth = parser.getWidth();
        mHeight = parser.getHeight();
        mBase = parser.getBase();
    }
    public CharInfo getCharInfo(int id)
    {
        return mCharacters.get(id);
    }
    public ByteBuffer getMeshForChar(char c)
    {
        return this.getMeshForChar((int)c);
    }
    public ByteBuffer getMeshForChar(int charID)
    {
        CharInfo info = mCharacters.get(charID);
        ByteBuffer BB = ByteBuffer.allocate(8*4*6);
        BB.order(ByteOrder.nativeOrder());
        FloatBuffer FB = BB.asFloatBuffer();
        float widthToHeight = (float)info.mWidth / (float)(info.mHeight + 1.0f);
        float heightRatio = info.mHeight / mLineHeight;
        float[] f2 = new float[]{
            1.0f * widthToHeight,1.0f * heightRatio,-1.0f, (info.mX + info.mWidth) / mWidth, 1.0f - (info.mY / mHeight),  0.0f,0.0f,1.0f, //1           2---1    
            -1.0f* widthToHeight,1.0f * heightRatio,-1.0f, info.mX / mWidth, 1.0f - (info.mY/ mHeight),  0.0f,0.0f,1.0f,                //2           |   |
            -1.0f * widthToHeight,-1.0f * heightRatio,-1.0f, info.mX / mWidth, 1.0f - ((info.mY + info.mHeight) / mHeight),  0.0f,0.0f,1.0f, //3        3---4
            1.0f * widthToHeight,1.0f * heightRatio,-1.0f, (info.mX + info.mWidth) / mWidth, 1.0f - (info.mY / mHeight),  0.0f,0.0f,1.0f, //1          
            -1.0f * widthToHeight,-1.0f * heightRatio,-1.0f, info.mX / mWidth, 1.0f - ((info.mY + info.mHeight) / mHeight),  0.0f,0.0f,1.0f, //3       
            1.0f * widthToHeight,-1.0f * heightRatio,-1.0f, (info.mX + info.mWidth) / mWidth, 1.0f - ((info.mY + info.mHeight) / mHeight),  0.0f,0.0f,1.0f, //4
        };
        FB.put(f2);
        return BB;
    }
}
