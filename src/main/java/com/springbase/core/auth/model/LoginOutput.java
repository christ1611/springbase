package com.springbase.core.auth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Getter
@Slf4j
public class LoginOutput  implements Serializable, Cloneable{
    private String result  ="ok";

    @Builder
    public void Result() {
        this.result = "ok";
    }
}