package error;

public class Error implements Comparable<Error> {
    private int lineNumber;
    private String type;

    @Override
    public int compareTo(Error o) {
        if (lineNumber == o.lineNumber) {
            return 0;
        } else if (lineNumber < o.lineNumber) {
            return -1;
        }
        return 1;
    }

    public Error(int lineNumber, String type) {
        this.lineNumber = lineNumber;
        this.type = type;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return lineNumber + " " + type;
    }
}
