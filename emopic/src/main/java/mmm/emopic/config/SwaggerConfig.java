package mmm.emopic.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import java.util.List;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group("emopic-public")
                .pathsToMatch("/**")
                .pathsToExclude("/actuator/**")
                .build();
    }
    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion, @Value("${swagger.request-url}") String apiServerUrl, @Value("${swagger.description}") String description) {

        SecurityScheme securityScheme = new SecurityScheme().type(Type.HTTP)
                .scheme("bearer").bearerFormat("JWT").in(In.HEADER).name("Authorization");

        Components components = new Components().addSecuritySchemes("bearerAuth", securityScheme);

        List<SecurityRequirement> securityRequirements = Arrays.asList(new SecurityRequirement().addList("bearerAuth"));


        Info info = new Info().title("EmoPic API Docs")
                .description("emopic API 정리한 문서").version(appVersion);

        List<Server> servers = Arrays.asList(makeServer(apiServerUrl, description));

        return new OpenAPI()
                .components(components)
                .security(securityRequirements)
                .info(info)
                .servers(servers);
    }
    private Server makeServer(final String url, final String description){
        Server server = new Server();
        server.setUrl(url);
        server.setDescription(description);
        return server;
    }
}
