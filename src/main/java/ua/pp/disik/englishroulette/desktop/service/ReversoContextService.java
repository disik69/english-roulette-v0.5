package ua.pp.disik.englishroulette.desktop.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Headers;
import feign.Logger;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReversoContextService {
    @Data
    public static class SimpleWriteTranslation {
        private String direction;
        private String source;
    }

    @Data
    public static class SimpleReadTranslation {
        private Boolean error;
        private Boolean success;

        @Data
        public static class Source {
            @Data
            public static class Translation {
                private String translation;

                @Data
                public static class Context {
                    private String source;
                    private String target;
                }
                private List<Context> contexts;
            }
            private List<Translation> translations;
        }
        private List<Source> sources;
    }

    private interface Repository {
        @RequestLine("POST /TranslateSimple")
        @Headers({
                "Content-Type: application/json",
                "Accept: application/json"
        })
        SimpleReadTranslation translateSimple(SimpleWriteTranslation body);
    }

    @Slf4j
    private static class ServiceFeignLogger extends Logger {
        @Override
        protected void log(String configKey, String format, Object... args) {
            log.debug(String.format(methodTag(configKey) + format, args));
        }
    }

    private static final String URL = "https://cps.reverso.net/api2";
    public static final String EN_RU_TRANSLATION = "en-ru";
    public static final String RU_EN_TRANSLATION = "ru-en";

    private final Repository repository;

    public ReversoContextService() {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        repository = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .logger(new ServiceFeignLogger())
                .logLevel(Logger.Level.BASIC)
                .target(Repository.class, URL);
    }

    public SimpleReadTranslation translateSimple(String direction, String source) {
        SimpleWriteTranslation body = new SimpleWriteTranslation();
        body.setDirection(direction);
        body.setSource(source);

        return repository.translateSimple(body);
    }
}
