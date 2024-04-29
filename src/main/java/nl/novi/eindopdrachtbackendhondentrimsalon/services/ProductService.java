package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.RecordNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Product;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    //Retrieving product details
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Product not found with ID: " + productId));
    }


    //Adding new products to the system
    public Product addProduct(Product product) {
        //Perform any additional validation or business logic before saving the product
        return productRepository.save(product);
    }

    //Updating product information (e.g. name, price, stock)
    public void updateProduct(Long productId, Product updatedProduct) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product existingProduct = productOptional.get();

            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStock(updatedProduct.getStock());

            productRepository.save(existingProduct);
        } else {
            throw new RecordNotFoundException("Product not found with id: " + productId); //Product not found
        }
    }

    public void deleteProduct(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            productRepository.deleteById(productId);
        } else {
            throw new RecordNotFoundException("Product not wound with id: " + productId);
        }

        //Managing product inventory (e.g. adjusting stock levels)
        //Associating products with appointments
    }
}