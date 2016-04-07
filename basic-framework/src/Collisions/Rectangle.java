package Collisions;

/**
 *
 * @author Andrew Polanco
 */
public class Rectangle
{
    int mX,mZ,mWidth,mHeight;
    public Rectangle(int x, int z, int width, int height)
    {
        mX = x;
        mZ = z;
        mWidth = width;
        mHeight = height;
    }

    public int getX() {
        return mX;
    }

    public int getZ() {
        return mZ;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }
}
