package com.niit.onlivestream.vo.SpectatorRequest;

import lombok.Data;

import java.io.Serializable;


@Data
public class FunRequest implements Serializable {

    private String uuid;

    private String type;
}
