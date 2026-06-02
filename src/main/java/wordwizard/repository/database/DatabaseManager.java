package wordwizard.repository.database;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wordwizard.repository.entities.Word;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public interface DatabaseManager extends JpaRepository<Word, Long> {

    @NotNull
    public static Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USERNAME");
        String pass = System.getenv("DB_PASSWORD");

        return DriverManager.getConnection(url, user, pass);
    }


    public List<String> getAllThemes() {return null;}

    public Optional<Word> getWord() {return Optional.empty();}

    public void saveWord() {}
}
