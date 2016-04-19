package Collisions;

/**
 * HashMap class designed to store any types of key and value. 
 * Includes basic functionality for a HashMap
 * @param <K> Key Type
 * @param <V> Value Type
 */
public class HashMap <K,V>
{
    /**
     * What the iterator will return when next() is called
     */
    public enum IteratorType
    {
        key, 
        value
    }
    
    Object[] mHashTable; //Array to store all of our HashNodes in
    int mCapacityIncrease; //Number of slots to add when we expand out array
    float mLoadFactor; //Ratio of filled slots to total slots that lets us know to expand the array when we go over
    public int mTableSize; //Current size of our table
    int mFilledNodes = 0; //Current number of filled nodes
    
    /**
     * Simple constructor that just initializes our instance variables
     * @param initialCapacity Original size of our array
     * @param capacityIncrease Number of slots to increase the array by when we expand it
     * @param loadFactor Percentage of filled slots to total slots that we want to make the array expand
     */
    public HashMap(int initialCapacity, int capacityIncrease, float loadFactor)
    {
        mHashTable = new Object[initialCapacity];
        mTableSize = initialCapacity;
        mCapacityIncrease = capacityIncrease;
        mLoadFactor = loadFactor;
    }
    
    /**
     * Sets the certain index of the HashTable to the specified value
     * @param key Will be used to calculate what index to use
     * @param value The value to inject into that index
     */
    public void set(K key, V value)
    {
        int index = key.hashCode() % mTableSize; //Index to start at
        int counter = 0;
        //Go until we hit a null node or the one with our key
        while(mHashTable[index + counter] != null && ((HashNode)mHashTable[index + counter]).mKey != key)
        {
            counter++;
            if(index + counter >= mTableSize)
            {
                index = 0;
                counter = 0;
            }
        }
        if(mHashTable[index + counter] == null)
        {
            mFilledNodes++;
        }
        //Set that index to a node with our key and value
        mHashTable[index + counter] = new HashNode(key, value);
        
        //Check if we need to expand our HashTable
        if((float)mFilledNodes / mTableSize >= mLoadFactor)
        {
            expandTable();
        }
    }
    
    /**
     * Grab the value of the node with the specified key
     * @param key Key to find the node
     * @return 
     */
    public V get(K key)
    {
        int index = key.hashCode() % mTableSize; //Starting index
        int counter = 0;
        Object o = mHashTable[index];
        //Go until we hit a null or the right key
        while(mHashTable[index + counter] != null && ((HashNode)mHashTable[index + counter]).mKey != key)
        {
            counter++;
            if(index + counter >= mTableSize)
            {
                index = 0;
                counter = 0;
            }
        }
        o = mHashTable[index + counter];
        //Return either null or the value of the node
        if (o == null)
        {
            return null;
        }
        else
        {
            return ((HashNode)o).mValue;
        }
    }
    
    /**
     * Used for remove, functions like get but returns the integer position of the node
     * @param key
     * @return 
     */
    private int getIndex(K key)
    {
        int index = key.hashCode() % mTableSize; //Starting index
        int counter = 0;
        Object o = mHashTable[index];
        //Go until we hit null or the node with the right key
        while(mHashTable[index + counter] != null && ((HashNode)mHashTable[index + counter]).mKey != key)
        {
            counter++;
        }
        //Return that index number
        return index + counter;
    }
    
    /**
     * Removes the node with the specified key from the table
     * @param key Key for the node we wish to remove
     */
    public void remove(K key)
    {
        mHashTable[getIndex(key)] = null;       
    }
    
    /**
     * Overrides toString() to provide a formatted string containing all non-null key:value pairs
     * @return Formatted string
     */
    @Override
    public String toString()
    {
        String temp = "{";
        for(Object o : mHashTable)
        {
            if(o != null)
            {
                temp += o + ", ";
            }
        }
        temp = temp.substring(0, temp.length() - 2); //Remove the last comma 
        temp += "}";
        return temp;
    }
    
    /**
     * Used for debugging. Prints the entire array out showing nulls
     *  in oder to check the position of nodes
     */
    public void fullPrint()
    {
        String temp = "Table with " + mFilledNodes + "/" + mTableSize + " nodes filled:\n";
        int index = 0;
        for(Object o : mHashTable)
        {
            temp += "\t" + index + "[" + o + "]\n";
            index++;
        }
        System.out.println(temp);
    }
    
    /**
     * Expands the hash table
     */
    private void expandTable()
    {
        //Change the size of the table and prep the size variabe for rehashing
        int tempTableSize = mTableSize + mCapacityIncrease;
        Object[] newTable = new Object[tempTableSize]; //Create a new table to dump values in
        HashMapIterator IK = iterator(IteratorType.key);
        HashMapIterator IV = iterator(IteratorType.value);
        
        //Rehash all the nodes in the table
        while(IK.hasNext())
        {
            K key = (K)IK.next();
            V value = (V)IV.next();
            int index = key.hashCode() % tempTableSize;
            int counter = 0;
            while(newTable[index + counter] != null && ((HashNode)newTable[index + counter]).mKey != key)
            {
                counter++;
                if(index + counter >= mTableSize)
                {
                    index = 0;
                    counter = 0;
                }
            }
            newTable[index + counter] = new HashNode(key, value);
        }
        mHashTable = newTable;
        mTableSize = tempTableSize;
    }
    
    /**
     * Returns the Index size of the HashMap. 
     * @return The number of filled nodes
     */
    public int getSize()
    {
        return mFilledNodes;
    }
    
    /**
     * Provides an iterator for the HashMap, set to find values
     * @return HashMapIterator for this HashMap 
     */
    public HashMapIterator iterator()
    {
        return new HashMapIterator(this);
    }
    
    /**
     * Provides an iterator for the HashMap, allows you to choose whether to return keys or values
     * @param type Of what you want to return, either keys or values
     * @return HashMapIterator for this HashMap
     */
    public HashMapIterator iterator(HashMap.IteratorType type)
    {
        return new HashMapIterator(this, type);
    }
    
    /**
     * Super simple node class to store a key and a value
     */
    protected class HashNode
    {
        K mKey; //Key for the node
        V mValue; //Actual value for the node
        
        /**
         * Set the instance variables
         * @param key What we want the key to be for this node
         * @param value  What we want the value to be for this node
         */
        protected HashNode(K key, V value)
        {
            mKey = key;
            mValue = value;
        }
        
        /**
         * Returns a nice formatted string
         * @return A formatted string of key:value
         */
        @Override
        public String toString()
        {
            return mKey.toString() + ":"  + mValue.toString();
        }
    }
}