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

    //Endpoint to retrieve all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //Endpoint to retrieve a product by ID
    @GetMapping("/{productid}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productid) {
        Product product = productService.getProductById(productid);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //Adding a new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product newProduct = productService.addProduct(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    //Update an existing product
    @PutMapping("/{productId}")
//    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product product) {
//        Product updatedProduct = productService.updateProduct(productId, product);
//        if (updatedProduct != null) {
//            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    //Delete a product by ID
    @DeleteMapping("/{productId}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
//        boolean deleted = productService.deleteProduct(productId);
//        if (deleted) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

}
