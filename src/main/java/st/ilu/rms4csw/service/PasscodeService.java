package st.ilu.rms4csw.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mischa Holz
 *
 */
@Service
public class PasscodeService {

    @Value("${emojis}")
    private String emojis;

    private boolean isMarker(char ch) {
        return ch == 0xD83C || ch == 0xD83D || ch == 0xD83E;
    }

    public List<String> getPossibleCharacters() {
        return splitEmojiString(this.emojis);
    }

    public List<String> splitEmojiString(String string) {
        List<String> characters = new ArrayList<>();

        char[] chars = string.toCharArray();
        char ch1;
        char ch2;

        for(int i = 0; i < chars.length - 1; i++) {
            ch1 = chars[i];

            if(i == 0 && !isMarker(ch1)) {
                characters.add("" + ch1);
            } else if(isMarker(ch1)) {
                ch2 = chars[i + 1];

                characters.add("" + ch1 + ch2);
                i++;
            } else if(chars[i + 1] == 65039) {
                ch2 = chars[i + 1];

                characters.add("" + ch1 + ch2);
                i++;
            } else if(i >= 1 && !isMarker(chars[i - 1])) {
                characters.add("" + ch1);
            }
        }

        return characters;
    }

    public String generateRandomPassword() {
        SecureRandom random = new SecureRandom();

        List<String> characters = new ArrayList<>();
        List<String> possibleCharacters = getPossibleCharacters();

        for(int i = 0; i < 5; i++) {
            int index = random.nextInt(possibleCharacters.size());

            characters.add(possibleCharacters.get(index));
        }

        return characters.stream().reduce("", (a, b) -> a + b);
    }

}
