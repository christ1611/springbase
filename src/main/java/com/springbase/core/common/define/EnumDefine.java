package com.springbase.core.common.define;


public class EnumDefine
{
    public enum ExhgRtCd {
        BASIC_RATE,          // Basic Rate
        CASH_BUY,            // Cash buy rate
        CASH_SELL,           // Cash sell rate
        TT_BUY,              // TTB rate
        TT_SELL,             // TTS rate
        POSIT_BUY,
        POSIT_SELL,
        POSITION            // Position rate
    }


    public enum YesNoDvCd {
        YES("Y"),          // YES
        NO("N");          // NO

        private String vlu;

        YesNoDvCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (YesNoDvCd eItm : YesNoDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static YesNoDvCd isKindOf(String vlu) {
            for (YesNoDvCd eItm : YesNoDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }

    }


    public enum OnshOfShDvCd {

        ON_SHOW_TRSC("0"),          // On Show Transaction
        OFF_SHOW_TRSC("1");        // Off Show Transaction

        private String vlu;

        OnshOfShDvCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (OnshOfShDvCd eItm : OnshOfShDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static OnshOfShDvCd isKindOf(String vlu) {
            for (OnshOfShDvCd eItm : OnshOfShDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }


    public enum ExrtCalcMthdCd {
        METHOD_MULTI("M"),
        METHOD_DIVIDE("D");

        private String vlu;

        ExrtCalcMthdCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (ExrtCalcMthdCd eItm : ExrtCalcMthdCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static ExrtCalcMthdCd isKindOf(String vlu) {
            for (ExrtCalcMthdCd eItm : ExrtCalcMthdCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }


    public enum DtDvCd {
        BUSINESS_DAY("1"),
        LEGAL_HOLIDAY("2"),
        INST_HOLIDAY("3"),
        WEEK_HOLIDAY("4"),
        TEMP_HOLIDAY("5");

        private String vlu;

        DtDvCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (DtDvCd eItm : DtDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static boolean isBizDay(String vlu) {
            return BUSINESS_DAY == isKindOf(vlu) ? true : false;
        }


        public static boolean isHoliDay(String vlu) {
            return isBizDay(vlu);
        }

        public static DtDvCd isKindOf(String vlu) {
            for (DtDvCd eItm : DtDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }

    }


    public enum RoundCalcMthdCd {
        CALC_CEIL("U"),
        CALC_ROUND("R"),
        CALC_TRUNC("D");

        private String vlu;


        RoundCalcMthdCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (RoundCalcMthdCd eItm : RoundCalcMthdCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static RoundCalcMthdCd isKindOf(String vlu) {
            for (RoundCalcMthdCd eItm : RoundCalcMthdCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }


    public enum ChnlTypCd {
        CHNL_SWG("0"),            //  Simulator(Swagger)
        CHNL_ATM("A"),             // ATM
        CHNL_IBS("T"),             // Internet Banking
        CHNL_MOBILE("M"),          // Mobile Banking
        CHNL_PHONE("R"),           // Phone Banking
        CHNL_EDC("E"),             // EDC
        CHNL_FIRM("F"),            // Firm Banking
        CHNL_CORPORATE("C"),       // Corporate Internet Banking
        CHNL_GLOBAL("G"),          // Global OnLine service
        CHNL_BICNET("B"),          // BICNET
        CHNL_1QPIONEER("N"),       // 1Q Pioneer
        CHNL_1QTRANSFER("Q"),      // 1Q Transfer
        CHNL_KEBINET("I"),         // KEB-INET
        CHNL_COLLECTION("K"),      // Collection system
        CHNL_AUTOTRANSFER("U"),    // aUto Transfer
        CHNL_TABLETBRANCH("N"),    // Tablet Branch
        CHNL_CMSTERMINAL("Y"),     // CMS 단말기
        CHNL_POS("P"),             // POS
        CHNL_SMS("S"),             //SMS
        CHNL_ETC("0"),             // ETC
        CHNL_BATCH("0");            //BATCH


        private final String vlu;


        ChnlTypCd(final String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(final String vlu) {
            for (final ChnlTypCd eItm : ChnlTypCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static ChnlTypCd isKindOf(final String vlu) {
            for (final ChnlTypCd eItm : ChnlTypCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }

        public static boolean isChannel(ChnlTypCd chnlTypCd) {
            if(chnlTypCd == CHNL_CMSTERMINAL
                    || chnlTypCd == CHNL_BATCH || chnlTypCd == CHNL_ETC || chnlTypCd == CHNL_AUTOTRANSFER) {
                return false;
            }
            return true;
        }

        public static boolean isTerminal(ChnlTypCd chnlTypCd) {
            if(chnlTypCd == CHNL_CMSTERMINAL) {
                return true;
            }
            return false;
        }

        public static boolean isBatch(ChnlTypCd chnlTypCd) {
            if(chnlTypCd == CHNL_BATCH || chnlTypCd == CHNL_ETC) {
                return true;
            }
            return false;
        }

        public static boolean isNotAllowAccounting(ChnlTypCd chnlTypCd) {
            if ( chnlTypCd == CHNL_ETC
                    || chnlTypCd == CHNL_CMSTERMINAL)
            {
                return false;
            }
            return true;
        }
    }


    public enum JnlzAmtDvCd {
        /**
         * 일반   현금지급
         */
        CASH_DR("021"),
        /**
         * 자기앞 타점지급
         */
        CHK_DR("121"),
        /**
         * 일반   대체지급
         */
        NTR_DR("221"),
        /**
         * 연동   대체지급
         */
        YTR_DR("222"),
        /**
         * 타자   대체지급
         */
        TTR_DR("223"),

        /**
         * 일반   현금입금
         */
        CASH_CR("001"),
        /**
         * 자기앞 타점입금
         */
        CHK_CR("101"),
        /**
         * 일반   대체입금
         */
        NTR_CR("201"),
        /**
         * 연동   대체입금
         */
        YTR_CR("202"),
        /**
         * 타자   대체입금
         */
        TTR_CR("203");

        private String vlu;
        private String subJnlzDvCd;


        JnlzAmtDvCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public String isDrCrVlu() {
            if (this.vlu != null && this.vlu.length() >= 2) {
                this.subJnlzDvCd = this.vlu == null ? "" : this.vlu.substring(1, 2);
            }

            return this.subJnlzDvCd;
        }

        public static boolean isExist(String vlu) {
            for (JnlzAmtDvCd eItm : JnlzAmtDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static JnlzAmtDvCd isKindOf(String vlu) {
            for (JnlzAmtDvCd eItm : JnlzAmtDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }


    public enum RsltStCd {
        U("UPDATED"),
        I("INSERTED"),
        D("DELETED"),
        UC("UNCHANGED");

        private final String vlu;

        RsltStCd(String vlu) {
            this.vlu = vlu;
        }

        public String getCode() {
            return name();
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (RsltStCd eItm : RsltStCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static RsltStCd isKindOf(String vlu) {
            for (RsltStCd eItm : RsltStCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }


    public enum TrscDvCd {

        ONLOAD("0"),
        CREATE("1"),
        READ("2"),
        UPDATE("3"),
        DELETE("4"),
        EXTRA("5");

        private final String vlu;

        TrscDvCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (TrscDvCd eItm : TrscDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static TrscDvCd isKindOf(String vlu) {
            for (TrscDvCd eItm : TrscDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }

    }


    /**
     *  @MethodName:
     *  @Author : nayoseph
     *  @Date : 2024-08-26
     *  @Param :
     *  @Description : for CH17
     */
    public enum BrDvCd {
        /**
         * HEAD OFFICE
         */
        HEAD_OFFICE("1"),
        /**
         * AREA
         */
        AREA("2"),
        /**
         * REGIONAL BRANCH
         */
        REGIONAL_BRANCH("3"),
        /**
         * MAIN BRANCH
         */
        MAIN_BRANCH("4"),
        /**
         * SUPPORT BRANCH
         */
        SUPPORT_BRANCH("5"),
        /**
         * CASH OFFICE BRANCH
         */
        CASH_OFFICE_BRANCH("6"),
        /**
         * ABB BRANCH
         */
        ABB_BRANCH("7");

        private final String value;

        private BrDvCd(String val) {
            this.value = val;
        }

        public String getCode() {
            return name();
        }

        @Override
        public String toString() {
            return value;
        }

        public static BrDvCd isKindOf(String vlu) {
            final BrDvCd[] eSetItems = BrDvCd.values();
            for (BrDvCd eItm : eSetItems) {
                if (eItm.value.equals(vlu)) {
                    return eItm;
                }
            }
            return null;
        }
    }

    /**
     *  @MethodName:
     *  @Author : nayoseph
     *  @Date : 2024-08-26
     *  @Param :
     *  @Description : for CH17
     */
    public enum ActDvCd {
        /**
         * ACTIVE
         */
        ACTIVE("0"),
        /**
         * INACTIVE
         */
        INACTIVE("1");

        private final String value;

        private ActDvCd(String val) {
            this.value = val;
        }

        public String isValue() {
            return this.value;
        }

        public String getCode() {
            return name();
        }

        @Override
        public String toString() {
            return value;
        }

        public static ActDvCd isKindOf(String vlu) {
            final ActDvCd[] eSetItems = ActDvCd.values();
            for (ActDvCd eItm : eSetItems) {
                if (eItm.value.equals(vlu)) {
                    return eItm;
                }
            }
            return null;
        }
    }


    /**
     *  @MethodName:
     *  @Author : nayoseph
     *  @Date : 2024-08-26
     *  @Param :
     *  @Description : Type and Process Type for CH99
     */
    public enum SysOpenBegYn {
        /**
         * [Type]: Open, [Process Type]: System Locking
         */
        OPEN_SYS_LOCKING("9"),
        /**
         * [Type]: Open, [Process Type]: Start Opening
         */
        OPEN_START_OPENING("1"),
        /**
         * [Type]: Close, [Process Type]: System Locking
         */
        CLOSE_SYS_LOCKING("8"),
        /**
         * [Type]: Close, [Process Type]: Start Opening
         */
        CLOSE_START_OPENING("0");

        private final String value;

        private SysOpenBegYn(String val) {
            this.value = val;
        }

        public String getCode() {
            return name();
        }

        @Override
        public String toString() {
            return value;
        }

        public static SysOpenBegYn isKindOf(String vlu) {
            final SysOpenBegYn[] eSetItems = SysOpenBegYn.values();
            for (SysOpenBegYn eItm : eSetItems) {
                if (eItm.value.equals(vlu)) {
                    return eItm;
                }
            }
            return null;
        }
    }


    /**
     *  @MethodName:
     *  @Author : nayoseph
     *  @Date : 2024-08-26
     *  @Param :
     *  @Description : Country Code & Lanugage Code
     */
    public enum LnggDvCd {
        en("US"),
        ko("KR");

        private final String value;

        private LnggDvCd(String val) {
            this.value = val;
        }

        public String getCode() {
            return name();
        }

        @Override
        public String toString() {
            return value;
        }

        public static LnggDvCd isKindOf(String vlu) {
            final LnggDvCd[] eSetItems = LnggDvCd.values();
            for (LnggDvCd eItm : eSetItems) {
                if (eItm.value.equals(vlu)) {
                    return eItm;
                }
            }
            return null;
        }
    }

    public enum CommRduCd {
        /**
         * 정상
         */
        NORMAL("0"),
        /**
         * 감면
         */
        REDUCTION("1"),
        /**
         * 면제
         */
        EXEMPTION("2"),
        /**
         * 징수
         */
        COLLECTION("3");

        private String vlu;
        private String subJnlzDvCd;


        CommRduCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (CommRduCd eItm : CommRduCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static CommRduCd isKindOf(String vlu) {
            for (CommRduCd eItm : CommRduCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }

    }

    public enum TrscStCd {
        /**
         * 정상
         */
        NORMAL("0"),
        /**
         * 취소
         */
        CANCEL("1");

        private String vlu;


        TrscStCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (TrscStCd eItm : TrscStCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static TrscStCd isKindOf(String vlu) {
            for (TrscStCd eItm : TrscStCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

    public enum AcrTypCd {
        /**
         * straight line method
         */
        A_TYPE("A"),
        /**
         * effective interest method
         */
        B_TYPE("B");

        private String vlu;

        AcrTypCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (AcrTypCd eItm : AcrTypCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static AcrTypCd isKindOf(String vlu) {
            for (AcrTypCd eItm : AcrTypCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

    public enum AcrMethCd {
        /**
         * Accrual
         */
        ACCRUAL("A"),
        /**
         * Amortizing
         */
        AMORTIZING("M");

        private String vlu;

        AcrMethCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (AcrMethCd eItm : AcrMethCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static AcrMethCd isKindOf(String vlu) {
            for (AcrMethCd eItm : AcrMethCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

    public enum AcrDvCd {
        /**
         * NORMAL Accrual
         */
        ACCRUAL("A"),
        /**
         * Delinquency Accrual
         */
        DLQY_ACCRUAL("D"),
        /**
         * NPL Accrual
         */
        NPL_ACCRUAL("N"),
        /**
         * Simulation
         */
        SIMULATION("S");

        private String vlu;

        AcrDvCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (AcrDvCd eItm : AcrDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static AcrDvCd isKindOf(String vlu) {
            for (AcrDvCd eItm : AcrDvCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

    public enum AcrKindCd {
        /**
         * Accrued expense
         * 미지급비용
         */
        ACCRUED_EXPENSE("1"),
        /**
         * Prepaid expense
         * 선급비용
         */
        PREPAID_EXPENSE("2"),
        /**
         * Accrued Revenue
         * 미수수익
         */
        ACCRUED_REVENUE("3"),
        /**
         * Unearned Revenue
         * 선수수익
         */
        UNEARNED_REVENUE("4"),
        /**
         * 미수연체
         */
        ACCRUED_PENALTY("5"),

        /**
         * 기타
         */
        ETC("9");

        private String vlu;

        AcrKindCd(String vlu) {
            this.vlu = vlu;
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (AcrKindCd eItm : AcrKindCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static AcrKindCd isKindOf(String vlu) {
            for (AcrKindCd eItm : AcrKindCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }


    public enum TrscTypCd {
        BIC_CODE("0"),
        BANK_NAME("1"),
        COUNTRY_CODE("2");

        private String vlu;


        TrscTypCd(String vlu) {
            this.vlu = vlu;
        }

        public String getCode() {
            return name();
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (TrscTypCd eItm : TrscTypCd.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static TrscTypCd isKindOf(String vlu) {
            for (TrscTypCd eItm : TrscTypCd.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

    public enum RclsYn {
        OPEN("0"),
        CLOSE("1"),
        ERROR("9");

        private String vlu;


        RclsYn(String vlu) {
            this.vlu = vlu;
        }

        public String getCode() {
            return name();
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (RclsYn eItm : RclsYn.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static RclsYn isKindOf(String vlu) {
            for (RclsYn eItm : RclsYn.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

    public enum BegYn {
        OPEN("0"),
        CLOSE("1");

        private String vlu;


        BegYn(String vlu) {
            this.vlu = vlu;
        }

        public String getCode() {
            return name();
        }

        public String isValue() {
            return this.vlu;
        }

        public static boolean isExist(String vlu) {
            for (BegYn eItm : BegYn.values()) {
                if (eItm.vlu.equals(vlu)) return true;
            }
            return false;
        }

        public static BegYn isKindOf(String vlu) {
            for (BegYn eItm : BegYn.values()) {
                if (eItm.vlu.equals(vlu)) return eItm;
            }
            return null;
        }
    }

    public enum RowStCd {
        INSERT("i"),
        UPDATE("u"),
        DELETE("d"),
        UNCHANGED("uc");

        private String value;

        RowStCd(String val) {
            this.value = val;
        }

        public String isValue() {
            return this.value;
        }

        public static RowStCd isKindOf(String val) {
            RowStCd result = null;
            final RowStCd[] eSetItems = RowStCd.values();
            for (RowStCd eItm : eSetItems) {
                if (eItm.value.equals(val)) {
                    return eItm;
                }
            }
            return result;
        }
    }

    public enum ProcTyp {
        REGISTER("1"),
        MODIFY("2");

        private String value;

        ProcTyp(String val) {
            this.value = val;
        }

        public static ProcTyp isKindOf(String val) {
            ProcTyp result = null;
            final ProcTyp[] eSetItems = ProcTyp.values();
            for (ProcTyp eItm : eSetItems) {
                if (eItm.value.equals(val)) {
                    return eItm;
                }
            }
            return result;
        }
    }

    public enum ThrKindDvCd {
        PETTY_CASH("0"),
        CASH("1"),
        IO_TRANSFER("2"),
        WASH("3"),
        DEPOSIT("4"),
        NOSTRO("5"),
        PL_RECEIVABLE("6"),
        SUSPENSE_RCVPAY("7"),
        IDR_CLEARING("8"),
        OTHER("9"),
        CHK_SETTLE("T"),
        NORMAL("N")
        ;


        private String value;

        ThrKindDvCd(String val) {
            this.value = val;
        }

        public String isValue() {
            return this.value;
        }

        public static ThrKindDvCd isKindOf(String val) {
            ThrKindDvCd result = null;
            final ThrKindDvCd[] eSetItems = ThrKindDvCd.values();
            for (ThrKindDvCd eItm : eSetItems) {
                if (eItm.value.equals(val)) {
                    return eItm;
                }
            }
            return result;
        }
    }

    public enum OvrdCd {
        /* Default */
        OVRD_CD_00("00"),
        OVRD_CD_01("01"),
        OVRD_CD_02("02"),
        OVRD_CD_03("03"),
        OVRD_CD_04("04"),
        OVRD_CD_05("05"),
        OVRD_CD_06("06"),
        OVRD_CD_07("07"),
        OVRD_CD_08("08"),
        OVRD_CD_09("09"),
        OVRD_CD_10("10"),
        OVRD_CD_11("11"),
        OVRD_CD_12("12"),
        OVRD_CD_13("13"),
        OVRD_CD_14("14"),
        OVRD_CD_15("15"),
        OVRD_CD_16("16"),
        OVRD_CD_17("17"),
        OVRD_CD_18("18"),
        OVRD_CD_19("19"),
        OVRD_CD_20("20"),
        OVRD_CD_21("21"),
        OVRD_CD_22("22"),
        OVRD_CD_23("23"),
        OVRD_CD_24("24"),
        OVRD_CD_25("25"),
        OVRD_CD_26("26"),
        OVRD_CD_27("27"),
        OVRD_CD_28("28"),
        OVRD_CD_29("29"),
        OVRD_CD_30("30"),
        OVRD_CD_31("31"),
        OVRD_CD_32("32"),
        OVRD_CD_33("33"),
        OVRD_CD_34("34"),
        OVRD_CD_35("35"),
        OVRD_CD_36("36"),
        OVRD_CD_37("37"),
        OVRD_CD_38("38"),
        OVRD_CD_39("39"),
        OVRD_CD_40("40"),
        OVRD_CD_41("41"),
        OVRD_CD_42("42"),
        OVRD_CD_43("43"),
        OVRD_CD_44("44"),
        OVRD_CD_45("45"),
        OVRD_CD_46("46"),
        OVRD_CD_47("47"),
        OVRD_CD_48("48"),
        OVRD_CD_49("49"),
        OVRD_CD_50("50"),
        OVRD_CD_51("51"),
        OVRD_CD_52("52"),
        OVRD_CD_53("53"),
        OVRD_CD_54("54"),
        OVRD_CD_55("55"),
        OVRD_CD_56("56"),
        OVRD_CD_57("57"),
        OVRD_CD_58("58"),
        OVRD_CD_59("59"),
        OVRD_CD_60("60"),
        OVRD_CD_61("61"),
        OVRD_CD_62("62"),
        OVRD_CD_63("63"),
        OVRD_CD_64("64"),
        OVRD_CD_65("65"),
        OVRD_CD_66("66"),
        OVRD_CD_67("67"),
        OVRD_CD_68("68"),
        OVRD_CD_69("69"),
        OVRD_CD_70("70"),
        OVRD_CD_71("71"),
        OVRD_CD_72("72"),
        OVRD_CD_73("73"),
        OVRD_CD_74("74"),
        OVRD_CD_75("75"),
        OVRD_CD_76("76"),
        OVRD_CD_77("77"),
        OVRD_CD_78("78"),
        OVRD_CD_79("79"),
        OVRD_CD_80("80"),
        OVRD_CD_81("81"),
        OVRD_CD_82("82"),
        OVRD_CD_83("83"),
        OVRD_CD_84("84"),
        OVRD_CD_85("85"),
        OVRD_CD_86("86"),
        OVRD_CD_87("87"),
        OVRD_CD_88("88"),
        OVRD_CD_89("89"),
        OVRD_CD_90("90"),
        OVRD_CD_91("91"),
        OVRD_CD_92("92"),
        OVRD_CD_93("93"),
        OVRD_CD_94("94"),
        OVRD_CD_95("95"),
        OVRD_CD_96("96"),
        OVRD_CD_97("97"),
        OVRD_CD_98("98"),
        OVRD_CD_99("99"),

        /* HOBIS PROD DATA */
        BACK_VALUE("01"),
        BACK_DATED("02"),
        CANCEL_REVERSE("03"),
        TRANSACTION_LIMIT("11"),
        OVERDRAFT("12"),
        INCREASE_DECREASE("13"),
        WAIVE_FEE("14"),
        OPEN("21"),
        UPDATE("22"),
        BLOCK_RELEASE("23"),
        RE_TRANSACTION("24"),
        RESTRICTED_TRX("31"),
        BULK_TRANSFER_REGISTRATION("33"),
        AUTO_TRANSFER_REGISTRATION("35"),
        AUTO_TRANSFER_CANCELATION("36"),
        UPLOAD_DOWNLOAD("41"),
        CERT_MANAGEMENT("49"),
        CUSTOMER_INTEGRATION("99"),
        MANUAL_TIME_DEPOSIT_ROLLOVER("99"),

        /* ACCOUNTING */
        BACK_DATE("99"),
        CANCEL_REVERSE_LOAN_REPAYMENT("99"),
        APPROVAL_PROCESS("99"),
        ITEM_CHANGE("99"),
        CANCEL("99"),

        /* LON */
        LON_TRX_DATE_TRANSACTION("01"),
        LON_EN09_ELOAN_LEDGER_MODIFY("09"),
        LON_EN11_ELOAN_CONTRACT_REGISTRY("11"),
        LON_EN13_RELATIONAL_CORPORATE_REGISTRY("13"),
        LON_CANCEL_EA_R_CONTRACT("18"),
        LON_EN71_PURCHASE_REQUISTION_REGISTRY("71"),
        LON_EN72_LOAN_APPLICATION("72"),
        LON_CREDIT_ANALYSIS_RECEIPT_CANCE("02"),
        LON_EXTERNAL_CREDIT_RATING_REGISTRY_CANCEL("02"),
        LON_PARAMETER_BORROWING_RATING_FI_MODIFY("01"),
        LON_PARAMETER_BORROWING_RATING_FI_REGISTRY("01"),
        LON_PARAMETER_BORROWING_RATING_FI_RELEASE("01"),
        LON_SUBSTITUTE_PAYMENT_REGISTRY("71"),
        LON_SUBSTITUTE_PAYMENT_RECOVERY("72"),
        LON_BULK_WO_REGISTER("01"),
        LON_MAIN_ITEM_CHANGE_RELEASE("19"),
        LON_MODIFY_INFORMATION("11"),
        LON_REDUCTION_INT_OR_PAST_DUE("01"),
        LON_APPROVAL_TRANSACTION("22"),
        LON_LOAN_DISBURSEMENT("11"),
        LON_AYDA_WRITE_OFF_PROCESS("01"),
        LON_AYDA_WO_WO_REC_CANCEL_PROCESS("01"),
        LON_CANCEL_REVERSE("01"),
        LON_SETTLEMENT_AYDA_WO_TRANSACTION("11"),
        LON_CANCEL_TRANSACTION("11"),
        LON_CANCEL_AUTO_WRITE_OFF("03"),

        /* RT */
        RT_CONFIRMATION_REMITTANCE_INFORMATION("31"),
        RT_BACK_DATED("02"),
        RT_COMMISSION_REDUCTION("14"),
        RT_REMITTANCE_FOR_RESTRICTED_COUNTRY("31"),
        RT_EXIST_THE_SAME_REMITTANCE("50"),
        RT_APPROVAL_TRANSACTION_FOR_OVERDRAFT_DEPO("22"),
        RT_OVER_LIMIT_FOR_EACH_TRANSACTION("11"),
        RT_CUSTOMER_IS_IN_TERRORIST_LIST("23"),
        RT_CUSTOMER_IS_IN_BLACKLIST("24"),
        RT_AMOUNT_MODIFIED("22"),
        RT_VALUE_DATE_CHANGED("22"),
        RT_CONFIRMATION_REJECT_OR_RECALL_REMITTANCE("31"),
        RT_ITEM_CHANGE("19"),
        RT_CONFIRM_USING_APPROVAL_NO("12"),
        RT_IN_CASE_T_T_BUT_MT_103_NOT_MAKE("01"),
        RT_MODIFY_INFORMATION_BENEFICIARY_ACCT("21"),
        RT_MODIFY_INFORMATION_BENEFICIARY_NAME("22"),
        RT_MODIFY_INFORMATION_BENEFICIARY_ADDRESS("23"),
        RT_MODIFY_INFORMATION_APPLICANT_NAME("24"),
        RT_MODIFY_INFORMATION_APPLICANT_ADDRESS("25"),
        RT_CANCEL_REVERSE("03"),
        RT_CREATE_NEW_TRANSACTION("21"),
        RT_MODIFY_INFORMATION_RECEIVING_BANK("22"),
        RT_MODIFY_INFORMATION_PAY_ACCT_NOT_CHECKED("31"),
        RT_LIMIT("11"),
        RT_APPLY_FX_RATE_ERROR("95"),
        RT_INVALID_CIF_NO("66"),
        RT_WAIVE_FEE("14"),
        RT_GET_SEND_OF_CASH_TRANSACTION("31"),
        RT_GET_SEND_OF_PETTY_CASH_TRANSACTION("31"),
        RT_SEND_GET_TO_FROM_ATM("31"),
        RT_SEND_GET_TO_FROM_ATM_OUTSIDE("31"),
        RT_SEND_GET_TO_FROM_OTHER_BANK("31"),
        RT_GOLBAL_ONLINE_SERVICE("52"),
        RT_GOLBAL_ONLINE_SERVICE_CANCEL("52"),
        RT_CANCEL_REJECT_OUT_REMITANCE("03"),
        RT_CANCEL_REJECT_IN_REMITANCE("03"),
        RT_BACK_VALUE("01"),
        RT_CANCEL_TRX("11"),

        ;

        private String value;

        OvrdCd(String val) {
            this.value = val;
        }

        public String isValue() {
            return this.value;
        }

        public static OvrdCd isKindOf(String val) {
            OvrdCd result = null;
            final OvrdCd[] eSetItems = OvrdCd.values();
            for (OvrdCd eItm : eSetItems) {
                if (eItm.value.equals(val)) {
                    return eItm;
                }
            }
            return result;
        }
    }

    public enum EnvrDvCd {
        LOCAL("I3"),
        SIT("I3"),
        LOCAL2("I3"),
        SEOUL("I3"),
        DEV("I3"),
        UAT("I4"), //UAT
        PROD("I1"),
        HOBIS("I3"),
        LINE("I3"),
        STR("I4");

        private String value;

        EnvrDvCd(String val) {
            this.value = val;
        }

        public String isValue() {
            return this.value;
        }

        public static EnvrDvCd isKindOf(String val) {
            for (EnvrDvCd eItm : EnvrDvCd.values()) {
                if (eItm.value.equals(val)) {
                    return eItm;
                }
            }
            return null;
        }
    }


    // 기존에 사용하던 SubProc Code, 더이상 BT에서 받지않고 해당업무에서 Fix하여 사용한다.
    public enum SubprocCd  {

        VERIFY_ACCOUNT("2407A01"),
        VERIFY_ACCOUNT_2("2407A02"),
        TRANSFER ("2507A01"),
        TRANSFER_2 ("2507A02"),
        TRANSFER_WITHOUT_PIN("2517A01"),
        TRANSFER_PAYMENT("2527A01"),
        CARDLESS_WITHDRAWAL("2530A01"),
        OUT_CARDLESS_WITHDRAWAL("NWDLCRL"),
        INQUIRY_QRIS_PAYMENT("2470A01"),  // only use Inoutward
        QRIS_STATUS_CHK("2471A01"),
        QRIS_PAYMENT("2570A01"),
        QRIS_PAYMENT_2("2570A02"),
        INQUIRY_BILL_PAYMENT("2420A01"),
        BILL_PAYMENT("2520A01"),
        DUKCAPIL("NDUKCHK"),
        DUKCAPIL_RANK_LINK("NINQRSK"),
        OVERBOOKING("2501A01"),   // only use Inoutward
        SECURE_LINK("0811H01"),

        CHANGE_DUKCAPIL_MATCH("DUKCAPL"),
        CHANGE_DUKCAPIL_RANK_LINK("#INQRSK"),
        CHANGE_BILL_PAYMENT_BLLPAY("#BLLPAY"),
        CHANGE_BILL_PAYMENT_BLLPOS("#BLLPOS"),
        CHANGE_BILL_PAYMENT_BLLINQ("#BLLINQ"),
        CHANGE_BILL_PAYMENT_HNCNINQ("HNCNINQ"),
        CHANGE_BILL_PAYMENT_HNCNPAY("HNCNPAY");


        private final String value;
        public String isValue() {
            return this.value;
        }
        private SubprocCd(String val) { this.value = val; }
        public String getCode() { return name(); }
        @Override
        public String toString() { return value; }
        public static SubprocCd isKindOf(String vlu) {
            SubprocCd result = null;
            final SubprocCd[] eSetItems = SubprocCd.values();
            for (SubprocCd eItm : eSetItems )  {
                if ( eItm.value.equals(vlu) ) {
                    return eItm;
                }
            }
            return result;
        }
    }

    public enum TinProcCd  {

        TIN_REGIST("LTINREG"),
        TIN_RESET("LTINRST"),
        TIN_VERIFY("LTINVER"),
        TIN_CHANGE("LTINCHN"),
        TIN_CHECK("LTINCHK"),
        PIN_REGIST("LPINREG"),
        PIN_CHECK("LPINCHK"),
        PIN_CHANGE("LPINCHN");

        private final String value;
        public String isValue() {
            return this.value;
        }
        private TinProcCd(String val) { this.value = val; }
        public String getCode() { return name(); }
        @Override
        public String toString() { return value; }
        public static TinProcCd isKindOf(String vlu) {
            TinProcCd result = null;
            final TinProcCd[] eSetItems = TinProcCd.values();
            for (TinProcCd eItm : eSetItems )  {
                if ( eItm.value.equals(vlu) ) {
                    return eItm;
                }
            }
            return result;
        }
    }

    public enum OutwardOpNO {

        OUTWARD_OP_NO("LOO3100"),
        OUTWARD_TRM_CD("LO3100"),
        BILL_OP_NO("EOGBILL"),
        BILL_NUM_TRM_CD("LO4200"),
        QRIS_OP_NO("EOO7700");


        private final String value;
        private OutwardOpNO(String val) { this.value = val; }
        public String getCode() { return name(); }
        public String isValue() { return value; }
        public static OutwardOpNO isKindOf(String vlu) {
            OutwardOpNO result = null;
            final OutwardOpNO [] eSetItems = OutwardOpNO.values();
            for (OutwardOpNO eItm : eSetItems)  {
                if ( eItm.value.equals (vlu)) {
                    return eItm;
                }
            }
            return result;
        }
    }

    // Day calculation method code
    public enum DdCntCalcMethCd  {
        ACTUAL_ACTUAL("1"),
        ACTUAL_360("2"),
        ACTUAL_365_366("3"),
        ACTUAL_365("4"),
        ACTUAL_365_JPY("5"),
        ACTUAL_PLUS_1_360("6"),
        ACTUAL_PLUS_1_365("7"),
        BOND_BASC_30_360("8"),
        EURO_BOND_BASC_30E_360("9"),
        ACTUAL_360_ID_BI("A"),
        ID_30_360("B");

        private final String value;
        public String isValue() {
            return this.value;
        }
        private DdCntCalcMethCd(String val) { this.value = val; }
        public String getCode() { return name(); }
        @Override
        public String toString() { return value; }
        public static DdCntCalcMethCd isKindOf(String vlu) {
            DdCntCalcMethCd result = null;
            final DdCntCalcMethCd [] eSetItems = DdCntCalcMethCd.values();
            for (DdCntCalcMethCd eItm : eSetItems )  {
                if ( eItm.value.equals(vlu) ) {
                    return eItm;
                }
            }
            return result;
        }
    }

    public enum CenterCutStCd {
        BEFORE_START("0"),
        COMPLETE("1"),
        RUNNING("2"),
        FAIL("9");

        private String value;

        CenterCutStCd(String val) {
            this.value = val;
        }

        public String isValue() {
            return this.value;
        }

        public static CenterCutStCd isKindOf(String val) {
            for (CenterCutStCd eItm : CenterCutStCd.values()) {
                if (eItm.value.equals(val)) {
                    return eItm;
                }
            }
            return null;
        }
    }

    public enum FactoryActDvCd {
        /**
         * ACTIVE
         */
        ACTIVE("00"),
        /**
         * INACTIVE
         */
        INACTIVE("09");

        private final String value;

        private FactoryActDvCd(String val) {
            this.value = val;
        }

        public String isValue() {
            return this.value;
        }

        public String getCode() {
            return name();
        }

        @Override
        public String toString() {
            return value;
        }

        public static FactoryActDvCd isKindOf(String vlu) {
            final FactoryActDvCd[] eSetItems = FactoryActDvCd.values();
            for (FactoryActDvCd eItm : eSetItems) {
                if (eItm.value.equals(vlu)) {
                    return eItm;
                }
            }
            return null;
        }
    }

    public enum ExecutionStCd {
        COMPLETE,
        ERROR,
        RUNNING,
        UNKNOWN
    }
}
