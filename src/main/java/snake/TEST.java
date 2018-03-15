package snake;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class TEST {

    public static void main(String[] args) {

        Deque<Integer> test = new LinkedList<>();
        test.add(1);
        test.add(2);
        test.add(3);
        test.add(4);

        System.out.println(test.getFirst());
        System.out.println(test.getLast());

        test.add(5);
        test.removeFirst();

        System.out.println(test.toString());

        for (Integer each : test) {
            System.out.println(each);
        }


    }
}
