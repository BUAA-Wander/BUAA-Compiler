package ir;

public class LabelGenerator {
    public static int idx = 1;

    public static String nextLabel() {
        // #0 is always 0
        String res = "label_" + idx;
        idx++;
        return res;
    }
}
