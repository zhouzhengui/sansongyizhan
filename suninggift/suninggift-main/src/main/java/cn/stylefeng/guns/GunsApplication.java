/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.stylefeng.guns;

import cn.stylefeng.roses.core.config.MybatisDataSourceAutoConfiguration;
import cn.stylefeng.roses.core.config.WebAutoConfiguration;
import org.apache.catalina.valves.RemoteIpValve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import java.nio.charset.Charset;

/**
 * SpringBoot方式启动类
 *
 * @author stylefeng
 * @Date 2017/5/21 12:06
 */

@EnableFeignClients(basePackages = {"com.gzlplink.cloud.messagesend.feignClient"})
@SpringBootApplication(exclude = {WebAutoConfiguration.class, MybatisDataSourceAutoConfiguration.class})
@ComponentScan({"cn.stylefeng.guns.**","com.gzlplink.cloud.mps.client"})
@EnableAsync
public class GunsApplication {

    private final static Logger logger = LoggerFactory.getLogger(GunsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GunsApplication.class, args);
        logger.info(GunsApplication.class.getSimpleName() + " is success!");
    }

//    @Bean
//    public TomcatServletWebServerFactory containerFactory() {
//        return new TomcatServletWebServerFactory() {
//            protected void customizeConnector(Connector connector) {
//                int maxSize = 50000000;
//                super.customizeConnector(connector);
//                connector.setMaxPostSize(maxSize);
//                connector.setMaxSavePostSize(maxSize);
//                if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {
//                    ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(maxSize);
//                    logger.info("Set MaxSwallowSize "+ maxSize);
//                }
//            }
//        };
//
//    }

//    @Bean
//    public EmbeddedServletContainerFactory servletContainer(){
//        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
//        factory.setUriEncoding(Charset.forName("UTF-8"));
//        RemoteIpValve value = new RemoteIpValve();
//        value.setRemoteIpHeader("X-Forwarded-For");
//        value.setProtocolHeader("X-Forwarded-Proto");
//        value.setProtocolHeaderHttpsValue("https");
//        factory.addEngineValves(value);
//        return factory;
//    }

    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.setUriEncoding(Charset.forName("UTF-8"));
        RemoteIpValve value = new RemoteIpValve();
        value.setRemoteIpHeader("X-Forwarded-For");
        value.setProtocolHeader("X-Forwarded-Proto");
        value.setProtocolHeaderHttpsValue("https");
        factory.addEngineValves(value);
        return factory;







//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint constraint = new SecurityConstraint();
//                constraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                constraint.addCollection(collection);
//                context.addConstraint(constraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(httpConnector());
//        return tomcat;
    }
//
//    @Bean
//    public Connector httpConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        //Connector监听的http的端口号
//        connector.setPort(8081);
//        connector.setSecure(false);
//        //监听到http的端口号后转向到的https的端口号
//        connector.setRedirectPort(8996);
//        return connector;
//    }


//    @Bean
//    public EmbeddedServletContainerFactory servletContainer(){
//        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
//        factory.setUriEncoding(Charset.forName("UTF-8"));
//        RemoteIpValve value = new RemoteIpValve();
//        value.setRemoteIpHeader("X-Forwarded-For");
//        value.setProtocolHeader("X-Forwarded-Proto");
//        value.setProtocolHeaderHttpsValue("https");
//        factory.addEngineValves(value);
//        return factory;
//    }


}
