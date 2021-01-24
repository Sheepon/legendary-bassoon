package com.schoolthing.project2;

public class ItemClass {
    private String item;
    private Float cost;
    private static Float TotalCost = 0f;

    public ItemClass(String item, Float cost){
        this.item = item;
        this.cost = cost;
    }

    public ItemClass(){

    }

    public String getItem(){
        return item;
    }

    public void setItem(String item){
        this.item = item;
    }

    public float getCost(){
        return cost;
    }

    public void setCost(Float cost){
        this.cost = cost;
    }

    public static float getTotalCost(){
        return TotalCost;
    }

    public static void setTotalCost(Float cost){
        TotalCost = cost;
    }

    public static void addTotalCost(Float cost){
        TotalCost = TotalCost + cost;
    }
/*    public void summonTotalCost(){
        TotalCost = TotalCost + cost;
    }*/


}
