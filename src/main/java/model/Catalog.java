package model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Catalog {
    @SerializedName("data")
    private List<String> catalog;

    public Catalog() {

    }

    public Catalog(List<String> locations) {
        catalog = locations;
    }

    public List<String> getList() {
        return catalog;
    }
}
