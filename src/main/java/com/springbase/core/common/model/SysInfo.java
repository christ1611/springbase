package com.springbase.core.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springbase.core.common.define.EnumDefine;
import com.springbase.core.common.define.EnumDefine.ChnlTypCd;

import com.springbase.core.common.define.EnumDefine.YesNoDvCd;
import com.springbase.core.jpa.dsl.AcomAuthPtcl;
import com.springbase.core.jpa.repository.ModelJPA;
import com.springbase.core.logback.LogbackDefine;
import com.springbase.core.component.OneQBeanUtils;
import com.springbase.core.component.OneQCTX;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.springbase.core.common.define.CoreSystemDefine.DATE_FORMAT;

/**
 * packageName : com.oneqoncore.core.model
 * fileName : SysInfo
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
@ToString
public class SysInfo implements Serializable, Cloneable {

    @Serial
    private static final long serialVersionUID = 8764150825377145608L;

    @JsonIgnore
    private final ModelJPA modelJPA =  OneQBeanUtils.getBean(ModelJPA.class);

    private String instCd;                                                  /* 기관코드  */
    private EnumDefine.EnvrDvCd sysDvCd;                                    /* 시스템구분코드 / Ss Id */

    private String globIdTrsc;                                              /* 글로벌ID 거래내역적재용 */
    private String refNo;                                                   /* REF번호 / Ref_No */
    private Long trscSeqNo;                                                 /* 거래일련번호 / His_No + 1 & Trsc_Seq_No */
    private String subjCd;                                                  /* 과목코드 / Gwam */
    private String procSvcCd;                                               /* 처리서비스코드 */
    private LocalDate trscDt;                                               /* 거래일자 */

    //LOS new

    private String baseCurCd;                                               /* 기준통화코드 */
    private String scrnId;                                                  /* 화면번호 */
    private String globId;                                                  /* 글로벌ID  */
    private LocalDate sysDt;                                                /* 시스템일자  */
    private String sysDtStr;                                                /* 시스템일자  */
    private String userId;                                                  /* 사용자ID  */
    private YesNoDvCd smltnTrscYn;                               /* simulation 여부 */
    private ChnlTypCd chnlTypCd;                                 /* 채널유형코드  */

    private String lnkIndvCanYn = "N";                                      /* 연동개별취소여부 */
    private String dtMarkFrmt;                                              /* 일자표시포맷 */
    private YesNoDvCd drCrVrfcYn = YesNoDvCd.NO;      /* 총계정차대검증여부*/
    private String userIpAddr;                                              /* 사용자IP 주소 */
    private String lnggCd;                                                  /* 사용자등록언어코드 */
    private YesNoDvCd cmglClearYn = YesNoDvCd.YES;    /* cmgl 초기화 */

    /**
     *  @MethodName: initInfo
     *  @Author : nayoseph
     *  @Date : 2024-10-04
     *  @Param :
     *  @Description : SysInfo 초기화
     */
    public void initInfo(Map<String, Object> httpSysInfo) throws Exception
    {
        OneQCTX CTX = OneQCTX.getCTX();



        this.userId = (String) httpSysInfo.get("userId");

        this.baseCurCd = "KRW"; //todo setting value

        if (httpSysInfo.containsKey("globId") && StringUtils.isNotBlank((String) httpSysInfo.get("globId"))) {
            this.sysDt = LocalDate.parse(((String)httpSysInfo.get("globId")).substring(0,8), DateTimeFormatter.ofPattern(DATE_FORMAT));
            this.globId = makeGlobIdAddSeq((String)httpSysInfo.get("globId"));
        } else {
            this.globId = makeGlobId(this.userId);
        }

        //sysDt는 현재 시스템일자로 셋팅
        this.sysDt = LocalDate.now();
        this.sysDtStr = this.sysDt.format(DateTimeFormatter.ofPattern(DATE_FORMAT));



        this.globIdTrsc = this.globId;
        AcomAuthPtcl acomAuthPtcl = modelJPA.findAcomAuthPtcl(this.userId);
        this.lnggCd = acomAuthPtcl.getLnggCd();
        MDC.put(LogbackDefine.MDC_GLOBAL_ID, this.globId);

        log.debug("System httpInput : SysInfo = [{}]", httpSysInfo);
//        getLogInfoTbl();

    }



    /**
     *  @MethodName: initCallService
     *  @Author : nayoseph
     *  @Date : 2024-09-10
     *  @Param : sysInfoMap
     *  @Description : 서비스에서 타 서비스를 호출할 때 SysInfo를 해당 서비스에 맡게 초기화하기 위한 Method.
     *                 callservice 호출된 서비스의 SysInfo를 init하고, 호출 종료 후 원거래로 restore 시 사용.
     */
    public void initCallService(Map<String, Object> paramSysInfo) throws Exception
    {
        // glob_id는 BT에서 32자리로 들어옴, callSeq는 1씩 증가시켜서 채번
        this.globId     = makeGlobIdAddSeq(getGlobId());

        if (paramSysInfo.get("userId") != null)
        {
            this.userId = paramSysInfo.get("userId").toString();
        }

//        getUserInfoTbl("CALL");

        this.scrnId     = paramSysInfo.get("scrnId").toString();

        this.subjCd     = String.format("%2.2s", paramSysInfo.get("scrnId").toString());


        if (paramSysInfo.get("chnlTypCd") != null && StringUtils.isNotBlank((String) paramSysInfo.get("chnlTypCd")))
        {
            this.chnlTypCd = ChnlTypCd.isKindOf((String) paramSysInfo.get("chnlTypCd"));
        }

         if (paramSysInfo.get("refNo")    != null) this.refNo    = paramSysInfo.get("refNo").toString();

        if (paramSysInfo.get("trscSeqNo") != null && StringUtils.isNotEmpty(paramSysInfo.get("trscSeqNo").toString()))
            this.trscSeqNo = (Long) paramSysInfo.get("trscSeqNo");

    }

    /**
     *  @MethodName: makeGlobId
     *  @Author : nayoseph
     *  @Date : 2024-09-10
     *  @Param : REQUEST
     *  @Description : 임시 globId 생성
     *                 Format >>> 20240910REQUEST____F12F9643D7100
     */
    private String makeTempGlobId(String szUserId)
    {
        LocalDateTime sysDtm = LocalDateTime.now();
        String szDateTime = sysDtm.format(DateTimeFormatter.ofPattern("HHmmssSSS")) + String.format("%05d", new Random().nextInt(100000));
        Long nTime = Long.parseLong(szDateTime);

        String szGlobId = String.format("%8s%7.7s%3.3s%12s00", sysDtm.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                , szUserId
                , ""
                , String.format("%12X", nTime));

        return szGlobId.replace(" ", "_");
    }

    /**
     *  @MethodName: makeGlobId
     *  @Author : nayoseph
     *  @Date : 2024-09-10
     *  @Param : userId, chnlTypCd
     *  @Description : Create Global Id
     *                 Format >>> 2024091TESTUSRGLB
     */
    // NOTE: HBS Check. -> 회사마다 룰이 다를거라 일단 HBS로 두기로 함.
    private String makeGlobId(String szUserId)
    {
        String mdcGlobId = makeTempGlobId("REQUEST");
        this.sysDt = LocalDate.parse(mdcGlobId.substring(0, 8), DateTimeFormatter.ofPattern(DATE_FORMAT));
        Long nSeq = Long.parseLong(mdcGlobId.substring(30)) + 1;

        return String.format("%8.8s%7.7s%3.3s%12.12s%02d"
                , mdcGlobId
                , szUserId
                , "GLB"
                , mdcGlobId.substring(18)
                , nSeq
        );
    }

    /**
     *  @MethodName: makeGlobIdAddSeq
     *  @Author : nayoseph
     *  @Date : 2024-09-10
     *  @Param : globId
     *  @Description : globId +1 로 채번
     *                 Format >>> 202409100002426HBS_F12F9643D7102
     */
    private String makeGlobIdAddSeq(String mdcGlobId)
    {
        Long nSeq = Long.parseLong(mdcGlobId.substring(30)) + 1;
        return String.format("%30.30s%02d", mdcGlobId, nSeq);
    }
    private LocalDate findNowBussDt(LocalDate bussDt)
    {
        return  LocalDate.now();
    }
    private LocalDate findNextBussDt(LocalDate bussDt)
    {
        return  LocalDate.now();
    }
    private LocalDate findPrevBussDt(LocalDate bussDt)
    {
        return  LocalDate.now();
    }
    @JsonIgnore
    public Map<String, Object> getOutMap() throws Exception
    {
        Map<String, Object> rsltOutMap = new LinkedHashMap<>();
        rsltOutMap.put("userId"        , this.userId        == null ? "" : this.userId);
        rsltOutMap.put("sysDt"         , this.sysDt         == null ? "" : this.sysDt.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        rsltOutMap.put("scrnId"        , this.scrnId        == null ? "" : this.scrnId);
        rsltOutMap.put("smltnTrscYn"   , this.smltnTrscYn   == null ? "" : this.smltnTrscYn);
        rsltOutMap.put("lnggCd"        , this.lnggCd        == null ? "" : this.lnggCd);
        rsltOutMap.put("chnlTypCd"     , this.chnlTypCd     == null ? "" : this.chnlTypCd.isValue());
         rsltOutMap.put("baseCurCd"     , this.baseCurCd     == null ? "" : this.baseCurCd);
        return rsltOutMap;
    }
    public static String getCtxUserId() {
        return Optional.ofNullable(OneQCTX.getCtxSysInfo()).map(SysInfo::getUserId).orElse("");
    }

    public static void getCtxUserId(String userId) {
        userId = getCtxUserId();
    }



    public static String getCtxGlobId() {
        return Optional.ofNullable(OneQCTX.getCtxSysInfo()).map(SysInfo::getGlobId).orElse("");
    }

    public static void getCtxGlobId(String globId) {
        globId = getCtxGlobId();
    }


    public static LocalDate getCtxSysDt() {
        return Optional.ofNullable(OneQCTX.getCtxSysInfo()).map(SysInfo::getSysDt).orElse(null);
    }



    public static void getCtxSysDt(LocalDate sysDt) {
        sysDt = getCtxSysDt();
    }

    public static String getCtxSysDtStr() {
        return Optional.ofNullable(OneQCTX.getCtxSysInfo()).map(SysInfo::getSysDtStr).orElse("");
    }

    public static void getCtxSysDtStr(String sysDtStr) {
        sysDtStr = getCtxSysDtStr();
    }

    public static LocalDate getCtxTrscDt() {
        return Optional.ofNullable(OneQCTX.getCtxSysInfo()).map(SysInfo::getTrscDt).orElse(null);
    }


    public String getChnlTypCdStr() {
        return this.chnlTypCd == null ? "" : this.chnlTypCd.isValue();
    }

    public static String getCtxChnlTypCdStr() {
        return Optional.ofNullable(OneQCTX.getCtxSysInfo()).map(SysInfo::getChnlTypCd).map(ChnlTypCd::isValue).orElse("");
    }

    public void setSmltnTrscYn(YesNoDvCd smltnTrscYn) {
        this.smltnTrscYn = smltnTrscYn;
    }

    public static YesNoDvCd getCtxSmltnTrscYn() {
        return Optional.ofNullable(OneQCTX.getCtxSysInfo()).map(SysInfo::getSmltnTrscYn).orElse(YesNoDvCd.NO);
    }

    public static void getCtxSmltnTrscYn(YesNoDvCd smltnTrscYn) {
        smltnTrscYn = getCtxSmltnTrscYn();
    }

    public static String getCtxScrnId() {
        return Optional.ofNullable(OneQCTX.getCtxSysInfo()).map(SysInfo::getScrnId).orElse("");
    }

    public static void getCtxScrnId(String scrnId) {
        scrnId = getCtxScrnId();
    }

    public static String getCtxBaseCurCd() {
        return Optional.ofNullable(OneQCTX.getCtxSysInfo()).map(SysInfo::getBaseCurCd).orElse("");
    }

    public static void getCtxBaseCurCd(String baseCurCd) {
        baseCurCd = getCtxBaseCurCd();
    }

    public static String getCtxLnggCd() {
        return Optional.ofNullable(OneQCTX.getCtxSysInfo()).map(SysInfo::getLnggCd).orElse("");
    }

    public static void getCtxLnggCd(String lnggCd) {
        lnggCd = getCtxLnggCd();
    }

    public void setLnggCd(String lnggCd) {
        this.lnggCd = lnggCd;
    }

}
