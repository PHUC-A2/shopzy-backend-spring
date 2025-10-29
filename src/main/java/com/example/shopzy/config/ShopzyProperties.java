package com.example.shopzy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "shopzy")
public class ShopzyProperties {

    private UploadFile uploadFile = new UploadFile();
    private Jwt jwt = new Jwt();

    public UploadFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(UploadFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public static class UploadFile {
        private String baseUri;

        public String getBaseUri() {
            return baseUri;
        }

        public void setBaseUri(String baseUri) {
            this.baseUri = baseUri;
        }
    }

    public static class Jwt {
        private String base64Secret;
        private int refreshTokenValidityInSecond;
        private int refreshTokenValidityInSeconds;
        private int accessTokenValidityInSeconds;

        public String getBase64Secret() {
            return base64Secret;
        }

        public void setBase64Secret(String base64Secret) {
            this.base64Secret = base64Secret;
        }

        public int getRefreshTokenValidityInSecond() {
            return refreshTokenValidityInSecond;
        }

        public void setRefreshTokenValidityInSecond(int refreshTokenValidityInSecond) {
            this.refreshTokenValidityInSecond = refreshTokenValidityInSecond;
        }

        public int getRefreshTokenValidityInSeconds() {
            return refreshTokenValidityInSeconds;
        }

        public void setRefreshTokenValidityInSeconds(int refreshTokenValidityInSeconds) {
            this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
        }

        public int getAccessTokenValidityInSeconds() {
            return accessTokenValidityInSeconds;
        }

        public void setAccessTokenValidityInSeconds(int accessTokenValidityInSeconds) {
            this.accessTokenValidityInSeconds = accessTokenValidityInSeconds;
        }
    }
}
