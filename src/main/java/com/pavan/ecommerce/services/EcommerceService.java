package com.pavan.ecommerce.services;

import com.pavan.ecommerce.models.Order;
import com.pavan.ecommerce.models.Product;
import com.pavan.ecommerce.models.ProductGroup;
import com.pavan.ecommerce.models.ProductImage;
import com.pavan.ecommerce.repositories.GroupRepository;
import com.pavan.ecommerce.repositories.OrderRepository;
import com.pavan.ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Service
public class EcommerceService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    OrderRepository orderRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /* PRODUCT */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public String addProductImage(final String productId, final String filename) {
        ProductImage image = new ProductImage();
        image.setProductId(Long.parseLong(productId));
        image.setPath(filename);

        try {
            entityManager.persist(image);
            return String.valueOf(image.getId());
        } catch (Exception e) {
            System.out.print("Error saving product image: " + e.getMessage());
        }
        return "";
    }

    /* GROUPS */
    public List<ProductGroup> getGroups() {
        return groupRepository.findAll();
    }

    public ProductGroup getGroup(long id) {
        return groupRepository.findById(id).orElse(null);
    }

    public ProductGroup saveGroup(ProductGroup group) {
        return groupRepository.save(group);
    }

    /* ORDERS */
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
}
