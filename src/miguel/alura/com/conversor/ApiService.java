package miguel.alura.com.conversor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiService {
    private final String apikey;

    public ApiService() throws IOException{
        ConfigLoader config = new ConfigLoader();
        this.apikey = config.getApiKey();
    }

    public String obtenerDatos(String monedaBase) throws IOException, InterruptedException{
        String url = "https://v6.exchangerate-api.com/v6/" + apikey + "/latest/" + monedaBase;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

}