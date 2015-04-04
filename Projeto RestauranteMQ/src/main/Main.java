package main;

import java.util.InputMismatchException;
import java.util.Scanner;
import model.Cliente;
import model.Funcionario;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    static {
        //scanner.useDelimiter(System.lineSeparator());
    }
    private final static int SAIR = 0;
    private final static int CADASTRO_CLIENTE = 1;
    private final static int LOGIN_CLIENTE = 2;
    private final static int LOGIN_FUNCIONARIO = 3;

    public static void main(String[] args) {
        System.out.println("BEM VINDO AO RESTAURANTEMQ\n");

        int opcao;

        while ((opcao = menuInicial()) != SAIR) {
            switch (opcao) {
                case CADASTRO_CLIENTE:
                    cadastroCliente();
                    break;
                case LOGIN_CLIENTE:
                    loginCliente();

                    break;
                case LOGIN_FUNCIONARIO:
                    loginFuncionario();
                    break;

                default:
            }
        }

    }

    private static int menuInicial() {
        System.out.println("Escolha sua opção:");
        System.out.println(CADASTRO_CLIENTE + " - Cadastrar cliente");
        System.out.println(LOGIN_CLIENTE + " - Login como cliente");
        System.out.println(LOGIN_FUNCIONARIO + " - Login como funcionario");
        System.out.println(SAIR + " - Sair");

        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            return 0;
        }
    }

    private static void cadastroCliente() {
        System.out.println("CADASTRO DE CLIENTE\n");
    }

    private static void loginFuncionario() {
        System.out.println("LOGIN DE FUNCIONARIO\n");
    }

    private static void loginCliente() {
        System.out.println("LOGIN DE CLIENTE\n");
        System.out.print("Nome de usuario:");
        String username = scanner.next();
        System.out.print("Senha:");
        String senha = scanner.next();

        Cliente cliente = new Cliente();
        boolean login = cliente.login(username, senha);
        if (login) {
            System.out.println("Bem vindo, " + cliente.getNome() + "!");
            menuCliente(cliente);
        } else {
            System.out.println("Nome de usuario e/ou senha incorretos");
        }
    }

    private static void menuCliente(Cliente cliente) {
        //TODO: implementar    
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void menuFuncionario(Funcionario funcionario) {
        //TODO: implementar    
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }
}
