package com.github.janbnz;

import net.simon04.webpush.CryptoService;
import net.simon04.webpush.PushController;
import net.simon04.webpush.ServerKeys;
import net.simon04.webpush.dto.Subscription;
import net.simon04.webpush.dto.SubscriptionKeys;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class WebPush {

    /*
     * You can generate your vapid keys with a generator like https://www.attheminute.com/vapid-key-generator
     * Don't forget to add your public key in the frontend
     */
    private static final String VAPID_PUBLIC_KEY = "YOUR_PUBLIC_KEY";
    private static final String VAPID_PRIVATE_KEY = "YOUR_PRIVATE_KEY";

    public static int push(String text, JSONObject data) {
        final String endpoint = data.getString("endpoint");
        final String p256dh = data.getJSONObject("keys").getString("p256dh");
        final String auth = data.getJSONObject("keys").getString("auth");

        try {
            final PushController controller = new PushController(new ServerKeys(VAPID_PUBLIC_KEY, VAPID_PRIVATE_KEY));
            final SubscriptionKeys keys = new SubscriptionKeys(p256dh, auth);
            final Subscription subscription = new Subscription(endpoint, 60 * 60L, keys);

            HttpClient client = HttpClient.newBuilder().build();

            byte[] message = new CryptoService().encrypt(text, keys, 0);
            HttpRequest request = controller.prepareRequest(subscription, message).build();

            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode();
        } catch (IOException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException |
                 InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
                 InterruptedException ex) {
            ex.printStackTrace();
        }
        return 500;
    }
}