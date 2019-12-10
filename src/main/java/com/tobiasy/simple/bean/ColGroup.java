package com.tobiasy.simple.bean;

/**
 * @author tobiasy
 * @date 2019/10/28
 */
public class ColGroup {
    private Integer index;
    private Integer width;

    public ColGroup(Integer index, Integer width) {
        this.index = index;
        this.width = width;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
