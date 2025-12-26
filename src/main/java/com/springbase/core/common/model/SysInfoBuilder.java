package com.springbase.core.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema
public class SysInfoBuilder {
    @Schema(description = "기준통화코드", defaultValue = "KRW")
    String baseCurCd;
    @Schema(description = "사용자 ID", defaultValue = "COREUSR")
    String userId;
    @Schema(description = "스크린 ID", defaultValue = "CO01")
    String scrnId;
    @Schema(description = "채널타입코드(기본값을 단말로 했으니 필요하면 변경 꼭 하세요)", defaultValue = "Y")
    String chnlTypCd;
//    @Schema(description = "SegmentationID", defaultValue = "GTIID01")
//    String sgmtId;
    @Schema(description = "Simulation Y/N", defaultValue = "N")
    String smltnTrscYn;
    @Schema(description = "Language", defaultValue = "KR")
    String lnggCd;
//    @Schema(description = "Transaction branch", defaultValue = "0888")
//    String trscBrNo;
//    @Schema(description = "Accounting branch", defaultValue = "0888")
//    String acBrNo;
}