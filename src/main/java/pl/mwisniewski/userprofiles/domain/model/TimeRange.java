package pl.mwisniewski.userprofiles.domain.model;

public record TimeRange(
        long startTimestamp,
        long endTimestamp
) {
}
