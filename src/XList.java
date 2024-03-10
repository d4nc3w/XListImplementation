import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class XList<T> implements List<T> {
    Node<T> head;
    int size;

    class Node<T> {
        T value;
        Node<T> next;

        public Node(T t) {
            value = t;
            next = null;
        }
    }

    //Constructors:
    public XList() {
        size = 0;
        head = null;
    }

    //XList from arguments of elements
    public XList(T... elements) {
        for (T t : elements) {
            add(t);
        }
    }

    //XList from collection
    public XList(Collection<T> collection) {
        if(collection.isEmpty()){
            size = 0;
            head = null;

        } else {
            for (T t : collection) {
                add(t);
            }
        }
    }

    //XList from array
    public static <T> XList<T> of(T... elements) {
        return new XList<>(elements);
    }

    //XList from collection
    public static <T> XList<T> of(Collection<T> collection) {
        return new XList<>(collection);
    }

    //Method to create XList of chars from String
    public static XList<String> charsOf(String input) {
        String[] chr = input.split("");
        return new XList<>(chr);
    }

    //Overloaded method tokensOf without specified separator that return it with the default one (space)
    public static XList<String> tokensOf(String input) {
        return tokensOf(input, "\\s+");
    }

    //Method to create XList of tokens from a string using a separator specified as arg
    public static XList<String> tokensOf(String input, String separator) {
        return new XList<>(input.split(separator));
    }

    //Method to perform union of two XLists (add them up)
    public XList<T> union(Collection<T> collection) {
        XList<T> result = new XList<>(this);
        result.addAll(collection); // Add elements from the given collection directly
        return result;
    }

    //Overloaded method to perform union of two XLists (add them up)
    public XList<T> union(T... elements) {
        return union(new XList<>(elements));
    }

    //Method comparing elements between two collections and identifies the unique elements and returns them
    public XList<T> diff(Collection<T> collection) {
        XList<T> result = new XList<>();
        for (T element : this) {
            if (!collection.contains(element)) {
                result.add(element);
            }
        }
        return result;
    }

    //Method to return unique elements in the XList
    public XList<T> unique() {
        XList<T> result = new XList<>();
        Set<T> checked = new HashSet<>();

        for (T element : this) {
            if (checked.add(element)) {
                result.add(element);
            }
        }
        return result;
    }

    //Method to return all possible combinations of elements in the XList
    public XList<XList<T>> combine(){
        XList<XList<T>> result = new XList<>();
        XList<T> current = new XList<>();
        XList<List<T>> toRead = (XList<List<T>>) this;
        combineRecursion(result, current,this.size() -1, toRead);
        return result;
    }

    public void combineRecursion(XList<XList<T>> result, XList<T> current, int index, XList<List<T>> toRead){
        if (index < 0) {
            result.add(new XList<>(current));
            return;
        }

        List<T> one = toRead.get(index);
        for (T elem : one) {
            current.add(0, elem);
            combineRecursion(result, new XList<>(current), index - 1, toRead);
            current.remove(0);
        }
    }

    //Method to return all possible permutations of elements in the XList
    public <R> XList<R> collect(Function<T, R> function) {
        XList<R> result = new XList<>();
        for (T element : this) {
            result.add(function.apply(element));
        }
        return result;
    }

    //Method to join elements in the XList using a separator
    public String join(String separator) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            result.append(get(i));
            if (i < size() - 1) {
                result.append(separator);
            }
        }
        return result.toString();
    }

    //If no separator is given, use an empty string
    public String join() {
        return join("");
    }

    //Method to perform an action on each element of the XList
    public void forEachWithIndex(BiConsumer<T, Integer> consumer) {
        for (int i = 0; i < this.size(); i++) {
            consumer.accept(this.get(i), i);
        }
    }

//------------------------------------------------------------------------//
    /** Returns the number of elements in this list **/
    @Override
    public int size() {
        return size;
    }

    /** Returns true if this list contains no elements **/
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns value as a string **/
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            result.append(get(i));
            if (i < size - 1) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }

    /** Returns true if this list contains the specified element **/
    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (Node<T> current = head; current != null; current = current.next) {
                if (current.value == null) {
                    return true;
                }
            }
        } else {
            for (Node<T> current = head; current != null; current = current.next) {
                if (o.equals(current.value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Returns an iterator over the elements in this list in proper sequence **/
    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<T> {
        Node<T> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            Node<T> result = current;
            current = current.next;
            return result.value;
        }
    }

    /** Appends the specified element to the end of this list **/
    @Override
    public boolean add(T t) {
        Node<T> newNode = new Node<>(t);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            // Now current.next == null
            current.next = newNode;
        }
        size++;
        return true;
    }

    /** Removes the first occurrence of the specified element from this list if present **/
    @Override
    public boolean remove(Object o) {
        if (head == null || o == null) {
            return false; //If the list is empty or the object is null, return false
        }

        if(o.equals(head.value)) {
            head = head.next; //If the element to remove is head, move head to the next element
            size--;
            return true;
        }

        Node<T> current = head;
        while(current.next != null) {
            if(o.equals(current.next.value)) {
                current.next = current.next.next; //Skip the node containing the element to remove
                size--;
                return true;
            }
            current = current.next;
        }
        return false; //Element not found in the list
    }

    /** Inserts all the elements in the specified collection into this list at the specified position **/
    @Override
    public boolean addAll(Collection<? extends T> c) {
        for(T element : c) {
            add(element);
        }
        return true;
    }

    /** Removes from this list all of its elements that are contained in the specified collection **/
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if(remove(element)) {
                modified = true; ///If any element is removed, set modified to true
            }
        }
        return modified;
    }

    /** Returns the element at the specified position in the list **/
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Out of bounds: " + index);
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        // Now current points to the node with index == index
        return current.value;
    }

    /** Replaces the element at the specified position in this list with the specified element **/
    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= size || head == null) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        //Traverse the list to reach the node at the specified index
        Node<T> current = head;
        for(int i = 0; i < index; i++) {
            current = current.next;
        }

        //Store the old value, replace it with the new one and return the old value
        T oldValue = current.value;
        current.value = element;
        return oldValue;
    }

    /** Inserts the specified element at the specified position in this list **/
    @Override
    public void add(int index, T element) {
        Node<T> newNode = new Node<>(element);
        if(index == 0) {
            newNode.next = head;
            head = newNode;
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }

    /** Removes the element at the specified position in this list **/
    @Override
    public T remove(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Out of bounds: " + index);
        }

        T removedElement;
        if(index == 0) {
            removedElement = head.value;
            head = head.next;
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            removedElement = current.next.value;
            current.next = current.next.next;
        }
        size--;
        return removedElement;
    }

    /** Returns an array containing all the elements in this list in proper sequence **/
    //I don't use this method anywhere
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size]; // Create an array of Object with size equal to the number of elements
        int index = 0;
        for (Node<T> current = head; current != null; current = current.next) {
            result[index++] = current.value; // Assign each element from the list to the array
        }
        return result;
    }

    /** Also returns an array containing all elements but casted to type T array **/
    //I don't use this method anywhere
    @Override
    public <T> T[] toArray(T[] a) {
        Object[] data = toArray();
        T[] result = (T[]) data;
        return result;
    }

//---------------------------------------------------------------//

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public void clear() {
        //Ignore
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }
}