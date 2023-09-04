package com.iscas.etm.excelParser.parser.continuedSheet;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContinuedPoint implements Cloneable{
    private int row;
    private int col;
    private int rowLength;
    private int colLength;
    private ContinuedPoint right = null;
    private ContinuedPoint down = null;

    public ContinuedPoint(int row, int col){
        this.row = row;
        this.col = col;
    }

    @Override
    public ContinuedPoint clone() {
        ContinuedPoint clone = new ContinuedPoint(this.getRow(), this.getCol());

        return clone;
    }
}
