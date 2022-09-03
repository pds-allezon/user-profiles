package pl.mwisniewski.userprofiles.adapters.dummy;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.mwisniewski.userprofiles.domain.model.UserTag;
import pl.mwisniewski.userprofiles.domain.port.UserProfileRepository;

import java.util.List;

@Component
@Profile("test")
public class DummyUserProfileRepository implements UserProfileRepository {
    @Override
    public List<UserTag> getBuys(String cookie) {
        return List.of();
    }

    @Override
    public List<UserTag> getViews(String cookie) {
        return List.of();
    }

    @Override
    public void addUserTag(UserTag userTag) {
        // Do nothing.
    }
}
