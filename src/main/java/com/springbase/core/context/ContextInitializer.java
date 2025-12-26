package com.springbase.core.context;

import com.springbase.core.component.OneQCTX;

public interface ContextInitializer {
    void initialize(OneQCTX ctx) throws Exception;
}
