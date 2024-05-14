package ua.wms.warehouse.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/logout").setViewName("logout");
        registry.addViewController("/products").setViewName("products");
        registry.addViewController("/orders").setViewName("orders");
        registry.addViewController("/warehouses").setViewName("warehouses");
    }

}