package com.pellegrinoprincipe;

import javax.swing.JOptionPane;

public class JDialogContainers
{
    private Object[] options = {"Confirm dialog", "Option dialog"};
    private Object result;

    public JDialogContainers()
    {
        // finestra di dialogo di tipo input
        result = JOptionPane.showInputDialog(null, "Scegliere la finestra di dialogo...", "DIALOG",
                JOptionPane.INFORMATION_MESSAGE, null, options, 0);

        if (result == null)
            // finestra di dialogo di tipo message
            JOptionPane.showMessageDialog(null, "Attenzione nessuna scelta effettuata.",
                    "Errore", JOptionPane.ERROR_MESSAGE);
        else if (result == "Confirm dialog")
            showConfirm();
        else if (result == "Option dialog")
            showOption();
    }

    private void showConfirm()
    {
        // finestra di dialogo di tipo confirm
        int n = JOptionPane.showConfirmDialog(null, "Procedere con l'inserimento?", "Avviso",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        showAnswer(n);
    }

    private void showOption()
    {
        Object[] labels = {"DAI, prosegui", "OPS, non proseguire", "RESET AUTOMATICO"};

        // finestra di dialogo di tipo option
        int n = JOptionPane.showOptionDialog(null, "Riavvio il sistema?", "????",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, labels, labels[1]);
        showAnswer(n);
    }

    private void showAnswer(int n)
    {
        if (n == JOptionPane.YES_OPTION)
            JOptionPane.showMessageDialog(null, "Scelto il pulsante YES");
        else if (n == JOptionPane.NO_OPTION)
            JOptionPane.showMessageDialog(null, "Scelto il pulsante NO");
        else if (n == JOptionPane.CANCEL_OPTION)
            JOptionPane.showMessageDialog(null, "Scelto il pulsante CANCEL");
        else if (n == JOptionPane.CLOSED_OPTION)
            JOptionPane.showMessageDialog(null, "Chiusa la finestra di dialogo");
    }

    public static void main(String args[])
    {
        JDialogContainers dialogs = new JDialogContainers();
    }
}
