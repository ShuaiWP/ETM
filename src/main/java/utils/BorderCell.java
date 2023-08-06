package utils;

public class BorderCell{
    private boolean up;
    private boolean left;
    private boolean bottom;

    public boolean isExistUpBorder() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isExistLeftBorder() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isExistBottom() {
        return bottom;
    }

    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }
}
