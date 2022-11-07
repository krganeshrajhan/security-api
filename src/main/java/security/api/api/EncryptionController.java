package security.api.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.*;
import java.security.*;

@RestController
@RequestMapping("/api/encryption")
@Slf4j
public class EncryptionController {

    private static Cipher cipher = getCipher();
    private static SecretKey secretKey = getSecretKey();

    public static Cipher getCipher() {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchPaddingException e) {
            log.error("Exception thrown: {}", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error("Exception thrown: {}", e.getMessage());
        }
        return cipher;
    }

    public static SecretKey getSecretKey() {
        KeyGenerator key = null;
        try {
            key = KeyGenerator.getInstance("AES");
            key.init(128);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return key.generateKey();
    }

    @GetMapping("/encrypt/{message}")
    public ResponseEntity<String> encryptMessage(@PathVariable("message") String message)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        log.info("Encrypt String");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] messageInBytes = message.getBytes();
        byte[] cipherTextInBytes = cipher.doFinal(messageInBytes);
        String cipherText = Base64.encodeBase64String(cipherTextInBytes);
        return ResponseEntity.ok(cipherText);
    }

    @GetMapping("/decrypt")
    public ResponseEntity<String> decryptMessage(@RequestHeader("encryptedString") String encryptedString)
            throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        log.info("Decrypt String");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] messageInBytes = encryptedString.getBytes();
        byte[] cipherTextInBytes = cipher.doFinal(messageInBytes);
        String cipherText = Base64.encodeBase64String(cipherTextInBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedText = cipher.doFinal(Base64.decodeBase64(cipherText));
        return ResponseEntity.ok(String.valueOf(decryptedText));
    }

    @GetMapping("hash/{message}")
    public ResponseEntity<String> hashMessage(@PathVariable("message") String message) throws NoSuchAlgorithmException {
        byte[] salt = getSalt();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt);
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i]&0xff)+0x100,16).substring(1));
            }
            return ResponseEntity.ok(sb.toString());
        } catch (Exception e) {
            log.error("Exception occured: {}", e.getMessage());
        }
        return (ResponseEntity<String>) ResponseEntity.internalServerError();
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    @GetMapping("/encoder/{password}")
    public ResponseEntity<String> encodePassword(@PathVariable("password") String password) {
        log.info("Encoding password");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return ResponseEntity.ok(passwordEncoder.encode(password));
    }
}
