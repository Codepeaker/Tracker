package com.codepeaker.hora.enums;

public enum FirebaseEnums {
    CHILDREN("children"), PARENTS("parents");

    private String value;

    public String getValue() {
        return value;
    }

    FirebaseEnums(String children) {
        this.value = children;
    }
}
