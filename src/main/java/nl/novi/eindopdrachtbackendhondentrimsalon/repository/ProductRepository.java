package nl.novi.eindopdrachtbackendhondentrimsalon.repository;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    public Optional<Product> findById(Long productId) {
    }

    public List<Product> findAll() {
    }

    public Product save(Product product) {
    }
}
