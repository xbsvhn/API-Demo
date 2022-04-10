package com.fsoft.apidemo.controller;

import com.fsoft.apidemo.models.Product;
import com.fsoft.apidemo.models.ResponseObject;
import com.fsoft.apidemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductRepository repository;

    @GetMapping("")
    List<Product> getAllProducts() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getById(@PathVariable Long id) {
        Optional<Product> foundProduct = repository.findById(id);
        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Query product successfully", foundProduct)
                ) :

                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("Failed", "Can not find product with id: " + id, "")
                );
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct) {
        List<Product> listProduct = repository.findByProductName(newProduct.getProductName().trim());
        return listProduct.size() > 0 ?
                ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("Failed", "Product's Name is already taken", "")
                ) :
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Insert product successfully", repository.save(newProduct))
                );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updatedProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Product updateProduct = repository.findById(id)
                .map(
                        product -> {
                            product.setProductName(newProduct.getProductName());
                            product.setYear(newProduct.getYear());
                            product.setPrice(newProduct.getPrice());
                            return repository.save(product);
                        }
                ).orElseGet(() -> repository.save(newProduct));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update product successfully", updateProduct)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id){
        boolean exist = repository.existsById(id);
        if (exist) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Delete product successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Can not find product to delete", "")
        );
    }
}
