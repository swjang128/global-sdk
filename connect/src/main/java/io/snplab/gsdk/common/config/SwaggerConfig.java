package io.snplab.gsdk.common.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
		name = "X-Auth-Token",
		type = SecuritySchemeType.APIKEY,
		description = "Api token",
		in = SecuritySchemeIn.HEADER,
		paramName = "X-Auth-Token"
)
@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI sdkOpenAPI() {
		Info info = new Info()
				.title("Global SDK API")
				.version("v1")
				.description("Global SDK API Collection");

		return new OpenAPI()
				.addServersItem(new Server().url("/"))
				.components(new Components())
				.info(info);
	}
}
