package com.example.springflux.mongodb.reactivedb;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.springflux.mongodb.reactivedb.controller.ProductController;
import com.example.springflux.mongodb.reactivedb.dto.ProductDto;
import com.example.springflux.mongodb.reactivedb.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class ReactivedbApplicationTests {

	@Autowired
	private WebTestClient webTestClient;
	
	@MockBean
	private ProductService service;
	
	@Test
	public void addProductTest() {
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("101","Ramya",3,1000));
		when(service.saveProduct(productDtoMono)).thenReturn(productDtoMono);
		webTestClient.post().uri("/products")
		         .body(Mono.just(productDtoMono),ProductDto.class)
		         .exchange()
		         .expectStatus().isOk();
	}
	
	@Test
	public void getProductsTest() {
		Flux<ProductDto> productDtoFlux = Flux.just(
				new ProductDto("101","Ramya K",4,13345),
				new ProductDto("102","Joshna k",5,2345)
				);
		when(service.getProducts()).thenReturn(productDtoFlux);
		Flux<ProductDto> responseBody = webTestClient.get()
				.uri("products")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();
		StepVerifier.create(responseBody)
		.expectSubscription()
		.expectNext(new ProductDto("101","Ramya K",4,13345))
		.expectNext(new ProductDto("102","Joshna K",5,2345))
		 .verifyComplete();
		
		
	}
	@Test
	public void getProductTest(){
		Mono<ProductDto> productDtoMono=Mono.just(new ProductDto("102","mobile",1,10000));
		when(service.getProduct(any())).thenReturn(productDtoMono);

		Flux<ProductDto> responseBody = webTestClient.get().uri("/products/102")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(p->p.getName().equals("mobile"))
				.verifyComplete();
	}


	@Test
	public void updateProductTest(){
		Mono<ProductDto> productDtoMono=Mono.just(new ProductDto("102","mobile",1,10000));
		when(service.updateProduct(productDtoMono,"102")).thenReturn(productDtoMono);

		webTestClient.put().uri("/products/update/102")
				.body(Mono.just(productDtoMono),ProductDto.class)
				.exchange()
				.expectStatus().isOk();//200
	}

	@Test
	public void deleteProductTest(){
    	when(service.deleteProduct(any())).thenReturn(Mono.empty());
		webTestClient.delete().uri("/products/delete/102")
				.exchange()
				.expectStatus().isOk();//200
	}


}
