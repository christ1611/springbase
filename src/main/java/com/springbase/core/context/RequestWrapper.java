package com.springbase.core.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbase.core.component.OneQBeanUtils;
import com.springbase.core.component.OneQCTX;
import com.springbase.core.component.OneQCTXFactory;
import com.springbase.core.logback.LogPropertyUtil;
import com.springbase.core.security.util.IpAddressUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @JsonIgnore
    private final LogPropertyUtil propertyUtil = OneQBeanUtils.getBean(LogPropertyUtil.class);

    //API 호출시 Request Parameter가 아닌(POST방식) Request Body에 JSON데이터를 넣어서 호출을 처리하기
    @Getter
    private String body;
    @Getter
    private Map<String, Object> httpInput;

    private String input;
    private boolean bodyOutput = true;
    private ArrayList<String> serviceList = new ArrayList<>();



    private OneQCTXFactory ctxFactory  = OneQBeanUtils.getBean(OneQCTXFactory.class);

    public static String getBodyMessage(CachedBodyHttpServletRequest cachedRequest) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = cachedRequest.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
            return stringBuilder.toString();
        } catch (IOException ex) {
            log.error("IOException ", ex);
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public RequestWrapper(HttpServletRequest request) throws Exception {
        super(request);
        log.debug("[START] RequestWrapper");
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);

        if (request instanceof RequestWrapper rw) {
            body = rw.getBody();
        } else {
            body = getBodyMessage(cachedRequest);
        }

        if (StringUtils.isNotBlank(body)) {
            httpInput = objectMapper.readValue(body, LinkedHashMap.class);

            if (httpInput.containsKey("input")) {
                input = objectMapper.writeValueAsString(httpInput.get("input"));
                bodyOutput = false;
            } else if (httpInput.containsKey("sysInfo")) {
                bodyOutput = false;
            }
        } else {
            httpInput = new LinkedHashMap<>();
        }

        log.debug("RequestWrapper controller requestBody, Input = {}", input);


        initOneQCTX(request);

        String serviceCd = StringUtils.substringAfterLast(request.getRequestURI(), "/");
        serviceCd = StringUtils.replace(serviceCd, ".", "");
        propertyUtil.setProperty(serviceCd);

        if(cachedRequest != null){
            // 캐시 사용이 모두 완료된 후, 메모리에서 캐시 삭제
            cachedRequest.clearCachedBody();
        }
    }

    private void initOneQCTX(HttpServletRequest request) throws Exception {
        log.debug("[START] RequestWrapper initOneQCTX()");
        try {

            httpInput.put("ipAddr", IpAddressUtils.getUserIpaddress(request));
            OneQCTX.set(ctxFactory.createCTX(httpInput));


            OneQCTX CTX = OneQCTX.getCTX();


//            CTX.initializeCTX(httpInput);

        } catch (Exception e) {
            log.error("Error during CTX.initializeCTX() in RequestWrapper", e);
            throw e;
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream byteArrayInputStream;

        if (bodyOutput) {
            byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        } else {
            byteArrayInputStream = new ByteArrayInputStream(input.getBytes());
        }

        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }


    @Override
    public Object getAttribute(String name) {
        if (StringUtils.equals(name, "httpInput")) return httpInput;
        if (StringUtils.equals(name, "body")) return body;
        if (StringUtils.equals(name, "ServiceList")) return serviceList;
        if (StringUtils.equals(name, "input")) return input;

        return super.getAttribute(name);
    }

    @Override
    public void setAttribute(String name, Object o) {
        if (StringUtils.equals("bodyOutput", name)) {
            if (o == null) bodyOutput = false;
            else bodyOutput = true;
        }
        if (StringUtils.equals("ServiceList", name)) {
            serviceList.add((String) o);
        }


        super.setAttribute(name, o);
    }

    @Override
    public ServletRequest getRequest() {
        return super.getRequest();
    }
}