package pl.mwisniewski.userprofiles.adapters.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.mwisniewski.userprofiles.domain.model.ProductInfo;

public record ProductInfoResponse(@JsonProperty("product_id") String productId,
                                  @JsonProperty("brand_id") String brandId,
                                  @JsonProperty("category_id") String categoryId,
                                  int price) {
    public static ProductInfoResponse of(ProductInfo productInfo) {
        return new ProductInfoResponse(
                productInfo.productId(),
                productInfo.brandId(),
                productInfo.categoryId(),
                productInfo.price()
        );
    }
}
