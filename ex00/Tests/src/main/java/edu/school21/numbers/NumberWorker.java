package edu.school21.numbers;

public class NumberWorker {

	public boolean isPrime(int number) {
		if (number < 2) {
			throw new IllegalNumberException();
		}
		for (int i = 2; i * i <= number && i <= 46340; i++) {
			if (number % i == 0)
				return (false);
		}
		return (true);
	}

	public int digitsSum(int number) {
		return (String.valueOf(number).chars().map(Character::getNumericValue).sum());
	}

	class IllegalNumberException extends RuntimeException {
		public IllegalNumberException() {
			super("Number has to be >= 2");
		}
	}
}
