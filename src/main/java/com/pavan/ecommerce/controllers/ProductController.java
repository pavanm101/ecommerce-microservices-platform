package com.pavan.ecommerce.controllers;

import com.pavan.ecommerce.hateoas.ProductResource;
import com.pavan.ecommerce.models.Product;
import com.pavan.ecommerce.models.ProductImage;
import com.pavan.ecommerce.repositories.ProductRepository;
import com.pavan.ecommerce.services.EcommerceService;
import com.pavan.ecommerce.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController extends CoreController {

    @Autowired
    private EcommerceService ecommerceService;

    @Autowired
    private StorageService storageService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    Validator productValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(productValidator);
    }

    @GetMapping
    public List<ProductResource> index() {
        List<Product> res = ecommerceService.getProducts();
        List<ProductResource> output = new ArrayList<ProductResource>();
        res.forEach((p) -> {
            ProductResource pr = new ProductResource(p);
            pr.add(createHateoasLink(p.getId()));
            output.add(pr);
        });
        return output;
    }

    @PostMapping
    public Product create(@RequestBody @Valid Product product) {
        return ecommerceService.saveProduct(product);
    }

    @GetMapping("/{id}")
    public RepresentationModel<?> view(@PathVariable("id") long id) {
        Product p = ecommerceService.getProduct(id);
        ProductResource pr = new ProductResource(p);
        pr.add(createHateoasLink(p.getId()));
        return pr;
    }

    @PostMapping(value = "/{id}")
    public Product edit(@PathVariable("id") long id, @RequestBody @Valid Product product) {
        Product updatedProduct = ecommerceService.getProduct(id);

        if (updatedProduct == null) {
            return null;
        }

        updatedProduct.setName(product.getName());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setDescription(product.getDescription());

        return ecommerceService.saveProduct(updatedProduct);
    }

    @GetMapping("/{id}/images")
    public List<ProductImage> viewImages(@PathVariable("id") String productId) {
        return entityManager.createQuery(
                "FROM ProductImage WHERE productId = :product_id", ProductImage.class)
                .setParameter("product_id", Long.parseLong(productId))
                .getResultList();
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable("id") String id) {
        ProductImage image = entityManager.find(ProductImage.class, Long.parseLong(id));

        // Relative path to StorageProperties.rootLocation
        String path = "product-images/" + image.getProductId() + "/";

        Resource file = storageService.loadAsResource(path + image.getPath());
        String mimeType = "image/png";
        try {
            mimeType = file.getURL().openConnection().getContentType();
        } catch (IOException e) {
            System.out.println("Can't get file mimeType. " + e.getMessage());
        }
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .body(file);
    }

    @PostMapping("/{id}/uploadimage")
    public String handleFileUpload(@PathVariable("id") String id, @RequestParam("file") MultipartFile file) {
        // Relative path to the rootLocation in storageService
        String path = "/product-images/" + id;
        String filename = storageService.store(file, path);
        return ecommerceService.addProductImage(id, filename);
    }
}
