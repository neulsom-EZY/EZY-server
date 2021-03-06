package com.server.EZY.config.message;

import net.rakugakibox.util.YamlResourceBundle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * i18n를 위한 configuration class
 * @author 정시원
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
public class MessageConfiguration implements WebMvcConfigurer {

    /**
     * 세션의 기본값을 한국으로 설정한다.
     * @return sessionLocaleResolver - 세션의 지역 정보를 한국으로 설정한 객체
     * @author 정시원
     */
    @Bean
    public SessionLocaleResolver sessionLocalResolver(){
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.KOREAN);
        return sessionLocaleResolver;
    }

    /**
     * Request의 Locale 정보에 의해 Locale을 변경한 객체(LocaleChangeInterceptor)를 반환한다.
     * @return LocaleChangeInterceptor - Locale 정보를 변경한 Interceptor객체
     * @author 정시원
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    /**
     * 로컬 정보를 변경한 interceptor({@link #localeChangeInterceptor} 참고)를 시스템 레지스터리에 저장한다.<br>
     * @param registry Interceptor의 저장 및 엑세스를 제공한다. (Spring에서 주입한다.)
     * @author 정시원
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * resources/i18n 에 있는 메시지를 가져오기 위한 설정
     * @param basename 사용자 지정 메시지가 정의되어있는 base 디렉토리 위치를 나타낸다. (i18n/exception)
     * @param encoding 인코딩 정보 (application.yml에 UTF-8로 설정했다.)
     * @return (YamlMessageSource)MessageSource - yml속 메시지를 사용하기 위해 설정한 객체
     * @author 정시원
     */
    @Bean
    public MessageSource messageSource(
            @Value("${spring.messages.basename}") String basename,
            @Value("${spring.messages.encoding") String encoding
    ){
        YamlMessageSource ymlMessageSource = new YamlMessageSource();
        ymlMessageSource.setBasename(basename);
        ymlMessageSource.setDefaultEncoding(encoding);
        ymlMessageSource.setAlwaysUseMessageFormat(true);
        ymlMessageSource.setUseCodeAsDefaultMessage(true);
        ymlMessageSource.setFallbackToSystemLocale(true);
        return ymlMessageSource;
    }

    /**
     * 메시지 다국화를 위한 class<br>
     * 해당 inner class는 다음 메서드에서 사용됩니다. {@link #messageSource(String, String)}
     * @author 정시원
     */
    private static class YamlMessageSource extends ResourceBundleMessageSource {
        /**
         * 파일이나 클래스의 정보를 가져오는 객체({@link ResourceBundle})를 반환하는 매서드 <br>
         * 우리 프로젝트에서는 yml속 message를 가져오는 데 사용한다.
         * @param basename 사용자 지정 메시지가 정의되어있는 base 디렉토리 위치를 나타낸다.
         * @param locale 지역(국가)정보
         * @return resourceBundle - 다국어 관련 자원 파일에 대한 정보를 담고 있는 객체 (다국어 관련 자원 파일은 i18n/exception_ko.yml, i18n/exception_en.yml과 같은 파일를 의미합니다.)
         * @throws MissingResourceException 리소스가 없을 때 발생한다. (예시. i18n 속 yml에 메시지가 없을 때)
         * @author 정시원
         */
        @Override
        protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
            return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
        }
    }
}
