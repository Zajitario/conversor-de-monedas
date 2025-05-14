package miguel.alura.com.conversor;

import java.util.Map;

public record TasaDeCambio(String base_code, Map<String, Double> conversion_rates) {
}
