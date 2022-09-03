package pl.mwisniewski.userprofiles.adapters.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.mwisniewski.userprofiles.domain.UserProfileService;
import pl.mwisniewski.userprofiles.domain.model.UserTag;

@Component
@Profile("prod")
public class KafkaUserTagConsumer {
    final UserProfileService userProfileService;

    public KafkaUserTagConsumer(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @KafkaListener(topics = "user-tags", groupId = "user-profiles")
    void consume(UserTag userTag) {
        logger.debug("Consuming user tag: {}", userTag);
        userProfileService.addUserTag(userTag);
    }

    private final Logger logger = LoggerFactory.getLogger(KafkaUserTagConsumer.class);
}
