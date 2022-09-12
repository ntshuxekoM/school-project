package com.phishing.app.payload;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardData implements Serializable {

    private long total;
    private int subTotal;
    private String  subTotalTitle;

}
