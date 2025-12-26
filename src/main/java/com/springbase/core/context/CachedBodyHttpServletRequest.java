package com.springbase.core.context;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;

public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
    private byte[] cachedBody;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request the {@link HttpServletRequest} to be wrapped.
     * @throws IllegalArgumentException if the request is null
     */
    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        // 요청 본문을 캐시하여 바이트 배열에 저장
        InputStream inputStream = request.getInputStream();
        this.cachedBody = inputStream.readAllBytes();
    }

    @Override
    public ServletInputStream getInputStream(){
        // 캐시된 본문을 바탕으로 새 InputStream 제공
        return new CachedBodyServletInputStream(this.cachedBody);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        // 캐시된 본문을 바탕으로 새 BufferedReader 제공
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    /**
     *  @MethodName: clearCachedBody
     *  @Author : handabin
     *  @Date : 2024-09-25
     *  @Description : 예외 발생 시 , 캐시된 데이터를 명시적으로 삭제
     */
    public void clearCachedBody(){
        this.cachedBody = null;
    }

    private static class CachedBodyServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream byteArrayInputStream;

        public CachedBodyServletInputStream(byte[] cachedBody) {
            this.byteArrayInputStream = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public boolean isFinished() {
            return byteArrayInputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {}

        @Override
        public int read() throws IOException {
            return byteArrayInputStream.read();
        }
    }
}
