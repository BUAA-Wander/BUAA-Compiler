package symbol;

public class AddressPtr {
    private static int globalAddr = 0;
    private static int localAddr = 0;

    public static void addGlobalAddr(int addr) {
        globalAddr += addr;
    }

    public static void addLocalAddr(int addr) {
        localAddr += addr;
    }

    public static int getGlobalAddr() {
        return globalAddr;
    }

    public static int getLocalAddr() {
        return localAddr;
    }

    public static void resetLocalAddr(int addr) {
        localAddr = addr;
    }
}