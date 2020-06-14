import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double a = scanner.nextDouble();
        double b = scanner.nextDouble();
        double c = scanner.nextDouble();
        double outputOne = quadraticEquation(a, b, c, '+');
        double outputTwo = quadraticEquation(a, b, c, '-');
        System.out.printf("%f %f", Math.min(outputOne, outputTwo), Math.max(outputOne, outputTwo));
    }

    static double quadraticEquation(double a, double b, double c, char operand) {
        double miniQuadrant = Math.sqrt(b * b - 4 * a * c);
        return operand == '+' ? (-b + miniQuadrant) / (2 * a) : (-b - miniQuadrant) / (2 * a);
    }
}