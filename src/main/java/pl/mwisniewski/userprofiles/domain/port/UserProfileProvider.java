package pl.mwisniewski.userprofiles.domain.port;

import pl.mwisniewski.userprofiles.domain.model.TimeRange;
import pl.mwisniewski.userprofiles.domain.model.UserProfile;
import pl.mwisniewski.userprofiles.domain.model.UserTag;

public interface UserProfileProvider {
    UserProfile getProfile(String cookie, TimeRange timeRange, int limit);

    void addUserTag(UserTag userTag);
}
