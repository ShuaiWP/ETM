package utils;

public class ColHeadingWrapper {
    private int index;  //行索引
    private String content;  //列标题的主体内容
    private int preBlankNum;    //前置空格数，用于分级
    private String unit = "";    //当前行的标题
    private String colHeading;  //合并后的列标题
    private String name;

    public ColHeadingWrapper(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPreBlankNum() {
        return preBlankNum;
    }

    public void setPreBlankNum(int preBlankNum) {
        this.preBlankNum = preBlankNum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getColHeading() {
        return colHeading;
    }

    public void setColHeading(String colHeading) {
        this.colHeading = colHeading;
    }
}
