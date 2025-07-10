package org.example;

import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // INJEÇÃO DE DEPENDÊNCIA MANUAL
        TarefaDAO tarefaDAO = new TarefaDAOImpl();
        TarefaService tarefaService = new TarefaServiceImpl(tarefaDAO);

        iniciarMenu(tarefaService);

        Database.closeConnection();
    }

    private static void iniciarMenu(TarefaService tarefaService) {
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        while (executando) {
            exibirMenuPrincipal();
            try {
                int escolha = scanner.nextInt();
                scanner.nextLine();

                switch (escolha) {
                    case 1:
                        cadastrarNovaTarefa(scanner, tarefaService);
                        break;
                    case 2:
                        alterarStatusTarefa(scanner, tarefaService);
                        break;
                    case 3:
                        listarTodasAsTarefas(tarefaService);
                        break;
                    case 0:
                        executando = false;
                        System.out.println("Saindo do programa...");
                        break;
                    default:
                        System.err.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
            }


            System.out.println();
        }
        scanner.close();
    }

    private static void alterarStatusTarefa(Scanner scanner, TarefaService tarefaService) {
        System.out.print("Digite o ID da tarefa que deseja alterar: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Escolha o novo status:");
            System.out.println("1. Não Iniciada");
            System.out.println("2. Em Processamento");
            System.out.println("3. Concluida");
            System.out.print("Opção: ");
            int statusOpcao = scanner.nextInt();
            scanner.nextLine();

            StatusTarefa novoStatus;
            switch (statusOpcao) {
                case 1:
                    novoStatus = StatusTarefa.NAO_INICIADA;
                    break;
                case 2:
                    novoStatus = StatusTarefa.EM_PROCESSAMENTO;
                    break;
                case 3:
                    novoStatus = StatusTarefa.CONCLUIDA;
                    break;
                default:
                    System.err.println("Opção de status inválida.");
                    return;
            }
            tarefaService.alterarStatusTarefa(id, novoStatus);
        } catch (InputMismatchException e) {
            System.err.println("ID ou opção inválida. Por favor, digite um número.");
            scanner.nextLine();
        }
    }

    private static void exibirMenuPrincipal() {
        System.out.println("--- Gerenciador de Tarefas ---");
        System.out.println("1. Cadastrar nova tarefa");
        System.out.println("2. Alterar status de uma tarefa");
        System.out.println("3. Listar todas as tarefas"); // Este texto será alterado no futuro
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void cadastrarNovaTarefa(Scanner scanner, TarefaService tarefaService) {
        System.out.print("Digite a descrição da nova tarefa: ");
        String texto = scanner.nextLine();
        tarefaService.adicionarTarefa(texto);
    }

    private static void listarTodasAsTarefas(TarefaService tarefaService) {
        List<Tarefa> tarefas = tarefaService.listarTodasTarefas();
        System.out.println("\n--- Todas as Tarefas ---");
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
        } else {
            imprimirListaDeTarefas(tarefas);
        }
    }

    private static void imprimirListaDeTarefas(List<Tarefa> tarefas) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        for (Tarefa tarefa : tarefas) {
            System.out.printf("ID: %-4d | Status: %-16s | Última Alteração: %s\n",
                    tarefa.getId(),
                    tarefa.getStatus(),
                    tarefa.getDataAlteracao().format(formatter));
            System.out.printf("Descrição: %s\n", tarefa.getTexto());
            System.out.println("------------------------------------------------------------------");
        }
    }
}