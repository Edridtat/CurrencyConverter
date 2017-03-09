package com.goodelephantlab.currencyconverter.model.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

/**
 * Created by Edridtat on 06.03.2017.
 */
@Root(name = "ValCurs")
public class CurrencyList {

    private List<Currency> list;

    @ElementList(inline=true, name="Valute")
    public List<Currency> getList() {
        return list;
    }

    @ElementList(inline=true, name="Valute")
    public void setList(List<Currency> list) {
        this.list = list;
    }
}
