package pl.mwisniewski.userprofiles.domain.model;

public record ProductInfo(String productId,
                          String brandId,
                          String categoryId,
                          int price) {
}
