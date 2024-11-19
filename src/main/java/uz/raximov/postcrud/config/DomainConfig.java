package uz.raximov.postcrud.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("uz.raximov.postcrud.domain")
@EnableJpaRepositories("uz.raximov.postcrud.repos")
@EnableTransactionManagement
public class DomainConfig {
}
