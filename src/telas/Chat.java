/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package telas;

import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JTextArea;
import models.Message;
import models.Sala;
import models.Usuario;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

/**
 *
 * @author elysson
 */
public class Chat extends javax.swing.JFrame {
    
    private JavaSpace space;
    private Usuario usuario;
    private Sala sala;
    private int msgAtual; 
    /**
     * Creates new form SalasListagem
     */
    public Chat() {
        initComponents();
    }
    
    public Chat(Usuario usuario, JavaSpace space, Sala sala) {
        initComponents();
        
        menuBar.add(Box.createHorizontalGlue());
        JMenu sourceMenu = new JMenu("Sair");
        menuBar.add(sourceMenu);
        
        this.usuario = usuario;
        this.space = space;
        this.sala = sala;
        this.msgAtual = this.sala.qtdMsg + 1;//TODO
        
        jLabelTitulo.setText(sala.nome);
        
        Atualizador a = new Atualizador();
        a.start();
    }
    
    public void enviarMensagemChat(){
        String message = "";
        message = mensagem.getText();

        if(!message.isEmpty()){
            //chatArea.setText(chatArea.getText() + "\n Eu: " + msg);
            Sala sTemplate = new Sala();
            sTemplate.nome = this.sala.nome;
            Sala s = null;
            try {
                s = (Sala) space.take(sTemplate, null, Lease.FOREVER);
            } catch (UnusableEntryException | TransactionException | InterruptedException | RemoteException ex) {
                Logger.getLogger(SalaCadastro.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (s == null) {
                System.out.println("Tempo de espera esgotado. Encerrando...");
                System.exit(0);
            }
            
            Message msg = new Message();
            msg.content = message;
            msg.usuario = this.usuario.nome;
            s.qtdMsg += 1;
            msg.ordem = s.qtdMsg;
            msg.destino = s.nome;
            this.sala = s;
            
            try {
                this.space.write(msg, null, Lease.FOREVER);
                this.space.write(this.sala, null, Lease.FOREVER);
            } catch (TransactionException | RemoteException ex) {
                Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        mensagem.setText("");
    }
    
    private class Atualizador extends Thread{

        public Atualizador() {
        }

        @Override
        public void run(){
            while(true){
                Message template = new Message();
                template.ordem = msgAtual;
                template.destino = sala.nome;
                Message msg;
                try {
                    msg = (Message) space.read(template, null, Lease.FOREVER);

                    if(msg != null){
                        msgAtual += 1;
                        chatArea.setText(chatArea.getText() + "\n" + msg.usuario + ": " + msg.content);
                        chatArea.setCaretPosition(chatArea.getDocument().getLength());
                        Thread.sleep(10);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelPrincipal = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jButtonEnviar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        mensagem = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelPrincipal.setBackground(new java.awt.Color(255, 255, 255));

        jLabelTitulo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo.setText("Nome sala");

        jButtonEnviar.setText("Enviar");
        jButtonEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEnviarActionPerformed(evt);
            }
        });

        chatArea.setEditable(false);
        chatArea.setColumns(20);
        chatArea.setRows(5);
        jScrollPane3.setViewportView(chatArea);

        mensagem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mensagemKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanelPrincipalLayout = new javax.swing.GroupLayout(jPanelPrincipal);
        jPanelPrincipal.setLayout(jPanelPrincipalLayout);
        jPanelPrincipalLayout.setHorizontalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(mensagem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonEnviar))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelPrincipalLayout.setVerticalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonEnviar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mensagem))
                .addContainerGap())
        );

        jMenu1.setEnabled(false);
        menuBar.add(jMenu1);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEnviarActionPerformed
        // TODO add your handling code here:
        enviarMensagemChat();
    }//GEN-LAST:event_jButtonEnviarActionPerformed

    private void mensagemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mensagemKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){
            enviarMensagemChat();
        }
    }//GEN-LAST:event_mensagemKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Chat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatArea;
    private javax.swing.JButton jButtonEnviar;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField mensagem;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables
}
