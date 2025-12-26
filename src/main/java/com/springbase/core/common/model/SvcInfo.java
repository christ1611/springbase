package com.springbase.core.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springbase.core.common.define.EnumDefine;
import com.springbase.core.component.OneQBeanUtils;
import com.springbase.core.jpa.repository.ModelJPA;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * packageName : com.oneqoncore.core.model
 * fileName : SvcInfo
 * author : nayoseph
 * date : 2024-08-26
 * description :
 * ===========================================================
 * DATE           AUTHOR       NOTE
 * -----------------------------------------------------------
 * 2024-08-26     nayoseph      New
 * 2024-09-09     nayoseph      modify variable
 */

@Slf4j @Getter
public class SvcInfo implements Serializable, Cloneable
{
    private static final long serialVersionUID = -7887613244025324086L;
    @JsonIgnore
    private final ModelJPA modelJPA = (ModelJPA) OneQBeanUtils.getBean(ModelJPA.class);

    private String                  svcCd               ;    /* 서비스코드  */
    private String                  useYn               ;    /* 사용여부  */
    private String                  svcNm               ;    /* 서비스명  */
    private String                  subjCd              ;    /* 과목코드  */
    private String                  trscKindCd          ;    /* 거래종류코드  */
    private String                  expProcSvcNm        ;    /* 예외처리서비스명  */
    private String                  bizLogDvCd          ;    /* 업무로그구분코드  */
    private String                  ledgRdDvCd          ;    /* 원장읽기구분코드  */
    private String                  custInfoRdDvCd      ;    /* 고객정보읽기구분코드  */
    private String                  prdtInfoRdDvCd      ;    /* 상품정보읽기구분코드  */
    private EnumDefine.YesNoDvCd    inpTmsgImgSaveYn    ;    /* 입력전문이미지저장여부  */
    private EnumDefine.YesNoDvCd    prtTmsgImgSaveYn    ;    /* 출력전문이미지저장여부  */
    private EnumDefine.YesNoDvCd    hldTrscPossYn       ;    /* 휴일거래가능여부  */
    private EnumDefine.YesNoDvCd    closAfTrscPossYn    ;    /* 마감후거래가능여부  */
    private EnumDefine.YesNoDvCd    rcknDdTrscPossYn    ;    /* 기산일거래가능여부  */
    private EnumDefine.YesNoDvCd    bfBizTrscPossYn     ;    /* 전영업일거래가능여부  */
    private EnumDefine.YesNoDvCd    lnkTrscPossYn       ;    /* 연동거래가능여부  */
    private EnumDefine.YesNoDvCd    brTrscPossYn        ;    /* 지점간거래가능여부  */
    private EnumDefine.YesNoDvCd    hldTrmTrscPossYn    ;    /* 휴일단말외거래가능여부  */
    private EnumDefine.YesNoDvCd    extsTrscPossYn      ;    /* 연장거래가능여부  */
    private EnumDefine.YesNoDvCd    acRflcPossYn        ;    /* 계정반영가능여부  */
    private EnumDefine.YesNoDvCd    acClosPossYn        ;    /* 계정마감후가능여부  */
    private EnumDefine.YesNoDvCd    hldAcRflcTrscPossYn ;    /* 휴일계정반영거래가능여부  */
    private EnumDefine.YesNoDvCd    orgTrscRstoYn       ;    /* 원거래복원여부  */
    private EnumDefine.YesNoDvCd    otsdSyncCaloYn      ;    /* 대외거래동기호출여부  */
    private String                  acTrscKindDvCd      ;    /* 계정거래종류구분코드  */
    private EnumDefine.YesNoDvCd    nrstdCustTrscPossYn ;    /* 미등록고객거래가능여부  */
    private Long                    toutDrtm            ;    /* 타임아웃시간  */
    private EnumDefine.YesNoDvCd    scrnMsgPrtYn        ;    /* 화면메시지출력여부  */
    private EnumDefine.YesNoDvCd    rsprApvAlwnYn       ;    /* 화면메시지출력여부  */
    private EnumDefine.YesNoDvCd    vrtlAcctInqYn       ;    /* 가상계좌조회여부  */
    private EnumDefine.YesNoDvCd    normTrscPossYn      ;    /* 정상거래가능여부  */
    private String                  svrNm               ;    /* 서버명  */


    /* NOTE
    *
    * REPLACED
    * prnTmsgImgSaveYn -> prtTmsgImgSaveYn
    * bfBussTrscPossYn -> bfBizTrscPossYn
    * gearTrscPossYn -> lnkTrscPossYn
    * hldTmlTrscPossYn -> hldTrmTrscPossYn
    * ognTrscRstoYn -> orgTrscRstoYn
    * scrnMsgPrnYn -> scrnMsgPrtYn
    * serverNm -> svrNm
    *
    * */

