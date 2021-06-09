package edu.school21.repositories;

import edu.school21.models.Product;
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
	public void testUpdate(long id) throws Exception {
		ProductsRepositoryJdbcImpl repo = new ProductsRepositoryJdbcImpl(resetDBAndGetConnection());
		final Product EXPECTED_PRODUCT = repo.findById(id).orElse(null);

		EXPECTED_PRODUCT.setPrice(id);
		repo.update(EXPECTED_PRODUCT);
		Product result = repo.findById(id).orElse(null);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(id, result.getPrice());
	}

	@ParameterizedTest
	@ValueSource(longs = {1, 2, 3, 4, 5})
	public void testDelete(long id) throws Exception {
		ProductsRepositoryJdbcImpl repo = new ProductsRepositoryJdbcImpl(resetDBAndGetConnection());
		repo.delete(id);
		Assertions.assertFalse(repo.findById(id).isPresent());
	}

	@ParameterizedTest
	@ValueSource(longs = {1, 2, 3, 4, 5})
	public void testFindByIdTrue(long id) throws Exception {
		ProductsRepositoryJdbcImpl repo = new ProductsRepositoryJdbcImpl(resetDBAndGetConnection());
		Assertions.assertTrue(repo.findById(id).isPresent());
	}

	@ParameterizedTest
	@ValueSource(longs = {99, 99, 15874, 41})
	public void testFindByIdFalse(long id) throws Exception {
		ProductsRepositoryJdbcImpl repo = new ProductsRepositoryJdbcImpl(resetDBAndGetConnection());
		Assertions.assertFalse(repo.findById(id).isPresent());
	}

	@Test
	public void testFindAll() throws Exception {
		ProductsRepositoryJdbcImpl repo = new ProductsRepositoryJdbcImpl(resetDBAndGetConnection());
		long EXPECTED_SIZE = 7;
		Assertions.assertEquals(EXPECTED_SIZE, repo.findAll().size());
	}
}
