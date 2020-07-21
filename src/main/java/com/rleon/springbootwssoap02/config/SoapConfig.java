package com.rleon.springbootwssoap02.config;

import com.rleon.springbootwssoap02.exception.DetailSoapFaultDefinitionExceptionResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;

import java.util.Properties;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.saaj.SaajSoapFaultException;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;



@EnableWs
@Configuration
public class SoapConfig extends WsConfigurerAdapter {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public ServletRegistrationBean messageDispatcher(ApplicationContext context) {

        /*
         * Spring-WS uses it for handling SOAP requests. We need to inject
         * ApplicationContext to this servlet so that Spring-WS find other beans. It
         * also declares the URL mapping for the requests.
         */
        MessageDispatcherServlet messageDispatcher = new MessageDispatcherServlet();
        messageDispatcher.setApplicationContext(context);
        /*
         * This configuration also uses the WSDL location servlet transformation
         * servlet.setTransformWsdlLocations( true ) internally. If we see the exported
         * WSDL, the soap:address will have the localhost address. Similarly, if we
         * instead visit the WSDL from the public facing IP address assigned to the
         * deployed machine, we will see that address instead of localhost. So the
         * endpoint URL is dynamic based on the deployment environment.
         */
        messageDispatcher.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(messageDispatcher, "/service/*");
    }

    @Bean(name = "studentDetailsWsdl")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) {
        /*
         * DefaultWsdl11Definition exposes a standard WSDL 1.1 using XsdSchema. The bean
         * name studentDetailsWsdl will be the wsdl name that will be exposed. It will
         * be available under http://localhost:8081/service/studentDetailsWsdl.wsdl.
         * This is the simplest approach to expose the contract first wsdl in spring.
         */
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("StudentDetailsPort");
        wsdl11Definition.setLocationUri("/service/student-details");
        wsdl11Definition.setTargetNamespace("http://services.rleon.com/");
        wsdl11Definition.setSchema(countriesSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema countriesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("Student.xsd"));
    }

    /*
     * For the soap exceptions to be propagated properly we must register our
     * SoapFaultMappingExceptionResolver. We can define a default
     * SoapFaultDefinition. This default is used when the
     * SoapFaultMappingExceptionResolver does not have any appropriate mappings to
     * handle the exception. We can map our Custom Exception by setting the mapping
     * properties to the exception resolver. We do this by providing the fully
     * qualified class name of the exception as the key and
     * SoapFaultDefinition.SERVER as the value. Finally, we have to specify an
     * order, otherwise for some reason the mappings will not work.
     */
    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver() {
        SoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver();

        SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
        faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        exceptionResolver.setDefaultFault(faultDefinition);

        Properties errorMappings = new Properties();
        errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(SaajSoapFaultException.class.getName(), SoapFaultDefinition.SERVER.toString());
        exceptionResolver.setExceptionMappings(errorMappings);
        exceptionResolver.setOrder(1);
        return exceptionResolver;
    }
}
