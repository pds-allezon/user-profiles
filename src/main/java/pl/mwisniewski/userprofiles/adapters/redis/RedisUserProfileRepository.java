package pl.mwisniewski.userprofiles.adapters.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.mwisniewski.userprofiles.domain.model.Action;
import pl.mwisniewski.userprofiles.domain.model.TimeRange;
import pl.mwisniewski.userprofiles.domain.model.UserProfile;
import pl.mwisniewski.userprofiles.domain.model.UserTag;
import pl.mwisniewski.userprofiles.domain.port.UserProfileProvider;

import java.util.Comparator;
import java.util.List;

@Repository
@Profile("prod")
public class RedisUserProfileRepository implements UserProfileProvider {
    private static final int MAX_USER_TAGS_NUMBER = 200;

    private final RedisTemplate<String, RedisUserTag> template;

    public RedisUserProfileRepository(RedisTemplate<String, RedisUserTag> template) {
        this.template = template;
    }

    @Override
    public UserProfile getProfile(String cookie, TimeRange timeRange, int limit) {
        String buyKey = makeKey(cookie, Action.BUY.toString());
        String viewKey = makeKey(cookie, Action.VIEW.toString());

        // TODO: can be optimized with pipelining.
        List<RedisUserTag> buyUserTags = template.opsForList().range(buyKey, 0, -1);
        List<RedisUserTag> viewUserTags = template.opsForList().range(viewKey, 0, -1);

        buyUserTags = filterAndSort(buyUserTags, timeRange, limit);
        viewUserTags = filterAndSort(viewUserTags, timeRange, limit);

        return new UserProfile(
                cookie,
                viewUserTags.stream().map(RedisUserTag::toDomain).toList(),
                buyUserTags.stream().map(RedisUserTag::toDomain).toList()
        );
    }

    @Override
    public void addUserTag(UserTag userTag) {
        logger.debug("Adding user tag to redis: {}", userTag);

        String key = makeKey(userTag.cookie(), userTag.action().toString());
        template.opsForList().leftPush(key, RedisUserTag.of(userTag));
        template.opsForList().trim(key, 0, MAX_USER_TAGS_NUMBER - 1);
    }

    private String makeKey(String cookie, String action) {
        return "%s-%s".formatted(cookie, action);
    }

    private List<RedisUserTag> filterAndSort(List<RedisUserTag> userTagList, TimeRange timeRange, int limit) {
        Comparator<String> customTimeComparator = new CustomTimeComparator().reversed();
        Comparator<RedisUserTag> customUserTagComparator = new CustomUserTagComparator().reversed();

        return userTagList.stream()
                .filter(it ->
                        customTimeComparator.compare(it.time(), timeRange.startTime()) >= 0 &&
                                customTimeComparator.compare(it.time(), timeRange.endTime()) < 0
                )
                .sorted(customUserTagComparator)
                .limit(limit)
                .toList();
    }

    private final Logger logger = LoggerFactory.getLogger(RedisUserProfileRepository.class);

    private static class CustomUserTagComparator implements Comparator<RedisUserTag> {
        public int compare(RedisUserTag tag1, RedisUserTag tag2) {
            Comparator<String> timeComparator = new CustomTimeComparator();
            return timeComparator.compare(tag1.time(), tag2.time());
        }
    }

    private static class CustomTimeComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.substring(0, o1.length() - 1).compareTo(o2.substring(0, o2.length() - 1));
        }
    }
}
