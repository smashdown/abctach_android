package com.abctech.blogtalking.repository.realm;

import io.realm.RealmObject;

public class RealmString extends RealmObject {

    String value;

    public RealmString() {
    }

    public RealmString(String s) {
        this.value = s;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RealmString) {
            return ((RealmString) obj).value.equals(value);
        } else if (obj instanceof String) {
            return obj.equals(value);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : -1;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "";
    }
}
