package ru.maxima.springmvc.models;

public class ParamRec {
    public String type;
    public Object value;

    public ParamRec() {}

    public ParamRec(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
