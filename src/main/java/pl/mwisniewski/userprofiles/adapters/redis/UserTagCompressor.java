package pl.mwisniewski.userprofiles.adapters.redis;

import pl.mwisniewski.userprofiles.domain.model.Action;
import pl.mwisniewski.userprofiles.domain.model.Device;
import pl.mwisniewski.userprofiles.domain.model.ProductInfo;
import pl.mwisniewski.userprofiles.domain.model.UserTag;

public class UserTagCompressor {
    public String compress(UserTag userTag) {
        return "%s$%s$%s$%s$%s$%s$%s$%s".formatted(
                userTag.time(),
                userTag.country(),
                userTag.device().toString(),
                userTag.origin(),
                userTag.productInfo().productId(),
                userTag.productInfo().brandId(),
                userTag.productInfo().categoryId(),
                userTag.productInfo().price()
        );
    }

    public UserTag decompress(String cookie, Action action, String compressedUserTag) {
        String[] parts = compressedUserTag.split("\\$");
        return new UserTag(
                parts[0],
                cookie,
                parts[1],
                Device.valueOf(parts[2]),
                action,
                parts[3],
                new ProductInfo(
                        parts[4],
                        parts[5],
                        parts[6],
                        Integer.parseInt(parts[7])
                )
        );
    }
}
