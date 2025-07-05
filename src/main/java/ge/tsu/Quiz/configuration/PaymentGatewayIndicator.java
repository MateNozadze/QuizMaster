package ge.tsu.Quiz.configuration;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayIndicator implements HealthIndicator {
    @Override
    public Health health() {
        boolean serviceIsUp = checkPaymentGateway();

        if (serviceIsUp) {
            return Health.up().build();
        } else {
            return Health.down()
                    .withDetail("error", "Gateway unreachable!")
                    .build();
        }
    }

    private boolean checkPaymentGateway() {
        return false;
    }
}
