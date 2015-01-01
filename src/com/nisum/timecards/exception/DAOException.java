package com.nisum.timecards.exception;

public class DAOException extends Exception {

	String message;

	public DAOException() {
		super();

	}

	public DAOException(String message) {
		this.message = message;
	}

	public String toString() {
		return "DAO Exception: " + this.message;
	}
}
