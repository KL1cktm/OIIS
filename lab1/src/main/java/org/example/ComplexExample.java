package org.example;

import org.apache.commons.math3.complex.Complex;

public class ComplexExample {
    public static void main(String[] args) {
        // Значение k
        int k = 1;

        // Размерность (например, N = 4)
        int N = 4;

        // Рассчитываем угол θ = -2πk / N
        double angle = -2 * Math.PI * k / N;

        // Преобразуем угол в действительную и мнимую части
        double real = Math.cos(angle);
        double imaginary = Math.sin(angle);

        double threshold = 1e-10;
        real = Math.abs(real) < threshold ? 0.0 : real;
        imaginary = Math.abs(imaginary) < threshold ? 0.0 : imaginary;

        // Создаем комплексное число
        Complex W = new Complex(real, imaginary);

        // Печатаем результат
        System.out.println("Complex number: " + W);
    }
}

//
//    // Создаем комплексное число
//    Complex complexNumber = new Complex(3.0, 4.0); // 3 + 4i
//
//    // Действительное число для сложения
//    double realNumber = 5.0;
//
//    // Создаем новый объект Complex с действительным числом как действительной частью
//    Complex realAsComplex = new Complex(realNumber, 0.0);
//
//    // Складываем комплексное число с действительным числом
//    Complex result = complexNumber.add(realAsComplex);




// Создаем комплексное число
//Complex complexNumber = new Complex(3.0, 4.0); // 3 + 4i
//
//    // Действительное число для умножения
//    double realNumber = 2.0;
//
//    // Умножаем комплексное число на действительное число
//    Complex result = complexNumber.multiply(realNumber);