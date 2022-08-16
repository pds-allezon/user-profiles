package pl.mwisniewski.userprofiles.domain;

import org.springframework.stereotype.Component;
import pl.mwisniewski.userprofiles.domain.model.TimeRange;
import pl.mwisniewski.userprofiles.domain.model.UserProfile;
import pl.mwisniewski.userprofiles.domain.port.UserProfileProvider;

@Component
public class UserProfileService {

    private final UserProfileProvider userProfileProvider;

    public UserProfileService(UserProfileProvider userProfileProvider) {
        this.userProfileProvider = userProfileProvider;
    }

    public UserProfile getProfile(String cookie, TimeRange timeRange, int limit) {
        return userProfileProvider.getProfile(cookie, timeRange, limit);
    }
}
