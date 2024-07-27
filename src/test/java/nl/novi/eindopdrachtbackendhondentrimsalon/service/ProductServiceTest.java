package nl.novi.eindopdrachtbackendhondentrimsalon.service;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.ProductDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.ProductNotFoundException;
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

//@SpringBootTest
//public class ProductServiceTest {
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private ProductMapper productMapper;
//
//    @InjectMocks
//    private ProductService productService;
//
//    @Test
//    public void testGetAllProducts() {
//        // Arrange
//        List<Product> products = new ArrayList<>();
//        Mockito.when(productRepository.findAll()).thenReturn(products);
//        Mockito.when(productMapper.productsToProductDtos(products)).thenReturn(new ArrayList<>());
//
//        // Act
//        List<ProductDto> result = productService.getAllProducts();
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    public void testGetProductById_ExistingProduct() {
//        // Arrange
//        long productId = 1L;
//        Product product = new Product();
//        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
//        Mockito.when(productMapper.productToProductDto(product)).thenReturn(new ProductDto());
//
//        // Act
//        ProductDto result = productService.getProductById(productId);
//
//        // Assert
//        assertNotNull(result);
//    }

//    @Test
//    public void testGetProductById_NonExistingProduct() {
//        // Arrange
//        long productId = 1L;
//        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());
//
//        // Act/Assert
//        assertThrows(RecordNotFoundException.class, () -> productService.getProductById(productId));
//    }

//    @Test
//    public void testFindProductByName_ExistingProducts() {
//        // Arrange
//        String productName = "Test Product";
//        List<Product> products = new ArrayList<>();
//        products.add(new Product(1L, productName, 10.0, 5));
//        products.add(new Product(2L, productName, 15.0, 8));
//        Mockito.when(productRepository.findByNameIgnoreCase(productName)).thenReturn(products);
//        Mockito.when(productMapper.productToProductDto(Mockito.any(Product.class))).thenAnswer(
//                invocation -> {
//                    Product product = invocation.getArgument(0);
//                    return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getStock());
//                });
//
//        // Act
//        List<ProductDto> result = productService.findProductByName(productName);
//
//        // Assert
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        assertEquals(2, result.size());
//    }

//    @Test
//    public void testFindProductByName_NonExistingProduct() {
//        // Arrange
//        String productName = "Non-existing Product";
//        Mockito.when(productRepository.findByNameIgnoreCase(productName)).thenReturn(new ArrayList<>());
//
//        // Act
//        List<ProductDto> result = productService.findProductByName(productName);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }

//    @Test
//    public void testAddProduct_ValidProduct() {
//        // Arrange
//        ProductDto productDto = new ProductDto();
//        productDto.setName("Test Product");
//        productDto.setPrice(10.0);
//
//        Product product = new Product();
//        Mockito.when(productMapper.productDtoToProduct(productDto)).thenReturn(product);
//        Mockito.when(productRepository.save(any(Product.class))).thenReturn(product);
//        Mockito.when(productMapper.productToProductDto(product)).thenReturn(productDto);
//
//        // Act
//        ProductDto result = productService.addProduct(productDto);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(productDto.getName(), result.getName());
//        assertEquals(productDto.getPrice(), result.getPrice());
//    }

//    @Test
//    public void testAddProduct_InvalidProduct() {
//        // Arrange
//        ProductDto productDto = new ProductDto(); // Empty ProductDto
//
//        // Act/Assert
//        assertThrows(IllegalArgumentException.class, () -> productService.addProduct(productDto));
//    }

//    @Test
//    public void testUpdateProduct_ValidProduct() {
//        // Arrange
//        long productId = 1L;
//        ProductDto updatedProductDto = new ProductDto(productId, "Updated Product", 20.0, 3);
//        Product existingProduct = new Product(productId, "Old Product", 15.0, 5);
//
//        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
//        Mockito.when(productRepository.save(Mockito.any(Product.class)))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        Mockito.when(productMapper.productToProductDto(Mockito.any(Product.class)))
//                .thenAnswer(invocation -> {
//                    Product product = invocation.getArgument(0);
//                    return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getStock());
//                });
//
//        // Act
//        ProductDto result = productService.updateProduct(productId, updatedProductDto);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(updatedProductDto.getName(), result.getName());
//        assertEquals(updatedProductDto.getPrice(), result.getPrice());
//        assertEquals(updatedProductDto.getStock(), result.getStock());
//    }
//
//
//    @Test
//    public void testUpdateProduct_NonExistingProduct() {
//        // Arrange
//        long productId = 1L;
//        ProductDto updatedProductDto = new ProductDto(productId, "Updated Product", 20.0, 3);
//
//        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());
//
//        // Act/Assert
//        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(productId, updatedProductDto));
//    }
//
//    @Test
//    public void testUpdateProductStock_ValidProduct() {
//        // Arrange
//        long productId = 1L;
//        int newStock = 8;
//        Product existingProduct = new Product(productId, "Existing Product", 15.0, 5);
//
//        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
//        Mockito.when(productRepository.save(Mockito.any(Product.class)))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        Mockito.when(productMapper.productToProductDto(Mockito.any(Product.class)))
//                .thenAnswer(invocation -> {
//                    Product product = invocation.getArgument(0);
//                    return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getStock());
//                });
//
//        // Act
//        ProductDto result = productService.updateProductStock(productId, newStock);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(existingProduct.getStock(), newStock);
//    }
//
//    @Test
//    public void testUpdateProductStock_NonExistingProduct() {
//        // Arrange
//        long productId = 1L;
//        int newStock = 8;
//
//        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());
//
//        // Act/Assert
//        assertThrows(ProductNotFoundException.class, () -> productService.updateProductStock(productId, newStock));
//    }
//
//    @Test
//    public void testDeleteProduct_ExistingProduct() {
//        // Arrange
//        long productId = 1L;
//        Product existingProduct = new Product(productId, "Existing Product", 15.0, 5);
//
//        Mockito.when(productRepository.existsById(productId)).thenReturn(true);
//
//        // Act
//        assertDoesNotThrow(() -> productService.deleteProduct(productId));
//
//        // Assert
//        Mockito.verify(productRepository, Mockito.times(1)).deleteById(productId);
//    }
//
//    @Test
//    public void testDeleteProduct_NonExistingProduct() {
//        // Arrange
//        long productId = 1L;
//
//        Mockito.when(productRepository.existsById(productId)).thenReturn(false);
//
//        // Act/Assert
//        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(productId));
//
//        // Verify that deleteById method was not invoked
//        Mockito.verify(productRepository, Mockito.never()).deleteById(productId);
//    }
//}

