package com.example.qr;

import android.os.Parcel;
import android.os.Parcelable;

public class Inventory implements Parcelable {
    private String id; //Номер устройства
    private String name; // Наименование устройства
    private String serial;
    private String description; // Описание
    private String place; // Местоположение
    private String lastRepair; // Последняя дата проверки
    private String nameBreaking; // Описание поломки
    private String state; // Состояние = не установлен/установлен

    protected Inventory(Parcel in) {
        id = in.readString();
        name = in.readString();
        place = in.readString();
        description = in.readString();
        lastRepair = in.readString();
        nameBreaking = in.readString();
        state = in.readString();
    }

    public static final Parcelable.Creator<Asset> CREATOR = new Parcelable.Creator<Asset>() {
        @Override
        public Asset createFromParcel(Parcel in) {
            return new Asset(in);
        }

        @Override
        public Asset[] newArray(int size) {
            return new Asset[size];
        }
    };

    public String getNumber() {
        return id;
    }
    public void setNumber(String number) {
        this.id = number;
    }

    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastRepair() {
        return lastRepair;
    }
    public void setLastRepair(String lastRepair) {
        this.place = lastRepair;
    }

    public String getNameBreaking() {
        return nameBreaking;
    }
    public void setNameBreaking(String nameBreaking) {
        this.name = nameBreaking;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(place);
        parcel.writeString(description);
        parcel.writeString(lastRepair);
        parcel.writeString(nameBreaking);
        parcel.writeString(state);
    }
}
