package com.springbase.core.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springbase.core.component.OneQCTX;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * packageName : com.oneqoncore.core.model
 * fileName : BizDayInfo
 * author : nayoseph
 * date : 2024-08-26
 * description :
 * ===========================================================
 * DATE           AUTHOR       NOTE
 * -----------------------------------------------------------
 * 2024-08-26     nayoseph      New
 * 2024-09-09     nayoseph      modify variable
 */

@Slf4j
@Getter
public class DayInfo implements Serializable, Cloneable {

    private static final long serialVersionUID = 4172639644674509481L;



    private LocalDate dd3BfBussDt     ;    /* 3일전영업일자  */
    private LocalDate dd2BfBussDt     ;    /* 2일전영업일자  */
    private LocalDate dd1BfBussDt     ;    /* 1일전영업일자  */
    private LocalDate bussDt           ;    /* 현재영업일자  */
    private LocalDate dd1AfBussDt     ;    /* 1일익영업일자  */
    private LocalDate dd2AfBussDt     ;    /* 2일익영업일자  */
    private LocalDate dd3AfBussDt     ;    /* 3일익영업일자  */


    /**
     *  @MethodName: initInfo
     *  @Date : 2024-10-04
     *  @Param :
     *  @Description : BizDayInfo 초기화
     */
    public void initInfo(LocalDate trscDt) throws Exception {

        setDate(trscDt);

    }

    private void setDate(LocalDate prcBascDt) throws Exception {

        this.bussDt = prcBascDt;
        this.dd1AfBussDt = prcBascDt.plusDays(1L);
        this.dd2AfBussDt = prcBascDt.plusDays(2L);
        this.dd3AfBussDt = prcBascDt.plusDays(3L);
        this.dd1BfBussDt = prcBascDt.minusDays(1L);
        this.dd2BfBussDt = prcBascDt.minusDays(2L);
        this.dd3BfBussDt = prcBascDt.minusDays(3L);
    }



    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public static LocalDate getCtxDd3BfBussDt() throws Exception {
        return OneQCTX.getCtxBizDayInfo().getDd3BfBussDt();
    }

    public static void getCtxDd3BfBussDt(LocalDate dd3BfBussDt) throws Exception {
        dd3BfBussDt = getCtxDd3BfBussDt();
    }

    public static LocalDate getCtxDd2BfBussDt() throws Exception {
        return OneQCTX.getCtxBizDayInfo().getDd2BfBussDt();
    }

    public static void getCtxDd2BfBussDt(LocalDate dd2BfBussDt) throws Exception {
        dd2BfBussDt = getCtxDd2BfBussDt();
    }

    public static LocalDate getCtxDd1BfBussDt() throws Exception {
        return OneQCTX.getCtxBizDayInfo().getDd1BfBussDt();
    }

    public static void getCtxDd1BfBussDt(LocalDate dd1BfBussDt) throws Exception {
        dd1BfBussDt = getCtxDd1BfBussDt();
    }

    public static LocalDate getCtxBussDt() throws Exception {
        return OneQCTX.getCtxBizDayInfo().getBussDt();
    }

    public static void getCtxBussDt(LocalDate bussDt) throws Exception {
        bussDt = getCtxBussDt();
    }

    public static LocalDate getCtxDd3AfBussDt() throws Exception {
        return OneQCTX.getCtxBizDayInfo().getDd3AfBussDt();
    }

    public static void getCtxDd3AfBussDt(LocalDate dd3AfBussDt) throws Exception {
        dd3AfBussDt = getCtxDd3AfBussDt();
    }

    public static LocalDate getCtxDd2AfBussDt() throws Exception {
        return OneQCTX.getCtxBizDayInfo().getDd2AfBussDt();
    }

    public static void getCtxDd2AfBussDt(LocalDate dd2AfBussDt) throws Exception {
        dd2AfBussDt = getCtxDd2AfBussDt();
    }

    public static LocalDate getCtxDd1AfBussDt() throws Exception {
        return OneQCTX.getCtxBizDayInfo().getDd1AfBussDt();
    }

    public static void getCtxDd1AfBussDt(LocalDate dd1AfBussDt) throws Exception {
        dd1AfBussDt = getCtxDd1AfBussDt();
    }


    public void setCtxBussDt(LocalDate bussDt) {
        this.bussDt = bussDt;
    }

    public void setCtxDd1BfBussDt(LocalDate dd1BfBussDt) {
        this.dd1BfBussDt = dd1BfBussDt;
    }

    public void setCtxDd2BfBussDt(LocalDate dd2BfBussDt) {
        this.dd2BfBussDt = dd2BfBussDt;
    }

    public void setCtxDd3BfBussDt(LocalDate dd3BfBussDt) {
        this.dd3BfBussDt = dd3BfBussDt;
    }

    public void setCtxDd1AfBussDt(LocalDate dd1AfBussDt) {
        this.dd1AfBussDt = dd1AfBussDt;
    }

    public void setCtxDd2AfBussDt(LocalDate dd2AfBussDt) {
        this.dd2AfBussDt = dd2AfBussDt;
    }

    public void setCtxDd3AfBussDt(LocalDate dd3AfBussDt) {
        this.dd3AfBussDt = dd3AfBussDt;
    }

    @JsonIgnore
    public Map<String, Object> getOutMap() throws Exception
    {
        Map<String, Object> rsltOutMap = new LinkedHashMap<>();
        rsltOutMap.put("bussDt"        , this.bussDt        == null ? "" : this.bussDt);
        rsltOutMap.put("dd1AfBussDt"   , this.dd1AfBussDt        == null ? "" : this.dd1AfBussDt);
        rsltOutMap.put("dd2AfBussDt"   , this.dd2AfBussDt        == null ? "" : this.dd2AfBussDt);
        rsltOutMap.put("dd3AfBussDt"   , this.dd3AfBussDt        == null ? "" : this.dd3AfBussDt);
        rsltOutMap.put("dd1BfBussDt"   , this.dd1BfBussDt        == null ? "" : this.dd1BfBussDt);
        rsltOutMap.put("dd2BfBussDt"   , this.dd2BfBussDt        == null ? "" : this.dd2BfBussDt);
        rsltOutMap.put("dd3BfBussDt"   , this.dd3BfBussDt        == null ? "" : this.dd3BfBussDt);

        return rsltOutMap;
    }
}
