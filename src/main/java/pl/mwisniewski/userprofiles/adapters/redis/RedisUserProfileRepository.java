package pl.mwisniewski.userprofiles.adapters.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.mwisniewski.userprofiles.domain.model.Action;
import pl.mwisniewski.userprofiles.domain.model.UserTag;
import pl.mwisniewski.userprofiles.domain.port.UserProfileRepository;
import redis.clients.jedis.util.MurmurHash;

import java.util.List;

@Repository
@Profile("prod")
public class RedisUserProfileRepository implements UserProfileRepository {
    private static final int MAX_USER_TAGS_NUMBER = 200;

    private final RedisTemplate<String, String> template1;

    private final RedisTemplate<String, String> template2;

    private final UserTagCompressor compressor;

    private final MurmurHash hashingFunc;

    public RedisUserProfileRepository(@Qualifier("instance1") RedisTemplate<String, String> template1,
                                      @Qualifier("instance2") RedisTemplate<String, String> template2) {
        this.template1 = template1;
        this.template2 = template2;
        this.compressor = new UserTagCompressor();
        this.hashingFunc = new MurmurHash();
    }

    @Override
    public List<UserTag> getBuys(String cookie) {
        RedisTemplate<String, String> template = chooseInstance(cookie);
        String buyKey = makeKey(cookie, Action.BUY.toString());
        List<String> buyUserTags = template.opsForList().range(buyKey, 0, -1);

        return buyUserTags.stream().map(it ->
                compressor.decompress(cookie, Action.BUY, it)
        ).toList();
    }

    @Override
    public List<UserTag> getViews(String cookie) {
        RedisTemplate<String, String> template = chooseInstance(cookie);
        String viewKey = makeKey(cookie, Action.VIEW.toString());
        List<String> viewUserTags = template.opsForList().range(viewKey, 0, -1);

        return viewUserTags.stream().map(it ->
                compressor.decompress(cookie, Action.VIEW, it)
        ).toList();
    }

    @Override
    public void addUserTag(UserTag userTag) {
        RedisTemplate<String, String> template = chooseInstance(userTag.cookie());

        String key = makeKey(userTag.cookie(), userTag.action().toString());
        template.opsForList().leftPush(key, compressor.compress(userTag));
        template.opsForList().trim(key, 0, MAX_USER_TAGS_NUMBER - 1);
    }

    private RedisTemplate<String, String> chooseInstance(String cookie) {
        long mod = hash(cookie) % 2L;
        if (mod == 0L) {
            return template1;
        } else {
            return template2;
        }
    }

    private long hash(String cookie) {
        return hashingFunc.hash(cookie);
    }

    private String makeKey(String cookie, String action) {
        return "%s-%s".formatted(cookie, action);
    }

    private final Logger logger = LoggerFactory.getLogger(RedisUserProfileRepository.class);
}
