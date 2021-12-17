package ir;

public class IdGenerator {
    public static int idx = 1;

    public static String nextId() {
        // #0 is always 0
        String res = "#" + idx;
        idx++;
        return res;
    }
}