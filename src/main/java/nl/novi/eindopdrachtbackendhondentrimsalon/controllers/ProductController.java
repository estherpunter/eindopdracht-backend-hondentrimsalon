package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.ProductDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.ProductRequestDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtos = productService.getAllProducts();
        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        ProductDto productDto = productService.getProductById(productId);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> findProductByName(@RequestParam String name) {
        List<ProductDto> products = productService.findProductByName(name);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(@RequestBody ProductRequestDto productRequestDto) {
        ProductDto newProductDto = productService.addProduct(productRequestDto);

        Long productId = newProductDto.getId();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId,
                                                    @RequestBody ProductRequestDto productRequestDto) {
        ProductDto productDto = productService.updateProduct(productId, productRequestDto);
        return ResponseEntity.ok(productDto);
    }

    @PutMapping("/{productId}/stock")
    public ResponseEntity<ProductDto> updateProductStock(@PathVariable Long productId,
                                                         @RequestParam int stock) {
        ProductDto productDto = productService.updateProductStock(productId, stock);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }
}
