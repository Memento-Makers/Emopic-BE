package mmm.emopic.app.domain.photo.support;

import com.deepl.api.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class DeeplTranslator {

    @Value("${deepl-auth-key}")
    private String authKey;

    private Translator translator ;


    public String translate(String requiredTranslateText) throws Exception{
        translator = new Translator(authKey);
        TextResult result = translator.translateText(requiredTranslateText,"EN" , "KO");
        System.out.println(result.getText());
        return result.getText();
    }
}
