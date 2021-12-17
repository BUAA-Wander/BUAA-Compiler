package ir;

import java.util.Stack;

public class CallStack {
    public static Stack<String> labels = new Stack<>();

    public static boolean isEmpty() {
        return labels.isEmpty();
    }

    public static void call(String label) {
        labels.add(label);
    }

    public static String getReturnAddr() {
        return labels.peek();
    }

    public static void popLabel() {
        labels.pop();
    }
}
