package com.pavan.ecommerce.hateoas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pavan.ecommerce.models.GroupVariant;
import com.pavan.ecommerce.models.ProductGroup;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class GroupResource extends RepresentationModel<GroupResource> {
    @JsonProperty
    public long id;
    public String groupName;
    public String price;
    public List<GroupVariant> variants;

    public GroupResource(ProductGroup group) {
        this.id = group.getId();
        this.groupName = group.getGroupName();
        this.price = group.getPrice();
        this.variants = group.getGroupVariants();
    }
}
