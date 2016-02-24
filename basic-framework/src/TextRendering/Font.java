package TextRendering;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * Stores all the data necessary to create drawable characters.
 * @author Austin Ferguson
 */
public class Font 
{
    String mFilename;
    HashMap<Integer, CharInfo> mCharacters;
    float mLineHeight, mWidth, mHeight, mBase;
    
    /**
     * Creating a font creates a FontFileParser and gets the parsed data
     * @param filename Name of the font file (Should be a .txt)
     * @throws IOException Thrown if the specified file cannot be found
     */
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
    
    /**
     * Returns the info of the specified character
     * @param id ASCII value of the desired character
     * @return A CharInfo object with all the details about the specified character
     */
    public CharInfo getCharInfo(int id)
    {
        return mCharacters.get(id);
    }
    
    /**
     * Converts the char into an int and calls the other getMeshForChar
     * @param c The desired character
     * @return A bytebuffer for the character's vao
     */
    public ByteBuffer getMeshForChar(char c)
    {
        return this.getMeshForChar((int)c);
    }
    
    /**
     * Creates a unit square for the specified character
     * @param c ASCII value of the desired character
     * @return A bytebuffer for the character's vao
     */
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
