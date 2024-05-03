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
    public ProductDto addProduct(ProductDto productDto) {
        if (productDto.getId() != null && productRepository.existsById(productDto.getId())) {
            throw new RuntimeException("Product with ID " + productDto.getId() + " already exists.");
        }
        if (productDto.getName() == null || productDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Product name and price are required.");
        }
        Product product = productMapper.productDtoToProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.productToProductDto(savedProduct);
    }


    public ProductDto updateProduct(Long productId, ProductDto updatedProductDto) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        existingProduct.setName(updatedProductDto.getName());
        existingProduct.setPrice(updatedProductDto.getPrice());
        existingProduct.setStock(updatedProductDto.getStock());

        Product savedProduct = productRepository.save(existingProduct);
        return productMapper.productToProductDto(savedProduct);
    }

    public ProductDto updateProductStock(Long productId, int newStock) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        existingProduct.setStock(newStock);
        Product savedProduct = productRepository.save(existingProduct);
        return productMapper.productToProductDto(savedProduct);
    }

    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        productRepository.deleteById(productId);
    }
}