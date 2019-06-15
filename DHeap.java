/**
 * D-Heap
 */

public class DHeap
{
	
    private int size, max_size, d;
    private DHeap_Item[] array;

	// Constructor
	// m_d >= 2, m_size > 0
    DHeap(int m_d, int m_size) {
               max_size = m_size;
			   d = m_d;
               array = new DHeap_Item[max_size];
               size = 0;
    }
	
    public String toString() {
    	StringBuffer bString = new StringBuffer();
    	for (int i=0; i<size; i++) {
    		bString.append(array[i].getKey());
    		bString.append(", ");
    	}
    	return bString.toString();
    }
    
	/**
	* Heapifies-up the node I:
	* if the key of I.parent is bigger than the key of I, a switch between the two is performed.
	* continues until the key of I.parent is smaller or equal of the key of I *or* I is the root of the heap
	* 
	* @pre I!=null; I belongs to *this*
	*
	* @postcondition: isHeap()==true
	* @Returns the number of comparisons performed. 
	*/
    public int HeapifyUp(DHeap_Item I) {
    	int number_of_comparisons=0;
    	while (I.getPos()>0) {
    		int index_parent=parent(I.getPos(), this.d);
    		DHeap_Item I_parent=this.array[index_parent];
    		if (I_parent.getKey()<=I.getKey()) {
    			number_of_comparisons++;
    			return number_of_comparisons; //no more heapify-up
    		}
    		else if (I_parent.getKey()>I.getKey()) {
    			swtichTwoNodes(I,I_parent); //heapifing up
    			number_of_comparisons++;
    		}
    	}
    	return number_of_comparisons;
    }


	/**
	* Heapifies-downs the node I:
	* the method finds son of I with the minimum key. if the minimum key is smaller than the key of I, a switch is performed.
	* this continues as described until the key of I is smaller or equal to the minimum key *or* I is a leaf.
	* 
	* @pre I!=null; I belongs to *this*
	*
	* @postcondition: isHeap()==true
	* @Returns the number of comparisons performed. 
	*/
    public int HeapifyDown(DHeap_Item I) {
    	int pos=I.getPos(); 
    	int first_son_index=pos*d+1;
    	
    	if (first_son_index>=size) { //stop stage
    		return 0;
    	}
    	else {
    		//getting the son the minimum key + number of comparisons
    		DHeap_Item min_son=this.array[first_son_index];
        	int number_of_comparisons=0;
        	for (int i=first_son_index+1; i<=pos*d+d && i<this.size ;i++) {
        		number_of_comparisons++;
        		if (this.array[i].getKey()<min_son.getKey()) {
        			min_son=this.array[i];
        		}
        	}
        	//done finding the minimum key
        	if (I.getKey()<=min_son.getKey()) { //allgood
        		return number_of_comparisons+1;
        	}
        	else { //a heapifyDown is required.
        		
            	//switching between father and min_son
            	swtichTwoNodes(I, min_son);
            	
            	//moving to next stage
            	return HeapifyDown(I)+number_of_comparisons+1;
        	}
    	}
    }
    /**
     * 
     * @param node1
     * @param node2
     * 
     * performs a switch between two nodes.	
     */
    public void swtichTwoNodes(DHeap_Item node1, DHeap_Item node2) { //useful for heapify-up and heapify-down
    	int pos_of_node1=node1.getPos();
    	int pos_of_node2=node2.getPos();
    	array[pos_of_node1]=node2;
    	array[pos_of_node2]=node1;
    	node1.setPos(pos_of_node2);
    	node2.setPos(pos_of_node1);
    }
    
    
	/**
	 * public int getSize()
	 * Returns the number of elements in the heap.
	 */
	public int getSize() {
		return size;
	}
	
  /**
     * public int arrayToHeap()
     *
     * The function builds a new heap from the given array.
     * Previous data of the heap should be erased.
     * preconidtion: array1.length() <= max_size
     * postcondition: isHeap()
     * 				  size = array.length()
     * Returns number of comparisons along the function run. 
	 */
    public int arrayToHeap(DHeap_Item[] array1) {
        //general setting before turning to heap
    	this.size=array1.length;
    	for (int i=0; i<size; i++) { 
    		array1[i].setPos(i);
    		array[i]=array1[i];
    	}
        
    	//now turning to a heap
    	//finding the index of last parent
    	int index_of_last_parent=parent(size-1, this.d);
    	int total_comp=0;
    	for (int j=index_of_last_parent; j>=0; j--) {
    		total_comp+=HeapifyDown(array[j]);
    	}
    	return total_comp; 
    }

