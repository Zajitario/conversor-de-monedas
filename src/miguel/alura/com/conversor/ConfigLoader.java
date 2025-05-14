package miguel.alura.com.conversor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private Properties props;

    public ConfigLoader() throws IOException{
        props = new Properties();
        FileInputStream fis = new FileInputStream("config.properties");
        props.load(fis);
    }

    public String getApiKey(){
        return props.getProperty("API_KEY");
    }
}