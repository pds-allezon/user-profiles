package pl.mwisniewski.userprofiles.adapters.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.mwisniewski.userprofiles.domain.model.UserTag;

public record UserTagResponse(String time,
                              String cookie,
                              String country,
                              String device,
                              String action,
                              String origin,
                              @JsonProperty("product_info") ProductInfoResponse productInfo) {
    public static UserTagResponse of(UserTag userTag) {
        return new UserTagResponse(
                userTag.time(),
                userTag.cookie(),
                userTag.country(),
                userTag.device().toString(),
                userTag.action().toString(),
                userTag.origin(),
                ProductInfoResponse.of(userTag.productInfo())
        );
    }
}
