package com.example.mangaplusapp.object;

public class Mangas {
    private String NAME_MANGA, ID_MANGA, DESCRIPTION_MANGA, PICTURE_MANGA, CATEGORY_MANGA, ID_CATEGORY_MANGA, BOUGHT_MANGA, PRICE_MANGA;
    private boolean PREMIUM_MANGA;
    private long VIEW_MANGA;
    //empty for firebase
    public Mangas() {
    }
    public Mangas(String NAME_MANGA, String PICTURE_MANGA) {
        this.NAME_MANGA = NAME_MANGA;
        this.PICTURE_MANGA = PICTURE_MANGA;
    }

    public boolean isPREMIUM_MANGA() {
        return PREMIUM_MANGA;
    }

    public void setPREMIUM_MANGA(boolean PREMIUM_MANGA) {
        this.PREMIUM_MANGA = PREMIUM_MANGA;
    }

    public String getNAME_MANGA() {
        return NAME_MANGA;
    }

    public void setNAME_MANGA(String NAME_MANGA) {
        this.NAME_MANGA = NAME_MANGA;
    }

    public String getID_MANGA() {
        return ID_MANGA;
    }

    public void setID_MANGA(String ID_MANGA) {
        this.ID_MANGA = ID_MANGA;
    }

    public String getDESCRIPTION_MANGA() {
        return DESCRIPTION_MANGA;
    }

    public void setDESCRIPTION_MANGA(String DESCRIPTION_MANGA) {
        this.DESCRIPTION_MANGA = DESCRIPTION_MANGA;
    }

    public String getPICTURE_MANGA() {
        return PICTURE_MANGA;
    }

    public void setPICTURE_MANGA(String PICTURE_MANGA) {
        this.PICTURE_MANGA = PICTURE_MANGA;
    }

    public String getCATEGORY_MANGA() {
        return CATEGORY_MANGA;
    }

    public void setCATEGORY_MANGA(String CATEGORY_MANGA) {
        this.CATEGORY_MANGA = CATEGORY_MANGA;
    }

    public String getID_CATEGORY_MANGA() {
        return ID_CATEGORY_MANGA;
    }

    public void setID_CATEGORY_MANGA(String ID_CATEGORY_MANGA) {
        this.ID_CATEGORY_MANGA = ID_CATEGORY_MANGA;
    }

    public long getVIEW_MANGA() {
        return VIEW_MANGA;
    }

    public void setVIEW_MANGA(long VIEW_MANGA) {
        this.VIEW_MANGA = VIEW_MANGA;
    }

    public String getBOUGHT_MANGA() {
        return BOUGHT_MANGA;
    }

    public void setBOUGHT_MANGA(String BOUGHT_MANGA) {
        this.BOUGHT_MANGA = BOUGHT_MANGA;
    }

    public String getPRICE_MANGA() {
        return PRICE_MANGA;
    }

    public void setPRICE_MANGA(String PRICE_MANGA) {
        this.PRICE_MANGA = PRICE_MANGA;
    }
}
