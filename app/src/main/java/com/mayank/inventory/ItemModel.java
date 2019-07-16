package com.mayank.inventory;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Type;

public class ItemModel implements Comparable<ItemModel>, Parcelable {


    private String name;
    private String SKU;
    private String type;
    private String imageUrl;
    private Long count;
    private Long cost;
    private Long price;


    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public ItemModel(String name, String type, String SKU, String imageUrl, Long cost, Long price, Long count) {
        this.name = name;
        this.SKU = SKU;
        this.type = type;
        this.imageUrl = imageUrl;
        this.count = count;
        this.cost = cost;
        this.price = price;
    }



    protected ItemModel(Parcel in) {
        name = in.readString();
        SKU = in.readString();
        type = in.readString();
        imageUrl = in.readString();
        if (in.readByte() == 0) {
            count = null;
        } else {
            count = in.readLong();
        }
        if(in.readByte()==0){
            price= Long.valueOf(0);
        }
        else {
            price=in.readLong();
        }

        if(in.readByte()==0){
            cost= Long.valueOf(0);
        }
        else {
            cost=in.readLong();
        }
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    public Long getCount() {
        return count;
    }
    public void setCount(Long count) {
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
        count= Long.valueOf(0);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(SKU);
        parcel.writeString(type);
        parcel.writeString(imageUrl);
        if (count == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(count);
        }
        if (price == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(price);
        }
        if (cost == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(cost);
        }

    }
}
