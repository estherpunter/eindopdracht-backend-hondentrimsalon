package nl.novi.eindopdrachtbackendhondentrimsalon.service;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.ProductDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.RecordNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.ProductMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Product;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.ProductRepository;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testGetAllProducts() {
        // Arrange
        List<Product> products = new ArrayList<>();
        Mockito.when(productRepository.findAll()).thenReturn(products);
        Mockito.when(productMapper.productsToProductDtos(products)).thenReturn(new ArrayList<>());

        // Act
        List<ProductDto> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetProductById_ExistingProduct() {
        // Arrange
        long productId = 1L;
        Product product = new Product();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(productMapper.productToProductDto(product)).thenReturn(new ProductDto());

        // Act
        ProductDto result = productService.getProductById(productId);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetProductById_NonExistingProduct() {
        // Arrange
        long productId = 1L;
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act/Assert
        assertThrows(RecordNotFoundException.class, () -> productService.getProductById(productId));
    }

    @Test
    public void testAddProduct_ValidProduct() {
        // Arrange
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setPrice(10.0);

        Product product = new Product();
        Mockito.when(productMapper.productDtoToProduct(productDto)).thenReturn(product);
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(product);
        Mockito.when(productMapper.productToProductDto(product)).thenReturn(productDto);

        // Act
        ProductDto result = productService.addProduct(productDto);

        // Assert
        assertNotNull(result);
        assertEquals(productDto.getName(), result.getName());
        assertEquals(productDto.getPrice(), result.getPrice());
    }

    @Test
    public void testAddProduct_InvalidProduct() {
        // Arrange
        ProductDto productDto = new ProductDto(); // Empty ProductDto

        // Act/Assert
        assertThrows(IllegalArgumentException.class, () -> productService.addProduct(productDto));
    }

    // Add more tests for other methods like updateProduct, updateProductStock, deleteProduct, etc.
}
