package Collisions;

import java.util.Iterator;

/**
 * Iterator for a HashMap, can return either keys or values
 */
public class HashMapIterator implements Iterator
{

    HashMap mMap; //The map we are iterating through
    int mCurIndex; //Index we are currently at
    HashMap.IteratorType mType; //Whether we are returning keys or values
    
    /**
     * Constructor without a IteratorType, defaults to keys
     * @param hm HashMap to iterate through
     */
    public HashMapIterator(HashMap hm)
    {
        mMap = hm;
        mCurIndex = -1;
        mType = HashMap.IteratorType.value;
    }
    
    /**
     * Constructor that lets us pass the IteratorType
     * @param hm HashMap to iterate through
     * @param type Whether to return keys or values
     */
    public HashMapIterator(HashMap hm, HashMap.IteratorType type)
    {
        mMap = hm;
        mCurIndex = -1;
        mType = type;
    }
    
    /**
     * Check to see if there are more values to iterate through
     * @return Boolean representing if there are values yet to return
     */
    @Override
    public boolean hasNext() 
    {
        //Go until we hit a non-null or the end of the table
        int counter = 1;
        while(mCurIndex + counter < mMap.mTableSize && mMap.mHashTable[mCurIndex + counter] == null)
        {
            counter++;
        }
        //Return true if we are not at the end of the array 
        if(mCurIndex + counter <= mMap.mTableSize - 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Gets the next item in the HashMap
     * @return Either the key or value of the next item in the HashMap
     */
    @Override
    public Object next()
    {
        if(hasNext())
        {
            //Go until we hit something not null
            int counter = 1;
            while(mMap.mHashTable[mCurIndex + counter] == null)
            {
                counter++;
            }
            //Increment our current index to match
            mCurIndex += counter;
            if(mType == HashMap.IteratorType.key)
            {
                return ((HashMap.HashNode)(mMap.mHashTable[mCurIndex])).mKey;
            }
            else
            {
                return ((HashMap.HashNode)(mMap.mHashTable[mCurIndex])).mValue;
            }
        }
        else
        {
            return null;
        }
    }
    
}
