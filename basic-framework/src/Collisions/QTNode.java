package Collisions;

import framework.PhysicsBody;
import java.util.ArrayList;


/**
 * Node for a Quad Tree designed to 
 * @author Austin, redesigned by Andrew
 */
public class QTNode <E>
{
    Rectangle mBounds; //X,Y,W,H for the bounds of the node
    QTNode[] mChildNodes = null; //Array of the children of this node, either 0 or 4
    E[] mItems = null; //Array of items in this node. The bucket.
    int mCurIndex = 0; //Current index of the array to add to
    int mBucketSize; //Max size of the bucket before expansion
    static int numChildren = 0;
    
    /**
     * Initialize the variables
     * @param bounds Position and size of the node
     * @param bucketSize Max size of the bucket before expanding
     */
    public QTNode(Rectangle bounds, int bucketSize)
    {
        mBounds = bounds;
        mItems = (E[]) new Object[bucketSize];
        mBucketSize = bucketSize;
    }
    

    
    /**
     * Add the passed circle to either this node or to its children
     * @param value Circle to add
     */
    public void add(E value)
    {
        System.out.println("ADDED: " + value);
        //Add it to ourself if we have no chidren
        if(mChildNodes == null)
        {
            if(contains((E) value))
            {
                mItems[mCurIndex] = value;
                mCurIndex++;
//                System.out.println("CurIndex:"+mCurIndex);
//                System.out.println("bucketSize:"+mBucketSize);
                
                if(mCurIndex >= mBucketSize)
                {
                    split();
                    numChildren+=4;
                    System.out.println("split");
                }
            }
        }
        else
        {
            System.out.println("else added: " + value);
           addToChildren(value);
        }
        for(int i = 0; i < mItems.length; i++)
        {
            System.out.println("mItems:"+ mItems[i]);
        }
        
        
    }
    
    /**
     * Helper function to shorten the code to pass a value to our children
     * @param value Value to add
     */
    private void addToChildren(E value)
    {
        for(int i = 0; i < 4; i++)
        {
            mChildNodes[i].add(value);
        }
    
    }
    
    /**
     * Calculate the number of overlaps in the circles
     * @param set HashSet of overlapping circles
     */
    public void findOverlap(HashSet<PhysicsBody> set) 
    {
        if(mChildNodes == null)
        {
            for(int i = 0; i < mCurIndex; i++)
            {
                PhysicsBody c1 = (PhysicsBody) mItems[i];
                if(i < mCurIndex)
                {
                    for(int j = i + 1; j < mCurIndex; j++)
                    {
                        PhysicsBody c2 = (PhysicsBody) mItems[j];
                        float dX = c1.mPos.x - c2.mPos.x;
                        float dZ = c1.mPos.z - c2.mPos.z;
                        //Use the distance formula to detect overlaps
                        if(c1.ot != c2.ot)
                        {
                            if(Math.sqrt(dX*dX + dZ*dZ) < c1.mRad + c2.mRad)
                            {
                                
                                set.add(c1);
                                set.add(c2);

                            }
                        }
                    }
                }
            }
        }
        //If we have children, call this function in them instead
        else
        {
            for(int i = 0; i < 4; i++)
            {
                mChildNodes[i].findOverlap(set);
            }
        }
    }
    
    /**
     * Check to see if the circle is in the node at all
     * @param value Circle to check
     * @return Boolean of if the circle is in the node
     */
    public boolean contains(E value)
    {
        PhysicsBody c = (PhysicsBody)value;
        float top = c.mPos.x - c.mRad;
        float bottom = c.mPos.x + c.mRad;
        float left = c.mPos.z - c.mRad;
        float right = c.mPos.z + c.mRad;
        //If the x and y values are in the node's range at some point
        if(right >= mBounds.getX() && left <= mBounds.getX() + mBounds.getWidth())
        {
            if(bottom >= mBounds.getZ() && top <= mBounds.getZ() + mBounds.getHeight())
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * I don't actually use this, but checks to see if the circle is completely inside the node
     * @param value Circle to check
     * @return Boolean value of if the circle is enveloped in the node
     */
    public boolean envelops(PhysicsBody value)
    {
        PhysicsBody c = value;
        if(c.mPos.x + c.mRad > mBounds.getX() && c.mPos.x - c.mRad < mBounds.getX() + mBounds.getWidth())
        {
            if(c.mPos.z + c.mRad > mBounds.getZ() && c.mPos.z - c.mRad < mBounds.getZ() + mBounds.getHeight())
            {
                return true;
            }   
        }
        return false;
    }
    
    /** 
     * Splits the node into four child nodes
     */
    public void split()                  
    {       
        mChildNodes = new QTNode[4];
        //Top-Left
        mChildNodes[0] = new QTNode(new Rectangle(mBounds.getX(), mBounds.getZ(), mBounds.getWidth() / 2, mBounds.getHeight() / 2), mBucketSize);
        //Top-Right
        mChildNodes[1] = new QTNode(new Rectangle(mBounds.getX() + mBounds.getWidth() / 2, mBounds.getZ(), mBounds.getWidth() / 2, mBounds.getHeight() / 2), mBucketSize);
        //Bottom-Left
        mChildNodes[2] = new QTNode(new Rectangle(mBounds.getX(), mBounds.getZ() + mBounds.getHeight() / 2, mBounds.getWidth() / 2, mBounds.getHeight() / 2), mBucketSize);
        //Bottom-Right
        mChildNodes[3] = new QTNode(new Rectangle(mBounds.getX() + mBounds.getWidth() / 2, mBounds.getZ() + mBounds.getHeight() / 2, mBounds.getWidth() / 2, mBounds.getHeight() / 2), mBucketSize);
        for(int i = 0; i < mCurIndex; i++)
        {
            addToChildren(mItems[i]);
        }
        mCurIndex = 0;
    }
}
