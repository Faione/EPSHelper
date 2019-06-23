package com.example.faione.epshelper;

public class EpsItem {
    private int id;
    private String epsSituation;
    private String epsName;
    private String epsNum;
    private String epsDate;
    private String epsBody;

    public EpsItem() {
        this.epsSituation = "";
        this.epsDate = "";
        this.epsName = "";
        this.epsNum = "";
        this.epsBody = "";
    }

    public EpsItem(String epsSituation,String epsDate,String epsName, String epsNum,String epsBody) {
        this.epsSituation = epsSituation;
        this.epsDate = epsDate;
        this.epsName = epsName;
        this.epsNum = epsNum;
        this.epsBody = epsBody;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getExpressSituation() {
        return epsSituation;
    }
    public void setExpressSituation(String epsSituation) {
        this.epsSituation = epsSituation;
    }

    public String getExpressName() {
        return epsName;
    }
    public void setExpressName(String epsName) {
        this.epsName = epsName;
    }

    public String getExpressNum() {
        return epsNum;
    }
    public void setExpressNum(String epsNum) {
        this.epsNum = epsNum;
    }

    public String getExpressDate() {
        return epsDate;
    }
    public void setExpressDate(String epsDate) {
        this.epsDate = epsDate;
    }

    public String getExpressBody() {
        return epsBody;
    }
    public void setExpressBody(String epsBody) {
        this.epsBody = epsBody;
    }
}

