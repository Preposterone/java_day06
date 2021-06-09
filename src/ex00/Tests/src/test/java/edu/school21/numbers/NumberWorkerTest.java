package edu.school21.numbers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class NumberWorkerTest {

	@ParameterizedTest
	@ValueSource(ints = {2, 3, 5, 7, 11, 13, 17, 71, 73, 79, 83, 89, 97, 2147483647})
	public void isPrimeForPrimes(int prime) throws Exception {
		assertTrue(new NumberWorker().isPrime(prime));
	}

	@ParameterizedTest
	@ValueSource(ints = {4, 6, 8, 9, 34, 35, 36, 38, 39, 40, 42, 44, 45, 49, 50, 51, 52, 54, 55, 62, 63})
	public void isPrimeForNotPrimes(int notPrime) throws Exception {
		assertFalse(new NumberWorker().isPrime(notPrime));
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, Integer.MIN_VALUE, -12105 - 1212024})
	public void isPrimeForIncorrectNumbers(int incorrectNum) throws Exception {
		assertThrows(RuntimeException.class, () -> new NumberWorker().isPrime(incorrectNum));
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/data.csv", numLinesToSkip = 1)
	public void isSumCorrect(int input, int expected) throws Exception {
		assertEquals(expected, new NumberWorker().digitsSum(input));
	}

	@Test
	public void testNegativeSum() throws Exception	{
		assertEquals(-10, new NumberWorker().digitsSum(-154));
	}
}
