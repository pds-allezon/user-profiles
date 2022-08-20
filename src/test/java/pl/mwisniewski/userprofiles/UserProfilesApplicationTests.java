package pl.mwisniewski.userprofiles;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import pl.mwisniewski.userprofiles.adapters.api.ProductInfoResponse;
import pl.mwisniewski.userprofiles.adapters.api.UserProfileResponse;
import pl.mwisniewski.userprofiles.adapters.api.UserProfilesEndpoint;
import pl.mwisniewski.userprofiles.adapters.api.UserTagResponse;

import java.util.List;

@SpringBootTest
class UserProfilesApplicationTests {

    @Autowired
    private UserProfilesEndpoint endpoint;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldReturnExpectedResultIfPresent() {
        // given
        String cookie = "cookie";
        String timeRange = "2022-03-22T12:15:00.000_2022-03-22T12:30:00.000";
        int limit = 5;
        UserTagResponse expectedView = new UserTagResponse(
                "2022-03-22T12:15:00.000Z",
                "some-cookie",
                "POLAND",
                "PC",
                "VIEW",
                "campaign1",
                new ProductInfoResponse(
                        "product1", "brand1", "category1", 11
                )
        );
        UserTagResponse expectedBuy = new UserTagResponse(
                "2022-03-22T12:15:01.000Z",
                "some-cookie",
                "USA",
                "TV",
                "BUY",
                "campaign2",
                new ProductInfoResponse(
                        "product2", "brand1", "category1", 12
                )
        );
        UserProfileResponse expectedResult = new UserProfileResponse(
                "some-cookie",
                List.of(expectedView),
                List.of(expectedBuy)
        );

        // when
        ResponseEntity<UserProfileResponse> response = endpoint.userProfiles(
                cookie, timeRange, limit, expectedResult
        );

        // then
        assert response.getBody() == expectedResult;
    }
}
