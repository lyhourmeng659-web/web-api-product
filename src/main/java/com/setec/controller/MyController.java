package com.setec.controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.setec.entities.PostProductDAO;
import com.setec.entities.Product;
import com.setec.entities.PutProductDAO;
import com.setec.repos.ProductRepo;

@RestController
@RequestMapping("/api/product")
public class MyController {
    // http://localhost:8080/swagger-ui/index.html#/my-controller/products
    @Autowired
    private ProductRepo productRepo;

    // get all products, if products is empty return 404 with message "Product is
    // Empty!"
    @GetMapping
    public Object products() {
        var products = productRepo.findAll();

        if (products.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Product is Empty!"));
        }

        return products;
    }

    // post add product with file upload, file will be saved to myApp/static folder
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(@ModelAttribute PostProductDAO postProductDAO) throws Exception {
        // get absolute path of uploadDir
        String uploadDir = new File("myApp/static").getAbsolutePath();
        File dir = new File(uploadDir);

        // check if uploadDir exist, if not create it
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // get file from postProductDAO and save to uploadDir
        var file = postProductDAO.getFile();

        // random file name to avoid duplicate file name
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // save file to uploadDir
        String filePath = Paths.get(uploadDir, uniqueFileName).toString();
        file.transferTo(new File(filePath));

        // save product to database with imageUrl is /static/uniqueFileName
        var pro = new Product();
        pro.setName(postProductDAO.getName());
        pro.setPrice(postProductDAO.getPrice());
        pro.setQty(postProductDAO.getQty());
        pro.setImageUrl("/static/" + uniqueFileName);
        productRepo.save(pro);
        return ResponseEntity.status(201).body(pro);
    }

    // put update product with file upload, file will be saved to myApp/static
    // folder, if product id not found return 404 with message "Product ID: {id} not
    // found!"
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(@ModelAttribute PutProductDAO putProductDAO) throws Exception {
        // get product by id
        var p = productRepo.findById(putProductDAO.getId());
        // check if product exist
        if (p.isPresent()) {
            var update = p.get();
            update.setName(putProductDAO.getName());
            update.setPrice(putProductDAO.getPrice());
            update.setQty(putProductDAO.getQty());
            if (putProductDAO.getFile() != null) {
                // get absolute path of uploadDir
                String uploadDir = new File("myApp/static").getAbsolutePath();
                File dir = new File(uploadDir);

                // check if uploadDir exist, if not create it
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // get file from putProductDAO and save to uploadDir
                var file = putProductDAO.getFile();

                // random file name to avoid duplicate file name
                String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                // save file to uploadDir
                String filePath = Paths.get(uploadDir, uniqueFileName).toString();

                new File("myApp/" + update.getImageUrl()).delete(); // delete old file

                file.transferTo(new File(filePath));
                update.setImageUrl("/static/" + uniqueFileName);
            }

            // update product on database
            productRepo.save(update);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(update);
        }

        // if product not exist return 404 with message "Product ID: {id} not found!"
        return ResponseEntity.status(404)
                .body(Map.of("message", "Product ID = " + putProductDAO.getId() + " not found!"));
    }

    // get product by id
    @GetMapping({ "/{id}", "/id/{id}" })
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        var pro = productRepo.findById(id);
        if (pro.isPresent()) {
            return ResponseEntity.status(200).body(pro.get());
        }
        return ResponseEntity.status(404).body(Map.of("message", "Product ID = " + id + " not found!"));
    }

    // delete product by id
    @DeleteMapping({ "/{id}", "/id/{id}" })
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        var pro = productRepo.findById(id);
        if (pro.isPresent()) {
            // delete image in folder
            new File("myApp/" + pro.get().getImageUrl()).delete();
            // delete product from database by id
            productRepo.delete(pro.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(Map.of("message", "Product ID = " + id + " has been deleted!"));
        }
        return ResponseEntity.status(404).body(Map.of("message", "Product ID = " + id + " not found!"));
    }
}
