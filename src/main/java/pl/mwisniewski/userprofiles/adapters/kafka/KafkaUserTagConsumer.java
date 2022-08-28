package pl.mwisniewski.userprofiles.adapters.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.mwisniewski.userprofiles.domain.model.UserTag;
import pl.mwisniewski.userprofiles.domain.port.UserProfileProvider;

@Component
@Profile("prod")
public class KafkaUserTagConsumer {
    final
    UserProfileProvider userProfileProvider;

    public KafkaUserTagConsumer(UserProfileProvider userProfileProvider) {
        this.userProfileProvider = userProfileProvider;
    }

    @KafkaListener(topics = "user-tags", groupId = "user-profiles")
    void consume(UserTag userTag) {
        logger.info("Consuming user tag: {}", userTag);
        userProfileProvider.addUserTag(userTag);
    }

    private final Logger logger = LoggerFactory.getLogger(KafkaUserTagConsumer.class);
}
