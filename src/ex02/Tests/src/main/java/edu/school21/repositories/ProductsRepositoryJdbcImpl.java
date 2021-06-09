package edu.school21.repositories;

import edu.school21.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ProductsRepositoryJdbcImpl implements ProductsRepository {

	private final Connection connection;

	public ProductsRepositoryJdbcImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<Product> findAll() {
		final String query = "SELECT * FROM products";
		List<Product> ret = new LinkedList<>();
		ResultSet rs = null;

		try {
			PreparedStatement statement = connection.prepareStatement(query);
			rs = statement.executeQuery();
			while (rs.next()) {
				ret.add(new Product(
						rs.getLong("id"),
						rs.getString("name"),
						rs.getLong("price")
				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (ret);
	}

	@Override
	public Optional<Product> findById(Long id) {
		final String query = "SELECT * FROM products WHERE id= ?";
		ResultSet rs = null;
		Product ret = null;

		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, id);
			rs = statement.executeQuery();
			if (rs.next()) {
				ret = new Product(
						rs.getLong("id"),
						rs.getString("name"),
						rs.getLong("price")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (Optional.ofNullable(ret));
	}

	@Override
	public void update(Product product) {
		final String query = "UPDATE products SET "
				+ "name=?, "
				+ "price=? "
				+ "WHERE id=?";

		try {
			if (findById(product.getId()).isPresent()) {
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, product.getName());
				statement.setLong(2, product.getPrice());
				statement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void save(Product product) {
		final String query = "INSERT INTO products (name, price) VALUES (?, ?); CALL IDENTITY();";
		ResultSet rs = null;

		try {
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, product.getName());
				statement.setLong(2, product.getPrice());
				rs = statement.executeQuery();
				if (rs.next()) {
					product.setId(rs.getLong("id"));
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void delete(Long id) {
		final String query = "DELETE FROM products WHERE id=?";

		try {
			if (findById(id).isPresent()) {
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setLong(1, id);
				statement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
