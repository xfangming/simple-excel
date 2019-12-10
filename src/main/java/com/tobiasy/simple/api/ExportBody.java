package com.tobiasy.simple.api;

import java.util.List;

/**
 * @author tobiasy
 * @date 2019/11/11
 */
public class ExportBody<T> {
    private List<T> data;
    private Position position;

    public ExportBody() {
        position = Position.of();
    }

    public ExportBody(List<T> data, Position position) {
        this.data = data;
        this.position = position;
    }

    public ExportBody(Position position) {
        this.position = position;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
