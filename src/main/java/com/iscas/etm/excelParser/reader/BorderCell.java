package com.iscas.etm.excelParser.reader;

public class BorderCell implements Cloneable{
    private boolean up;
    private boolean left;
    private boolean bottom;
    private boolean right;

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

    public boolean isExistRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    @Override
    public BorderCell clone() {
        BorderCell clone = new BorderCell();
        clone.setBottom(this.bottom);
        clone.setLeft(this.left);
        clone.setRight(this.right);
        clone.setUp(this.up);

        return clone;
    }
}
