package com.sgl.mywallet;

public class Currency {

    private int id, image, format;
    private String coinAcronym, coinFullName, coinValue;

    public Currency(int id, int image, int format, String coinAcronym, String coinFullName,
                    String coinValue) {
        this.id = id;
        this.image = image;
        this.coinAcronym = coinAcronym;
        this.coinFullName = coinFullName;
        this.coinValue = coinValue;
        this.format = format;
    }

    public int getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public int getFormat() {
        return format;
    }

    public String getCoinAcronym() {
        return coinAcronym;
    }

    public String getCoinFullName() {
        return coinFullName;
    }

    public String getCoinValue() {
        return coinValue;
    }
}
