package Collisions;

import java.util.Iterator;

//Implementation of the Iterator class that transverses a Linked List
public class LinkedListIterator implements Iterator
{
    protected LinkedList mList; //The list we are tranversing
    protected LinkedList.Node mCurrentNode; //The node we are currently on
    protected boolean mReverse = false; //Are we going in reverse order?
    protected boolean hasHadNext = false; //Boolean used for throwing 
    public int mCurrentIndex = -1;

    //Constructor that initializes a few variables, and sets the mCurrentNodes next or prev accordingly.
    public LinkedListIterator(LinkedList L, boolean reverse)
    {
        mList = L;
        mReverse = reverse;
        mCurrentNode = L.mPointer;
        if(!mReverse)
        {
            mCurrentNode.mNext = L.mBegin; //Prepare for forwards transversal
        }
        else
        {
            mCurrentNode.mPrev = L.mEnd; //Prepare for backwards transversal
        }
    }
    
    @Override
    public boolean hasNext() //Returns true if there is an element after this one
    {
        if(!mReverse) //If we are going forwards, check for mNext
        {
            if(mCurrentNode.mNext != null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else //If we are going backwards, check for mPrev
        {
            if(mCurrentNode.mPrev != null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    
    @Override
    public Object next() //Return the next object in the list
    {
        if(hasNext()) //Make sure we have somewhere to go...
        {
            if(!mReverse) //Go to next node if going forward
            {
                mCurrentNode = mCurrentNode.mNext;
            }
            else //Go to previous node if going backward
            {
                mCurrentNode = mCurrentNode.mPrev;
            }
            hasHadNext = true; //So remove works
            mCurrentIndex++;
            return mCurrentNode.mValue;
        }
        else //If we have no where to go, fail
        {
            return null;
        }     
    }
    
    //Remove the current node from the list
    @Override 
    public void remove() throws IllegalStateException
    {
        if(hasHadNext) //Is it a legal move?
        {
            //Set some pointers
            mCurrentNode.mPrev.mNext = mCurrentNode.mNext;
            mCurrentNode.mNext.mPrev = mCurrentNode.mNext;
            mList.mSize--; //Decrement the size
            //Some checks for beginning and end
            if(mList.mEnd == mCurrentNode)             
            {
                mList.mEnd = mCurrentNode.mPrev;
            }
            if(mList.mBegin == mCurrentNode)             
            {
                mList.mBegin = mCurrentNode.mNext;
            }
            mCurrentIndex--;
            hasHadNext = false; //Can't remove twice without next-ing
        }
        else //It w not a legal move. 
        {
            throw new IllegalStateException("You have to call next before trying to remove again!");
        }
    }
}
