package st.ilu.rms4csw.security.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import st.ilu.rms4csw.model.user.User;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

/**
 * @author Mischa Holz
 */
public class TokenHandler {

    private static final String HMAC_ALGO = "HmacSHA256";
    private static final String SEPARATOR = ",";
    private static final String SEPARATOR_SPLITTER = ",";

    private ObjectMapper objectMapper;


    private Mac mac;

    public TokenHandler(byte[] secret) {
        this.objectMapper = new ObjectMapper();

        try {
            mac = Mac.getInstance(HMAC_ALGO);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        try {
            mac.init(new SecretKeySpec(secret, HMAC_ALGO));
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public String createTokenForUser(User user, long validFor) {
        Token token = new Token();
        token.setLoginName(user.getLoginName());
        token.setId(user.getId());
        token.setRole(user.getRole());

        Date in3Minutes = new Date((new Date()).getTime() + validFor);

        token.setExpires(in3Minutes);

        byte[] userBytes = toJson(token);
        byte[] hash = createHmac(userBytes);

        return toBase64(userBytes) + SEPARATOR + toBase64(hash);
    }

    public Token validateToken(String strToken) {
        String[] parts = strToken.split(SEPARATOR_SPLITTER);
        if(parts.length != 2 || parts[0].length() <= 0 || parts[1].length() <= 0) {
            throw new TokenFormatException("Could not parse the token. It consists of 2 parts separated by " + SEPARATOR);
        }

        byte[] userBytes = fromBase64(parts[0]);
        byte[] hash = fromBase64(parts[1]);

        if(validateHash(userBytes, hash)) {
            try {
                Token token = objectMapper.readValue(userBytes, Token.class);

                if(new Date().before(token.getExpires())) {
                    return token;
                }

                throw new TokenExpiredException("This token is already expired");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        throw new TokenHashException("Invalid hash");
    }

    private byte[] toJson(Token token) {
        try {
            return objectMapper.writeValueAsBytes(token);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized byte[] createHmac(byte[] content) {
        return mac.doFinal(content);
    }

    private boolean validateHash(byte[] userBytes, byte[] hash) {
        return Arrays.equals(createHmac(userBytes), hash);
    }

    private String toBase64(byte[] content) {
        return Base64.getEncoder().encodeToString(content);
    }

    private byte[] fromBase64(String content) {
        return Base64.getDecoder().decode(content);
    }

}
