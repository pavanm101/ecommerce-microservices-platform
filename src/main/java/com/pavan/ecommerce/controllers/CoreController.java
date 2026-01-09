package com.pavan.ecommerce.controllers;

import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class CoreController {
    protected Link createHateoasLink(long id) {
        Link link = linkTo(getClass()).slash(id).withSelfRel();
        return link;
    }
}
