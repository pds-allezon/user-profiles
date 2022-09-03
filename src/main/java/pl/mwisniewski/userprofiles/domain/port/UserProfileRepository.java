package pl.mwisniewski.userprofiles.domain.port;

import pl.mwisniewski.userprofiles.domain.model.UserTag;

import java.util.List;

public interface UserProfileRepository {
    List<UserTag> getBuys(String cookie);

    List<UserTag> getViews(String cookie);

    void addUserTag(UserTag userTag);
}
