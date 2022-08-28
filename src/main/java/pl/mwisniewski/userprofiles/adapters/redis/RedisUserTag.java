package pl.mwisniewski.userprofiles.adapters.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import pl.mwisniewski.userprofiles.domain.model.Action;
import pl.mwisniewski.userprofiles.domain.model.Device;
import pl.mwisniewski.userprofiles.domain.model.ProductInfo;
import pl.mwisniewski.userprofiles.domain.model.UserTag;

@RedisHash("RedisUserTag")
public record RedisUserTag(
        @Id
        String cookie,
        String time,
        String country,
        String device,
        String action,
        String origin,
        String productId,
        String brandId,
        String categoryId,
        int price
) {
    public static RedisUserTag of(UserTag userTag) {
        return new RedisUserTag(
                userTag.cookie(),
                userTag.time(),
                userTag.country(),
                userTag.device().toString(),
                userTag.action().toString(),
                userTag.origin(),
                userTag.productInfo().productId(),
                userTag.productInfo().brandId(),
                userTag.productInfo().categoryId(),
                userTag.productInfo().price()
        );
    }

    public UserTag toDomain() {
        return new UserTag(
                time, cookie, country, Device.valueOf(device), Action.valueOf(action), origin,
                new ProductInfo(productId, brandId, categoryId, price)
        );
    }
}
