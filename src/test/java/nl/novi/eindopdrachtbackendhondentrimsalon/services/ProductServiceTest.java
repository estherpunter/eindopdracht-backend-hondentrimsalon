package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import jakarta.validation.ValidationException;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.ProductDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.ProductRequestDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.ProductNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.ProductMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Product;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDto productDto;
    private ProductRequestDto productRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(10.0);
        product.setStock(5);

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setProductName("Test Product");
        productDto.setPrice(10.0);
        productDto.setStock(5);

        productRequestDto = new ProductRequestDto();
        productRequestDto.setProductName("Test Product");
        productRequestDto.setPrice(10.0);
        productRequestDto.setStock(5);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void testGetAllProducts() {
        // Arrange
        List<Product> products = Collections.singletonList(product);
        List<ProductDto> productDtos = Collections.singletonList(productDto);

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.productsToProductDtos(products)).thenReturn(productDtos);

        // Act
        List<ProductDto> result = productService.getAllProducts();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getProductName());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void testGetProductById_ExistingProduct() {
        // Arrange
        long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.productToProductDto(product)).thenReturn(productDto);

        // Act
        ProductDto foundProduct = productService.getProductById(productId);

        // Assert
        assertEquals(productId, foundProduct.getId());
        assertEquals("Test Product", foundProduct.getProductName());
    }

    @Test
    public void testGetProductById_NonExistingProduct() {
        // Arrange
        long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void testFindProductByName_ExistingProducts() {
        // Arrange
        String productName = "Test Product";
        List<Product> products = Collections.singletonList(product);
        List<ProductDto> productDtos = Collections.singletonList(productDto);

        when(productRepository.findByNameIgnoreCase(productName)).thenReturn(products);
        when(productMapper.productsToProductDtos(products)).thenReturn(productDtos);

        // Act
        List<ProductDto> result = productService.findProductByName(productName);

        // Assert
        assertEquals(1, result.size());
        assertEquals(productName, result.get(0).getProductName());
    }

    @Test
    public void testFindProductByName_NonExistingProduct() {
        // Arrange
        String productName = "Non-existing Product";
        when(productRepository.findByNameIgnoreCase(productName)).thenReturn(Collections.emptyList());

        // Act
        List<ProductDto> result = productService.findProductByName(productName);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddProduct_ValidProduct() {
        // Arrange
        when(productRepository.findByNameIgnoreCase(productRequestDto.getProductName())).thenReturn(Collections.emptyList());
        when(productMapper.productToProductDto(any(Product.class))).thenReturn(productDto);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        ProductDto savedProduct = productService.addProduct(productRequestDto);

        // Assert
        assertEquals("Test Product", savedProduct.getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testAddProduct_InvalidProduct() {
        // Arrange
        productRequestDto.setProductName(""); // Invalid Product

        // Act & Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(productRequestDto));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateProduct_ValidProduct() {
        // Arrange
        long productId = 1L;

        ProductRequestDto updatedProductRequest = new ProductRequestDto();
        updatedProductRequest.setProductName("New Name");
        updatedProductRequest.setPrice(20.0);
        updatedProductRequest.setStock(10);

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("New Name");
        updatedProduct.setPrice(20.0);
        updatedProduct.setStock(10);

        ProductDto updatedProductDto = new ProductDto();
        updatedProductDto.setId(productId);
        updatedProductDto.setProductName("New Name");
        updatedProductDto.setPrice(20.0);
        updatedProductDto.setStock(10);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.productToProductDto(updatedProduct)).thenReturn(updatedProductDto);

        // Act
        ProductDto result = productService.updateProduct(productId, updatedProductRequest);

        // Assert
        assertEquals("New Name", result.getProductName());
        assertEquals(20.0, result.getPrice());
        assertEquals(10, result.getStock());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct_NonExistingProduct() {
        // Arrange
        long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(productId, productRequestDto));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void testUpdateProductStock_ValidProduct() {
        // Arrange
        long productId = 1L;
        int newStock = 50;

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setStock(newStock);

        ProductDto updatedProductDto = new ProductDto();
        updatedProductDto.setId(productId);
        updatedProductDto.setStock(newStock);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.productToProductDto(updatedProduct)).thenReturn(updatedProductDto);

        // Act
        ProductDto result = productService.updateProductStock(productId, newStock);

        // Assert
        assertEquals(newStock, result.getStock());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testUpdateProductStock_NonExistingProduct() {
        // Arrange
        long productId = 1L;
        int newStock = 8;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.updateProductStock(productId, newStock));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteProduct_ExistingProduct() {
        // Arrange
        long productId = 1L;

        when(productRepository.existsById(productId)).thenReturn(true);

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testDeleteProduct_NonExistingProduct() {
        // Arrange
        long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(false);

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(productId));
    }
}
