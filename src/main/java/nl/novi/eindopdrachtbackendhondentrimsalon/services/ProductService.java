package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.ProductDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.ProductNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.RecordNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.ProductMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Product;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.productsToProductDtos(products);
    }

    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Product not found with ID: " + productId));
        return productMapper.productToProductDto(product);
    }

    public List<ProductDto> findProductByName(String name) {
        List<Product> products = productRepository.findByNameIgnoreCase(name);
        return products.stream()
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }

    public ProductDto addProduct(String name, double price, int stock) {
        if (name == null || name.trim().isEmpty() || price <= 0) {
            throw new IllegalArgumentException("Product name and price are required.");
        }

        List<Product> products = productRepository.findByNameIgnoreCase(name);
        if (!products.isEmpty()) {
            throw new IllegalArgumentException("Product with this name already exists.");
        }

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);

        Product newProduct = productRepository.save(product);

        return productMapper.productToProductDto(newProduct);
    }


    public ProductDto updateProduct(Long productId, String name, double price) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.setName(name);
        product.setPrice(price);

        Product savedProduct = productRepository.save(product);

        return productMapper.productToProductDto(savedProduct);
    }

    public ProductDto updateProductStock(Long productId, int stock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.setStock(stock);

        Product updatedProduct = productRepository.save(product);

        return productMapper.productToProductDto(updatedProduct);
    }

    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        productRepository.deleteById(productId);
    }
}