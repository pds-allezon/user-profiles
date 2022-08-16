package pl.mwisniewski.userprofiles.domain.port;

import pl.mwisniewski.userprofiles.domain.model.TimeRange;
import pl.mwisniewski.userprofiles.domain.model.UserProfile;

public interface UserProfileProvider {
    UserProfile getProfile(String cookie, TimeRange timeRange, int limit);
}
