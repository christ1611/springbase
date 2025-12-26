package com.springbase.core.logback.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



@Slf4j
public class CustomFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {

        // MDC 정보를 가지고 옴
        Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();

        /* logTgt key로 등록된 MDC 값 조회
        *  - Format >>> logTgt = USERID1:SVCCD1|USERID2:SVCCD2
        */
        String logTgt = mdcPropertyMap.get("logTgt");
//        String curUser = SysInfo.getCtxUserId();
//        String curSvc = SysInfo.getCtxProcSvcCd();

        log.debug("Saved Logging Target         >>> {}", logTgt);
//        log.debug("Current Logged-in user       >>> {}", curUser);           // 로그인된 User
//        log.debug("Current Processed Service    >>> {}", curSvc);            // 로깅 중인 Service Code

        List<String> logTgtInfo = Arrays.asList(logTgt.split("\\|"));  // 로깅대상 단위를 '|' 구분자로 잘라서 리스트 생성
        log.debug("Logging target information    >>> {}", logTgtInfo);

        // Log Target List 와 현재 유저/서비스를 체크함
        for (String tgt : logTgtInfo) {
            String[] tgtInfo = tgt.split(":");      // ':' 기호를 구분자로 자름
//
//            if(tgtInfo.length == 1) {                   // 유저 아이디만 있는 경우
//                if(tgtInfo[0].equals(curUser)){         // 유저 아이디와 일치 여부 확인 후 출력 여부 결정
//                    return FilterReply.ACCEPT;
//                }
//            }else if(tgtInfo.length == 2) {             // Service Code 가 Logging 대상에 있는 경우
//                if(tgtInfo[0].equals("")) {             // 서비스 코드만 있는 경우
//                    if(tgtInfo[1].equals(curSvc)) {     // 현재 서비스코드와 일치하면 Log 출력
//                        return FilterReply.ACCEPT;
//                    }
//                }else {                                 // 유저아이디 & 서비스 코드 둘 다 있는 경우
//                    if(tgtInfo[0].equals(curUser)       // 현재 유저, 서비스코드와 일치하면 출력
//                    && tgtInfo[1].equals(curSvc)) {
//                        return FilterReply.ACCEPT;
//                    }
//                }
//            }
        }

        // Default -> ERROR 레벨만 출력
        if(event.getLevel().equals(Level.ERROR)){
            return FilterReply.ACCEPT;
        }else {
            return FilterReply.DENY;
        }
    }

}
