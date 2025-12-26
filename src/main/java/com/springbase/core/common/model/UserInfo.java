package com.springbase.core.common.model;

import com.springbase.core.component.OneQCTX;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Optional;

/**
 * packageName : com.oneqoncore.core.model
 * fileName : CustInfo
 * author : nayoseph
 * date : 2024-08-27
 * description :
 * ===========================================================
 * DATE           AUTHOR       NOTE
 * -----------------------------------------------------------
 * 2024-08-27     nayoseph      New
 * 2024-09-09     nayoseph      modify variable
 */


@Getter @Slf4j
public class UserInfo implements Serializable, Cloneable
{
    private static final long serialVersionUID =  9128966808770087502L;
    private String userId;    /* 고객번호  */



    private static UserInfo getCustInfo() throws Exception {
        return null;
//        return Optional.ofNullable(OneQCTX.getCtxCustInfo())
//                .orElseThrow(() -> new OneQSystemException(CoreErrCode.GET_SYS_INFO_ERROR));
    }
    public void initInfo(OneQCTX ctx) throws Exception
    {
        this.userId = getCtxUserId();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public static String getCtxUserId() {
        return Optional.ofNullable(OneQCTX.getCtxCustInfo()).map(UserInfo::getUserId).orElse("");
    }

    public static void getCtxUserId(String userId) {
        userId = getCtxUserId();
    }
}