package io.snplab.gsdk.common.util;

import io.snplab.gsdk.common.aws.EnvironmentKey;
import io.snplab.gsdk.common.aws.ServerConfig;

public class Environment {

    public static final String SNPLAB_RANDOM_ID;
    public static final String APP_PUSH_RANDOM_ID;
    public static final String B2B_CRYPTO_KEY;

    public static final String WEB_ROOT_PATH;

    public static final String B2B_WEB_PUBLIC_URL;

    static {
        SNPLAB_RANDOM_ID = ServerConfig.get(EnvironmentKey.SNPLAB_RANDOM_ID);
        APP_PUSH_RANDOM_ID = ServerConfig.get(EnvironmentKey.APP_PUSH_RANDOM_ID);
        B2B_CRYPTO_KEY = ServerConfig.get(EnvironmentKey.B2B_PASSWORD_KEY);
        WEB_ROOT_PATH = ServerConfig.get(EnvironmentKey.B2B_WEB_ROOT_PATH);
        B2B_WEB_PUBLIC_URL = ServerConfig.get(EnvironmentKey.B2B_WEB_PUBLIC_URL);
    }

    public static String getRootPath(RootPath root) {

        String rootPath = null;
        switch (root) {

            case WEB :
                rootPath = WEB_ROOT_PATH;
                break;
            default:
                break;
        }
        return rootPath;
    }

    public static String getServiceUrl() {
        return B2B_WEB_PUBLIC_URL;
    }
}
