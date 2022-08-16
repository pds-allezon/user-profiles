package pl.mwisniewski.userprofiles.adapters.dummy;

import org.springframework.stereotype.Component;
import pl.mwisniewski.userprofiles.domain.model.TimeRange;
import pl.mwisniewski.userprofiles.domain.model.UserProfile;
import pl.mwisniewski.userprofiles.domain.port.UserProfileProvider;

import java.util.List;

@Component
public class DummyUserProfileProvider implements UserProfileProvider {
    @Override
    public UserProfile getProfile(String cookie, TimeRange timeRange, int limit) {
        return new UserProfile(
                cookie,
                List.of(),
                List.of()
        );
    }
}
