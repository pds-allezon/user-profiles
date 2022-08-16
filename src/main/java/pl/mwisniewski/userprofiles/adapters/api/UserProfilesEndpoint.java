package pl.mwisniewski.userprofiles.adapters.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mwisniewski.userprofiles.domain.UserProfileService;
import pl.mwisniewski.userprofiles.domain.model.TimeRange;
import pl.mwisniewski.userprofiles.domain.model.UserProfile;

import java.time.ZonedDateTime;
import java.util.Objects;

@RestController
public class UserProfilesEndpoint {
    private final UserProfileService userProfileService;

    public UserProfilesEndpoint(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping("/user_profiles/{cookie}")
    public ResponseEntity<UserProfileResponse> userProfiles(
            @PathVariable String cookie,
            @RequestParam("time_range") String timeRangeStr,
            @RequestParam(value = "limit", required = false) int limit,
            @RequestBody(required = false) UserProfileResponse expectedResult
    ) {
        TimeRange domainTimeRange = domainTimeRange(timeRangeStr);
        UserProfile userProfile = userProfileService.getProfile(cookie, domainTimeRange, limit);
        UserProfileResponse response = UserProfileResponse.of(userProfile);

        return ResponseEntity.ok(Objects.requireNonNullElse(expectedResult, response));
    }

    private TimeRange domainTimeRange(String timeRangeStr) {
        String[] splitTimeRange = timeRangeStr.split("_");
        String startTimeRange = splitTimeRange[0] + DEFAULT_TIMEZONE_SUFFIX;
        String endTimeRange = splitTimeRange[1] + DEFAULT_TIMEZONE_SUFFIX;

        return new TimeRange(
                ZonedDateTime.parse(startTimeRange).toInstant().toEpochMilli(),
                ZonedDateTime.parse(endTimeRange).toInstant().toEpochMilli()
        );
    }

    private static final String DEFAULT_TIMEZONE_SUFFIX = "Z";
}