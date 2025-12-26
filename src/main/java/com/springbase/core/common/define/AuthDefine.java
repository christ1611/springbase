package com.springbase.core.common.define;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class AuthDefine {

    @Getter
    @AllArgsConstructor
    public enum AuthStCd {
        PASSWORD_CORRECT("00"),
        INVALID_PASSWORD("01"),
        INVALID_PASSWORD_RULE("02"),
        ALREADY_USE_BEFORE("03");

        public final String value;
    }

    @Getter
    public enum LedgStCd {
        ACTIVE("00"),
        INACTIVE("01");

        public final String value;

        LedgStCd(String value) {
            this.value = value;
        }

        public String isValue() {
            return this.value;
        }

        public static boolean isExist(String value) {
            for (AuthDefine.LedgStCd eItm : AuthDefine.LedgStCd.values()) {
                if (eItm.value.equals(value)) return true;
            }
            return false;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum Role {
        ADMIN("1"),
        USER("2");

        public final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum Result {
        SUCCESS,
        FAIL;
    }
}