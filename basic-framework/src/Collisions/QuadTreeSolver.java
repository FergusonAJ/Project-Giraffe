package Collisions;

import static Collisions.QTNode.numChildren;
import framework.PhysicsBody;
import java.util.ArrayList;

/**
 * Basic implementation of circle solver
 * @author Austin, Andrew repurposed it for this project
 */
public class QuadTreeSolver
{
    QuadTree mTree; //The actual tree that does the solving
    int mWidth, mHeight, mBucketSize; //Dimensions and bucket size of the tree 
    
    
    /**
     * Set all the instance variables and drop in all the circles
     * @param list List of circles to solve
     * @param width Width of the root node
     * @param height Height of the root node
     * @param bucketSize Size of the bucket for each node
     */
    public QuadTreeSolver(LinkedList list, int width, int height, int bucketSize)
    {
        mTree = new QuadTree(width, height, bucketSize); //Create the tree
        mWidth = width;
        mHeight = height;
        mBucketSize = bucketSize;
        //Add each circle

        LinkedListIterator I = list.iterator();
       
        while(I.hasNext())
        {
            mTree.add(I.next());
        }
    }
    
    /**
     * Change the list of the tree, by dropping the list and re-adding
     * @param list 
     */
    public void updateList(LinkedList list)
    {
        numChildren = 0;
        mTree = new QuadTree(mWidth, mHeight, mBucketSize);
        LinkedListIterator I = list.iterator();
        //System.out.println("size of list:" +list.mSize);
        int numIt = 0;
        //System.out.println(list.toString());
        while(I.hasNext())
        {
          //  System.out.println(I.next());
            mTree.add(I.next());
            numIt++;
        }
        //System.out.println("numIt"+numIt);
        //System.out.println("numChildren"+numChildren);
    }
    
    /**
     * Calculate the overlaps between the circles
     * @param set HashSet to add the overlapping circles to
     * 
     */
    public void findOverlap(HashSet set) 
    {
        //long startTime = System.nanoTime();
        mTree.findOverlap(set); //Recursive function that we call in the root node
        
        //long now = System.nanoTime();
        //return (double)((now - startTime) / 1_000_000_000.0);
    }
    
}
