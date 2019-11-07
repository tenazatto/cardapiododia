import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan("br.com.cardapiododia")
@EnableAutoConfiguration
@EnableMongoRepositories
public class CardapioDoDiaApp {
    public static final String CHROMEDRIVER_PATH = "C:\\Program Files (x86)\\Google\\chromedriver\\chromedriver.exe";

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_PATH);

        SpringApplication.run(CardapioDoDiaApp.class, args);
    }
}
