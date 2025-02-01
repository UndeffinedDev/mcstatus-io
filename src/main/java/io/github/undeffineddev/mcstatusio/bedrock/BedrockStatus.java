package io.github.undeffineddev.mcstatusio.bedrock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class BedrockStatus {

    private static final String API_URL = "https://api.mcstatus.io/v2/status/bedrock/";

    private JSONObject statusData;

    /**
     * Constructor to fetch the status of a Bedrock server.
     *
     * @param address The server address.
     */
    public BedrockStatus(String address) {
        this(address, 5.0);
    }

    /**
     * Constructor to fetch the status of a Bedrock server with a custom timeout.
     *
     * @param address The server address.
     * @param timeout The timeout in seconds.
     */
    public BedrockStatus(String address, double timeout) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Server address cannot be null or empty");
        }

        try {
            String urlString = API_URL + address + "?timeout=" + timeout;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    statusData = new JSONObject(response.toString());
                }
            } else {
                throw new RuntimeException("Error: " + responseCode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if the server is online.
     *
     * @return True if the server is online, false otherwise.
     */
    public boolean isOnline() {
        return statusData != null && statusData.getBoolean("online");
    }

    /**
     * Gets the server's host address.
     *
     * @return The server's host address.
     */
    public String getHost() {
        return statusData.getString("host");
    }

    /**
     * Gets the server's port.
     *
     * @return The server's port.
     */
    public int getPort() {
        return statusData.getInt("port");
    }

    /**
     * Gets the server's IP address.
     *
     * @return The server's IP address, or null if not available.
     */
    public String getIpAddress() {
        return statusData.optString("ip_address", null);
    }

    /**
     * Checks if the server is blocked due to EULA violations.
     *
     * @return True if the server is EULA blocked, false otherwise.
     */
    public boolean isEulaBlocked() {
        return statusData.getBoolean("eula_blocked");
    }

    /**
     * Gets the timestamp when the data was retrieved.
     *
     * @return The timestamp when the data was retrieved.
     */
    public long getRetrievedAt() {
        return statusData.getLong("retrieved_at");
    }

    /**
     * Gets the timestamp when the data expires.
     *
     * @return The timestamp when the data expires.
     */
    public long getExpiresAt() {
        return statusData.getLong("expires_at");
    }

    /**
     * Gets the version name of the server.
     *
     * @return The version name, or null if the server is offline.
     */
    public String getVersionName() {
        if (isOnline()) {
            return statusData.getJSONObject("version").getString("name");
        }
        return null;
    }

    /**
     * Gets the protocol version of the server.
     *
     * @return The protocol version, or -1 if the server is offline.
     */
    public int getVersionProtocol() {
        if (isOnline()) {
            return statusData.getJSONObject("version").getInt("protocol");
        }
        return -1;
    }

    /**
     * Gets the number of players currently online.
     *
     * @return The number of players online, or 0 if the server is offline.
     */
    public int getPlayersOnline() {
        if (isOnline()) {
            return statusData.getJSONObject("players").getInt("online");
        }
        return 0;
    }

    /**
     * Gets the maximum number of players allowed on the server.
     *
     * @return The maximum number of players, or 0 if the server is offline.
     */
    public int getMaxPlayers() {
        if (isOnline()) {
            return statusData.getJSONObject("players").getInt("max");
        }
        return 0;
    }

    /**
     * Gets the raw MOTD (Message of the Day) of the server.
     *
     * @return The raw MOTD, or null if the server is offline.
     */
    public String getMotdRaw() {
        if (isOnline()) {
            return statusData.getJSONObject("motd").getString("raw");
        }
        return null;
    }

    /**
     * Gets the clean MOTD (Message of the Day) of the server.
     *
     * @return The clean MOTD, or null if the server is offline.
     */
    public String getMotdClean() {
        if (isOnline()) {
            return statusData.getJSONObject("motd").getString("clean");
        }
        return null;
    }

    /**
     * Gets the HTML MOTD (Message of the Day) of the server.
     *
     * @return The HTML MOTD, or null if the server is offline.
     */
    public String getMotdHtml() {
        if (isOnline()) {
            return statusData.getJSONObject("motd").getString("html");
        }
        return null;
    }

    /**
     * Gets the game mode of the server.
     *
     * @return The game mode, or null if the server is offline.
     */
    public String getGamemode() {
        if (isOnline()) {
            return statusData.getString("gamemode");
        }
        return null;
    }

    /**
     * Gets the server ID.
     *
     * @return The server ID, or null if the server is offline.
     */
    public String getServerId() {
        if (isOnline()) {
            return statusData.getString("server_id");
        }
        return null;
    }

    /**
     * Gets the edition of the server (e.g., "MCPE" or "MCEE").
     *
     * @return The server edition, or null if the server is offline.
     */
    public String getEdition() {
        if (isOnline()) {
            return statusData.getString("edition");
        }
        return null;
    }
}