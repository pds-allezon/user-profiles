package pl.mwisniewski.userprofiles.adapters.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mwisniewski.userprofiles.domain.UserProfileService;
import pl.mwisniewski.userprofiles.domain.model.*;

import java.util.Objects;

@RestController
public class TestApi {
    private final UserProfileService userProfileService;

    public TestApi(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping("/test")
    public ResponseEntity<UserProfileResponse> userProfiles(
    ) {
        TimeRange timeRange = new TimeRange("2022-03-22T12:15:00.000Z", "2022-03-22T12:30:00.000Z");
        userProfileService.getProfile("test", timeRange, 100);

        UserTag userTag = new UserTag(
                "test",
                "test",
                "test",
                Device.TV,
                Action.BUY,
                "test",
                new ProductInfo(
                        "test",
                        "test",
                        "test",
                        123
                )
        );
        userProfileService.addUserTag(userTag);

        return ResponseEntity.ok(null);
    }
}