    /**
     * public boolean isHeap()
     *
     * The function returns true if and only if the D-ary tree rooted at array[0]
     * satisfies the heap property or has size == 0.
     *   
     */
    public boolean isHeap()	{
        if (size==0) {
        	return true;
        }
        else {
        	int index_of_last_child=size-1;
        	while (index_of_last_child>0) {
        		DHeap_Item child = array[index_of_last_child];
        		DHeap_Item parent = array[parent(child.getPos(),this.d)];
        		
        		if (child.getKey()>=parent.getKey()) {
        			index_of_last_child--;
        			continue;
        		}
        		else {
        			return false;
        		}
        	}
        	return true;
        }
    }


 /**
     * public static int parent(i,d), child(i,k,d)
     * (2 methods)
     *
     * precondition: i >= 0, d >= 2, 1 <= k <= d
     *
     * The methods compute the index of the parent and the k-th child of 
     * vertex i in a complete D-ary tree stored in an array. 
     * Note that indices of arrays in Java start from 0.
     */
    public static int parent(int i, int d) {
    	return (i-1)/d;
    }
    	
    public static int child (int i, int k, int d) {
    	return d*i+k;
    }

    /**
    * public int Insert(DHeap_Item item)
    *
	* Inserts the given item to the heap.
	* Returns number of comparisons during the insertion.
	*
    * precondition: item != null
    *               isHeap()
    *               size < max_size
    * 
    * postcondition: isHeap()
    */
    public int Insert(DHeap_Item item) 
    {        
    	item.setPos(size);
    	array[size]=item;
    	size+=1;
    	return HeapifyUp(item);
    }

    
 /**
    * public int Delete_Min()
    *
	* Deletes the minimum item in the heap.
	* Returns the number of comparisons made during the deletion.
    * 
	* precondition: size > 0
    *               isHeap()
    * 
    * postcondition: isHeap()
    */
    public int Delete_Min() {
    	swtichTwoNodes(array[0],array[size-1]);
    	size-=1;
    	return HeapifyDown(array[0]);
    }


    /**
     * public DHeap_Item Get_Min()
     *
	 * Returns the minimum item in the heap.
	 *
     * precondition: heapsize > 0
     *               isHeap()
     *		size > 0
     * 
     * postcondition: isHeap()
     */
    public DHeap_Item Get_Min() {
    	return array[0];
    }
	
  /**
     * public int Decrease_Key(DHeap_Item item, int delta)
     *
	 * Decerases the key of the given item by delta.
	 * Returns number of comparisons made as a result of the decrease.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Decrease_Key(DHeap_Item item, int delta) {
    	item.setKey(item.getKey()-delta);
	    return HeapifyUp(item);
    }
	
	  /**
     * public int Delete(DHeap_Item item)
     *
	 * Deletes the given item from the heap.
	 * Returns number of comparisons during the deletion.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Delete(DHeap_Item item) {
    	int num_of_dec_key_comp = Decrease_Key(item,item.getKey()-this.Get_Min().getKey()+1); //decreasing the key of item 
    	//in order to make item the "new" minimum.
    	int num_of_del_min_comp = Delete_Min();
    	return num_of_dec_key_comp+num_of_del_min_comp;
    }
	
	/**
	* Sort the input array using heap-sort (build a heap, and 
	* perform n times: get-min, del-min).
	* Sorting should be done using the DHeap, name of the items is irrelevant.
	* 
	* Returns the number of comparisons performed.
	* 
	* postcondition: array1 is sorted 
	*/
	public static int DHeapSort(int[] array1, int d) {
		DHeap_Item[] array1_DHeap_items = new DHeap_Item[array1.length];
		DHeap Darray1 = new DHeap(d, array1.length);
		//coping the items into the DHeap
		for (int i=0; i<Darray1.max_size; i++) { 
			array1_DHeap_items[i] = new DHeap_Item("i", array1[i]); //inserting keys of array1 to the array array1_DHeap_items items
			array1_DHeap_items[i].setPos(i);
		}
		int number_of_comp=Darray1.arrayToHeap(array1_DHeap_items);
		for (int j=Darray1.max_size-1; j>=0; j--) {
			DHeap_Item tmp_MIN = Darray1.Get_Min();
			number_of_comp+=Darray1.Delete_Min();
			Darray1.array[j]=tmp_MIN;
		}
		
		//reversing the up-down sorted heap
		for (int i=0; i<Darray1.max_size/2; i++) {
			DHeap_Item tmp_Item = Darray1.array[Darray1.max_size-i-1];
			Darray1.array[Darray1.max_size-i-1]=Darray1.array[i];
			Darray1.array[i]=tmp_Item;
		}
		
		for (int i=0; i<Darray1.max_size; i++) {
			array1[i]=Darray1.array[i].getKey();
		}
		return number_of_comp;
	}
	
}
