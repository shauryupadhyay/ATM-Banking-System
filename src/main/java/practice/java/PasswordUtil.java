
package practice.java;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.util.Base64;

public class PasswordUtil {

    public static String hashPin(String pin) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(
                pin.toCharArray(), salt, 210000, 256
        );

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(salt)
                    + ":"
                    + Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new IllegalStateException("PIN hashing failed.", e);
        } finally {
            spec.clearPassword();
        }
    }

    public static boolean verifyPin(String pin, String storedValue) {
        if (storedValue == null) {
            return false;
        }

        try {
            String[] parts = storedValue.split(":");

            if (parts.length != 2) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

            PBEKeySpec spec = new PBEKeySpec(
                    pin.toCharArray(), salt, 210000, 256
            );

            try {
                SecretKeyFactory factory =
                        SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

                byte[] actualHash =
                        factory.generateSecret(spec).getEncoded();

                return MessageDigest.isEqual(expectedHash, actualHash);
            } finally {
                spec.clearPassword();
            }

        } catch (Exception e) {
            return false;
        }
    }
}
