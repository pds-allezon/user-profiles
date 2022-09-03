package pl.mwisniewski.userprofiles.domain;

import org.springframework.stereotype.Component;
import pl.mwisniewski.userprofiles.domain.model.TimeRange;
import pl.mwisniewski.userprofiles.domain.model.UserProfile;
import pl.mwisniewski.userprofiles.domain.model.UserTag;
import pl.mwisniewski.userprofiles.domain.port.UserProfileRepository;

import java.util.Comparator;
import java.util.List;

@Component
public class UserProfileService {

    private final UserProfileRepository userProfileProvider;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileProvider = userProfileRepository;
    }

    public UserProfile getProfile(String cookie, TimeRange timeRange, int limit) {
        List<UserTag> buys = userProfileProvider.getBuys(cookie);
        List<UserTag> views = userProfileProvider.getViews(cookie);

        buys = filterAndSort(buys, timeRange, limit);
        views = filterAndSort(views, timeRange, limit);

        return new UserProfile(cookie, views, buys);
    }

    public void addUserTag(UserTag userTag) {
        userProfileProvider.addUserTag(userTag);
    }

    private List<UserTag> filterAndSort(List<UserTag> userTags, TimeRange timeRange, int limit) {
        Comparator<String> customTimeComparator = new CustomTimeComparator();
        Comparator<UserTag> customUserTagComparator = new CustomUserTagComparator();

        return userTags.stream()
                .filter(it ->
                        customTimeComparator.compare(it.time(), timeRange.startTime()) >= 0 &&
                                customTimeComparator.compare(it.time(), timeRange.endTime()) < 0
                )
                .sorted(customUserTagComparator.reversed())
                .limit(limit)
                .toList();
    }

    private static class CustomUserTagComparator implements Comparator<UserTag> {
        public int compare(UserTag tag1, UserTag tag2) {
            Comparator<String> timeComparator = new CustomTimeComparator();
            return timeComparator.compare(tag1.time(), tag2.time());
        }
    }

    private static class CustomTimeComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            if (o1.length() < o2.length()) {
                o1 = String.format("%s.000Z", o1.substring(0, o1.length() - 1));
            }
            else if (o2.length() < o1.length()) {
                o2 = String.format("%s.000Z", o2.substring(0, o2.length() - 1));
            }

            return o1.compareTo(o2);
        }
    }
}
