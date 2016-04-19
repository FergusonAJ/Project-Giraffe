
package Collisions;

//A basic homeade linked list that uses a generic type.
//Allows dynamic addition and removal of items.
public class LinkedList <E> 
{
    protected Node mBegin; //First Node of the List
    protected Node mEnd; // Last Node of the List
    protected Node mPointer; //Generic Node used for the LinkedListIterator
    protected int mSize; //Number of elements in the list
    
    //Basic constructor that only initializes the variables
    public LinkedList()
    {
        //Initialize all out variables
        mBegin = null;
        mEnd = null;
        mPointer = new Node(null);
        mSize = 0;
    }
   
    //Tack on a new node to the end of the list, and modify pointers appropriately.
    public void addToEnd(E value)
    {
        Node n = new Node(value); //Our new node
        if(mEnd == null) //If there is no mEnd yet, which should only be when this is the first item.
        {
            mEnd = n;
        }
        else //Otherwise, set pointers correctly using the current mEnd
        {
            mEnd.mNext = n;
            n.mPrev = mEnd;
            mEnd = n;
        }
        if(mSize == 0) //If it is the only element, it should be the beginning, too.
        {
            mBegin = n;
        }
        mSize++; //Don't forget to increment mSize!
    }
    
    //Tack on a new node to the beginning of the list, and modify the pointers appropriately.
    public void addToBegin(E value)
    {
        Node n = new Node(value); //Our new node
        if(mBegin == null) //If there is no current beginning, should only be true when the list is empty
        {
            mBegin = n;
        }
        else //Otherwise, set pointers using the current mBegin
        {
            mBegin.mPrev = n;
            n.mNext = mBegin;
            mBegin = n;
        }
        if(mSize == 0) //If there is no mEnd, which should only be when the list is empty.
        {
            mEnd = n;
        }
        mSize++; //Don't forget to increase the size!
    }
    
    //Add a new node after a certain index of the list.
    public void addAfter(int index, E value)
    {
        Node n = new Node(value); //Our new node
        Node original = getAt(index, true).mCurrentNode; //The node we are adding after
        if(original != mEnd) // Set the mPrev of the node after the original, if it exists
        {
            original.mNext.mPrev = n;
        }
        else
        {
            mEnd = n;
        }
        //Set all the pointers appropriately
        n.mNext = original.mNext;
        original.mNext = n;
        n.mPrev = original;
        mSize++; //Don't forget to add to the size!
    }
    
    //Add a new node after the current Node of the Iterator
    public void addAfter(LinkedListIterator LI, E value)
    {
        Node n = new Node(value); //Our new node
        Node original = LI.mCurrentNode; //The node we are adding after
        if(original != mEnd)//Set the mPrev of the node after the original, if it exists
        {
            original.mNext.mPrev = n;
        }
        else
        {
            mEnd = n;
        }
        //Set all the pointers
        n.mNext = original.mNext;
        original.mNext = n;
        n.mPrev = original;
        mSize++; //Don't forget to increment the size!
    }
    
    
    //Iterate through the list, and remove all nodes that have the certain value
    public int removeAll(E value)
    {
        int counter = 0;
        int numRemoved = 0;
        Node n = mBegin; //Start at the beginning of the list
        while(counter <= mSize + 1) //Loop through them!
        {
            if(n != null && n.mValue.equals(value)) //If we have a match
            {
                if(mSize != 1)
                {
                    if(n.mPrev != null && n.mNext != null) //If it is a 'normal' node (In the middle)
                    {
                        n.mPrev.mNext = n.mNext;
                        n.mNext.mPrev = n.mPrev;
                    }

                    if(mBegin == n) // If it is the first node in the list
                    {
                        mBegin = n.mNext;
                        n.mNext.mPrev = null;
                    }
                    if(mEnd == n) //If it is the last node in the list
                    {
                        mEnd = n.mPrev;
                        n.mPrev.mNext = null;
                    }
                }
                else
                {
                    mEnd = null;
                    mBegin = null;
                }
                mSize--; //Decrement the size
                numRemoved++; //Increment the counter
            }
            counter++; //Step through the list
            if(n != null) //If we have an actual node and not null, proceed
            {
                n = n.mNext;
            }
        }
        return numRemoved;
    }
    
    //Count all the nodes with a certain value
    public int count(E value)
    {
        int counter = 0; //Used for looping
        int numOccurrences = 0; //Our actual instance counter
        Node n = mBegin; //Start at the beginning
        while(counter < mSize) //Loop through the list
        {
            if(n != null && n.mValue == value)//If we have a hit, incrememnt the counter
            {
                numOccurrences++; 
            }
            counter++;
            if( n!= null)//Should be unneeded, but I like checking for nulls...
            {
                n = n.mNext;
            }
        }
        return numOccurrences;
    }
    
    //Get the node at a certain indedx, and return an iterator
    public LinkedListIterator getAt(int index, boolean startAtBeg)
    {
        if(startAtBeg) //If we go in the forward direction
        {
            int counter = 0;
            Node n = mBegin;
            while(counter < index) //Loop through the array until we hit the index
            {
                counter++;
                n = n.mNext;
            }
            LinkedListIterator LI = new LinkedListIterator(this, false); //Grab our node in an iterator
            LI.mCurrentNode = n;
            return LI;
        }
        else //If we want to traverse backwards
        {
            int counter = mSize;
            Node n = mEnd;
            while(counter - 1 > index) //Loop through the array until we hit the index
            {
                counter--;
                n = n.mPrev;
            }
            LinkedListIterator LI = new LinkedListIterator(this, false); //Grab our node in an iterator (Keeping it a forward iterator... could be changed)
            LI.mCurrentNode = n;
            return LI;
        }
    }
    
    //Return an iterator for this list
    public LinkedListIterator iterator()
    {
        return new LinkedListIterator(this, false);
    }
    
    //Return an iterator for this list in reverse order
    public LinkedListIterator riterator()
    {
        return new LinkedListIterator(this, true);
    }
    
    //Return true if list is empty
    public boolean isEmpty()
    {
        if(mSize == 0)
        {
            return true;
        }
        else   
        {
            return false;
        }
    }
    
    //Override the toString function, and returning a nice formatted string
    @Override
    public String toString()
    {
        if(mSize == 0) //Return a basic <empty> if the list is well... empty.
        {
            return "<empty>";
        }
        else
        {
            String output = "<"; //Start formatting
            int counter = 0;
            Node n = mBegin;
            while(counter < mSize) //Add a [value] for each node in list
            {
                output += "[" + n.mValue + "]";
                counter++;
                n = n.mNext;
            }
            output += ">"; //End formatting
            return output;
        }
    }

    public int Length()
    {
        return mSize;
    }
    //Basic Node class, which is what the linked list is comprised of. 
    protected class Node
    {
        protected Node mPrev; //Node that comes before this one
        protected Node mNext; //Node that comes after this one
        protected E mValue; //The value of the node
        
        //Basic constructor, just initialized the variables
        public Node(E value)
        {
            mValue = value;
            mPrev = null;
            mNext = null;
        }
        
        //Override the toString for fun! Basic formatting
        @Override
        public String toString()
        {
            return "Node: [" + mValue + "]";     
        }
    }
}