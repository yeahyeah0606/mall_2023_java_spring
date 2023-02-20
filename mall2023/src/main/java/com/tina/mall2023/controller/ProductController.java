package com.tina.mall2023.controller;

import com.tina.mall2023.constant.ProductCategory;
import com.tina.mall2023.dto.ProductQueryParams;
import com.tina.mall2023.dto.ProductRequest;
import com.tina.mall2023.model.Product;
import com.tina.mall2023.service.ProductService;
import com.tina.mall2023.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
@Validated
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/products")
    public  ResponseEntity<Page<Product>> getProducts(
            // 查詢條件 filtering
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,
            // 排序 sorting
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,
            // 分頁 pagination
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0")  @Min(0) Integer offset ){
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);
        // 取得 product list
        List<Product> productList = productService.getProducts(productQueryParams);
        // 取得 product 總數
        Integer total = productService.countProduct(productQueryParams);
        // 分頁
        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
        Product product = productService.getProductById(productId);
        if(product != null){
            return  ResponseEntity.status(HttpStatus.OK).body(product);
        }else{
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
@PostMapping("/products")
    public  ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest prd){
       Integer productID =  productService.createProduct(prd);
       Product product = productService.getProductById(productID);
       return  ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
@PutMapping("/products/{productID}")
    public  ResponseEntity<Product> updateProduct( @PathVariable Integer productID,
                                                   @RequestBody @Valid ProductRequest productRequest){
        //檢查 product是否存在
        Product product = productService.getProductById(productID);
        if(product == null){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        // 修改商品的數據
        productService.updateProduct(productID, productRequest);
        Product updateProduct = productService.getProductById(productID);
        return  ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }
@DeleteMapping("/products/{productID}")
    public  ResponseEntity<?> deleteProduct(@PathVariable Integer productID){
        productService.deleteProductById(productID);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
