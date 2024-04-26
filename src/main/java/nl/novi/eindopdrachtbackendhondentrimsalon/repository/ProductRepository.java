package nl.novi.eindopdrachtbackendhondentrimsalon.repository;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    public Optional<Product> findById(Long productId) {
    }

    public List<Product> findAll() {
    }

    public Product save(Product product) {
    }
}
