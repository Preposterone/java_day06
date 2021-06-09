package edu.school21.repositories;

import edu.school21.models.Product;
import edu.school21.repositories.ProductsRepositoryJdbcImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.Connection;
import java.sql.SQLException;

public class ProductsRepositoryJdbcImplTest {

	private Connection resetDBAndGetConnection() throws SQLException {
		return (new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.HSQL)
				.addScript("schema.sql")
				.addScript("data.sql")
				.build().getConnection());
	}

	@Test
	public void testSave() throws Exception {
		final Product EXPECTED_PRODUCT = new Product(null, "Stuff", 8800555L);

		ProductsRepositoryJdbcImpl repo = new ProductsRepositoryJdbcImpl(resetDBAndGetConnection());
		repo.save(EXPECTED_PRODUCT);
		Assertions.assertNotNull(EXPECTED_PRODUCT.getId());
		Assertions.assertEquals(EXPECTED_PRODUCT, repo.findById(EXPECTED_PRODUCT.getId()).orElse(null));
	}

	@ParameterizedTest
	@ValueSource(longs = {1, 2, 3, 4, 5})
	public void testUpdate(long num) throws Exception {
		ProductsRepositoryJdbcImpl repo = new ProductsRepositoryJdbcImpl(resetDBAndGetConnection());
		final Product EXPECTED_PRODUCT = repo.findById(num).orElse(null);

		EXPECTED_PRODUCT.setPrice(num);
		repo.update(EXPECTED_PRODUCT);
		Product result = repo.findById(num).orElse(null);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(num, result.getPrice());
	}
}
