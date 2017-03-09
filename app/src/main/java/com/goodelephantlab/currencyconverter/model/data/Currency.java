package com.goodelephantlab.currencyconverter.model.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Edridtat on 06.03.2017.
 */
@Root(name = "Valute")
public class Currency {

    private String id;
    private int numCode;
    private String charCode;
    private int nominal;
    private String name;
    private String value;
    private String flagImageName;

    public Currency(){}

    public Currency(String id, int numCode, String charCode,
                            String name, int nominal, String value) {
        this.value = value;
        this.name = name;
        this.nominal = nominal;
        this.charCode = charCode;
        this.numCode = numCode;
        this.id = id;
        this.flagImageName = charCode.toLowerCase().concat("_");
    }

    @Attribute(name = "ID")
    public String getId() {
        return id;
    }

    @Attribute(name = "ID")
    public void setId(String id) {
        this.id = id;
    }

    @Element(name = "NumCode")
    public int getNumCode() {
        return numCode;
    }

    @Element(name = "NumCode")
    public void setNumCode(int numCode) {
        this.numCode = numCode;
    }

    @Element(name = "CharCode")
    public String getCharCode() {
        return charCode;
    }

    @Element(name = "CharCode")
    public void setCharCode(String charCode) {
        this.charCode = charCode;
        this.flagImageName = charCode.toLowerCase().concat("_");
    }

    @Element(name = "Nominal")
    public int getNominal() {
        return nominal;
    }

    @Element(name = "Nominal")
    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    @Element(name = "Name")
    public String getName() {
        return name;
    }

    @Element(name = "Name")
    public void setName(String name) {
        this.name = name;
    }

    @Element(name = "Value")
    public String getValue() {
        return value;
    }

    @Element(name = "Value")
    public void setValue(String value) {
        this.value = value.replace(',', '.');
    }

    public String getFlagImageName() {
        return flagImageName;
    }
}
