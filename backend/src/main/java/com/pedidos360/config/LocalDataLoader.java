package com.pedidos360.config;

import com.pedidos360.model.Producto;
import com.pedidos360.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.math.BigDecimal;

@Configuration
@Profile("local")
public class LocalDataLoader {
    @Bean
    CommandLineRunner demoData(ProductoRepository productos) {
        return args -> {
            if (productos.count() == 0) {
                productos.save(new Producto("Notebook Pro", "Equipo para trabajo y estudio", new BigDecimal("649990"), 8));
                productos.save(new Producto("Monitor 27", "Monitor IPS Full HD", new BigDecimal("189990"), 12));
                productos.save(new Producto("Teclado mecanico", "Teclado USB retroiluminado", new BigDecimal("59990"), 20));
            }
        };
    }
}
