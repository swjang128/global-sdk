package io.snplab.gsdk.common.util;

import io.snplab.gsdk.common.aws.EnvironmentKey;
import io.snplab.gsdk.common.aws.ServerConfig;

public class KeyUtil {

    public static final String SNPLAB_RANDOM_ID;
    public static final String APP_PUSH_RANDOM_ID;
    public static final String B2B_CRYPTO_KEY;

    static {
        SNPLAB_RANDOM_ID = ServerConfig.get(EnvironmentKey.SNPLAB_RANDOM_ID);
        APP_PUSH_RANDOM_ID = ServerConfig.get(EnvironmentKey.APP_PUSH_RANDOM_ID);
        B2B_CRYPTO_KEY = ServerConfig.get(EnvironmentKey.B2B_PASSWORD_KEY);
    }
}
