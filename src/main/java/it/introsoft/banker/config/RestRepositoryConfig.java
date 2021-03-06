package it.introsoft.banker.config;

import it.introsoft.banker.model.jpa.Account;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class RestRepositoryConfig extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Account.class);
        config.setBasePath("api");
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("beforeCreate", validator());
        validatingListener.addValidator("afterCreate", validator());
        validatingListener.addValidator("beforeSave", validator());
        validatingListener.addValidator("afterSave", validator());
        validatingListener.addValidator("beforeLinkSave", validator());
        validatingListener.addValidator("afterLinkSave", validator());
        validatingListener.addValidator("beforeDelete", validator());
        validatingListener.addValidator("afterDelete", validator());
    }

}
