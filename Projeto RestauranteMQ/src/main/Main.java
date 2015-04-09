package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import model.Cliente;
import model.Funcionario;
import model.Item;
import model.Mesa;
import model.Pedido;
import model.Reserva;
import persistencia.ItemPreparavelDAO;
import persistencia.ItemProntoDAO;
import persistencia.MesaDAO;
import persistencia.PedidoDAO;
import persistencia.ReservaDAO;

public class Main {

    private final static MesaDAO MESA_DAO = new MesaDAO();
    private final static ReservaDAO RESERVA_DAO = new ReservaDAO();
    private final static Scanner scanner = new Scanner(System.in);
    private final static PedidoDAO PEDIDO_DAO = new PedidoDAO();

    static {
        scanner.useDelimiter(Pattern.compile("\n|\r\n"));
    }
    private final static int SAIR = 0;
    private final static int CADASTRO_CLIENTE = 1;
    private final static int LOGIN_CLIENTE = 2;
    private final static int LOGIN_FUNCIONARIO = 3;
    private final static int VER_CARDAPIO = 4;

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
                case VER_CARDAPIO:
                    mostraCardapio();
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
        System.out.println(VER_CARDAPIO + " - Consultar cardapio");
        System.out.println(SAIR + " - Sair");

        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            return 0;
        }
    }

    private static void cadastroCliente() {
        System.out.println("CADASTRO DE CLIENTE\n");
        //TODO: cadastro de cliente
    }

    private static void loginFuncionario() {
        System.out.println("LOGIN DE FUNCIONARIO\n");
        System.out.print("Nome de usuario:");
        String username = scanner.next();
        System.out.print("Senha:");
        String senha = scanner.next();

        Funcionario funcionario = new Funcionario();
        boolean login = funcionario.login(username, senha);
        if (login) {
            menuFuncionario(funcionario);
        } else {
            System.out.println("Nome de usuario e/ou senha incorretos");
        }
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
            menuCliente(cliente);
        } else {
            System.out.println("Nome de usuario e/ou senha incorretos");
        }
    }

    private static void menuCliente(Cliente cliente) {
        final int CLIENTE_PEDIDO = 1;
        final int CLIENTE_RESERVA = 2;
        final int CLIENTE_LOGOUT = 0;
        int opcao;

        System.out.println("Bem vindo, " + cliente.getNome() + "!\n");

        do {
            System.out.println("Escolha sua opção:");
            System.out.println(CLIENTE_PEDIDO + " - Fazer pedido");
            System.out.println(CLIENTE_RESERVA + " - Fazer reserva");
            System.out.println(CLIENTE_LOGOUT + " - Sair");

            opcao = scanner.nextInt();
            switch (opcao) {
                case CLIENTE_PEDIDO:
                    clientePedido(cliente);
                    break;
                case CLIENTE_RESERVA:
                    clienteReserva(cliente);
                    break;

                default:
            }
        } while (opcao != CLIENTE_LOGOUT);
    }

    private static void menuFuncionario(Funcionario funcionario) {
        //TODO: implementar menuFuncionario 
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    private static void clientePedido(Cliente cliente) {
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEndereco(cliente.getEndereco());
        pedido.setDataEHora(Calendar.getInstance());
        System.out.println("Observações:  ");
        String observacao = scanner.nextLine();
        pedido.setObservações(observacao);
        PEDIDO_DAO.save(pedido);
     
        List<Item> itens = mostraCardapio();
        List<Integer> itensDoPedido = new ArrayList<>();
        
        int opcao = 0;
        while(true){
            System.out.println("Digite o numero de um item para adicionar a seu pedido,"
                    + " digite 0 quando terminar:");

            if((opcao = scanner.nextInt()) == 0){
                break;
            }else{
                itensDoPedido.add(opcao);
            }            
        }
        
        for (int i = 0; i < itensDoPedido.size(); i++) {
            Item item = itens.get(itensDoPedido.get(i) - 1);
            PEDIDO_DAO.adicionaItem(item.getId().intValue(), PEDIDO_DAO.getLastPedido().getId().intValue());
        }
        
        
    }

    private static void clienteReserva(Cliente cliente) {
        SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        System.out.println("Escolha a data e a hora que deseja reservar (dd/mm/aaaa HH:mm)");
        System.out.println("Escolher horário múltiplo de 30 minutos ");
        String dataString = scanner.nextLine();
        Calendar dataEHora = Calendar.getInstance();
        try {
            dataEHora.setTime(formatData.parse(dataString));
        } catch (ParseException ex) {
            System.out.println("formato de data invalido");
            return;
        }

        System.out.println("Escolha o numero de lugares que voce deseja");
        int lugares;
        try {
            lugares = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Erro");
            return;
        }
        List<Mesa> mesasDisponiveis = RESERVA_DAO.checkDisponibilidade(dataEHora, lugares);
        if (mesasDisponiveis.isEmpty()) {
            System.out.println("nao ha mesa disponivel no horario desejado "
                    + "com o numero de lugares desejado");
            return;
        }
        Mesa mesa = mesasDisponiveis.get(0);
        Reserva reserva = new Reserva();
        reserva.setMesa(mesa);
        reserva.setDataEHora(dataEHora);

        try {
            RESERVA_DAO.save(reserva);
        } catch (Exception e) {
            System.out.println("Erro ao registrar reserva: " + e.getMessage());
            return;
        }
        System.out.println("Sua reserva foi feita com sucesso\n"
                + "Sua mesa eh a numero " + mesa.getNumero() + ","
                + " reservada para " + dataString);
    }

    private static List<Item> mostraCardapio() {
        List<Item> itens = new ArrayList<>();
        itens.addAll(itemPreparavelDAO.listAll());
        itens.addAll(itemProntoDAO.listAll());
        System.out.println("CARDAPIO");
        System.out.println("Nº | CATEGORIA        |        NOME");
        System.out.println("-----------------------------------");
        for (int i = 1; i <= itens.size(); i++) {
            Item item = itens.get(i - 1);
            System.out.println(i + " | " + item.getCategoria() + " | " 
                    + item.getNome());
        }
        
        return itens;
    }
    private static final ItemProntoDAO itemProntoDAO = new ItemProntoDAO();
    private static final ItemPreparavelDAO itemPreparavelDAO = new ItemPreparavelDAO();
}
