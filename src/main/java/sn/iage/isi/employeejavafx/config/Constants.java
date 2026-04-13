package sn.iage.isi.employeejavafx.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Constants {
    private static final Dotenv dotenv = Dotenv.load();

    public static final String HOST = dotenv.get("HOST");
    public static final int PORT = Integer.parseInt(dotenv.get("PORT"));
    public static final String DATABASE = dotenv.get("DATABASE");
    public static final String USER = dotenv.get("USER");
    public static final String PASSWORD = dotenv.get("PASSWORD");
    public static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
}
