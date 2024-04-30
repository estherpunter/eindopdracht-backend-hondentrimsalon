package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Product;
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

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Product>> findProductByName(@RequestParam String name) {
        List<Product> products = productService.findProductByName(name);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product newProduct = productService.addProduct(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                 @RequestBody Product updatedProduct) {
        Product product = productService.updateProduct(productId, updatedProduct);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Product> updateProductStock(@PathVariable Long productId,
                                                      @RequestParam int newStock) {
        Product product = productService.updateProductStock(productId, newStock);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }


}
