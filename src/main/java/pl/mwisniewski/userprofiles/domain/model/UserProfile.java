package pl.mwisniewski.userprofiles.domain.model;

import java.util.List;

public record UserProfile(
        String cookie,
        List<UserTag> views,
        List<UserTag> buys
) {
}
