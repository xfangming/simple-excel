package com.tobiasy.simple.api;

/**
 * @author tobiasy
 * @date 2019/11/11
 */
public class Position {
    private Integer sheetAt = 0;
    private Integer startRow = 0;
    private Integer startCol = 0;

    public Position() {
    }

    public Position(Integer sheetAt, Integer startRow, Integer startCol) {
        if (sheetAt != null) {
            this.sheetAt = sheetAt;
        }
        if (startRow != null) {
            this.startRow = startRow;
        }
        if (startCol != null) {
            this.startCol = startCol;
        }
    }

    public static Position of() {
        return new Position();
    }

    public static Position of(Integer sheetAt, Integer startRow, Integer startCol) {
        return new Position(sheetAt, startRow, startCol);
    }

    public static Position withStartRow(Integer startRow) {
        return new Position(null, startRow, null);
    }

    public Integer getSheetAt() {
        return sheetAt;
    }

    public void setSheetAt(Integer sheetAt) {
        this.sheetAt = sheetAt;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getStartCol() {
        return startCol;
    }

    public void setStartCol(Integer startCol) {
        this.startCol = startCol;
    }
}
