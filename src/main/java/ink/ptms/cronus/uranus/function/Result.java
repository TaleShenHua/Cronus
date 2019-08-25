package ink.ptms.cronus.uranus.function;

public class Result {

    private String start;
    private String center;
    private String end;

    public Result(String start, String center, String end) {
        this.start = start;
        this.center = center;
        this.end = end;
    }

    public String replace(String after) {
        return start + after + end;
    }

    public String getStart() {
        return start;
    }

    public String getCenter() {
        return center;
    }

    public String getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Result{" +
                "start='" + start + '\'' +
                ", center='" + center + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}