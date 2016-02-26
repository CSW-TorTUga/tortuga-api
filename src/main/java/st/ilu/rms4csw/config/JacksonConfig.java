package st.ilu.rms4csw.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import st.ilu.rms4csw.controller.base.ChangeSet;
import st.ilu.rms4csw.model.base.PersistentEntity;

import java.io.IOException;

/**
 * @author Mischa Holz
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Module jdk8Module() {
        return new Jdk8Module();
    }


    @Bean
    public Module changeSetModule() {
        SimpleModule ret = new SimpleModule();

        ret.addDeserializer(ChangeSet.class, new JsonDeserializer<ChangeSet>() {
            @Override
            public ChangeSet deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                ChangeSet<PersistentEntity> ret = new ChangeSet<>();

                JsonNode node = p.readValueAsTree();

                node.fieldNames().forEachRemaining(n -> ret.getPatchedFields().add(n));

                JsonParser parser = node.traverse(p.getCodec());

                JavaType type = deserializationContext.getContextualType();

                return ret;
            }
        });

        return ret;
    }

}
