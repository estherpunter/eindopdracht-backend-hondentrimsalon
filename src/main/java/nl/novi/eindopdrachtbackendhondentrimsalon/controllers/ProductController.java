package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.ProductDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/allproducts")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtos = productService.getAllProducts();
        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        ProductDto productDto = productService.getProductById(productId);
        if (productDto != null) {
            return new ResponseEntity<>(productDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<ProductDto>> findProductByName(@RequestParam String name) {
        List<ProductDto> products = productService.findProductByName(name);
        return ResponseEntity.ok(products);
    }

    @PostMapping("")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        ProductDto newProductDto = productService.addProduct(productDto);
        return new ResponseEntity<>(newProductDto, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId,
                                                    @RequestBody ProductDto updatedProductDto) {
        ProductDto productDto = productService.updateProduct(productId, updatedProductDto);
        return ResponseEntity.ok(productDto);
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<ProductDto> updateProductStock(@PathVariable Long productId,
                                                         @RequestParam int newStock) {
        ProductDto productDto = productService.updateProductStock(productId, newStock);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
