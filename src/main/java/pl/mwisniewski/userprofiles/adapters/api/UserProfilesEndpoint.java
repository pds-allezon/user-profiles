package pl.mwisniewski.userprofiles.adapters.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mwisniewski.userprofiles.adapters.redis.RedisUserProfileRepository;
import pl.mwisniewski.userprofiles.domain.UserProfileService;
import pl.mwisniewski.userprofiles.domain.model.TimeRange;
import pl.mwisniewski.userprofiles.domain.model.UserProfile;
import pl.mwisniewski.userprofiles.domain.model.UserTag;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
            @RequestParam(value = "limit", required = false, defaultValue = "200") int limit,
            @RequestBody(required = false) UserProfileResponse expectedResult
    ) {
        TimeRange domainTimeRange = domainTimeRange(timeRangeStr);
        UserProfile userProfile = userProfileService.getProfile(cookie, domainTimeRange, limit);
        UserProfileResponse response = UserProfileResponse.of(userProfile);

        if (!response.equals(expectedResult)) {
            logDifferentAnswers(response, expectedResult);
        }

        return ResponseEntity.ok(response);
    }

    private TimeRange domainTimeRange(String timeRangeStr) {
        String[] splitTimeRange = timeRangeStr.split("_");
        String startTimeRange = splitTimeRange[0] + DEFAULT_TIMEZONE_SUFFIX;
        String endTimeRange = splitTimeRange[1] + DEFAULT_TIMEZONE_SUFFIX;

        return new TimeRange(startTimeRange, endTimeRange);
    }

    private void logDifferentAnswers(UserProfileResponse actualResponse, UserProfileResponse expectedResponse) {
        if (expectedResponse == null) {
            return;
        }

        Set<UserTagResponse> actualBuySet = new HashSet<>(actualResponse.buys());
        Set<UserTagResponse> actualViewSet = new HashSet<>(actualResponse.views());

        Set<UserTagResponse> expectedBuySet = new HashSet<>(expectedResponse.buys());
        Set<UserTagResponse> expectedBuySet2 = new HashSet<>(expectedResponse.buys());
        Set<UserTagResponse> expectedViewSet = new HashSet<>(expectedResponse.views());
        Set<UserTagResponse> expectedViewSet2 = new HashSet<>(expectedResponse.views());

        expectedBuySet.removeAll(actualBuySet);
        expectedViewSet.removeAll(actualViewSet);
        actualBuySet.removeAll(expectedBuySet2);
        actualViewSet.removeAll(expectedViewSet2);

        logger.debug("Different answers!");
        logger.debug("Set difference: actual.buys - expected.buys: {}", actualBuySet);
        logger.debug("Set difference: actual.views - expected.views: {}", actualViewSet);
        logger.debug("Set difference: expected.buys - actual.buys: {}", expectedBuySet);
        logger.debug("Set difference: expected.views - actual.views: {}", expectedViewSet);

        logger.debug("Full diff");
        logger.debug("Actual: {}", actualResponse);
        logger.debug("Expected: {}", expectedResponse);
    }

    private static final String DEFAULT_TIMEZONE_SUFFIX = "Z";
    private final Logger logger = LoggerFactory.getLogger(UserProfilesEndpoint.class);
}
