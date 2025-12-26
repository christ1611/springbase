package com.springbase.core.logback;

import ch.qos.logback.classic.Level;
import com.springbase.core.logback.properties.LogRuleProperties;
import org.springframework.context.ApplicationContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Getter
@Setter
@Component
@RequiredArgsConstructor
public class LogPropertyUtil {

    private final LogbackManager logbackManager;
    private final LogRuleProperties logRuleProperties;
    private final ApplicationContext applicationContext;

    private Level defaultLogLevel = Level.ERROR; // ??
    private final Map<String, String> initialProperty = new HashMap<>(); // 초기 설정을 저장하기 위한 Map
    private boolean isLogLevelChanged = false; // 로그 레벨 변경 여부 확인 플래그

    public Map<String, String> getProperty() {
        // 초기 설정을 변경하지 않기 위해 매번 복사본을 반환
        if (initialProperty.isEmpty()) {
            initialProperty.putAll(logRuleProperties.getLevel().getSpringbase());
        }
        return new HashMap<>(initialProperty);
    }

    public void setProperty(String svcCd) throws Exception {
        log.trace("[START] Change Log Level. Service = [{}]", svcCd);

        setProperty(svcCd,Level.DEBUG);
    }

    private void setProperty(String svcCd, Level logLevel) {
        Map<String, String> property = getProperty();
        log.trace("Before Log Property = [{}]", property);

        // 기존 로그 설정에 해당 서비스가 존재하지 않는 경우
        boolean isError = false;
        if(property.get(svcCd) == null) {
            // logLevel 이 Default 로그 레벨과 같은 경우 나머지 로그 레벨 체크
            if(defaultLogLevel.equals(logLevel)) {
                isError = true;
            }
            // logLevel 이 Default 로 설정된 로그 레벨과 다른 경우 로그 설정에 추가
            else {
                property.put(svcCd, logLevel.toString());
            }
        }
        // 기존 로그 설정에 해당 서비스가 존재하는 경우
        else {
            // logLevel 이 Default 로 설정된 로그 레벨과 같은 경우 기존 로그 설정에서 삭제 (나머지 로그레벨 변경)
            if(defaultLogLevel.equals(logLevel)) {
                isError = true;
                property.remove(svcCd, property.get(svcCd));
            }
            // logLevel 이 Default 로 설정된 로그 레벨 이외인 경우 기존 로그 설정에 추가 (모든 로그레벨 동일하게 변경)
            else {
                // 기존 로그 레벨과 동일한 경우 return
                if(StringUtils.compare(property.get(svcCd), logLevel.toString()) == 0) {
                    return;
                }
                property.put(svcCd, logLevel.toString());
            }
        }

        for(String logKey : property.keySet()) {
            // isDebug 가 TRUE 인 경우에는 기존 로그 레벨이 Default 로 설정된 로그 인지 체크
            if(isError) {
                if(defaultLogLevel.equals(Level.valueOf(property.get(logKey)))) {
                    continue;
                }
            }

            logbackManager.getLogger(logKey, logLevel); // 전체 로그레벨 동일하게 변경
            property.put(logKey, logLevel.toString());
        }

        log.debug("After Log Property = [{}]", property);
    }

    // 모든 서비스의 로그 레벨을 초기 상태로 복원하는 메서드
    public void resetPropertiesIfNeeded() {
        if (isLogLevelChanged) {
            for (String logKey : initialProperty.keySet()) {
                logbackManager.getLogger(logKey, Level.valueOf(initialProperty.get(logKey)));
            }
            isLogLevelChanged = false; // 초기화 후 플래그를 다시 설정
        }
        log.info("Log levels reset to initial settings: [{}]", initialProperty);
    }

}