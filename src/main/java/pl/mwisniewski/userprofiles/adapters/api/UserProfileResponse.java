package pl.mwisniewski.userprofiles.adapters.api;

import pl.mwisniewski.userprofiles.domain.model.UserProfile;

import java.util.List;

public record UserProfileResponse(
        String cookie,
        List<UserTagResponse> views,
        List<UserTagResponse> buys
) {
    public static UserProfileResponse of(UserProfile userProfile) {
        return new UserProfileResponse(
                userProfile.cookie(),
                userProfile.views().stream().map(UserTagResponse::of).toList(),
                userProfile.buys().stream().map(UserTagResponse::of).toList()
        );
    }
}
