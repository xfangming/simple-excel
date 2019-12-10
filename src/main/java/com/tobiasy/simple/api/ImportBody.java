package com.tobiasy.simple.api;

import java.util.function.Function;

/**
 * @author tobiasy
 * @date 2019/11/12
 */
public class ImportBody {
    private Function<String, Object>[] functions;
    private Position position;

    public ImportBody(){}

    public ImportBody(Position position){
        this.position = position;
    }

    public ImportBody(Function<String, Object>[] functions, Position position){
        this.functions = functions;
        this.position = position;
    }

    public Function<String, Object>[] getFunctions() {
        return functions;
    }

    public ImportBody setFunctions(Function<String, Object>[] functions) {
        this.functions = functions;
        return this;
    }

    public Position getPosition() {
        return position;
    }

    public ImportBody setPosition(Position position) {
        this.position = position;
        return this;
    }
}
