package com.niit.onlivestream.vo.SpectatorRequest;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;


@Data
public class FunResponse implements Serializable {


    private Integer number;

    private String type;

    private ArrayList<FunInfo> funInfoList;

}
