package com.example.mangaplusapp.object;
import java.util.List;

public class Categories {
    private String NAME_CATEGORY, ID_CATEGORY;
    private List<Mangas> truyenTranhList;
    //Constructor empty for firebase
    public Categories(){

    }

    public Categories(String NAME_CATEGORY, String ID_CATEGORY) {
        this.NAME_CATEGORY = NAME_CATEGORY;
        this.ID_CATEGORY = ID_CATEGORY;
    }

    public Categories(String NAME_CATEGORY, List<Mangas> truyenTranhList) {
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

    public List<Mangas> getTruyenTranhList() {
        return truyenTranhList;
    }

    public void setTruyenTranhList(List<Mangas> truyenTranhList) {
        this.truyenTranhList = truyenTranhList;
    }
}
