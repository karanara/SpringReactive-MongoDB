package com.example.springflux.mongodb.reactivedb.repository;

import com.example.springflux.mongodb.reactivedb.dto.ProductDto;
import com.example.springflux.mongodb.reactivedb.entity.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product,String> {
    Flux<ProductDto> findByPriceBetween(Range<Double> priceRange);
}
