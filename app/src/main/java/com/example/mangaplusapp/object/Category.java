package com.example.mangaplusapp.object;
import java.util.List;

public class Category {
    private String NAME_CATEGORY, ID_CATEGORY;
    private List<TruyenTranh> truyenTranhList;
    //Constructor empty for firebase
    public Category(){

    }

    public Category(String NAME_CATEGORY, String ID_CATEGORY) {
        this.NAME_CATEGORY = NAME_CATEGORY;
        this.ID_CATEGORY = ID_CATEGORY;
    }

    public Category(String NAME_CATEGORY, List<TruyenTranh> truyenTranhList) {
        this.NAME_CATEGORY = NAME_CATEGORY;
        this.truyenTranhList = truyenTranhList;
    }

    public String getNAME_CATEGORY() {
        return NAME_CATEGORY;
    }

    public void setNAME_CATEGORY(String NAME_CATEGORY) {
        this.NAME_CATEGORY = NAME_CATEGORY;
    }

    public String getID_CATEGORY() {
        return ID_CATEGORY;
    }

    public void setID_CATEGORY(String ID_CATEGORY) {
        this.ID_CATEGORY = ID_CATEGORY;
    }

    public List<TruyenTranh> getTruyenTranhList() {
        return truyenTranhList;
    }

    public void setTruyenTranhList(List<TruyenTranh> truyenTranhList) {
        this.truyenTranhList = truyenTranhList;
    }
}
