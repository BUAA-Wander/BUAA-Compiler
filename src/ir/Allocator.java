package ir;

public class Allocator {
    public static int currentPtr = 0;

    public static int alloc(int size) {
        currentPtr += size;
        return currentPtr - size;
    }
}
