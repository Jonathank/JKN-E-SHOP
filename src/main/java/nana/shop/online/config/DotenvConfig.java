/**
 * 
 */
package nana.shop.online.config;


import io.github.cdimascio.dotenv.Dotenv;
/**
 * @author JONATHAN
 */

public class DotenvConfig {
    private static final Dotenv dotenv = Dotenv.load();

    public static void loadEnvToSystem() {
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
}
