package com.alterra.pos;

import com.alterra.pos.controller.ProductController;
import com.alterra.pos.entity.Category;
import com.alterra.pos.entity.PriceAndStock;
import com.alterra.pos.entity.Product;
import com.alterra.pos.repository.CategoryRepository;
import com.alterra.pos.repository.PriceAndStockRepository;
import com.alterra.pos.repository.ProductRepository;
import com.alterra.pos.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;

@SpringBootTest(classes = PosApplication.class)
class PosApplicationTests {
	// @Mock
	// private ProductRepository productRepository;
	// @Mock
	// private CategoryRepository categoryRepository;
	// @Mock
	// private PriceAndStockRepository priceAndStockRepository;
	// @InjectMocks
	// private ProductService productService;
	// private Product product;
	// private Category category;
	// private PriceAndStock priceAndStock;

	// @BeforeEach
	// void setup() {
	// 	category = Category.builder().id(1).name("Food").description("Makanan berat").isValid(true).build();
	// 	priceAndStock = PriceAndStock.builder().id(1)
	// 			.price(Double.valueOf(15000)).stock(1)
	// 			.modifiedBy("system").modifiedAt(new Date()).build();
	// 	product = Product.builder().id(1).category(category).priceAndStock(priceAndStock)
	// 			.name("Ayam Goreng").description("Ayam digoreng")
	// 			.isValid(true).createdBy("system").createdAt(new Date())
	// 			.modifiedBy("system").modifiedAt(new Date()).build();
	// }

	@Test
	void contextLoads() {
		// // given
		// given(productRepository.findAllByIsValidTrue()).willReturn(List.of(product));

		// // when
		// List<Product> productsList = productService.getProducts();

		// // then
		// assertThat(productsList).isNotNull();
		// assertThat(productsList.size()).isEqualTo(1);
	}


}
