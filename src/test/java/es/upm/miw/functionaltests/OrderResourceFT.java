package es.upm.miw.functionaltests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class OrderResourceFT {

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10_000);
        requestFactory.setReadTimeout(10_000);
        this.restTemplate = new RestTemplate(requestFactory);
    }

    @Test
    void testCreateOrder() {
        Order requestOrder = Order.builder()
                .productId("product-123")
                .user(User.builder().identity("user-identity-123").build())
                .build();

        ResponseEntity<Order> response = restTemplate.postForEntity(
                "http://localhost:8082/orders",
                requestOrder,
                Order.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .satisfies(order -> {
                    assertThat(order.getIdentity()).isNotNull();
                    assertThat(order.getProductId()).isEqualTo("product-123");
                    assertThat(order.getUser()).isNotNull();
                    assertThat(order.getCreatedAt()).isNotNull();
                });
    }

}
