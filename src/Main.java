import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

// The Main.java file can be modified freely,
// but you will obtain points for proper implementation of each XList class methods.

// If you don't implement all methods, you should remove those fragments from Main class,
// which causes compilation errors - otherwise you will not get any points.

public
class Main {

    public static void main(String[] args) {

        // Some additional data sets
        Integer[] ints = { 100, 200, 300 };
        Set<Integer> set = new HashSet<>(Arrays.asList(3, 4, 5));

        // Ways of creating XList
        XList<Integer> list1 = new XList<>(1, 3, 9, 11);
        XList<Integer> list2 = XList.of(5, 6, 9);
        XList<Integer> list3 = new XList(ints);
        XList<Integer> list4 = XList.of(ints);
        XList<Integer> list5 = new XList(set);
        XList<Integer> list6 = XList.of(set);

        System.out.println(list1);
        System.out.println(list2);
        System.out.println(list3);
        System.out.println(list4);
        System.out.println(list5);
        System.out.println(list6);

        // --- and auxiliary methods for creating from strings
        XList<String> slist1 = XList.charsOf("ala ma kota");
        XList<String> slist2 = XList.tokensOf("ala ma kota");
        XList<String> slist3 = XList.tokensOf("A-B-C", "-");

        System.out.println(slist1);
        System.out.println(slist2);
        System.out.println(slist3);

        //union method - sum of elements
        List<Integer> m1 = list1.union(list2);  // of course, you can assign result to List
        System.out.println(m1);
        // You can perform all the operations of the List interface, for example:
        m1.add(11);
        System.out.println(m1);
        XList<Integer> m2 = (XList<Integer>) m1;
        //I had to wrap ints using List.of -- Check if we can modify Main class
        XList<Integer> m3 = m2.union(ints).union(XList.of(4, 4));
        System.out.println(m2); // m2 doesn't change
        System.out.println(m3); // result is in m3
        m3 = m3.union(set);
        System.out.println(m3);

        // We saw union method
        // Now diff method (with any collection as an argument)
        System.out.println(m3.diff(set));  // all from m3, which is not in set
        System.out.println(XList.of(set).diff(m3)); // all what is in set, but not in m3

        // Unique method - returns a new XList without duplicates
        XList<Integer> uniq = m3.unique(); // a list, not a Set
        System.out.println(uniq);

        // combinations (order of elements does matter)
        List<String> sa = Arrays.asList( "a", "b");
        List<String> sb = Arrays.asList( "X", "Y", "Z" );
        XList<String> sc = XList.charsOf( "12" );
        XList toCombine = XList.of(sa, sb, sc);  // can we avoid of usage of raw type?
        System.out.println(toCombine);
        XList<XList<String>> cres = toCombine.combine();
        System.out.println(cres);

        // collect and join
        XList<String> j1 = cres.collect( list -> list.join());
        System.out.println(j1.join(" "));
        XList<String> j2 =cres.collect( list -> list.join("-"));
        System.out.println(j2.join(" "));

        // forEachWithIndex
        XList<Integer> lmod = XList.of(1,2,8, 10, 11, 30, 3, 4);
        lmod.forEachWithIndex( (e, i) -> lmod.set(i, e*2));
        System.out.println(lmod);
        lmod.forEachWithIndex( (e, i) -> { if (i % 2 == 0) lmod.remove(e); } );
        System.out.println(lmod);
        lmod.forEachWithIndex( (e, i) -> { if (i % 2 == 0) lmod.remove(i); } );
        System.out.println(lmod); // Question: why do we have such an effect?
        //Answer -> Because we are removing elements from the list while iterating over it
    }
}