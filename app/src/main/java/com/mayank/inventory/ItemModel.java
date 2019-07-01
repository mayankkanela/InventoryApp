package com.mayank.inventory;

import java.lang.reflect.Type;

public class ItemModel implements Comparable<ItemModel>{
    String name;
    String SKU;
    String type;
    public ItemModel(String name, String type, String SKU) {
        this.SKU = SKU;
        this.name = name;
        this.type = type;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSKU() {
        return SKU;
    }
    public void setSKU(String SKU) {
        this.SKU = SKU;
    }
    @Override
    public int compareTo(ItemModel itemModel) {
        return (itemModel.getSKU().compareTo(this.SKU));
    }



    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
