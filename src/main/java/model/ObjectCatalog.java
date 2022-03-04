package model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ObjectCatalog {
    @SerializedName("data")
    private List<Location> objectCatalog;

    public ObjectCatalog() {

    }

    ObjectCatalog(List<Location> locations) {objectCatalog = locations;}

    public List<Location> getLocations() {
        return objectCatalog;
    }
}
