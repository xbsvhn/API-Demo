package com.fsoft.apidemo.database;

import com.fsoft.apidemo.models.Product;
import com.fsoft.apidemo.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2Database {
    private static final Logger logger = LoggerFactory.getLogger(H2Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Product product1 = new Product("Macboook Air", 2020, 30.0, "");
                Product product2 = new Product("Macboook Pro", 2020, 30.0, "");
                logger.info("Insert data: " + productRepository.save(product1));
                logger.info("Insert data: " + productRepository.save(product2));
            }
        };
    }

}