    /**
     *  @MethodName: initInfo
     *  @Author : nayoseph
     *  @Date : 2024-10-04
     *  @Param :
     *  @Description : SvcInfo 초기화
     */
    public void initInfo( String svrNm, String svcCd)  throws Exception
    {
        String szEnumVlu;

        if(svcCd.equals("LOGINSVC")
                || svcCd.equals("FORGETSVC")
                || svcCd.equals("CHGPWDSVC")
                || svcCd.equals("LOGOUTSVC")
                || svcCd.equals("RESETREDISSVC"))
            svrNm = "HANABANK"; // TODO svcNm 변경

//        CoreAcomSvcInfo coreAcomSvcInfo = modelJPA.getSvcInfo(svrNm, svcCd);
//
//        if (coreAcomSvcInfo == null)
//        {
//            throw new OneQSystemException(CoreErrCode.SERVICE_NOT_FOUND, "ACOM_SVC_INFO [%s]", svcCd);
//        }
//
//        this.svcCd                = coreAcomSvcInfo.getSvcCd();                          // 서비스코드
//        this.svcNm                = coreAcomSvcInfo.getSvcNm();                          // 서비스명
//        this.subjCd               = coreAcomSvcInfo.getSubjCd();                         // 과목코드
//        this.trscKindCd           = coreAcomSvcInfo.getTrscKindCd();                     // CHO010
//        this.expProcSvcNm         = coreAcomSvcInfo.getExpProcSvcNm();                   // 예외처리서비스명
//        this.bizLogDvCd           = coreAcomSvcInfo.getBizLogDvCd();                     // 업무로그구분코드
//        this.ledgRdDvCd           = coreAcomSvcInfo.getLedgRdDvCd();                     // 원장읽기구분코드
//        this.custInfoRdDvCd       = coreAcomSvcInfo.getCustInfoRdDvCd();                 // 고객정보읽기구분코드
//        this.prdtInfoRdDvCd       = coreAcomSvcInfo.getPrdtInfoRdDvCd();                 // 상품정보읽기구분코드
//        szEnumVlu                 = coreAcomSvcInfo.getInpTmsgImgSaveYn();
//        this.inpTmsgImgSaveYn     = EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);        // 입력전문이미지저장여부
//        szEnumVlu                 = coreAcomSvcInfo.getPrnTmsgImgSaveYn();
//        this.prtTmsgImgSaveYn     = EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);        // 출력전문이미지저장여부
//        szEnumVlu                 = coreAcomSvcInfo.getHldTrscPossYn();
//        this.hldTrscPossYn        =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);       // 휴일거래가능여부
//        szEnumVlu                 = coreAcomSvcInfo.getClosAfTrscPossYn();
//        this.closAfTrscPossYn     =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);       // 마감후거래가능여부
//        szEnumVlu                 = coreAcomSvcInfo.getRcknDdTrscPossYn();
//        this.rcknDdTrscPossYn     =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);       // 기산일거래가능여부
//        szEnumVlu                 = coreAcomSvcInfo.getBfBussDdTrscPossYn();
//        this.bfBizTrscPossYn     =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);        // 전영업일거래가능여부
//        szEnumVlu                 = coreAcomSvcInfo.getSvcNm();
//        this.lnkTrscPossYn       =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);        // 연동거래가능여부
//        szEnumVlu                 = coreAcomSvcInfo.getBrTrscPossYn();
//        this.brTrscPossYn         =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);       // 지점간거래가능여부
//        szEnumVlu                 = coreAcomSvcInfo.getHldTmlTrscPossYn();
//        this.hldTrmTrscPossYn     =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);       // 휴일단말외거래가능여부
//        szEnumVlu                 = coreAcomSvcInfo.getExtsTrscPossYn();
//        this.extsTrscPossYn       =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);       // 연장거래가능여부
//        szEnumVlu                 = coreAcomSvcInfo.getAcRflcPossYn();
//        this.acRflcPossYn         =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);       // 계정반영가능여부
//        szEnumVlu                 = coreAcomSvcInfo.getAcClosAfPossYn();
//        this.acClosPossYn         =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);       // 계정마감후가능여부
//        szEnumVlu                 = coreAcomSvcInfo.getHldAcRflcTrscPossYn();
//        this.hldAcRflcTrscPossYn  =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);       // 휴일계정반영거래가능여부
//        szEnumVlu                 = coreAcomSvcInfo.getOgnTrscRstoYn();
//        this.orgTrscRstoYn        =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);       // 원거래복원여부
//        szEnumVlu                 = coreAcomSvcInfo.getOtsdSyncCaloYn();
//        this.otsdSyncCaloYn       =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);       // 대외거래동기호출여부
//        this.acTrscKindDvCd       = coreAcomSvcInfo.getAcTrscKindDvCd();                 // 계정거래종류구분코드
//        szEnumVlu                 = coreAcomSvcInfo.getNrstdCustTrscPossYn();
//        this.nrstdCustTrscPossYn  =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);       // 미등록고객거래가능여부
//        this.toutDrtm             = coreAcomSvcInfo.getToutTm();                         // 타임아웃시간
//        szEnumVlu                 = coreAcomSvcInfo.getScrnMsgPrnYn();
//        this.scrnMsgPrtYn         =  EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);       // 화면메시지출력여부
//        szEnumVlu                 = coreAcomSvcInfo.getRsprApvAlwnYn();
//        this.rsprApvAlwnYn        = EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);        // 책임자승인허용여부
//        szEnumVlu                 = coreAcomSvcInfo.getVrtlAcctInqYn();
//        this.vrtlAcctInqYn        = EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);        // 채임자승인허용여부
//        this.svrNm             = coreAcomSvcInfo.getServerNm();                          // 서버이름(linebank or hanabank) TODO
//        szEnumVlu                 = coreAcomSvcInfo.getNormTrscPossYn();
//        this.normTrscPossYn       = EnumDefine.YesNoDvCd.isKindOf(szEnumVlu);        // 정상거래가능여부
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

//    public static Map<String, Object> getCtxOutMap() throws Exception {
//        return OneQCTX.getCtxSvcInfo().getOutMap();
//    }
//
//    @JsonIgnore
//    public Map<String, Object> getOutMap() throws Exception
//    {
//        String toJson = MapperUtil.printJson(getSvcInfo());
//        Map<String, Object> outMap = MapperUtil.getObjectMapper().readValue(toJson, HashMap.class);
//
//        return outMap;
//    }
//
//    private static SvcInfo getSvcInfo() throws Exception {
//        return Optional.ofNullable(OneQCTX.getCtxSvcInfo())
//                .orElseThrow(() -> new OneQSystemException(CoreErrCode.GET_SYS_INFO_ERROR));
//    }
}
