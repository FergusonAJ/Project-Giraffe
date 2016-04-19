package Collisions;

/**
 * Basic HashSet class that inherits from HashMap
 * @param <K> Type of data you wish to store
 */
public class HashSet <K> extends HashMap
{
    
    /**
     * Basic constructor that is really just a wrapper for HashMap's constructor
     * @param initialCapacity Initial size of your HashTable
     * @param capacityIncrease How much you want to add to the HashTable if it is too small
     * @param loadFactor Percentage of filled nodes to total nodes, if the table has a higher percentage, expand it
     */
    public HashSet(int initialCapacity, int capacityIncrease, float loadFactor) 
    {
        super(initialCapacity, capacityIncrease, loadFactor);
    }
    
    /**
     * Wrapper for HashMap.set() that only takes one argument, K and V are the same here
     * @param key 
     */
    public void add(K key)
    {
        super.set(key, key);
    }
    
    /**
     * Provides a nice formatted string
     * @return A string in form {key1, key2, ....., keyn}
     */
    @Override
    public String toString()
    {
        String temp = "{";
        for(Object o : mHashTable)
        {
            if(o != null)
            {
                temp += ((HashMap.HashNode)o).mKey + ", ";
            }
        }
        //Remove the last comma
        if(temp.length() > 2)
        {
            temp = temp.substring(0,temp.length() - 2); 
        }
        return temp + "}";
    }
    
    /**
     * For debugging. Prints all the nodes including nulls to show position in the array
     */
    public void fullPrint()
    {
        String temp = "Table with " + mFilledNodes + "/" + mTableSize + " nodes filled:\n";
        int index = 0;
        for(Object o : mHashTable)
        {
            if(o != null)
            {
                temp += "\t" + index + "[" + ((HashMap.HashNode)o).mKey + "]\n";
            }
            else
            {
                temp += "\t" + index + "[" + null + "]\n";
            }
            index++;
        }
        System.out.println(temp);
    }
    
    /**
     * Combines two HashSets
     * @param hs other HashSet to combine to this one
     * @return New HashSet that is both this one and the argument combined
     */
    public HashSet<K> union(HashSet<K> hs)
    {
        HashSet<K> newSet = new HashSet(mTableSize, mCapacityIncrease, mLoadFactor);
        HashMapIterator I1 = iterator();
        HashMapIterator I2 = hs.iterator();
        //Dump in all of this HashMap's value
        while(I1.hasNext())
        {
            newSet.add((K)I1.next());
        }
        //Dump in all of the other HashMap's values
        while(I2.hasNext())
        {
            newSet.add((K)I2.next());
        }
        return newSet;
                
    }
    
    /**
     * Returns a HashSet with values shared between both sets
     * @param hs HashSet to get the intersection with this one
     * @return New HashSet containing the intersection of this set and hs.
     */
    public HashSet<K> intersection(HashSet<K> hs)
    {
        HashSet<K> newSet = new HashSet(mTableSize, mCapacityIncrease, mLoadFactor);
        HashMapIterator I1 = iterator(HashMap.IteratorType.key);
        //Iterate through this set
        while(I1.hasNext())
        {
            K o = (K)I1.next();
            //If the other HashSet has this value, add it to the new set
            if(hs.get(o) != null)
            {
                newSet.add(o);
            }
        }
        return newSet;
                
    }
    
    /**
     * Returns a HashSet containing the values specific to this set and not the one passed
     * @param hs HashSet to check against this one
     * @return New HashSet containing the difference between this set and hs
     */
    public HashSet<K> relativeDifference(HashSet<K> hs)
    {
        HashSet<K> newSet = new HashSet(mTableSize, mCapacityIncrease, mLoadFactor);
        HashMapIterator I1 = iterator(HashMap.IteratorType.key);
        HashMapIterator I2 = hs.iterator(HashMap.IteratorType.key);
        //Add all of the values from this set
        while(I1.hasNext())
        {
            newSet.add((K)I1.next());
        }
        //Attempt to remove all the values that are in the other set
        while(I2.hasNext())
        {
            newSet.remove((K)I2.next());
        }
        return newSet;
                
    }
}
