package com.example.qr;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.LinkedHashMap;

public class Asset implements Parcelable {
    private String idInventory; //Номер устройства
    private String nameInventory; // Название
    private String typeInventory; // Тип инвентаря
    private String assetNumber; //Инвентарный номер
    private String factoryNumber;
    private String dateOfAcceptance;
    private String cost;
    private String characteristic;
    private String simpleNameKafedra;
    private String nameKafedra;
    private String numberClass;
    private String typeClass;
    private String state;
    private String problem;

    protected Asset(Parcel in) {
        idInventory = in.readString();
        nameInventory = in.readString();
        typeInventory = in.readString();
        characteristic = in.readString();
        assetNumber = in.readString();
        factoryNumber= in.readString();
        dateOfAcceptance= in.readString();
        cost= in.readString();
        simpleNameKafedra  = in.readString();
        nameKafedra  = in.readString();
        numberClass= in.readString();
        typeClass= in.readString();
        state= in.readString();
        problem = in.readString();
    }

    public static final Creator<Asset> CREATOR = new Creator<Asset>() {
        @Override
        public Asset createFromParcel(Parcel in) {
            return new Asset(in);
        }

        @Override
        public Asset[] newArray(int size) {
            return new Asset[size];
        }
    };

    public String getId() {
        return idInventory;
    }
    public void setId(String id) {
        this.idInventory = idInventory;
    }

    public String getName() {
        return nameInventory;
    }
    public void setName(String name) {
        this.nameInventory = nameInventory;
    }

    public String getTypeInventory() {
        return typeInventory;
    }
    public void setTypeInventory(String nameTypeInventory) {
        this.typeInventory = typeInventory;
    }

    public String getAssetNumber() {
        return assetNumber;
    }
    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getFactoryNumber() {
        return factoryNumber;
    }
    public void setFactoryNumber(String factory_number) {
        this.factoryNumber = factoryNumber;
    }

    public String getDateOfAcceptance() {
        return dateOfAcceptance;
    }
    public void setDateOfAcceptance(String date_of_acceptance) {
        this.dateOfAcceptance = dateOfAcceptance;
    }

    public String getCost() {
        return cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getNumberClass() {
        return numberClass;
    }
    public void setNumberClass(String numberClass) {
        this.numberClass = numberClass;
    }

    public String getTypeClass() {
        return typeClass;
    }
    public void setTypeClass(String nameTypeClass) {
        this.typeClass = typeClass;
    }

    public String getKafedra() {
        return nameKafedra;
    }
    public void setKafedra(String nameKafedra) {
        this.nameKafedra = nameKafedra;
    }

    public String getState() {
        switch(state){
            case "0": return "Не зарегистрирован";
            case "1": return "Зарегистрирован, складирован";
            case "2": return "В активном состоянии";
            case "3": return "В процессе передачи на другую кафедру";
            case "4": return "В пользовании сотрудника";
            case "5": return "Выведен из эксплуатации";
        }

        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getStateN() {return state;}

    public String getProblem() {
        return problem;
    }
    public void setProblem(String problem) {
        this.problem = problem;
    }

    public LinkedHashMap<String,String> listCharacteristicCreator(boolean admin){
        LinkedHashMap <String,String> characteristics = new LinkedHashMap<>();
        characteristics.put("ID актива",idInventory);
        characteristics.put("Тип",typeInventory);
        if(admin) {
            characteristics.put("Состояние",getState());
            characteristics.put("Инвентарный номер", assetNumber);
            characteristics.put("Заводской номер", factoryNumber);
            characteristics.put("Дата принятия", dateOfAcceptance);
            characteristics.put("Цена", cost);
        }
        characteristics.put("Характеристика",characteristic);
        if(simpleNameKafedra!=null || nameKafedra != null)
            characteristics.put("Название кафедры",nameKafedra);
        if(numberClass!=null || typeClass != null)
            characteristics.put("Номер класса",numberClass + " (" + typeClass + ")");
        characteristics.put("Поломка",problem);

        return characteristics;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idInventory);
        parcel.writeString(nameInventory);
        parcel.writeString(typeInventory);
        parcel.writeString(characteristic);
        parcel.writeString(assetNumber);
        parcel.writeString(factoryNumber);
        parcel.writeString(dateOfAcceptance);
        parcel.writeString(cost);
        parcel.writeString(simpleNameKafedra);
        parcel.writeString(nameKafedra);
        parcel.writeString(numberClass);
        parcel.writeString(typeClass);
        parcel.writeString(state);
        parcel.writeString(problem);
    }
}