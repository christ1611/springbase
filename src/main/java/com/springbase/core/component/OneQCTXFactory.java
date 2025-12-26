package com.springbase.core.component;

import com.springbase.core.common.model.DayInfo;
import com.springbase.core.common.model.SysInfo;
import com.springbase.core.context.ContextInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OneQCTXFactory {
    private final List<ContextInitializer> contextInitializers;

    public OneQCTX createCTX(Map<String, Object> httpInput) throws Exception{
        OneQCTX ctx = new OneQCTX();
        ctx.initializeCTX(httpInput);
        SysInfo sysInfo = ctx.getSysInfo();
        Map<String, Object> httpSysInfo = new LinkedHashMap<>();

        if (httpInput.containsKey("sysInfo")) {
            httpSysInfo = (Map<String, Object>) httpInput.get("sysInfo");
            if (ctx.getInput().containsKey("userId"))
            {
                httpSysInfo.put("userId",ctx.getInput().get("userId"));

            }

            // TODO: 01/29/2024
            log.debug("CTX initialize SysInfo = [{}]", httpSysInfo);
        }
        sysInfo.initInfo(httpSysInfo);
        DayInfo dayInfo = ctx.getDayInfo();
        dayInfo.initInfo(ctx.getSysInfo().getSysDt());
        // run all initializers automatically
        for (ContextInitializer initializer : contextInitializers) {
            initializer.initialize(ctx);
        }

        return ctx;
    }
}
