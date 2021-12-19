package mips;

public class Annotation extends MipsCode {
    private String annotation;
    public Annotation(String annotation) {
        this.annotation = annotation;
    }

    public String toString() {
        return "# " + annotation;
    }
}
