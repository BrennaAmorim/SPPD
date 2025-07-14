package controller; // Ou onde você o colocou, mas precisa estar em algum pacote

import view.AppView; // Importe sua classe AppView

/**
 * Classe principal para iniciar a aplicação JavaFX.
 * Esta classe contém o método main tradicional que chama o método launch da AppView.
 * Isso garante que Application.launch() seja chamado do thread correto (não-JavaFX Application Thread).
 */
public class MainApp {

    public static void main(String[] args) {
        // Chamada ao método main da AppView, que por sua vez chama Application.launch()
        AppView.main(args);
    }
}