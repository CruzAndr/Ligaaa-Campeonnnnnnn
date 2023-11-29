/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cinema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import cinema.Jframe.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.imageio.ImageIO;

public class Cinema extends JFrame {

    private static final String API_KEY = "f846867b6184611eeff179631d3f9e26";
    private List<JsonObject> movies;
    private Jframe jframe;

    public Cinema() {
        setTitle("Movies from TMDb");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBackground(Color.BLACK); // Fondo negro

        JScrollPane scrollPane = new JScrollPane(contentPane);

        // Agregar campo de búsqueda y botón
        JTextField searchField = new JTextField();
        searchField.setBackground(Color.DARK_GRAY); // Color de fondo oscuro
        searchField.setForeground(Color.WHITE); // Color de texto blanco
        JButton searchButton = new JButton("Buscar");
        searchButton.setBackground(Color.BLUE); // Fondo azul
        searchButton.setForeground(Color.WHITE); // Texto blanco

        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim().toLowerCase();

            // Filtra las películas por título y descripción, siendo insensible a mayúsculas y minúsculas
            List<JsonObject> filteredMovies = movies.stream()
                    .filter(movie -> movie.get("title").getAsString().toLowerCase().contains(searchTerm) ||
                            movie.get("overview").getAsString().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList());

            // Limpia el contenido actual antes de mostrar los resultados de la búsqueda
            contentPane.removeAll();

            // Si se encontraron resultados, agregarlos al contentPane
            if (!filteredMovies.isEmpty()) {
                for (JsonObject movie : filteredMovies) {
                    // Agrega la información de la película al contentPane
                    displayMovieInfo(contentPane, movie);
                }
            } else {
                // No se encontraron resultados
                JLabel noResultsLabel = new JLabel("No se encontraron resultados para: " + searchTerm);
                noResultsLabel.setForeground(Color.WHITE); // Texto blanco
                contentPane.add(noResultsLabel);
            }

            // Asegúrate de volver a validar y repintar la interfaz gráfica
            contentPane.revalidate();
            contentPane.repaint();
        });

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        searchPanel.setBackground(Color.BLACK); // Fondo negro

        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Llamada a la API para obtener la lista de películas
        try {
            URL url = new URL("https://api.themoviedb.org/3/trending/movie/week?api_key=" + API_KEY + "&language=es-ES&page=1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            StringBuilder response;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray results = jsonObject.getAsJsonArray("results");

            movies = new ArrayList<>();
            for (JsonElement element : results) {
                movies.add(element.getAsJsonObject());
            }

            // Agrega un MouseListener a cada panel de película para manejar el clic
            for (JsonObject movie : movies) {
                JPanel moviePanel = createMoviePanel(movie);
                contentPane.add(moviePanel);
                contentPane.add(Box.createRigidArea(new Dimension(0, 10))); // Espaciado vertical
            }

            contentPane.revalidate();
            contentPane.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    private JPanel createMoviePanel(JsonObject movie) {
        // Obtener información de la película
        String title = movie.get("title").getAsString();
        JsonElement overview = movie.get("overview");
        String posterPath = movie.get("poster_path").getAsString();
        int movieId = movie.get("id").getAsInt();
        double voteAverage = movie.get("vote_average").getAsDouble();

        // Crear panel para la película
        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(new BoxLayout(moviePanel, BoxLayout.Y_AXIS));
        moviePanel.setBackground(Color.DARK_GRAY); // Fondo oscuro

        // Título de la película
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Gotham", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE); // Texto blanco
        moviePanel.add(titleLabel);

        // Votación de la película
        JLabel voteLabel = new JLabel("Votación: " + voteAverage);
        voteLabel.setForeground(Color.WHITE); // Texto blanco
        moviePanel.add(voteLabel);

        // Imagen de la película
        BufferedImage img = loadImage(posterPath);
        JLabel picLabel = new JLabel(new ImageIcon(img));
        moviePanel.add(picLabel);

        // Agrega un MouseListener para manejar el clic en la película
        moviePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Abre la ventana de detalles cuando se hace clic
                showMovieDetails(movie);
            }
        });

        return moviePanel;
    }

    private void displayMovieInfo(JPanel contentPane, JsonObject movie) {
        // Agrega información de la película al contentPane
        JPanel moviePanel = createMoviePanel(movie);
        contentPane.add(moviePanel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10))); // Espaciado vertical
    }

  private void showMovieDetails(JsonObject movie) {
    jframe = new Jframe();
    // Llamada a la API para obtener detalles adicionales de la película
    try {
        int movieId = movie.get("id").getAsInt();
        URL movieDetailsURL = new URL("https://api.themoviedb.org/3/movie/"
                + movieId + "?api_key=" + API_KEY + "&language=es-ES&append_to_response=credits");
        HttpURLConnection detailsConnection = (HttpURLConnection) movieDetailsURL.openConnection();
        detailsConnection.setRequestMethod("GET");

        StringBuilder detailsResponse;
        try (BufferedReader detailsReader = new BufferedReader(new InputStreamReader(detailsConnection.getInputStream()))) {
            detailsResponse = new StringBuilder();
            String detailsLine;
            while ((detailsLine = detailsReader.readLine()) != null) {
                detailsResponse.append(detailsLine);
            }
        }

        JsonObject movieDetails = JsonParser.parseString(detailsResponse.toString()).getAsJsonObject();

        // Muestra la información en tu Jframe existente
        SwingUtilities.invokeLater(() -> {
            // Utiliza el método updateMovieDetails para actualizar la interfaz gráfica
            jframe.updateMovieDetails(movieDetails);

            // Hacer visible el JFrame
            jframe.setVisible(true);
        });

    } catch (IOException e) {
        e.printStackTrace();
    }
}





    private String getGenres(JsonArray genres) {
        StringBuilder genresText = new StringBuilder();
        for (JsonElement genreElement : genres) {
            JsonObject genre = genreElement.getAsJsonObject();
            genresText.append(genre.get("name").getAsString()).append(", ");
        }
        if (genresText.length() > 2) {
            genresText.delete(genresText.length() - 2, genresText.length()); // Eliminar la última coma y espacio
        }
        return genresText.toString();
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

    private BufferedImage loadImage(String imageUrl) {
        try {
            URL url = new URL("https://image.tmdb.org/t/p/w500" + imageUrl);
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Cinema());
    }
}
