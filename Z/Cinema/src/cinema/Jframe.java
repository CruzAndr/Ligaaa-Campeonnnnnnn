/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package cinema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author metal
 */
public class Jframe extends javax.swing.JFrame {
private javax.swing.JLabel movieInfoLabel;
 private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel overviewLabel;
    private javax.swing.JLabel voteLabel;
    private javax.swing.JLabel directorLabel;
    private javax.swing.JPanel castPanel;
    /**
     * Creates new form Jframe
     */
    public Jframe() {
        initComponents();
         initAdditionalComponents();
    }
    
    private void initAdditionalComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // Configura el tamaño del JFrame secundario
        setSize(800, 600);

        // Configura el diseño del contenedor principal
        setLayout(new BorderLayout());

        // Inicializa los componentes principales
        movieInfoLabel = new javax.swing.JLabel();
        movieInfoLabel.setForeground(java.awt.Color.WHITE);

        // Inicializa otros componentes necesarios
        titleLabel = new javax.swing.JLabel();
        overviewLabel = new javax.swing.JLabel();
        voteLabel = new javax.swing.JLabel();
        directorLabel = new javax.swing.JLabel();
        castPanel = new javax.swing.JPanel();

        // Configura los textos iniciales si es necesario
        titleLabel.setText("Título: ");
        overviewLabel.setText("Sinopsis: ");
        voteLabel.setText("Rating: ");
        directorLabel.setText("Director: ");

        // Configura el diseño del panel de actores
        castPanel.setLayout(new BoxLayout(castPanel, BoxLayout.Y_AXIS));

        // Crea un panel para la información de la película
        JPanel movieInfoPanel = new JPanel();
        movieInfoPanel.setLayout(new BoxLayout(movieInfoPanel, BoxLayout.Y_AXIS));
        movieInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Añade un espacio alrededor

        // Añade componentes al panel de información de la película
        movieInfoPanel.add(titleLabel);
        movieInfoPanel.add(overviewLabel);
        movieInfoPanel.add(voteLabel);
        movieInfoPanel.add(directorLabel);

        // Crea un panel principal para organizar los elementos
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(movieInfoPanel, BorderLayout.WEST);
        mainPanel.add(castPanel, BorderLayout.EAST);

        // Agrega componentes al contenedor principal
        add(mainPanel, BorderLayout.CENTER);
    
    }



    public void showMovieDetails(String movieDetails) {
        // Muestra la información de la película en el JLabel
        movieInfoLabel.setText(movieDetails);
    }
    
    public void updateMovieDetails(JsonObject movieDetails) {
    titleLabel.setText("Título: " + movieDetails.get("title").getAsString());
    overviewLabel.setText("Sinopsis: " + movieDetails.get("overview").getAsString());
    voteLabel.setText("Rating: " + movieDetails.get("vote_average").getAsDouble());

    // Actualiza la información del director
    String director = getDirector(movieDetails.getAsJsonObject("credits").getAsJsonArray("crew"));
    directorLabel.setText("Director: " + director);

    // Actualiza la información de los actores
  JsonArray cast = movieDetails.getAsJsonObject("credits").getAsJsonArray("cast");
updateCastInfo(cast);
}

    
    private String getDirector(JsonArray crew) {
    for (JsonElement crewMember : crew) {
        JsonObject crewObject = crewMember.getAsJsonObject();
        if ("Director".equals(crewObject.get("job").getAsString())) {
            return crewObject.get("name").getAsString();
        }
    }
    return "Desconocido";
}

private void updateCastInfo(JsonArray cast) {
    // Limpia el panel de actores antes de actualizar
    castPanel.removeAll();

    for (JsonElement castMember : cast) {
        if (castMember.isJsonObject()) {
            JsonObject castObject = castMember.getAsJsonObject();
            JsonElement actorNameElement = castObject.get("name");

            if (actorNameElement != null && actorNameElement.isJsonPrimitive()) {
                String actorName = actorNameElement.getAsString();
                JLabel actorLabel = new JLabel("Actor: " + actorName);
                castPanel.add(actorLabel);
            }
        }
    }

    // Asegúrate de volver a validar y repintar el panel de actores
    castPanel.revalidate();
    castPanel.repaint();
}





    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Jframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Jframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Jframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Jframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Jframe().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
