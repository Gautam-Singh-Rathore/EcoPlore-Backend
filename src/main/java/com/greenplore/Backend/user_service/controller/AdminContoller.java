package com.greenplore.Backend.user_service.controller;

import com.greenplore.Backend.order_service.repo.OrderRepo;
import com.greenplore.Backend.product_service.entity.Category;
import com.greenplore.Backend.product_service.entity.Product;
import com.greenplore.Backend.product_service.entity.SubCategory;
import com.greenplore.Backend.product_service.exception.CategoryNotFound;
import com.greenplore.Backend.product_service.exception.SubCategoryNotFound;
import com.greenplore.Backend.product_service.repo.CategoryRepo;
import com.greenplore.Backend.product_service.repo.ProductRepo;
import com.greenplore.Backend.product_service.repo.SubCategoryRepo;
import com.greenplore.Backend.product_service.service.Mapper;
import com.greenplore.Backend.user_service.dto.AddSubCategoryDto;
import com.greenplore.Backend.user_service.dto.CustomerProfile;
import com.greenplore.Backend.user_service.dto.SellerProfile;
import com.greenplore.Backend.user_service.repo.CustomerRepo;
import com.greenplore.Backend.user_service.repo.SellerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/admin")
public class AdminContoller {
    @Autowired
    private SellerRepo sellerRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private Mapper mapper;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private SubCategoryRepo subCategoryRepo;
    @Autowired
    private ProductRepo productRepo;

    // get all sellers
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all-sellers")
    public ResponseEntity getAllSellers(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(sellerRepo.findAll().stream()
                        .map((var seller)->{
                            return new SellerProfile(
                                    seller.getUser().getEmail(),
                                    seller.getCompanyName(),
                                    seller.getMobileNo(),
                                    seller.getGSTNumber(),
                                    seller.getUser().getCreatedAt().toString(),
                                    seller.getPickUpAddress(),
                                    seller.getBankDetails()
                            );
                        })
                        .toList()
                );
    }

    // get all customers
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all-customers")
    public ResponseEntity getAllCustomers(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(customerRepo.findAll().stream()
                        .map((var cust)->{
                            return new CustomerProfile(
                                    cust.getFirstName(),
                                    cust.getLastName(),
                                    cust.getMobileNo(),
                                    cust.getUser().getEmail(),
                                    cust.getUser().getCreatedAt().toString()
                            );
                        })
                        .toList()
                );
    }

    // get all orders
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all-orders")
    public ResponseEntity getAllOrders(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderRepo.findAll().stream()
                        .map(mapper::orderToOrderResponseDto)
                        .toList()
                );
    }

    // create category
    @GetMapping("create-category/{name}")
    public ResponseEntity createCategory(
            @PathVariable String name
    ){
        try{
            Category newCategory = Category.builder()
                    .name(name)
                    .build();

            categoryRepo.save(newCategory);
            return ResponseEntity.ok("Category created");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Category not created");
        }
    }

    // delete category
    @DeleteMapping("delete-category/{id}")
    @Transactional
    public ResponseEntity deleteCategory(
            @PathVariable Long id
    ){
        try {
            Category category = categoryRepo.findById(id)
                    .orElseThrow(()-> new RuntimeException("Category Not Found"));
            // set is deleted true for the category
            category.setDeleted(true);
            categoryRepo.save(category);
            // cascade delet to all the sub categories
            for(SubCategory subCategory : category.getSubCategories()){
                deleteSubCategory(subCategory.getId());
            }
            return ResponseEntity.ok("Category deleted");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Category not deleted");
        }
    }

    // create sub-category
    @PostMapping("/create-subcategory")
    public ResponseEntity createSubCategory(
            @RequestBody AddSubCategoryDto addSubCategoryDto
            ){
        try{
            Category category = categoryRepo.findByIdAndIsDeletedFalse(addSubCategoryDto.categoryId())
                    .orElseThrow(()-> new CategoryNotFound("Category Not Found"));
            SubCategory subCategory = SubCategory.builder()
                    .name(addSubCategoryDto.name())
                    .imageUrl(addSubCategoryDto.imageUrl())
                    .category(category)
                    .build();
            subCategoryRepo.save(subCategory);
            return ResponseEntity.ok("Subcategory created");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Subcategory not created");
        }
    }

    // delete sub-category
    @DeleteMapping("delete-subcategory/{id}")
    @Transactional
    public ResponseEntity deleteSubCategory(
            @PathVariable Long id
    ){
        try {
            SubCategory subCategory = subCategoryRepo.findById(id)
                    .orElseThrow(()-> new SubCategoryNotFound("Subcategory not found"));
            // set is deleted true in the subcategory
            subCategory.setDeleted(true);
            subCategoryRepo.save(subCategory);
            // set is deleted true for all the subsequent products
            for(Product product: subCategory.getProducts()){
                product.setDeleted(true);
                productRepo.save(product);
            }
            return ResponseEntity.ok("Subcategory deleted");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Subcategory not deleted");
        }
    }

    // count the no of products
    @GetMapping("/products/count")
    public ResponseEntity countOfProducts(){
        return ResponseEntity.status(HttpStatus.OK).body(
            productRepo.count()
        );
    }
}
