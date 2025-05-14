package miguel.alura.com.conversor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Principal {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<String> historial = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ApiService apiservice = new ApiService();
            Map<String, String> monedasDisponibles = CurrencyLoader.obtenerMonedas();
            List<String> codigosOrdenados = new ArrayList<>(monedasDisponibles.keySet());

            while (true) {
                System.out.println("\n╔════════════════════════════════════╗");
                System.out.println("║       🌍 CONVERSOR DE MONEDAS      ║");
                System.out.println("╠════════════════════════════════════╣");
                System.out.println("║ 1️⃣  Convertir monedas              ║");
                System.out.println("║ 2️⃣  Ver historial de conversiones ║");
                System.out.println("║ 3️⃣  Ver monedas disponibles        ║");
                System.out.println("║ 4️⃣  Salir                          ║");
                System.out.println("╚════════════════════════════════════╝");
                System.out.print("👉 Seleccione una opción: ");
                String entrada = scanner.nextLine();

                int opcion;
                try {
                    opcion = Integer.parseInt(entrada);
                } catch (NumberFormatException e) {
                    System.out.println("⚠️  Entrada inválida. Por favor ingrese un número entero (1 a 4).");
                    continue; // vuelve al menú, evito que se rompa el programa.
                }

                switch (opcion) {
                    case 1:
                        convertirMoneda(apiservice, monedasDisponibles, codigosOrdenados);
                        break;
                    case 2:
                        mostrarHistorial();
                        break;
                    case 3:
                        mostrarMonedasDisponibles(monedasDisponibles);
                        break;
                    case 4:
                        System.out.println("Gracias por usar el conversor. Hasta luego!");
                        return;
                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la configuración: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("La solicitud fue interrumpida: " + e.getMessage());
        }
    }

    private static void convertirMoneda(ApiService apiservice, Map<String, String> monedasDisponibles, List<String> codigosOrdenados) throws IOException, InterruptedException {
        System.out.println("\n🌐 Monedas disponibles para conversión:");
        mostrarMonedasEnumeradas(monedasDisponibles);

        String monedaBase = solicitarCodigoMoneda("Seleccione el número de la moneda BASE: ", codigosOrdenados);
        String monedaDestino = solicitarCodigoMoneda("Seleccione el número de la moneda DESTINO: ", codigosOrdenados);

        System.out.print("Ingrese la cantidad a convertir: ");
        double cantidad = scanner.nextDouble();
        scanner.nextLine(); // Limpiar buffer

        String respuestaJson = apiservice.obtenerDatos(monedaBase);
        Gson gson = new GsonBuilder().create();
        TasaDeCambio tasa = gson.fromJson(respuestaJson, TasaDeCambio.class);

        if (tasa.conversion_rates().containsKey(monedaDestino)) {
            double tasaCambio = tasa.conversion_rates().get(monedaDestino);
            double resultado = cantidad * tasaCambio;
            System.out.printf("\n💰 %.2f %s equivalen a %.2f %s\n", cantidad, monedaBase, resultado, monedaDestino);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String registro = String.format("[%s] %.2f %s -> %.2f %s", timestamp, cantidad, monedaBase, resultado, monedaDestino);
            historial.add(registro);
        } else {
            System.out.println("La moneda destino no está disponible.");
        }
    }
    private static void mostrarMonedasEnumeradas(Map<String, String> monedasDisponibles) {
        int index = 1;
        for (Map.Entry<String, String> entry : monedasDisponibles.entrySet()) {
            System.out.printf(" %2d. %s - %s\n", index++, entry.getKey(), entry.getValue());
        }
    }
    private static String solicitarCodigoMoneda(String mensaje, List<String> codigos) {
        while (true) {
            System.out.print(mensaje);
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                if (opcion >= 1 && opcion <= codigos.size()) {
                    return codigos.get(opcion - 1);
                } else {
                    System.out.println("❌ Número fuera de rango.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Ingrese un número.");
            }
        }
    }
    private static void mostrarMonedasDisponibles(Map<String, String> monedasDisponibles) {
        if (monedasDisponibles.isEmpty()) {
            System.out.println("No se pudieron obtener las monedas disponibles.");
            return;
        }

        System.out.println("\n🔎 Monedas disponibles (código - descripción):");
        monedasDisponibles.forEach((codigo, nombre) ->
                System.out.println("💱 " + codigo + " - " + nombre)
        );
    }

    private static void mostrarHistorial() {
        if (historial.isEmpty()) {
            System.out.println("No hay conversiones registradas aún.");
        } else {
            System.out.println("\nHistorial de Conversiones:");
            historial.forEach(System.out::println);
        }
    }

}