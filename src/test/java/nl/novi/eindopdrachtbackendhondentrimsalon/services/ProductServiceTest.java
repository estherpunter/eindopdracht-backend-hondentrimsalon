package nl.novi.eindopdrachtbackendhondentrimsalon.services;

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

import jakarta.validation.ValidationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        product.setStock(100);

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setProductName("Test Product");
        productDto.setPrice(10.0);
        productDto.setStock(100);

        productRequestDto = new ProductRequestDto();
        productRequestDto.setProductName("Test Product");
        productRequestDto.setPrice(10.0);
        productRequestDto.setStock(100);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));
        when(productMapper.productsToProductDtos(any())).thenReturn(Arrays.asList(productDto));

        // Act
        List<ProductDto> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getProductName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.productToProductDto(product)).thenReturn(productDto);

        // Act
        ProductDto result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getProductName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testFindProductByName() {
        // Arrange
        when(productRepository.findByNameIgnoreCase("Test Product")).thenReturn(Arrays.asList(product));
        when(productMapper.productToProductDto(product)).thenReturn(productDto);

        // Act
        List<ProductDto> result = productService.findProductByName("Test Product");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getProductName());
        verify(productRepository, times(1)).findByNameIgnoreCase("Test Product");
    }

    @Test
    void testAddProduct() {
        // Arrange
        when(productRepository.findByNameIgnoreCase("Test Product")).thenReturn(Arrays.asList());
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.productToProductDto(any(Product.class))).thenReturn(productDto);

        // Act
        ProductDto result = productService.addProduct(productRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testAddProduct_Invalid() {
        // Arrange
        productRequestDto.setProductName(null);

        // Act & Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(productRequestDto));
    }

    @Test
    void testAddProduct_Duplicate() {
        // Arrange
        when(productRepository.findByNameIgnoreCase("Test Product")).thenReturn(Arrays.asList(product));

        // Act & Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(productRequestDto));
    }

    @Test
    void testUpdateProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.productToProductDto(any(Product.class))).thenReturn(productDto);

        // Act
        ProductDto result = productService.updateProduct(1L, productRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getProductName());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_NotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, productRequestDto));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateProductStock() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.productToProductDto(any(Product.class))).thenReturn(productDto);

        // Act
        ProductDto result = productService.updateProductStock(1L, 50);

        // Assert
        assertNotNull(result);
        assertEquals(50, result.getStock());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProductStock_NotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.updateProductStock(1L, 50));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, times(1)).existsById(1L);
    }
}
