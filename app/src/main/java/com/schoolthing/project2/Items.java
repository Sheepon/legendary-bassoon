package com.schoolthing.project2;

public class Items {
    public Float fCost;
    public String strItem;

    public Items(){

    }

    public Items(Float fCost,String strItem){

        this.fCost = fCost;
        this.strItem = strItem;
    }

    public Items(Float fCost){
        this.fCost = fCost;
    }

    public String getItemName(){
        return strItem;
    }

    public Float getCost(){
        return  fCost;
    }
}
