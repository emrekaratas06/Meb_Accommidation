package meb.gov.tr.meb_accommidation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EntityScan("meb.gov.tr.meb_accommidation.entity")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class MebAccommidationApplication {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new SpringSecurityAuditorAware();
    }
    public static void main(String[] args) {
        SpringApplication.run(MebAccommidationApplication.class, args);
    }

}
