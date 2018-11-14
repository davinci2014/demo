package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Security security = new Security();
    private final CorsConfiguration cors = new CorsConfiguration();
    private final Logging logging = new Logging();
    private final Aliyun aliyun = new Aliyun();
    private final Async async = new Async();

    public Security getSecurity() {
        return security;
    }

    public CorsConfiguration getCors() {
        return cors;
    }

    public Logging getLogging() {
        return logging;
    }

    public Aliyun getAliyun() {
        return aliyun;
    }

    public Async getAsync() {
        return async;
    }

    public static class Security {

        private final ClientAuthorization clientAuthorization = new ClientAuthorization();

        private final Authentication authentication = new Authentication();

        private final RememberMe rememberMe = new RememberMe();

        public ClientAuthorization getClientAuthorization() {
            return clientAuthorization;
        }

        public Authentication getAuthentication() {
            return authentication;
        }

        public RememberMe getRememberMe() {
            return rememberMe;
        }

        public static class ClientAuthorization {

            private String accessTokenUri = ApplicationDefaults.Security.ClientAuthorization.accessTokenUri;

            private String tokenServiceId = ApplicationDefaults.Security.ClientAuthorization.tokenServiceId;

            private String clientId = ApplicationDefaults.Security.ClientAuthorization.clientId;

            private String clientSecret = ApplicationDefaults.Security.ClientAuthorization.clientSecret;

            public String getAccessTokenUri() {
                return accessTokenUri;
            }

            public void setAccessTokenUri(String accessTokenUri) {
                this.accessTokenUri = accessTokenUri;
            }

            public String getTokenServiceId() {
                return tokenServiceId;
            }

            public void setTokenServiceId(String tokenServiceId) {
                this.tokenServiceId = tokenServiceId;
            }

            public String getClientId() {
                return clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }

            public String getClientSecret() {
                return clientSecret;
            }

            public void setClientSecret(String clientSecret) {
                this.clientSecret = clientSecret;
            }
        }

        public static class Authentication {

            private final Jwt jwt = new Jwt();

            public Jwt getJwt() {
                return jwt;
            }

            public static class Jwt {

                private String secret = ApplicationDefaults.Security.Authentication.Jwt.secret;

                private long tokenValidityInSeconds = ApplicationDefaults.Security.Authentication.Jwt
                        .tokenValidityInSeconds;

                private long tokenValidityInSecondsForRememberMe = ApplicationDefaults.Security.Authentication.Jwt
                        .tokenValidityInSecondsForRememberMe;

                public String getSecret() {
                    return secret;
                }

                public void setSecret(String secret) {
                    this.secret = secret;
                }

                public long getTokenValidityInSeconds() {
                    return tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }

                public long getTokenValidityInSecondsForRememberMe() {
                    return tokenValidityInSecondsForRememberMe;
                }

                public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
                    this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
                }
            }
        }

        public static class RememberMe {

            @NotNull
            private String key = ApplicationDefaults.Security.RememberMe.key;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }
    }

    public static class Logging {

        private final Logstash logstash = new Logstash();

        public Logstash getLogstash() {
            return logstash;
        }

        public static class Logstash {

            private boolean enabled = ApplicationDefaults.Logging.Logstash.enabled;

            private String host = ApplicationDefaults.Logging.Logstash.host;

            private int port = ApplicationDefaults.Logging.Logstash.port;

            private int queueSize = ApplicationDefaults.Logging.Logstash.queueSize;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public int getQueueSize() {
                return queueSize;
            }

            public void setQueueSize(int queueSize) {
                this.queueSize = queueSize;
            }
        }
    }

    public static class Aliyun {
        private final Sms sms = new Sms();

        public Sms getSms() {
            return sms;
        }

        public static class Sms {
            // 短信API产品名称
            private String product = ApplicationDefaults.Aliyun.Sms.product;
            // 短信API产品域名
            private String domain = ApplicationDefaults.Aliyun.Sms.domain;

            private String accessKey = ApplicationDefaults.Aliyun.Sms.accessKey;

            private String accessSecret = ApplicationDefaults.Aliyun.Sms.accessSecret;

            private String signName = ApplicationDefaults.Aliyun.Sms.signName;

            private final Templates templates = new Templates();

            public String getProduct() {
                return product;
            }

            public void setProduct(String product) {
                this.product = product;
            }

            public String getDomain() {
                return domain;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public String getAccessKey() {
                return accessKey;
            }

            public void setAccessKey(String accessKey) {
                this.accessKey = accessKey;
            }

            public String getAccessSecret() {
                return accessSecret;
            }

            public void setAccessSecret(String accessSecret) {
                this.accessSecret = accessSecret;
            }

            public String getSignName() {
                return signName;
            }

            public void setSignName(String signName) {
                this.signName = signName;
            }

            public Templates getTemplates() {
                return templates;
            }

            public static class Templates {
                private String register = ApplicationDefaults.Aliyun.Sms.Templates.register;
                private String login = ApplicationDefaults.Aliyun.Sms.Templates.login;

                public String getRegister() {
                    return register;
                }

                public void setRegister(String register) {
                    this.register = register;
                }

                public String getLogin() {
                    return login;
                }

                public void setLogin(String login) {
                    this.login = login;
                }
            }
        }
    }

    public static class Async {

        private int corePoolSize = ApplicationDefaults.Async.corePoolSize;

        private int maxPoolSize = ApplicationDefaults.Async.maxPoolSize;

        private int queueCapacity = ApplicationDefaults.Async.queueCapacity;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }
}
