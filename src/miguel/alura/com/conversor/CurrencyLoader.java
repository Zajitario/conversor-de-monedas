package miguel.alura.com.conversor;

import java.util.LinkedHashMap;
import java.util.Map;

public class CurrencyLoader {
    public static Map<String, String> obtenerMonedas() {
        Map<String, String> monedas = new LinkedHashMap<>();
        //Limitado manualmente

        monedas.put("USD", "United States Dollar");
        monedas.put("ARS", "Argentine Peso");
        monedas.put("MXN", "Mexican Peso");
        monedas.put("CLP", "Chilean Peso");
        monedas.put("COP", "Colombian Peso");
        monedas.put("BRL", "Brazilian Real");
        monedas.put("JPY", "Japanese Yen");
        monedas.put("EUR", "Euro");
        monedas.put("GBP", "British Pound Sterling");
        monedas.put("CAD", "Canadian Dollar");

        return monedas;
    }
}
