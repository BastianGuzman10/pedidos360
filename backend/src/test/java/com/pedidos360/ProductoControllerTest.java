package com.pedidos360;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedidos360.model.Producto;
import com.pedidos360.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"app.security.enabled=true", "app.security.issuer-uri=https://issuer.example.com", "app.security.audience=pedidos360-api"})
@AutoConfigureMockMvc
class ProductoControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean JwtDecoder jwtDecoder;
    @MockBean ProductoRepository repository;

    @Test void rechazaProductoInvalido() throws Exception {
        Producto invalido = new Producto("", "", BigDecimal.ZERO, -1);
        mockMvc.perform(post("/api/productos")
                .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_access_as_user")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(invalido)))
            .andExpect(status().isBadRequest());
    }

    @Test void creaProductoValido() throws Exception {
        Producto producto = new Producto("Notebook", "Equipo", new BigDecimal("599990"), 4);
        when(repository.save(any(Producto.class))).thenReturn(producto);
        mockMvc.perform(post("/api/productos")
                .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_access_as_user")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(producto)))
            .andExpect(status().isCreated());
    }
}
