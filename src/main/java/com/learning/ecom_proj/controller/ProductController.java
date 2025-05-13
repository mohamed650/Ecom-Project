package com.learning.ecom_proj.controller;

import com.learning.ecom_proj.model.Product;
import com.learning.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{prodId}")
    public ResponseEntity<Product> getProductById(@PathVariable int prodId) {
        Product product =  service.getProductById(prodId);
        if(product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile) {
        try {
            Product storeProduct = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{prodId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int prodId) {
        Product product = service.getProductById(prodId);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);
    }

    @PutMapping("/product/{prodId}")
    public ResponseEntity<String> updateProduct(@PathVariable int prodId, @RequestPart Product product,
                                                @RequestPart MultipartFile imageFile) throws IOException {
        Product updatedProduct = service.updateProduct(prodId, product, imageFile);
        if(updatedProduct != null) {
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to Update", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/product/{prodId}")
    public ResponseEntity<String> deleteProduct(@PathVariable int prodId) {
        Product product = service.getProductById(prodId);
        if( product != null) {
            service.deleteProduct(prodId);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product Not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@PathVariable String keyword) {
        List<Product> products = service.searchProducts(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
