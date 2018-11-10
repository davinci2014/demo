package com.example.demo.config;

public interface ApplicationDefaults {

    interface Security {

        interface ClientAuthorization {

            String accessTokenUri = null;
            String tokenServiceId = null;
            String clientId = null;
            String clientSecret = null;
        }

        interface Authentication {

            interface Jwt {

                String secret = null;
                long tokenValidityInSeconds = 1800; // 0.5 hour
                long tokenValidityInSecondsForRememberMe = 2592000; // 30 hours;
            }
        }

        interface RememberMe {

            String key = null;
        }
    }

    interface Logging {

        interface Logstash {

            boolean enabled = false;
            String host = "localhost";
            int port = 5000;
            int queueSize = 512;
        }
    }

    interface Aliyun {

        interface Sms {

            String product = "Dysmsapi";
            String domain = "dysmsapi.aliyuncs.com";
            String accessKey = null;
            String accessSecret = null;
            String signName = null;

            interface Templates {
                String register = null;
                String login = null;
            }
        }
    }

    interface Async {

        int corePoolSize = 2;
        int maxPoolSize = 50;
        int queueCapacity = 10000;
    }
}
