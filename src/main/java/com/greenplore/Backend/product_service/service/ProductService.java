package com.greenplore.Backend.product_service.service;

import com.greenplore.Backend.product_service.dto.AddProductDto;
import com.greenplore.Backend.product_service.dto.ProductCardResponseDto;
import com.greenplore.Backend.product_service.dto.ProductResponseDto;
import com.greenplore.Backend.product_service.entity.Category;
import com.greenplore.Backend.product_service.entity.Product;
import com.greenplore.Backend.product_service.entity.SubCategory;
import com.greenplore.Backend.product_service.exception.CategoryNotFound;
import com.greenplore.Backend.product_service.exception.ProductNotFoundException;
import com.greenplore.Backend.product_service.exception.SellerNotFound;
import com.greenplore.Backend.product_service.exception.SubCategoryNotFound;
import com.greenplore.Backend.product_service.repo.CategoryRepo;
import com.greenplore.Backend.product_service.repo.ProductRepo;
import com.greenplore.Backend.product_service.repo.SubCategoryRepo;
import com.greenplore.Backend.user_service.auth.UserDetailsImpl;
import com.greenplore.Backend.user_service.entity.Seller;
import com.greenplore.Backend.user_service.entity.User;
import com.greenplore.Backend.user_service.exception.UserNotFoundException;
import com.greenplore.Backend.user_service.repo.SellerRepo;
import com.greenplore.Backend.user_service.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SellerRepo sellerRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private SubCategoryRepo subCategoryRepo;
    @Autowired
    private Mapper mapper;


    @Transactional
    public String addProduct(UserDetailsImpl user, AddProductDto productDto) {

        User sellerUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new SellerNotFound("Seller not found for email :"+user.getUsername()));

        Seller seller = sellerRepo.findByUser(sellerUser)
                .orElseThrow(()-> new SellerNotFound("Seller not found for email :"+user.getUsername()));

        Category category = categoryRepo.findById(productDto.categoryId())
                .orElseThrow(()-> new CategoryNotFound("Category not found for id :"+productDto.categoryId()));

        SubCategory subCategory = subCategoryRepo.findById(productDto.subCategoryId())
                .orElseThrow(()-> new SubCategoryNotFound("Sub Category Not found for id :"+productDto.subCategoryId()));

        Product newProduct = Product.builder()
                .name(productDto.name())
                .imageUrls(productDto.imageUrls())
                .price(productDto.price())
                .noOfUnits(productDto.noOfUnits())
                .description(productDto.description())
                .details(productDto.details())
                .category(category)
                .subCategory(subCategory)
                .seller(seller)
                .height(productDto.height())
                .length(productDto.length())
                .width(productDto.width())
                .weight(productDto.weight())
                .build();

        
                productRepo.save(newProduct);
        return "Product added";
    }

    public List<ProductCardResponseDto> getProductsByCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()-> new CategoryNotFound("Category not found for id :"+categoryId));
        List<Product> products = productRepo.findByCategory(category)
                .orElseThrow(()-> new ProductNotFoundException("Products not found for category "+category.getName()));
        return products
                 .stream()
                 .map(mapper::productsToProductsCardResponse)
                 .toList();
    }

    public List<ProductCardResponseDto> getProductsBySubCategory(Long subCategoryId) {
        SubCategory subCategory = subCategoryRepo.findById(subCategoryId)
                .orElseThrow(()-> new SubCategoryNotFound("Sub Category Not found for id :"+subCategoryId));
        List<Product> products = productRepo.findBySubCategory(subCategory)
                .orElseThrow(()-> new ProductNotFoundException("Products not found for sub-category "+subCategory.getName()));
        return products
                .stream()
                .map(mapper::productsToProductsCardResponse)
                .toList();
    }

    public ProductResponseDto getProductById(UUID id) {
        Product product = productRepo.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Products not found for id "+id));
        return mapper.productToProductResponse(product);
    }

    public List<ProductCardResponseDto> searchProduct(String name) {
        List<Product> products = productRepo.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(name,name);
        return products.stream()
                .map(mapper::productsToProductsCardResponse)
                .toList();
    }

    @Transactional
    public String deleteProduct(UserDetailsImpl user, UUID id) {
        Product product = productRepo.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found "));
        productRepo.deleteById(id);
        return "Product deleted";
    }

    @Transactional
    public String editProduct(UserDetailsImpl user, UUID id, AddProductDto product) {
        Product myProduct = productRepo.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found "));
        Category category = categoryRepo.findById(product.categoryId())
                        .orElseThrow(()-> new CategoryNotFound("Category Not Found"));
        SubCategory subCategory = subCategoryRepo.findById(product.subCategoryId())
                        .orElseThrow(()-> new SubCategoryNotFound("Sub Category Not Found"));

        myProduct.setName(product.name());
        myProduct.setImageUrls(product.imageUrls());
        myProduct.setPrice(product.price());
        myProduct.setDescription(product.description());
        myProduct.setNoOfUnits(product.noOfUnits());
        myProduct.setDetails(product.details());
        myProduct.setCategory(category);
        myProduct.setSubCategory(subCategory);
        myProduct.setHeight(product.height());
        myProduct.setLength(product.length());
        myProduct.setWidth(product.width());
        myProduct.setWeight(product.weight());

        productRepo.save(myProduct);
        return "Product edited successfully";
    }
}
