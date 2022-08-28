package pl.mwisniewski.userprofiles.adapters.redis;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.mwisniewski.userprofiles.domain.model.Action;
import pl.mwisniewski.userprofiles.domain.model.TimeRange;
import pl.mwisniewski.userprofiles.domain.model.UserProfile;
import pl.mwisniewski.userprofiles.domain.model.UserTag;
import pl.mwisniewski.userprofiles.domain.port.UserProfileProvider;

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
        List<RedisUserTag> buyUserTags = template.opsForList().range(buyKey, 0, limit - 1);
        List<RedisUserTag> viewUserTags = template.opsForList().range(viewKey, 0, limit - 1);

        return new UserProfile(
                cookie,
                viewUserTags.stream().map(RedisUserTag::toDomain).toList(),
                buyUserTags.stream().map(RedisUserTag::toDomain).toList()
        );
    }

    @Override
    public void addUserTag(UserTag userTag) {
        String key = makeKey(userTag.cookie(), userTag.action().toString());
        template.opsForList().leftPush(key, RedisUserTag.of(userTag));
        template.opsForList().trim(key, 0, MAX_USER_TAGS_NUMBER - 1);
    }

    private String makeKey(String cookie, String action) {
        return "%s-%s".formatted(cookie, action);
    }
}
