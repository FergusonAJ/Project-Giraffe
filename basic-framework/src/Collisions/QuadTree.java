package Collisions;

import framework.PhysicsBody;
import java.util.ArrayList;

/**
 * Quad Tree data structure currently set up for circle collisions
 * @author Austin, redesigned by Andrew
 */
public class QuadTree <E>
{
    public QTNode mRoot; //Root node of the tree
    int mBucketSize; //Size of the bucket in each node
    
    /**
     * Setup and create the root node
     * @param width Width of the whole tree
     * @param height Height of the whole tree
     * @param bucketSize Size of the bucket in each node
     */
    public QuadTree(int width, int height, int bucketSize)
    {
        mBucketSize = bucketSize;
        mRoot = new QTNode(new Rectangle(0,0,width, height), bucketSize);
    }
        
    /**
     * Call the recursive add method in the root node
     * @param value Circle to add
     */
    public void add(E value)
    {
        mRoot.add(value);
    }
    
    /** 
     * Call the recursive method in the root node to find the overlaps
     * @param set HashSet of circles to add the overlapping circles to.
     */
    public void findOverlap(HashSet set) 
    {
        mRoot.findOverlap(set);
    }
}
