package com.mayank.inventory;

import java.lang.reflect.Type;

public class ItemModel implements Comparable<ItemModel>{
    private String name;
    private String SKU;
    private String type;
    private String imageUrl;
    private Integer count;


    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ItemModel(String name, String type, String SKU, String imageUrl) {
        this.SKU = SKU;
        this.name = name;
        this.type = type;
        this.imageUrl = imageUrl;
        count=0;
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
    public int compareTo(ItemModel itemModel) { return (itemModel.getSKU().compareTo(this.SKU)); }



    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
