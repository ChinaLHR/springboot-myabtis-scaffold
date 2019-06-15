package cn.bfreeman.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : lhr
 * @Date : 14:25 2019/6/13
 */
@Configuration
@EnableSwagger2
public class ApiSwagger2Config {

    @Bean
    public Docket createRestApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        ParameterBuilder customerIdPar = new ParameterBuilder();
        ParameterBuilder uaPar = new ParameterBuilder();
        ParameterBuilder networkPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("x-access-token").description("token令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        customerIdPar.name("x-access-id").description("客户id").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        uaPar.name("x-ua").description("设备类型(ios|android|h5|web|weixin)").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        networkPar.name("x-network").description("网络(wifi|2g|3g|4g|unknown)").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        pars.add(customerIdPar.build());
        pars.add(uaPar.build());
        pars.add(networkPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 扫描该包下的所有需要在Swagger中展示的API，@ApiIgnore注解标注的除外
                .apis(RequestHandlerSelectors.basePackage("cn.bfreeman"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    /***
     *  创建API的基本信息，这些信息会在Swagger UI中进行显示
     */
    private ApiInfo apiInfo() {
        Contact contact = new Contact("BFreeman", "http://www.bfreeman.cn", "postmaster@bfreeman.cn");
        return new ApiInfoBuilder()
                // API 标题
                .title("bfreeman api接口文档")
                // API描述
                .description("bfreeman api接口文档")
                // termsOfServiceUrl
                .termsOfServiceUrl("http://www.bfreeman.cn")
                // 作者
                .contact(contact)
                // 链接显示文字
                .license("bfreeman api接口")
                // //网站链接
                .licenseUrl("http://www.bfreeman.cn/swagger-ui.html")
                // 版本
                .version("1.0.0")
                .build();
    }
}
