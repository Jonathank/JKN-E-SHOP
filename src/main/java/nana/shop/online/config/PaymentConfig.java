/**
 * 
 */
package nana.shop.online.config;

import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * @author JONATHAN
 */
@Configuration
public class PaymentConfig {

    private static final Dotenv dotenv = Dotenv.load();

    public final String STRIPE_SECRET_KEY = dotenv.get("STRIPE_SECRET_KEY");
    public final String STRIPE_PUBLISHABLE_KEY = dotenv.get("STRIPE_PUBLISHABLE_KEY");

    public final String RAZORPAY_KEY_ID = dotenv.get("RAZORPAY_KEY_ID");
    public final String RAZORPAY_SECRET = dotenv.get("RAZORPAY_SECRET");
}
