package com.springbase.core.common.define;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@AllArgsConstructor
@Getter
public enum CoreErrCode {


    UNKNOWN_SYSTEM_ERROR                         (00001, "unknownSystemError"),
    SERVICE_TIME_OUT                             (00002, "serviceTimeOut"),
    SYS_DB_DML_ERROR                             (00003, "sysDbDmlError"),
    NETWORK_ERROR                                (00004, "networkError"),
    APPROVAL_TRANACTION                          (00010, "approvalTranaction"),
    SIMULATION_TRANACTION                        (00020, "simulationTranaction"),
    NORM_TRSC_POSS_NO                            (00021, "normTrscPossNo"),
    SYS_INPUT_VALIDATION_ERROR                   (00030, "sysInputValidationError"),

    SYS_REQUIRED_NOT_FOUND_ERROR                 (00031, "sysRequiredNotFoundError"),
    ERROR_CONVERT_ENTITY                         (00032, "errorConvertEntity"),
    ERROR_CAST_ENTITY                            (00033, "errorCastEntity"),
    LOCK_VALUE                                   (00034, "The entity is locked by another transaction"),
    FILE_UPLOAD_FAIL                             (00035, "Error when uploading the file"),
    FILE_NOT_FOUND                               (00036, "File Not Found"),
    FILE_SYSTEM_NOT_REACH                        (00037, "File System is not Reachable"),
    INVALID_FILE_INFO                            (00041, "Need the file Information (sqmtId, refNo, bizDvCd)"),
    PK_NOT_FOUND                                 (00042, "Update Error, PK not found"),
    USER_NOT_FOUND                               (00101, "User Not Found"),
    NOT_ALLOWED_USER                             (00102, "notAllowedUser"),
    PWD_NOT_MATCH                                (00103, "verification password is not matched"),
    USER_IS_NULL                                 (00103, "user id cannot be empty"),
    USERNAME_IS_NULL                             (00104, "user name cannot be empty"),
    INACTIVE_USER                                (00105, "Inactive user, please check the user again"),
    LOCKED_USER                                  (00106, "User is locked"),


    DB_SQL_GRAMMAR_ERROR                         (00220, "SQL Grammar Error"),
    DB_INCORRECT_RESULT_SIZE_ERROR               (00221, "Size of return SQL invalid (Expect 1, receive many)"),
    DB_DML_ERROR                                 (00222, "SQl Integrity Error"),
    UNKNOWN_DBMS_ERROR                           (00223, "Error when insert or update data"),
    MANDATORY_PRODUCT_CODE                       (00302, "mandatoryProductCode"),
    MANDATORY_CONDITION_TYPE_CODE                (00303, "mandatoryConditionTypeCode"),
    CHK_PREDEF_INFO_NULL                         (00304, "chkPredefInfoNull"),
    INVALID_CONDITION                            (00305, "invalidCondition"),
    DUP_DEF_VALUE                                (00306, "dupDefValue"),
    RESULT_NOT_NUMBER                            (00307, "resultNotNumber"),
    NO_CHECK_CODE                                (00310, "noCheckCode"),
    CHECK_SCREEN_CODE                            (00311, "checkScreenCode"),
    CHECK_PRE_DEFINE_SCREEN_CODE                 (00312, "checkPreDefineScreenCode"),
    MANDATORY_CONDITION_VALUE                    (00313, "mandatoryConditionValue"),
    MANDATORY_INPUT_VALUE                        (00314, "mandatoryInputValue"),
    CHECK_SCREEN_TYPE_CODE                       (00315, "checkScreenTypeCode"),
    OUT_OF_RANGE_VALUE                           (00317, "outOfRangeValue"),
    MULTI_TYPE_EMPTY                             (00320, "multiTypeEmpty"),
    METHOD_ERROR                                 (00402, "Error inside the service: Either data type of Mapper got error"),
    SERVICE_NOT_FOUND                            (00403, "Service Not Found"),
    SERVICE_INPUT_NULL                           (00404, "serviceInputNull"),
    OLD_NEW_PWN_SAME                             (00405, "Password has been used");

    private final int code;
    private final String desc;

    public String getName() { return name(); }

    public int getCode() { return this.code; }
    public String getDesc() { return this.desc; }

    public static CoreErrCode fromName(final String name) {
        return Arrays.stream(values()).filter(v -> Objects.equals(name, v.getName())).findFirst().orElseThrow(null);
    }

    public static CoreErrCode fromCode(final int code ) {
        return Arrays.stream(values()).filter(v -> code == v.code).findFirst().orElseThrow(null);
    }

    public static CoreErrCode fromDesc(final String desc ) {
        return Arrays.stream(values()).filter(v -> Objects.equals(desc, v.getDesc())).findFirst().orElseThrow(null);
    }

    public static boolean isExist(final int code) {
        return ( fromCode(code) != null );
    }

    public static boolean isExistDesc(final String desc) {
        return ( fromDesc(desc) != null );
    }

    public static boolean isExistName(final String name) {
        return ( fromName(name) != null );
    }

}
